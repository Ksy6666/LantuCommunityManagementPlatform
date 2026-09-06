package com.example.lantu_web_java.entity;

import java.time.LocalDateTime;

public class Preregistration {

    private Long id;

    private String name;

    private String major;

    private String grade;

    private String intro;

    private LocalDateTime createdAt;

    public Preregistration() {}

    public Preregistration(String name, String major, String grade, String intro) {
        this.name = name;
        this.major = major;
        this.grade = grade;
        this.intro = intro;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getIntro() { return intro; }
    public void setIntro(String intro) { this.intro = intro; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
