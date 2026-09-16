package com.example.lantu_web_java.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 技术讨论点赞记录（库：lantu_web_dsn.post_likes）
 */
@TableName("lantu_web_dsn.post_likes")
public class PostLike {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long postId;

    private Long userId;

    private LocalDateTime createdAt;

    public PostLike() {}

    public PostLike(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
