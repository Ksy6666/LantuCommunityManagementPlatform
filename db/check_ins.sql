-- ============================================
-- Open_Lantu 签到记录表
-- 对应实体：CheckIn.java
-- ============================================

CREATE TABLE IF NOT EXISTS check_ins (
    id             BIGINT       NOT NULL AUTO_INCREMENT   COMMENT '主键 ID',
    user_id        BIGINT       NOT NULL                  COMMENT '用户 ID',
    check_in_date  DATE         NOT NULL                  COMMENT '签到日期（每天仅一次）',
    points         INT          NOT NULL DEFAULT 1        COMMENT '本次获得的积分',
    created_at     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_date (user_id, check_in_date),
    INDEX idx_user_id (user_id),
    INDEX idx_check_in_date (check_in_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='签到记录表';

-- ============================================
-- 总积分通过 SUM(check_ins.points) 实时计算，无需额外字段
-- ============================================
