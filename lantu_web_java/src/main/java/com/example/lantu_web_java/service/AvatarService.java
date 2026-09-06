package com.example.lantu_web_java.service;

import com.example.lantu_web_java.entity.User;
import com.example.lantu_web_java.entity.UserAvatar;
import com.example.lantu_web_java.mapper.UserAvatarMapper;
import com.example.lantu_web_java.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AvatarService {

    private static final Logger log = LoggerFactory.getLogger(AvatarService.class);

    private final UserAvatarMapper avatarMapper;
    private final UserMapper userMapper;
    private final Path uploadDir;

    public AvatarService(UserAvatarMapper avatarMapper, UserMapper userMapper) {
        this.avatarMapper = avatarMapper;
        this.userMapper = userMapper;
        // userTX 目录：直接放在项目根目录下
        // 从 lantu_web_java/ 启动 → ../userTX，从项目根启动 → userTX
        String ud = System.getProperty("user.dir");
        Path rootDir = Paths.get(ud, "..", "userTX").normalize();
        if (!Files.exists(rootDir)) {
            rootDir = Paths.get(ud, "userTX");
        }
        this.uploadDir = rootDir;
        log.info("头像上传目录: {}", uploadDir.toAbsolutePath());
    }

    /**
     * 上传用户头像
     * @param userId 用户 ID
     * @param file 上传的图片文件
     * @return 头像访问路径（相对 URL）
     */
    @Transactional
    public String uploadAvatar(Long userId, MultipartFile file) {
        // 校验文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("仅支持图片文件");
        }

        // 查询用户信息（获取 account 和 phone）
        User user = Optional.ofNullable(userMapper.selectById(userId))
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 确保上传目录存在
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("无法创建上传目录");
        }

        // 生成文件名：{account}_{phone后4位}.png
        String phone = user.getPhone();
        String phoneLast4 = phone.length() >= 4
                ? phone.substring(phone.length() - 4)
                : phone;
        String fileName = user.getAccount() + "_" + phoneLast4 + ".png";
        Path targetPath = uploadDir.resolve(fileName);

        // 删除旧头像文件（如果存在）
        try {
            Files.deleteIfExists(targetPath);
        } catch (IOException ignored) {}

        try {
            // 保存文件（强制转 PNG）
            file.transferTo(targetPath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("文件保存失败：" + e.getMessage());
        }

        // 删除旧头像记录
        avatarMapper.deleteByUserId(userId);

        // 创建新头像记录
        String relativePath = "/avatars/" + fileName;
        UserAvatar avatar = new UserAvatar(userId, relativePath);
        avatar.setCreatedAt(LocalDateTime.now());
        avatarMapper.insert(avatar);

        return relativePath;
    }

    /**
     * 获取用户头像路径
     */
    public Optional<String> getAvatarPath(Long userId) {
        return Optional.ofNullable(avatarMapper.selectByUserId(userId))
                .map(UserAvatar::getFilePath);
    }
}
