package com.example.lantu_web_java.service;

import com.example.lantu_web_java.entity.Comment;
import com.example.lantu_web_java.entity.Post;
import com.example.lantu_web_java.entity.PostFavorite;
import com.example.lantu_web_java.entity.PostLike;
import com.example.lantu_web_java.mapper.CommentMapper;
import com.example.lantu_web_java.mapper.PostFavoriteMapper;
import com.example.lantu_web_java.mapper.PostLikeMapper;
import com.example.lantu_web_java.mapper.PostMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 技术讨论模块业务逻辑（数据存储于 lantu_web_dsn 库）
 * 热点帖子优先使用 Redis ZSET 缓存，Redis 不可用时自动回退 MySQL 查询
 */
@Service
public class DiscussionService {

    private static final Logger log = LoggerFactory.getLogger(DiscussionService.class);
    private static final String HOT_POSTS_KEY = "discussion:hot:posts";
    private static final int HOT_POSTS_LIMIT = 10;
    private static final Set<String> CATEGORIES = Set.of("技术分享", "技术问答", "经验交流", "求助");

    private final PostMapper postMapper;
    private final CommentMapper commentMapper;
    private final PostLikeMapper postLikeMapper;
    private final PostFavoriteMapper postFavoriteMapper;
    private final ObjectProvider<StringRedisTemplate> redisProvider;

    public DiscussionService(PostMapper postMapper,
                             CommentMapper commentMapper,
                             PostLikeMapper postLikeMapper,
                             PostFavoriteMapper postFavoriteMapper,
                             ObjectProvider<StringRedisTemplate> redisProvider) {
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
        this.postLikeMapper = postLikeMapper;
        this.postFavoriteMapper = postFavoriteMapper;
        this.redisProvider = redisProvider;
    }

    /**
     * 安全获取 StringRedisTemplate，Redis 不可用时返回 null
     */
    private StringRedisTemplate getRedis() {
        try {
            return redisProvider.getIfAvailable();
        } catch (Exception e) {
            return null;
        }
    }

    // ==================== 帖子 ====================

    /**
     * 发布帖子
     */
    @Transactional(rollbackFor = Exception.class)
    public Post publishPost(Long userId, String title, String content, String category, String tags) {
        if (title == null || title.isBlank()) {
            throw new RuntimeException("标题不能为空");
        }
        if (content == null || content.isBlank()) {
            throw new RuntimeException("内容不能为空");
        }
        String finalCategory = (category == null || category.isBlank()) ? "技术分享" : category;
        if (!CATEGORIES.contains(finalCategory)) {
            throw new RuntimeException("无效的分类，可选：技术分享/技术问答/经验交流/求助");
        }

        Post post = new Post(userId, title.trim(), content.trim(), finalCategory, tags);
        postMapper.insert(post);
        return postMapper.selectById(post.getId());
    }

    /**
     * 分页查询帖子列表
     */
    public Map<String, Object> listPosts(String category, String keyword, int page, int size) {
        if (page < 1) page = 1;
        if (size < 1 || size > 50) size = 10;

        String normalizedCategory = (category == null || category.isBlank()) ? null : category;
        String normalizedKeyword = (keyword == null || keyword.isBlank()) ? null : keyword.trim();

        int offset = (page - 1) * size;
        List<Post> posts = postMapper.selectPage(normalizedCategory, normalizedKeyword, offset, size);
        long total = postMapper.countPosts(normalizedCategory, normalizedKeyword);

        Map<String, Object> result = new HashMap<>();
        result.put("list", posts);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        return result;
    }

