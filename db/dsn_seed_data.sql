-- ============================================
-- Open_Lantu 技术讨论种子数据（库：lantu_web_dsn）
-- 演示用中文示例数据（utf8mb4）
-- ============================================

USE lantu_web_dsn;

-- 清空旧数据（历史乱码数据清理）
DELETE FROM post_likes;
DELETE FROM post_favorites;
DELETE FROM comments;
DELETE FROM posts;
ALTER TABLE posts AUTO_INCREMENT = 1;
ALTER TABLE comments AUTO_INCREMENT = 1;
ALTER TABLE post_likes AUTO_INCREMENT = 1;
ALTER TABLE post_favorites AUTO_INCREMENT = 1;

-- 示例帖子（user_id = 1 即管理员 123）
INSERT INTO posts (user_id, title, content, category, tags, view_count, like_count, favorite_count, comment_count)
VALUES
(1, 'Spring Boot 自动配置原理深度解析', 'Spring Boot 的自动配置（AutoConfiguration）是基于条件注解实现的。核心机制如下：\n\n1. 通过 spring.factories / AutoConfiguration.imports 文件注册自动配置类\n2. 自动配置类使用 @ConditionalOnClass、@ConditionalOnMissingBean 等条件注解判断是否生效\n3. 用户自定义 Bean 优先于自动配置的默认 Bean\n\n【常见问题】\n- 自动配置生效顺序是怎样的？\n- 如何自定义 Starter？\n- @ConditionalOnMissingBean 的失效场景？\n\n欢迎大家交流讨论！', '技术分享', 'Spring Boot,Java,自动配置', 128, 15, 8, 3),
(1, 'Vue 3 中 setup 语法糖的最佳实践', '使用 <script setup> 语法糖可以大幅简化 Vue 3 组件写法。几个实践要点：\n\n- 使用 ref/reactive 管理响应式状态\n- 使用 computed 处理派生数据\n- 使用 watch/watchEffect 监听变化\n- 组件间通信优先使用 provide/inject 或 Pinia\n\n【问题】setup 语法糖中如何获取路由参数和当前实例？', '技术问答', 'Vue3,前端,TypeScript', 89, 10, 5, 2),
(1, 'MySQL 索引优化实战：从慢查询到毫秒级', '昨天优化了一个慢查询，记录一下思路：\n\n1. 先用 EXPLAIN 分析执行计划，发现全表扫描\n2. 查看 WHERE/JOIN/ORDER BY 涉及的列，建立联合索引\n3. 注意索引失效的场景：函数包裹、隐式类型转换、LIKE 前置通配符\n4. 必要时使用覆盖索引避免回表\n\n优化后查询时间从 2.3s 降到 40ms，效果显著！', '经验交流', 'MySQL,数据库,索引', 256, 32, 12, 5),
(1, 'Redis 在缓存场景下的缓存击穿与雪崩处理', '缓存穿透、击穿、雪崩是 Redis 缓存三大经典问题：\n\n- 穿透：查不到的数据，用布隆过滤器或缓存空值\n- 击穿：热点 key 过期，用互斥锁或逻辑过期\n- 雪崩：大量 key 同时过期，过期时间加随机值\n\n【讨论】你们在生产环境还用了哪些兜底策略？', '技术问答', 'Redis,缓存,高并发', 45, 8, 4, 1),
(1, 'Java 17 新特性速览与迁移建议', 'Java 17 是 LTS 版本，值得关注的新特性：\n\n- 密封类（Sealed Classes）\n- 模式匹配（Pattern Matching for instanceof）\n- 增强的伪随机数生成器\n- 新 macOS 渲染管线\n\n迁移建议：先用 jdeps 分析依赖，重点检查反射和模块化问题。', '技术分享', 'Java,后端,JVM', 76, 12, 6, 2);

-- 示例评论（parent_id = 0 为直接回答，> 0 为回复）
INSERT INTO comments (post_id, user_id, parent_id, content, like_count)
VALUES
(1, 1, 0, '自动装配的核心是 AutoConfiguration.imports 文件，Spring Boot 启动时通过 SpringFactoriesLoader 加载。', 5),
(1, 1, 1, '补充一点：@EnableAutoConfiguration 也是通过这个机制实现的。', 2),
(2, 1, 0, 'setup 语法糖中可以用 useRoute() 获取路由，useRouter() 获取路由实例。', 3),
(3, 1, 0, '联合索引要遵循最左前缀原则，否则索引会失效。', 4),
(3, 1, 4, '没错，还要注意区分范围查询和等值查询。', 1),
(5, 1, 0, '建议先迁移到 11 再迁移到 17，一步到位风险较大。', 2);

-- 示例点赞（user_id=1 已点赞帖子 1）
INSERT INTO post_likes (post_id, user_id) VALUES (1, 1);

-- 示例收藏（user_id=1 已收藏帖子 3）
INSERT INTO post_favorites (post_id, user_id) VALUES (3, 1);
