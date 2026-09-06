-- ============================================
-- Open_Lantu 角色表
-- ============================================

CREATE TABLE IF NOT EXISTS roles (
    id          BIGINT       NOT NULL AUTO_INCREMENT   COMMENT '主键 ID',
    role_name   VARCHAR(50)  NOT NULL                  COMMENT '角色名称',
    description VARCHAR(200) DEFAULT NULL              COMMENT '角色描述',
    created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_name (role_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- ============================================
-- 种子数据
-- ============================================
INSERT INTO roles (role_name, description) VALUES
    ('管理员', '系统管理员，拥有所有权限'),
    ('教师', '教师用户'),
    ('学生', '学生用户'),
    ('普通用户', '普通注册用户')
ON DUPLICATE KEY UPDATE role_name = VALUES(role_name);
