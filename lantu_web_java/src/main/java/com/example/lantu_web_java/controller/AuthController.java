package com.example.lantu_web_java.controller;

import com.example.lantu_web_java.dto.ApiResponse;
//import com.example.lantu_web_java.dto.*;
import com.example.lantu_web_java.dto.ChangePasswordEmailRequest;
import com.example.lantu_web_java.dto.ChangePasswordOldRequest;
import com.example.lantu_web_java.dto.LoginRequest;
import com.example.lantu_web_java.dto.RegisterRequest;
import com.example.lantu_web_java.entity.Role;
import com.example.lantu_web_java.entity.User;
import com.example.lantu_web_java.mapper.RoleMapper;
import com.example.lantu_web_java.service.AvatarService;
import com.example.lantu_web_java.service.UserService;
import com.example.lantu_web_java.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AvatarService avatarService;
    private final RoleMapper roleMapper;

    public AuthController(UserService userService, AvatarService avatarService, RoleMapper roleMapper) {
        this.userService = userService;
        this.avatarService = avatarService;
        this.roleMapper = roleMapper;
    }

    /** 加载所有角色映射：roleId -> roleName */
    private Map<Long, String> getRoleMap() {
        List<Role> roles;
        try {
            roles = roleMapper.selectAll();
        } catch (Exception e) {
            return Collections.emptyMap();
        }
        return roles.stream().collect(Collectors.toMap(Role::getId, Role::getRoleName));
    }

    /** 构建用户信息响应 Map（含角色信息） */
    private Map<String, Object> buildUserMap(User user, String avatarUrl, String token) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("account", user.getAccount());
        map.put("name", user.getName());
        map.put("nickname", user.getNickname() != null ? user.getNickname() : "");
        map.put("phone", user.getPhone());
        map.put("email", user.getEmail() != null ? user.getEmail() : "");
        map.put("birthday", user.getBirthday() != null ? user.getBirthday().toString() : "");
        map.put("avatarUrl", avatarUrl);
        map.put("roleId", user.getRoleId() != null ? user.getRoleId() : 4L);
        map.put("roleName", getRoleMap().getOrDefault(user.getRoleId(), "普通用户"));
        if (token != null) map.put("token", token);
        return map;
    }

    /**
     * POST /api/auth/register — 用户注册
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Map<String, Object>>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request);
            String token = JwtUtil.generateToken(user.getId(), user.getAccount());

            // 查询用户头像
            String avatarUrl = avatarService.getAvatarPath(user.getId()).orElse("");

            return ResponseEntity.ok(ApiResponse.success("注册成功", buildUserMap(user, avatarUrl, token)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * POST /api/auth/login — 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@Valid @RequestBody LoginRequest request) {
        try {
            User user = userService.login(request);
            String token = JwtUtil.generateToken(user.getId(), user.getAccount());

            // 查询用户头像
            String avatarUrl = avatarService.getAvatarPath(user.getId()).orElse("");

            return ResponseEntity.ok(ApiResponse.success("登录成功", buildUserMap(user, avatarUrl, token)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, e.getMessage()));
        }
    }

    /**
     * PUT /api/auth/birthday — 设置生日
     */
    @PutMapping("/birthday")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateBirthday(
            HttpServletRequest request,
            @RequestBody Map<String, String> body
    ) {
        try {
            Long userId = (Long) request.getAttribute("userId");

            // 解析生日
            String birthdayStr = body.get("birthday");
            if (birthdayStr == null || birthdayStr.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(400, "生日不能为空"));
            }
            LocalDate birthday = LocalDate.parse(birthdayStr);

            User user = userService.updateBirthday(userId, birthday);
            String avatarUrl = avatarService.getAvatarPath(user.getId()).orElse("");
            return ResponseEntity.ok(ApiResponse.success("生日设置成功",
                    buildUserMap(user, avatarUrl, null)));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "生日设置失败：" + e.getMessage()));
        }
    }

    /**
     * PUT /api/auth/profile — 更新个人信息
     */
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateProfile(
            HttpServletRequest request,
            @RequestBody Map<String, Object> body
    ) {
        try {
            Long userId = (Long) request.getAttribute("userId");

            String name = (String) body.get("name");
            String nickname = (String) body.get("nickname");
            String phone = (String) body.get("phone");
            String email = (String) body.get("email");
            LocalDate birthday = null;
            if (body.get("birthday") != null) {
                birthday = LocalDate.parse((String) body.get("birthday"));
            }

            User user = userService.updateProfile(userId, name, nickname, phone, birthday, email);

            String avatarUrl = avatarService.getAvatarPath(user.getId()).orElse("");
            return ResponseEntity.ok(ApiResponse.success("个人信息更新成功",
                    buildUserMap(user, avatarUrl, null)));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "更新失败：" + e.getMessage()));
        }
    }

    /**
     * PUT /api/auth/password/old — 通过原密码修改密码
     */
    @PutMapping("/password/old")
    public ResponseEntity<ApiResponse<Map<String, Object>>> changePasswordWithOld(
            HttpServletRequest request,
            @Valid @RequestBody ChangePasswordOldRequest req
    ) {
        try {
            Long userId = (Long) request.getAttribute("userId");

            if (!req.getNewPassword().equals(req.getConfirmPassword())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(400, "两次输入的密码不一致"));
            }
            userService.changePasswordWithOld(userId, req.getOldPassword(), req.getNewPassword());
            return ResponseEntity.ok(ApiResponse.success("密码修改成功", Map.of()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * POST /api/auth/password/email/code — 发送邮箱验证码（模拟）
     */
    @PostMapping("/password/email/code")
    public ResponseEntity<ApiResponse<Map<String, Object>>> sendEmailCode(
            @RequestBody Map<String, String> body
    ) {
        try {
            String email = body.get("email");
            if (email == null || email.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(400, "邮箱不能为空"));
            }
            userService.sendVerificationCode(email);
            return ResponseEntity.ok(ApiResponse.success("验证码已发送（模拟：1234）", Map.of()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * PUT /api/auth/password/email — 通过邮箱验证码修改密码
     */
    @PutMapping("/password/email")
    public ResponseEntity<ApiResponse<Map<String, Object>>> changePasswordByEmail(
            @Valid @RequestBody ChangePasswordEmailRequest request
    ) {
        try {
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(400, "两次输入的密码不一致"));
            }
            userService.changePasswordByEmail(request.getEmail(), request.getNewPassword());
            return ResponseEntity.ok(ApiResponse.success("密码修改成功", Map.of()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}
