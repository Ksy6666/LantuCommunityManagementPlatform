package com.example.lantu_web_java.controller;

import com.example.lantu_web_java.dto.ApiResponse;
import com.example.lantu_web_java.entity.Role;
import com.example.lantu_web_java.entity.User;
import com.example.lantu_web_java.mapper.CheckInMapper;
import com.example.lantu_web_java.mapper.RoleMapper;
import com.example.lantu_web_java.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserMapper userMapper;
    private final CheckInMapper checkInMapper;
    private final RoleMapper roleMapper;

    public AdminController(UserMapper userMapper, CheckInMapper checkInMapper, RoleMapper roleMapper) {
        this.userMapper = userMapper;
        this.checkInMapper = checkInMapper;
        this.roleMapper = roleMapper;
    }

    /** 检查当前用户是否为管理员（role_id = 1） */
    private boolean isAdmin(Long userId) {
        if (userId == null) return false;
        User user = userMapper.selectById(userId);
        return user != null && user.getRoleId() != null && user.getRoleId() == 1L;
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

    /**
     * GET /api/admin/dashboard — 仪表盘统计
     */
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> dashboard(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (!isAdmin(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(403, "无管理员权限"));
        }

        int totalUsers = userMapper.countTotalUsers();
        int todayCheckins = checkInMapper.countTodayCheckIns();
        String thisMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        int monthCheckins = checkInMapper.countThisMonthCheckIns(thisMonth);

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("todayCheckins", todayCheckins);
        stats.put("monthCheckins", monthCheckins);
        return ResponseEntity.ok(ApiResponse.success("ok", stats));
    }

    /**
     * GET /api/admin/users — 用户列表（分页+搜索）
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Map<String, Object>>> listUsers(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "") String keyword
    ) {
        Long userId = (Long) request.getAttribute("userId");
        if (!isAdmin(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(403, "无管理员权限"));
        }

        if (page < 1) page = 1;
        if (size < 1) size = 20;
        if (size > 100) size = 100;
        int offset = (page - 1) * size;

        List<User> users = userMapper.selectAllUsers(keyword, offset, size);
        int total = keyword.isEmpty() ? userMapper.countTotalUsers() : userMapper.countAllUsers(keyword);
        Map<Long, String> roleMap = getRoleMap();

        // 为每个用户填充 roleName
        List<Map<String, Object>> userList = new ArrayList<>();
        for (User u : users) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", u.getId());
            item.put("account", u.getAccount());
            item.put("name", u.getName());
            item.put("nickname", u.getNickname() != null ? u.getNickname() : "");
            item.put("phone", u.getPhone());
            item.put("email", u.getEmail() != null ? u.getEmail() : "");
            item.put("birthday", u.getBirthday() != null ? u.getBirthday().toString() : "");
            item.put("roleId", u.getRoleId() != null ? u.getRoleId() : 4L);
            item.put("roleName", roleMap.getOrDefault(u.getRoleId(), "普通用户"));
            item.put("createdAt", u.getCreatedAt() != null ? u.getCreatedAt().toString() : "");
            userList.add(item);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("list", userList);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        return ResponseEntity.ok(ApiResponse.success("ok", result));
    }

    /**
     * GET /api/admin/users/{id} — 用户详情
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> userDetail(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        Long currentUserId = (Long) request.getAttribute("userId");
        if (!isAdmin(currentUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(403, "无管理员权限"));
        }

        User user = userMapper.selectById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "用户不存在"));
        }

        Map<Long, String> roleMap = getRoleMap();
        Map<String, Object> detail = new LinkedHashMap<>();
        detail.put("id", user.getId());
        detail.put("account", user.getAccount());
        detail.put("name", user.getName());
        detail.put("nickname", user.getNickname() != null ? user.getNickname() : "");
        detail.put("phone", user.getPhone());
        detail.put("email", user.getEmail() != null ? user.getEmail() : "");
        detail.put("birthday", user.getBirthday() != null ? user.getBirthday().toString() : "");
        detail.put("roleId", user.getRoleId() != null ? user.getRoleId() : 4L);
        detail.put("roleName", roleMap.getOrDefault(user.getRoleId(), "普通用户"));
        detail.put("createdAt", user.getCreatedAt() != null ? user.getCreatedAt().toString() : "");
        return ResponseEntity.ok(ApiResponse.success("ok", detail));
    }

    /**
     * PUT /api/admin/users/{id}/role — 切换用户角色
     */
    @PutMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateUserRole(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestBody Map<String, Long> body
    ) {
        Long currentUserId = (Long) request.getAttribute("userId");
        if (!isAdmin(currentUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(403, "无管理员权限"));
        }

        Long roleId = body.get("roleId");
        if (roleId == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "角色ID不能为空"));
        }

        // 检查目标用户是否存在
        User target = userMapper.selectById(id);
        if (target == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "用户不存在"));
        }

        // 检查角色是否存在
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "角色不存在"));
        }

        userMapper.updateRole(id, roleId);
        // 清除 Redis 排行榜缓存（角色变化不影响积分，但刷新用户信息用）
        return ResponseEntity.ok(ApiResponse.success("角色已更新", Map.of("roleName", role.getRoleName())));
    }

    /**
     * DELETE /api/admin/users/{id} — 删除用户
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deleteUser(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        Long currentUserId = (Long) request.getAttribute("userId");
        if (!isAdmin(currentUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(403, "无管理员权限"));
        }

        if (currentUserId.equals(id)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "不能删除自己"));
        }

        User target = userMapper.selectById(id);
        if (target == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "用户不存在"));
        }

        userMapper.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("用户已删除", Map.of()));
    }
}
