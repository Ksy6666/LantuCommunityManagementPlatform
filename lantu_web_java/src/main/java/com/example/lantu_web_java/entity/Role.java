package com.example.lantu_web_java.entity;

import java.time.LocalDateTime;

public class Role {

    private Long id;
    private String roleName;
    private String description;
    private LocalDateTime createdAt;

    public Role() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
