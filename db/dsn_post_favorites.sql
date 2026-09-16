-- ============================================
-- Open_Lantu 技术讨论收藏表（库：lantu_web_dsn）
-- 对应实体：PostFavorite.java
-- 同一用户对同一帖子只能收藏一次（uk_post_user 唯一约束）
-- ============================================

CREATE TABLE IF NOT EXISTS post_favorites (
    id         BIGINT   NOT NULL AUTO_INCREMENT   COMMENT '主键 ID',
    post_id    BIGINT   NOT NULL                  COMMENT '帖子 ID',
    user_id    BIGINT   NOT NULL                  COMMENT '收藏用户 ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_post_user (post_id, user_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='技术讨论收藏表';
