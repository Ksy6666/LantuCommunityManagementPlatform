package com.example.lantu_web_java;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.lantu_web_java.mapper")
public class LantuWebJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LantuWebJavaApplication.class, args);
    }

}
