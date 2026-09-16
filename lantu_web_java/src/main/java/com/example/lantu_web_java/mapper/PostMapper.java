package com.example.lantu_web_java.mapper;

import com.example.lantu_web_java.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PostMapper {

    int insert(Post post);

    /** 查询帖子详情（含作者昵称/头像） */
    Post selectById(@Param("id") Long id);

    /** 分页查询帖子列表（含作者昵称/头像），按创建时间倒序 */
    List<Post> selectPage(@Param("category") String category,
                          @Param("keyword") String keyword,
                          @Param("offset") int offset,
                          @Param("limit") int limit);

    /** 帖子总数（按分类/关键词过滤） */
    long countPosts(@Param("category") String category,
                    @Param("keyword") String keyword);

    /** 浏览量 +1 */
    int incrementViewCount(@Param("id") Long id);

    /** 点赞数 +delta */
    int changeLikeCount(@Param("id") Long id, @Param("delta") int delta);

    /** 收藏数 +delta */
    int changeFavoriteCount(@Param("id") Long id, @Param("delta") int delta);

    /** 评论数 +delta */
    int changeCommentCount(@Param("id") Long id, @Param("delta") int delta);

    /** 查询用户是否已点赞某帖子 */
    int countLikeByUser(@Param("postId") Long postId, @Param("userId") Long userId);

    /** 查询用户是否已收藏某帖子 */
    int countFavoriteByUser(@Param("postId") Long postId, @Param("userId") Long userId);

    /** 查询用户发布的帖子列表 */
    List<Post> selectByUserId(@Param("userId") Long userId);

    /** 查询用户收藏的帖子列表 */
    List<Post> selectFavoriteByUserId(@Param("userId") Long userId);

    /** 热门帖子 TopN（按浏览+点赞+收藏综合热度） */
    List<Post> selectHotPosts(@Param("limit") int limit);
}
