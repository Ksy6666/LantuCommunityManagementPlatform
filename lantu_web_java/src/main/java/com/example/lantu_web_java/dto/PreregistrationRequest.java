package com.example.lantu_web_java.dto;

import jakarta.validation.constraints.NotBlank;

public class PreregistrationRequest {

    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotBlank(message = "专业不能为空")
    private String major;

    @NotBlank(message = "年级不能为空")
    private String grade;

    private String intro;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getIntro() { return intro; }
    public void setIntro(String intro) { this.intro = intro; }
}
