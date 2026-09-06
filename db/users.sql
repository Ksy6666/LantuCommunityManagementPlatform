-- ============================================
-- Open_Lantu 用户表
-- 对应实体：User.java
-- 由 JPA ddl-auto: update 自动建表，也可手动执行此脚本
-- ============================================

CREATE TABLE IF NOT EXISTS users (
    id          BIGINT       NOT NULL AUTO_INCREMENT   COMMENT '主键 ID',
    account     VARCHAR(50)  NOT NULL                  COMMENT '随机 11 位数字账号（唯一，登录用）',
    password    VARCHAR(100) NOT NULL                  COMMENT '密码（BCrypt 加密）',
    name        VARCHAR(50)  NOT NULL                  COMMENT '姓名',
    nickname    VARCHAR(50)  DEFAULT NULL              COMMENT '昵称',
    phone       VARCHAR(20)  NOT NULL                  COMMENT '手机号',
    email       VARCHAR(100) DEFAULT NULL              COMMENT '邮箱',
    birthday    DATE         DEFAULT NULL              COMMENT '生日',
    created_at  DATETIME     DEFAULT NULL              COMMENT '创建时间',
    updated_at  DATETIME     DEFAULT NULL              COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_account (account),
    UNIQUE KEY uk_nickname (nickname)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
