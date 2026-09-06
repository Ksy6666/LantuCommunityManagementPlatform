-- ============================================
-- Open_Lantu 用户头像表
-- 与 users 表通过 user_id 外键关联
-- 图片文件存储在 userTX/ 目录下
-- 由 JPA ddl-auto: update 自动建表，也可手动执行此脚本
-- ============================================

CREATE TABLE IF NOT EXISTS user_avatars (
    id          BIGINT       NOT NULL AUTO_INCREMENT   COMMENT '主键 ID',
    user_id     BIGINT       NOT NULL                  COMMENT '用户 ID（关联 users.id）',
    file_path   VARCHAR(255) NOT NULL                  COMMENT '头像文件存储路径',
    created_at  DATETIME     DEFAULT NULL              COMMENT '创建时间',
    PRIMARY KEY (id),
    CONSTRAINT fk_avatar_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户头像表';
