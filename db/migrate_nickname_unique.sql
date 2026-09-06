-- ============================================
-- 昵称唯一性迁移脚本
-- 用途：为已有数据库添加 nickname 唯一约束
-- ============================================

-- Step 1: 查看哪些昵称重复了
-- SELECT nickname, GROUP_CONCAT(id) AS ids, COUNT(*) AS cnt
-- FROM users
-- WHERE nickname IS NOT NULL
-- GROUP BY nickname
-- HAVING COUNT(*) > 1;

-- Step 2: 给重复昵称的第二条起追加随机后缀，保留最早的一条不变
UPDATE users u
JOIN (
    SELECT id, nickname,
           ROW_NUMBER() OVER (PARTITION BY nickname ORDER BY id) AS rn
    FROM users
    WHERE nickname IS NOT NULL
) t ON u.id = t.id
SET u.nickname = CONCAT(u.nickname, '_', FLOOR(100 + RAND() * 900))
WHERE t.rn > 1;

-- Step 3: 添加唯一约束
ALTER TABLE users ADD UNIQUE KEY uk_nickname (nickname);
