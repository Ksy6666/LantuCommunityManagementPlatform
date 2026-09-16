package com.example.lantu_web_java.controller;

import com.example.lantu_web_java.dto.ApiResponse;
import com.example.lantu_web_java.entity.Comment;
import com.example.lantu_web_java.entity.Post;
import com.example.lantu_web_java.service.DiscussionService;
import com.example.lantu_web_java.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 技术讨论模块（数据存储于 lantu_web_dsn 库）
 * 路径未注册 JWT 拦截器，写操作在 Controller 内手动校验 JWT
 */
@RestController
@RequestMapping("/api/discussion")
public class DiscussionController {

    private final DiscussionService discussionService;

    public DiscussionController(DiscussionService discussionService) {
        this.discussionService = discussionService;
    }

    /**
     * 从请求头解析当前登录用户 ID（未登录返回 null）
     */
    private Long resolveUserId(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth == null || auth.isBlank() || !auth.startsWith("Bearer ")) {
            return null;
        }
        String token = auth.substring(7).trim();
        if (!JwtUtil.validateToken(token)) {
            return null;
        }
        try {
            return JwtUtil.getUserIdFromToken(token);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * POST /api/discussion/posts — 发布帖子（需登录）
     */
    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<Post>> publishPost(
            HttpServletRequest request,
            @RequestBody Map<String, String> body
    ) {
        try {
            Long userId = resolveUserId(request);
            if (userId == null) {
                return ResponseEntity.status(401).body(ApiResponse.error(401, "请先登录"));
            }
            Post post = discussionService.publishPost(
                    userId,
                    body.get("title"),
                    body.get("content"),
                    body.get("category"),
                    body.get("tags")
            );
            return ResponseEntity.ok(ApiResponse.success("发布成功", post));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * GET /api/discussion/posts?page=1&size=10&category=技术问答&keyword=Spring — 帖子列表
     */
    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<Map<String, Object>>> listPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword
    ) {
        try {
            Map<String, Object> data = discussionService.listPosts(category, keyword, page, size);
            return ResponseEntity.ok(ApiResponse.success(data));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * GET /api/discussion/posts/{id} — 帖子详情（浏览量 +1）
     */
    @GetMapping("/posts/{id}")
    public ResponseEntity<ApiResponse<Post>> getPostDetail(@PathVariable Long id) {
        try {
            Post post = discussionService.getPostDetail(id);
            return ResponseEntity.ok(ApiResponse.success(post));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * GET /api/discussion/posts/{id}/comments — 评论列表
     */
    @GetMapping("/posts/{id}/comments")
    public ResponseEntity<ApiResponse<List<Comment>>> listComments(@PathVariable Long id) {
        try {
            List<Comment> comments = discussionService.listComments(id);
            return ResponseEntity.ok(ApiResponse.success(comments));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * POST /api/discussion/posts/{id}/comments — 发表评论/回答（需登录）
     * body: { content, parentId }
     */
    @PostMapping("/posts/{id}/comments")
    public ResponseEntity<ApiResponse<Comment>> addComment(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        try {
            Long userId = resolveUserId(request);
            if (userId == null) {
                return ResponseEntity.status(401).body(ApiResponse.error(401, "请先登录"));
            }
            Long parentId = null;
            if (body.get("parentId") != null && !body.get("parentId").isBlank()) {
                try {
                    parentId = Long.parseLong(body.get("parentId"));
                } catch (NumberFormatException ignored) {
                }
            }
            Comment comment = discussionService.addComment(userId, id, parentId, body.get("content"));
            return ResponseEntity.ok(ApiResponse.success("评论成功", comment));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * POST /api/discussion/posts/{id}/like — 点赞/取消点赞帖子（需登录）
     */
    @PostMapping("/posts/{id}/like")
    public ResponseEntity<ApiResponse<Map<String, Object>>> toggleLike(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        try {
            Long userId = resolveUserId(request);
            if (userId == null) {
                return ResponseEntity.status(401).body(ApiResponse.error(401, "请先登录"));
            }
            Map<String, Object> data = discussionService.toggleLike(userId, id);
            return ResponseEntity.ok(ApiResponse.success(data));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * POST /api/discussion/comments/{id}/like — 点赞/取消点赞评论（需登录）
     */
    @PostMapping("/comments/{id}/like")
    public ResponseEntity<ApiResponse<Map<String, Object>>> toggleCommentLike(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        try {
            Long userId = resolveUserId(request);
            if (userId == null) {
                return ResponseEntity.status(401).body(ApiResponse.error(401, "请先登录"));
            }
            int likeCount = discussionService.toggleCommentLike(userId, id);
            Map<String, Object> data = Map.of("likeCount", likeCount);
            return ResponseEntity.ok(ApiResponse.success(data));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * POST /api/discussion/posts/{id}/favorite — 收藏/取消收藏帖子（需登录）
     */
    @PostMapping("/posts/{id}/favorite")
    public ResponseEntity<ApiResponse<Map<String, Object>>> toggleFavorite(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        try {
            Long userId = resolveUserId(request);
            if (userId == null) {
                return ResponseEntity.status(401).body(ApiResponse.error(401, "请先登录"));
            }
            Map<String, Object> data = discussionService.toggleFavorite(userId, id);
            return ResponseEntity.ok(ApiResponse.success(data));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * GET /api/discussion/my/posts — 我发布的帖子（需登录）
     */
    @GetMapping("/my/posts")
    public ResponseEntity<ApiResponse<List<Post>>> myPosts(HttpServletRequest request) {
        try {
            Long userId = resolveUserId(request);
            if (userId == null) {
                return ResponseEntity.status(401).body(ApiResponse.error(401, "请先登录"));
            }
            List<Post> posts = discussionService.listMyPosts(userId);
            return ResponseEntity.ok(ApiResponse.success(posts));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * GET /api/discussion/my/favorites — 我收藏的帖子（需登录）
     */
    @GetMapping("/my/favorites")
    public ResponseEntity<ApiResponse<List<Post>>> myFavorites(HttpServletRequest request) {
        try {
            Long userId = resolveUserId(request);
            if (userId == null) {
                return ResponseEntity.status(401).body(ApiResponse.error(401, "请先登录"));
            }
            List<Post> posts = discussionService.listMyFavorites(userId);
            return ResponseEntity.ok(ApiResponse.success(posts));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * GET /api/discussion/hot — 热点帖子 TopN
     */
    @GetMapping("/hot")
    public ResponseEntity<ApiResponse<List<Post>>> hotPosts(
            @RequestParam(defaultValue = "10") int limit
    ) {
        try {
            List<Post> posts = discussionService.getHotPosts(limit);
            return ResponseEntity.ok(ApiResponse.success(posts));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
        }
    }
}
