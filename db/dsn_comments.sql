-- ============================================
-- Open_Lantu 技术讨论评论/回答表（库：lantu_web_dsn）
-- 对应实体：Comment.java
-- parent_id = 0 表示直接回答；> 0 表示对某条评论的回复
-- ============================================

CREATE TABLE IF NOT EXISTS comments (
    id         BIGINT   NOT NULL AUTO_INCREMENT   COMMENT '主键 ID',
    post_id    BIGINT   NOT NULL                  COMMENT '帖子 ID',
    user_id    BIGINT   NOT NULL                  COMMENT '评论用户 ID',
    parent_id  BIGINT   NOT NULL DEFAULT 0        COMMENT '父评论 ID，0 表示直接回答',
    content    TEXT     NOT NULL                  COMMENT '评论内容',
    like_count INT      NOT NULL DEFAULT 0        COMMENT '评论点赞数',
    status     TINYINT  NOT NULL DEFAULT 1        COMMENT '状态：1正常 0已删除',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_post_id (post_id),
    INDEX idx_user_id (user_id),
    INDEX idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='技术讨论评论/回答表';
