-- ============================================
-- Open_Lantu 预报名申请表
-- 核心字段：id（主键）、name（姓名）
-- 由 JPA ddl-auto: update 自动建表，也可手动执行此脚本
-- ============================================

CREATE TABLE IF NOT EXISTS preregistrations (
    id          BIGINT       NOT NULL AUTO_INCREMENT   COMMENT '主键 ID',
    name        VARCHAR(50)  NOT NULL                  COMMENT '姓名（必填）',
    major       VARCHAR(50)  NOT NULL                  COMMENT '专业（必填）',
    grade       VARCHAR(20)  NOT NULL                  COMMENT '年级（必填）',
    intro       TEXT         DEFAULT NULL              COMMENT '简短自我介绍',
    created_at  DATETIME     DEFAULT NULL              COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预报名表';
