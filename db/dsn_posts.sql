-- ============================================
-- Open_Lantu 技术讨论帖子表（库：lantu_web_dsn）
-- 对应实体：Post.java
-- ============================================

CREATE TABLE IF NOT EXISTS posts (
    id             BIGINT       NOT NULL AUTO_INCREMENT   COMMENT '主键 ID',
    user_id        BIGINT       NOT NULL                  COMMENT '发布者用户 ID',
    title          VARCHAR(200) NOT NULL                  COMMENT '标题',
    content        TEXT         NOT NULL                  COMMENT '正文内容',
    category       VARCHAR(50)  NOT NULL DEFAULT '技术分享' COMMENT '分类：技术分享/技术问答/经验交流等',
    tags           VARCHAR(200) DEFAULT NULL              COMMENT '标签，逗号分隔',
    view_count     INT          NOT NULL DEFAULT 0        COMMENT '浏览量',
    like_count     INT          NOT NULL DEFAULT 0        COMMENT '点赞数',
    favorite_count INT          NOT NULL DEFAULT 0        COMMENT '收藏数',
    comment_count  INT          NOT NULL DEFAULT 0        COMMENT '回答/评论数',
    status         TINYINT      NOT NULL DEFAULT 1        COMMENT '状态：1正常 0已删除',
    created_at     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_user_id (user_id),
    INDEX idx_category (category),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='技术讨论帖子表';
