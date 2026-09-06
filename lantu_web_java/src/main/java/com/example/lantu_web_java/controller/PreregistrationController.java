package com.example.lantu_web_java.controller;

import com.example.lantu_web_java.dto.ApiResponse;
import com.example.lantu_web_java.dto.PreregistrationRequest;
import com.example.lantu_web_java.service.PreregistrationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/preregister")
public class PreregistrationController {

    private final PreregistrationService preregistrationService;

    public PreregistrationController(PreregistrationService preregistrationService) {
        this.preregistrationService = preregistrationService;
    }

    /**
     * POST /api/preregister — 提交预报名
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> submit(@Valid @RequestBody PreregistrationRequest request) {
        try {
            preregistrationService.submit(request);
            return ResponseEntity.ok(ApiResponse.success("预报名成功，我们会尽快联系你！", Map.of()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "提交失败，请稍后重试"));
        }
    }
}
