package com.example.lantu_web_java.service;

import com.example.lantu_web_java.dto.PreregistrationRequest;
import com.example.lantu_web_java.entity.Preregistration;
import com.example.lantu_web_java.mapper.PreregistrationMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PreregistrationService {

    private final PreregistrationMapper preregistrationMapper;

    public PreregistrationService(PreregistrationMapper preregistrationMapper) {
        this.preregistrationMapper = preregistrationMapper;
    }

    /**
     * 提交预报名
     */
    public Preregistration submit(PreregistrationRequest request) {
        Preregistration preregistration = new Preregistration(
                request.getName(),
                request.getMajor(),
                request.getGrade(),
                request.getIntro()
        );
        preregistration.setCreatedAt(LocalDateTime.now());
        preregistrationMapper.insert(preregistration);
        return preregistration;
    }
}
