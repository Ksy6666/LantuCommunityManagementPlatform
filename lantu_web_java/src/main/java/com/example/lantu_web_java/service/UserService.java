package com.example.lantu_web_java.service;

import com.example.lantu_web_java.dto.LoginRequest;
import com.example.lantu_web_java.dto.RegisterRequest;
import com.example.lantu_web_java.entity.User;
import com.example.lantu_web_java.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // 模拟验证码存储（email -> code）
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 生成唯一的 11 位随机数字账号
     */
    private String generateUniqueAccount() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String account;
        int maxAttempts = 50;
        do {
            // 生成 10000000000 ~ 99999999999 之间的数字
            long num = random.nextLong(10000000000L, 100000000000L);
            account = String.valueOf(num);
            maxAttempts--;
        } while (userMapper.countByAccount(account) > 0 && maxAttempts > 0);

        if (maxAttempts <= 0) {
            throw new RuntimeException("账号生成失败，请稍后重试");
        }
        return account;
    }

    /**
     * 生成唯一的用户昵称（基于手机号后4位，冲突时追加随机数字）
     */
    private String generateUniqueNickname(String phone) {
        String suffix = phone.length() >= 4
                ? phone.substring(phone.length() - 4)
                : phone;
        String base = "用户" + suffix;
        if (userMapper.countByNickname(base) == 0) {
            return base;
        }
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < 50; i++) {
            String candidate = base + random.nextInt(100, 999);
            if (userMapper.countByNickname(candidate) == 0) {
                return candidate;
            }
        }
        throw new RuntimeException("昵称生成失败，请稍后重试");
    }

    /**
     * 遍历异常链判断是否为 UNIQUE KEY 重复
     */
    private static boolean isDuplicateEntry(RuntimeException e, String keyName) {
        Throwable cause = e;
        while (cause != null) {
            String msg = cause.getMessage();
            if (msg != null && msg.contains("Duplicate entry") && msg.contains(keyName)) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    /**
     * 用户注册
     */
    @Transactional(rollbackFor = Exception.class)
    public User register(RegisterRequest request) {
        if (userMapper.countByPhone(request.getPhone()) > 0) {
            throw new RuntimeException("该手机号已注册");
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()
                && userMapper.countByEmail(request.getEmail()) > 0) {
            throw new RuntimeException("该邮箱已被注册");
        }

        // 随机生成 11 位唯一账号
        String account = generateUniqueAccount();

        // 昵称为空时自动生成（唯一），否则检查唯一性
        String nickname = request.getNickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = generateUniqueNickname(request.getPhone());
        } else if (userMapper.countByNickname(nickname) > 0) {
            throw new RuntimeException("该昵称已被使用");
        }

        User user = new User(
                account,
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                nickname,
                request.getPhone(),
                request.getEmail()
        );
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        try {
            userMapper.insert(user);
        } catch (RuntimeException e) {
            // 兜底：UNIQUE 约束拦截的重复（MyBatis 抛 PersistenceException，需查 cause 链）
            if (isDuplicateEntry(e, "nickname")) {
                throw new RuntimeException("该昵称已被使用");
            }
            throw new RuntimeException("注册失败，请稍后重试");
        }
        return user;
    }

    /**
     * 用户登录（手机号 / 账号 + 密码）
     */
    public User login(LoginRequest request) {
        User user = Optional.ofNullable(userMapper.selectByPhoneOrAccount(request.getAccount()))
                .orElseThrow(() -> new RuntimeException("账号或密码错误"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("账号或密码错误");
        }

        return user;
    }

    /**
     * 根据 ID 查找用户
     */
    public User findById(Long id) {
        return Optional.ofNullable(userMapper.selectById(id))
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    /**
     * 更新用户生日
     */
    public User updateBirthday(Long userId, LocalDate birthday) {
        User user = findById(userId);
        userMapper.updateBirthday(userId, birthday);
        user.setBirthday(birthday);
        return user;
    }

    /**
     * 通过原密码修改密码
     */
    public User changePasswordWithOld(Long userId, String oldPassword, String newPassword) {
        User user = findById(userId);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        String encodedPwd = passwordEncoder.encode(newPassword);
        userMapper.updatePassword(userId, encodedPwd);
        user.setPassword(encodedPwd);
        return user;
    }

    /**
     * 更新用户信息（昵称、姓名、手机号、生日、邮箱）
     */
    @Transactional(rollbackFor = Exception.class)
    public User updateProfile(Long userId, String name, String nickname, String phone, LocalDate birthday, String email) {
        User user = findById(userId);
        if (email != null && !email.equals(user.getEmail()) && userMapper.countByEmail(email) > 0) {
            throw new RuntimeException("该邮箱已被其他账号绑定");
        }
        if (nickname != null && !nickname.equals(user.getNickname()) && userMapper.countByNickname(nickname) > 0) {
            throw new RuntimeException("该昵称已被使用");
        }
        if (name != null) user.setName(name);
        if (nickname != null) user.setNickname(nickname);
        if (phone != null) user.setPhone(phone);
        if (birthday != null) user.setBirthday(birthday);
        if (email != null) user.setEmail(email);

        try {
            userMapper.updateProfile(user);
        } catch (RuntimeException e) {
            if (isDuplicateEntry(e, "nickname")) {
                throw new RuntimeException("该昵称已被使用");
            }
            throw new RuntimeException("更新失败，请稍后重试");
        }
        return user;
    }

    /**
     * 发送邮箱验证码（模拟：默认验证码为 1234）
     */
    public void sendVerificationCode(String email) {
        User user = Optional.ofNullable(userMapper.selectByEmail(email))
                .orElseThrow(() -> new RuntimeException("该邮箱未绑定账号"));
        // 模拟发送，验证码固定为 1234
        verificationCodes.put(email, "1234");
    }

    /**
     * 通过邮箱验证码修改密码（验证码由前端校验，后端直接执行更新）
     */
    public User changePasswordByEmail(String email, String newPassword) {
        User user = Optional.ofNullable(userMapper.selectByEmail(email))
                .orElseThrow(() -> new RuntimeException("该邮箱未绑定账号"));
        String encodedPwd = passwordEncoder.encode(newPassword);
        userMapper.updatePassword(user.getId(), encodedPwd);
        user.setPassword(encodedPwd);
        return user;
    }
}
