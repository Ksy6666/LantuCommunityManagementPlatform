package com.example.lantu_web_java.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("user_avatars")
public class UserAvatar {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String filePath;

    private LocalDateTime createdAt;

    public UserAvatar() {}

    public UserAvatar(Long userId, String filePath) {
        this.userId = userId;
        this.filePath = filePath;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
