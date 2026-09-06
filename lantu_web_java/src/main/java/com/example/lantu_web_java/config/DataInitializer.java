package com.example.lantu_web_java.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 应用启动时初始化基础数据（角色等）
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final JdbcTemplate jdbcTemplate;

    public DataInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        initRoles();
    }

    private void initRoles() {
        // 1. 创建 roles 表（如果不存在）
        try {
            jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS roles (
                    id          BIGINT       NOT NULL AUTO_INCREMENT   COMMENT '主键 ID',
                    role_name   VARCHAR(50)  NOT NULL                  COMMENT '角色名称',
                    description VARCHAR(200) DEFAULT NULL              COMMENT '角色描述',
                    created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                    PRIMARY KEY (id),
                    UNIQUE KEY uk_role_name (role_name)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表'
            """);
        } catch (Exception e) {
            log.warn("创建 roles 表失败：{}", e.getMessage());
            return;
        }

        // 2. 检查 users 表是否有 role_id 列，没有则添加
        try {
            Integer colCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = (SELECT DATABASE()) AND TABLE_NAME = 'users' AND COLUMN_NAME = 'role_id'",
                Integer.class
            );
            if (colCount == null || colCount == 0) {
                jdbcTemplate.execute("ALTER TABLE users ADD COLUMN role_id BIGINT NOT NULL DEFAULT 4 COMMENT '角色 ID' AFTER birthday");
                jdbcTemplate.execute("ALTER TABLE users ADD INDEX idx_role_id (role_id)");
                log.info("users 表添加 role_id 列成功");
            }
        } catch (Exception e) {
            log.warn("检查/添加 role_id 列失败：{}", e.getMessage());
            return;
        }

        // 3. 将已有用户的 role_id 从 NULL 更新为默认值 4
        try {
            int updated = jdbcTemplate.update("UPDATE users SET role_id = 4 WHERE role_id IS NULL");
            if (updated > 0) {
                log.info("已更新 {} 个用户的 role_id 为默认值", updated);
            }
        } catch (Exception e) {
            log.warn("更新已有用户 role_id 失败：{}", e.getMessage());
        }

        // 4. 插入角色种子数据（如果为空）
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM roles", Integer.class);
            if (count != null && count > 0) {
                return;
            }
        } catch (Exception e) {
            log.warn("查询 roles 表失败：{}", e.getMessage());
            return;
        }

        try {
            jdbcTemplate.update("INSERT INTO roles (role_name, description) VALUES (?, ?)",
                    "管理员", "系统管理员，拥有所有权限");
            jdbcTemplate.update("INSERT INTO roles (role_name, description) VALUES (?, ?)",
                    "教师", "教师用户");
            jdbcTemplate.update("INSERT INTO roles (role_name, description) VALUES (?, ?)",
                    "学生", "学生用户");
            jdbcTemplate.update("INSERT INTO roles (role_name, description) VALUES (?, ?)",
                    "普通用户", "普通注册用户");
            log.info("角色数据初始化完成");
        } catch (Exception e) {
            log.warn("角色数据初始化失败：{}", e.getMessage());
        }
    }
}