    /**
     * 查询帖子详情（浏览量 +1）
     */
    public Post getPostDetail(Long postId) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在或已删除");
        }
        postMapper.incrementViewCount(postId);
        post.setViewCount((post.getViewCount() == null ? 0 : post.getViewCount()) + 1);
        return post;
    }

    /**
     * 查询用户发布的帖子
     */
    public List<Post> listMyPosts(Long userId) {
        return postMapper.selectByUserId(userId);
    }

    /**
     * 查询用户收藏的帖子
     */
    public List<Post> listMyFavorites(Long userId) {
        return postMapper.selectFavoriteByUserId(userId);
    }

    // ==================== 评论/回答 ====================

    /**
     * 发表评论/回答
     */
    @Transactional(rollbackFor = Exception.class)
    public Comment addComment(Long userId, Long postId, Long parentId, String content) {
        if (content == null || content.isBlank()) {
            throw new RuntimeException("评论内容不能为空");
        }
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在或已删除");
        }
        Long finalParentId = (parentId == null || parentId <= 0) ? 0L : parentId;
        if (finalParentId > 0) {
            Comment parent = commentMapper.selectById(finalParentId);
            if (parent == null || !parent.getPostId().equals(postId)) {
                throw new RuntimeException("回复的评论不存在");
            }
        }

        Comment comment = new Comment(postId, userId, finalParentId, content.trim());
        commentMapper.insert(comment);
        postMapper.changeCommentCount(postId, 1);

        // 有新评论时更新 Redis 热帖得分
        try {
            refreshRedisHotScore(postId);
        } catch (Exception e) {
            log.warn("Redis 热帖得分刷新失败：{}", e.getMessage());
        }
        return comment;
    }

    /**
     * 查询某帖子的评论列表
     */
    public List<Comment> listComments(Long postId) {
        return commentMapper.selectByPostId(postId);
    }

    // ==================== 点赞 ====================

    /**
     * 点赞/取消点赞帖子，返回最新点赞数与是否已点赞
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> toggleLike(Long userId, Long postId) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在或已删除");
        }
        boolean liked;
        int delta;
        if (postLikeMapper.countByPostAndUser(postId, userId) > 0) {
            postLikeMapper.deleteByPostAndUser(postId, userId);
            liked = false;
            delta = -1;
        } else {
            postLikeMapper.insert(new PostLike(postId, userId));
            liked = true;
            delta = 1;
        }
        postMapper.changeLikeCount(postId, delta);

        try {
            refreshRedisHotScore(postId);
        } catch (Exception e) {
            log.warn("Redis 热帖得分刷新失败：{}", e.getMessage());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("liked", liked);
        result.put("likeCount", Math.max(0, (post.getLikeCount() == null ? 0 : post.getLikeCount()) + delta));
        return result;
    }

    /**
     * 点赞/取消点赞评论
     */
    @Transactional(rollbackFor = Exception.class)
    public int toggleCommentLike(Long userId, Long commentId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new RuntimeException("评论不存在或已删除");
        }
        // 简化处理：同一用户对评论的点赞通过 like_count 增减模拟（不建独立表）
        int delta = comment.getLikeCount() != null && comment.getLikeCount() > 0 ? -1 : 1;
        commentMapper.changeLikeCount(commentId, delta);
        Comment updated = commentMapper.selectById(commentId);
        return updated != null && updated.getLikeCount() != null ? updated.getLikeCount() : 0;
    }

    // ==================== 收藏 ====================

    /**
     * 收藏/取消收藏帖子，返回最新收藏数与是否已收藏
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> toggleFavorite(Long userId, Long postId) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在或已删除");
        }
        boolean favorited;
        int delta;
        if (postFavoriteMapper.countByPostAndUser(postId, userId) > 0) {
            postFavoriteMapper.deleteByPostAndUser(postId, userId);
            favorited = false;
            delta = -1;
        } else {
            postFavoriteMapper.insert(new PostFavorite(postId, userId));
            favorited = true;
            delta = 1;
        }
        postMapper.changeFavoriteCount(postId, delta);

        Map<String, Object> result = new HashMap<>();
        result.put("favorited", favorited);
        result.put("favoriteCount", Math.max(0, (post.getFavoriteCount() == null ? 0 : post.getFavoriteCount()) + delta));
        return result;
    }

    /**
     * 查询当前用户对帖子列表的点赞/收藏状态
     */
    public Map<Long, Boolean> getLikedMap(List<Post> posts, Long userId) {
        Map<Long, Boolean> map = new HashMap<>();
        if (userId == null || posts == null) return map;
        for (Post p : posts) {
            map.put(p.getId(), postLikeMapper.countByPostAndUser(p.getId(), userId) > 0);
        }
        return map;
    }

    public boolean isPostLiked(Long userId, Long postId) {
        if (userId == null) return false;
        return postLikeMapper.countByPostAndUser(postId, userId) > 0;
    }

    public boolean isPostFavorited(Long userId, Long postId) {
        if (userId == null) return false;
        return postFavoriteMapper.countByPostAndUser(postId, userId) > 0;
    }

    // ==================== 热点帖子（Redis + MySQL 回退） ====================

    /**
     * 获取热点帖子 TopN，优先 Redis ZSET，不可用回退 MySQL 热度计算
     */
    public List<Post> getHotPosts(int limit) {
        if (limit <= 0 || limit > 50) limit = HOT_POSTS_LIMIT;

        StringRedisTemplate redis = getRedis();
        if (redis != null) {
            try {
                Set<String> members = redis.opsForZSet().reverseRange(HOT_POSTS_KEY, 0, limit - 1);
                if (members != null && !members.isEmpty()) {
                    List<Post> result = new ArrayList<>();
                    for (String member : members) {
                        Post post = postMapper.selectById(Long.valueOf(member));
                        if (post != null) {
                            result.add(post);
                        }
                    }
                    if (!result.isEmpty()) {
                        return result;
                    }
                }
            } catch (Exception e) {
                log.warn("Redis 读取热点帖子失败，回退 MySQL：{}", e.getMessage());
            }
        }
        return postMapper.selectHotPosts(limit);
    }

    /**
     * 刷新某帖子的 Redis 热帖得分（浏览量 + 点赞*5 + 收藏*10 + 评论*8）
     */
    private void refreshRedisHotScore(Long postId) {
        StringRedisTemplate redis = getRedis();
        if (redis == null) return;
        Post post = postMapper.selectById(postId);
        if (post == null) return;
        double score = (post.getViewCount() == null ? 0 : post.getViewCount())
                + (post.getLikeCount() == null ? 0 : post.getLikeCount()) * 5
                + (post.getFavoriteCount() == null ? 0 : post.getFavoriteCount()) * 10
                + (post.getCommentCount() == null ? 0 : post.getCommentCount()) * 8;
        redis.opsForZSet().add(HOT_POSTS_KEY, String.valueOf(postId), score);
    }

    /**
     * 应用启动时，将 MySQL 帖子热度同步到 Redis 热帖 ZSET
     */
    @jakarta.annotation.PostConstruct
    public void initHotPosts() {
        StringRedisTemplate redis = getRedis();
        if (redis == null) return;
        try {
            Long size = redis.opsForZSet().zCard(HOT_POSTS_KEY);
            if (size != null && size > 0) {
                log.info("Redis 热帖排行榜已有 {} 条记录，跳过初始化", size);
                return;
            }
            List<Post> hot = postMapper.selectHotPosts(100);
            for (Post p : hot) {
                double score = (p.getViewCount() == null ? 0 : p.getViewCount())
                        + (p.getLikeCount() == null ? 0 : p.getLikeCount()) * 5
                        + (p.getFavoriteCount() == null ? 0 : p.getFavoriteCount()) * 10
                        + (p.getCommentCount() == null ? 0 : p.getCommentCount()) * 8;
                redis.opsForZSet().add(HOT_POSTS_KEY, String.valueOf(p.getId()), score);
            }
            log.info("Redis 热帖排行榜初始化完成，共 {} 条记录", hot.size());
        } catch (Exception e) {
            log.warn("Redis 热帖排行榜初始化失败：{}", e.getMessage());
        }
    }
}
