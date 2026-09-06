package com.example.lantu_web_java.controller;

import com.example.lantu_web_java.dto.ApiResponse;
import com.example.lantu_web_java.service.AvatarService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    /**
     * POST /api/avatar/upload — 上传头像（需要 token）
     */
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadAvatar(
            HttpServletRequest request,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            Long userId = (Long) request.getAttribute("userId");

            // 上传头像
            String avatarPath = avatarService.uploadAvatar(userId, file);

            return ResponseEntity.ok(ApiResponse.success("头像上传成功", Map.of(
                    "avatarUrl", avatarPath
            )));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * GET /api/avatar/{userId} — 获取用户头像路径（公开）
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAvatar(@PathVariable Long userId) {
        var optPath = avatarService.getAvatarPath(userId);
        if (optPath.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(Map.of(
                    "avatarUrl", optPath.get()
            )));
        }
        return ResponseEntity.ok(ApiResponse.success(Map.of(
                "avatarUrl", ""
        )));
    }
}
