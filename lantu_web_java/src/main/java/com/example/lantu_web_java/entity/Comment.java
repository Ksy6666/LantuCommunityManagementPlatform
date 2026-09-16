package com.example.lantu_web_java.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 技术讨论评论/回答（库：lantu_web_dsn.comments）
 */
@TableName("lantu_web_dsn.comments")
public class Comment {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long postId;

    private Long userId;

    private Long parentId;

    private String content;

    private Integer likeCount;

    private Integer status;

    private LocalDateTime createdAt;

    /** 作者昵称（关联 lantu_web.users，非表字段） */
    @TableField(exist = false)
    private String authorNickname;

    /** 作者头像路径（关联 lantu_web.user_avatars，非表字段） */
    @TableField(exist = false)
    private String authorAvatar;

    public Comment() {}

    public Comment(Long postId, Long userId, Long parentId, String content) {
        this.postId = postId;
        this.userId = userId;
        this.parentId = parentId;
        this.content = content;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Integer getLikeCount() { return likeCount; }
    public void setLikeCount(Integer likeCount) { this.likeCount = likeCount; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getAuthorNickname() { return authorNickname; }
    public void setAuthorNickname(String authorNickname) { this.authorNickname = authorNickname; }

    public String getAuthorAvatar() { return authorAvatar; }
    public void setAuthorAvatar(String authorAvatar) { this.authorAvatar = authorAvatar; }
}
