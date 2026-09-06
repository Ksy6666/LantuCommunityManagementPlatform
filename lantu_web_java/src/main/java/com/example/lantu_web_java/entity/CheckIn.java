package com.example.lantu_web_java.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CheckIn {
    private Long id;
    private Long userId;
    private LocalDate checkInDate;
    private int points;
    private LocalDateTime createdAt;

    public CheckIn() {}

    public CheckIn(Long userId, LocalDate checkInDate, int points) {
        this.userId = userId;
        this.checkInDate = checkInDate;
        this.points = points;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
