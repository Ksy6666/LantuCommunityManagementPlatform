-- ============================================
-- users 表添加 role_id 字段
-- 默认角色：普通用户（id=4）
-- ============================================

ALTER TABLE users
ADD COLUMN role_id BIGINT NOT NULL DEFAULT 4 COMMENT '角色 ID' AFTER birthday;

ALTER TABLE users
ADD INDEX idx_role_id (role_id);
