# Open_Lantu 官网

> 一个完整的社团官网项目，包含前端展示、用户系统、签到积分系统、排行榜与管理后台。
>
> **组织 Gitee：** [https://gitee.com/open_lanTuDream](https://gitee.com/open_lanTuDream)

---

## 项目结构

```
Open_Lantu/
├── lantu_web/                      # 前端（Vue 3 + Vite + TypeScript）
│   ├── src/
│   │   ├── api/                    # API 请求封装（axios）
│   │   │   └── index.ts            # 所有 API 接口定义
│   │   ├── assets/                 # 静态资源
│   │   ├── components/             # 公共组件
│   │   │   └── AdminUsersSection.vue  # 管理后台用户管理组件
│   │   ├── composables/            # 可复用逻辑
│   │   │   ├── useScrollReveal.ts  # 滚动入场动画
│   │   │   └── useAutoLogout.ts    # 无操作自动登出
│   │   ├── layouts/                # 页面外壳
│   │   │   └── AppLayout.vue       # 导航栏 + 用户菜单 + 页脚 + 回到顶部
│   │   ├── router/                 # 路由配置
│   │   │   └── index.ts            # 路由表 + 导航守卫
│   │   ├── stores/                 # 全局状态管理 (Pinia)
│   │   │   ├── auth.ts             # 用户认证状态（含角色/头像/个人信息）
│   │   │   └── app.ts              # 应用全局状态
│   │   ├── views/                  # 页面组件
│   │   │   ├── HomePage.vue        # 首页
│   │   │   ├── AboutPage.vue       # 关于我们
│   │   │   ├── ActivitiesPage.vue  # 活动
│   │   │   ├── ProjectsPage.vue    # 项目
│   │   │   ├── JoinPage.vue        # 加入我们（含预报名表单）
│   │   │   ├── LoginPage.vue       # 登录
│   │   │   ├── RegisterPage.vue    # 注册
│   │   │   ├── BirthdayPage.vue    # 设置生日（选填）
│   │   │   ├── RegistrationComplete.vue  # 注册成功
│   │   │   ├── ProfilePage.vue     # 个人中心（含签到积分展示）
│   │   │   ├── ProfileEditPage.vue # 修改个人信息
│   │   │   ├── ChangePwdPage.vue   # 修改密码（选择方式）
│   │   │   ├── ChangePwdOld.vue    # 原密码修改
│   │   │   ├── ChangePwdPhone.vue   # 邮箱验证修改
│   │   │   ├── CheckinPage.vue     # 每日签到（日历 + 积分 + 排行榜）
│   │   │   ├── AdminPage.vue       # 管理后台（仪表盘 + ECharts 图表）
│   │   │   └── NotFoundPage.vue    # 404 页面
│   │   ├── style.css               # 全局样式（动画、布局工具类）
│   │   ├── App.vue                 # 根组件
│   │   └── main.ts                 # 入口文件（注册全局组件）
│   ├── index.html
│   └── package.json
│
├── lantu_web_java/                 # 后端（Spring Boot 3 + MyBatis + MySQL + Redis）
│   └── src/main/java/com/example/lantu_web_java/
│       ├── config/                 # 配置
│       │   ├── SecurityConfig.java         # BCrypt 密码编码
│       │   ├── WebConfig.java              # CORS + 静态资源 + JWT 拦截器
│       │   ├── JwtAuthInterceptor.java     # JWT 令牌校验拦截器
│       │   └── DataInitializer.java        # 启动时自动创建角色表并初始化种子数据
│       ├── controller/             # API 控制器
│       │   ├── AuthController.java         # 认证（登录/注册/修改密码/个人资料）
│       │   ├── AvatarController.java       # 头像上传
│       │   ├── PreregistrationController.java  # 预报名
│       │   ├── CheckInController.java      # 签到系统（含排行榜）
│       │   └── AdminController.java        # 管理后台（仪表盘/用户管理/角色管理）
│       ├── dto/                    # 请求/响应数据传输对象
│       │   ├── ApiResponse.java             # 统一响应格式
│       │   ├── LoginRequest.java
│       │   ├── RegisterRequest.java
│       │   ├── ChangePasswordOldRequest.java
│       │   ├── ChangePasswordEmailRequest.java
│       │   └── PreregistrationRequest.java
│       ├── entity/                 # 数据实体
│       │   ├── User.java               # 用户（含 roleId/roleName）
│       │   ├── UserAvatar.java         # 头像记录
│       │   ├── Preregistration.java     # 预报名
│       │   ├── CheckIn.java            # 签到记录
│       │   └── Role.java               # 角色
│       ├── mapper/                 # MyBatis 数据访问接口
│       │   ├── UserMapper.java
│       │   ├── UserAvatarMapper.java
│       │   ├── PreregistrationMapper.java
│       │   ├── CheckInMapper.java
│       │   └── RoleMapper.java
│       ├── service/                # 业务逻辑层
│       │   ├── UserService.java
│       │   ├── AvatarService.java
│       │   ├── PreregistrationService.java
│       │   ├── CheckInService.java      # 签到积分逻辑
│       │   └── RankingService.java      # 排行榜（Redis ZSET + MySQL 回退）
│       └── util/
│           └── JwtUtil.java            # JWT 生成与验证
│
├── LantuAppMg/                    # 移动端管理 App（HarmonyOS + ArkTS）
│   ├── AppScope/                  # 应用级配置（bundleName、图标、版本）
│   ├── entry/                     # 主模块（src/main/ets + resources）
│   ├── build-profile.json5        # 构建配置
│   ├── oh-package.json5           # 依赖配置
│   └── hvigor/                    # 构建工具链
│
├── db/                             # 数据库建表 SQL
│   ├── users.sql                   # 用户表
│   ├── user_avatars.sql            # 头像表
│   ├── preregistrations.sql        # 预报名表
│   ├── check_ins.sql               # 签到记录表
│   ├── roles.sql                   # 角色表 + 种子数据
│   ├── alter_users_add_role.sql    # users 表添加 role_id 字段
│   └── migrate_nickname_unique.sql # 唯一索引迁移
│
├── userTX/                         # 用户头像上传存储目录
├── start.bat                       # Windows 快速启动脚本
├── README.md                       # 本文件（中文）
└── README.en.md                    # 英文文档
```

---

## 技术栈

### 前端

| 技术 | 用途 |
|------|------|
| Vue 3 + TypeScript | 框架 |
| Vite | 构建工具 |
| Vue Router 4 | 路由 |
| Pinia | 状态管理 |
| Axios | HTTP 请求 |
| Element Plus | UI 组件库（图标） |
| Naive UI | UI 组件库（导航、表单、消息、数据表格等） |
| ECharts | 管理后台数据可视化 |

### 后端

| 技术 | 用途 |
|------|------|
| Spring Boot 3.2 | Web 框架 |
| MyBatis | ORM / 数据访问 |
| Spring Validation | Bean 校验 |
| MySQL 8 | 关系型数据库 |
| Redis | 排行榜缓存（可选，无 Redis 自动回退 MySQL） |
| JWT (jjwt 0.12) | 认证令牌 |
| BCrypt | 密码加密 |
| Lombok | 注解简化 Java 实体/DTO 样板代码 |
| Maven | 构建管理 |

---

## 功能模块

### 👤 用户系统
- 手机号/账号 + 密码注册与登录
- 自动生成 11 位唯一数字账号
- JWT 令牌鉴权（24 小时有效期）
- 10 分钟无操作自动退出（基于用户活动事件监听）
- 个人信息编辑（姓名、昵称、手机号、邮箱、生日、头像上传）
- 两种方式修改密码（原密码 / 邮箱验证码）
- 登录态持久化到 `sessionStorage`（关闭标签页即退出）

### 📅 签到积分系统
- 每日签到：每次 +1 积分
- 连续 7 天：额外 +2 积分
- 连续 30 天：额外 +10 积分
- 个人中心展示积分概况
- 独立签到页面：日历视图、统计卡片、签到动画
- 积分排行榜（Redis ZSET 排序，Redis 不可用时自动回退 MySQL）

### 🏆 排行榜
- 全站积分排行榜（TopN 可配置）
- 当前用户排名查询
- 展示昵称、头像、积分、角色徽章
- Redis ZSET 实现高性能排序

### 👑 角色权限系统
- 角色：管理员 / 教师 / 学生 / 普通用户
- 应用启动时自动创建 `roles` 表和种子数据
- 管理后台仅限管理员（role_id = 1）访问
- 用户下拉菜单根据角色动态显示"管理后台"入口

### 📋 预报名系统
- 加入我们页面预报名表单
- 提交姓名、专业、年级、自我介绍

### ⚙️ 管理后台
- ECharts 仪表盘：总用户数、今日签到数、本月签到数、签到率图表
- 用户管理：分页列表、关键词搜索、角色切换、删除用户

---

## 快速开始

### 1. 准备工作

确保已安装：

- **Node.js** >= 18
- **Java** >= 17
- **Maven** >= 3.8
- **MySQL** >= 8.0
- **Redis**（可选，排行榜如无 Redis 会自动退化为 MySQL 查询）

### 2. 创建数据库

```sql
CREATE DATABASE lantu_web CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

数据库连接配置在 `lantu_web_java/src/main/resources/application.yml` 中，按需修改用户名和密码。

### 3. 初始化表结构

执行 `db/` 目录下的 SQL 文件创建所有数据表（按以下顺序）：

```bash
# 手动导入
mysql -u root -p lantu_web < db/users.sql
mysql -u root -p lantu_web < db/user_avatars.sql
mysql -u root -p lantu_web < db/preregistrations.sql
mysql -u root -p lantu_web < db/check_ins.sql
mysql -u root -p lantu_web < db/roles.sql
mysql -u root -p lantu_web < db/alter_users_add_role.sql
```

> 注意：`roles.sql` 和 `alter_users_add_role.sql` 也可由 `DataInitializer.java` 在应用启动时自动执行。

### 4. 配置 Redis（可选）

如启用排行榜 Redis 缓存，需确保 Redis 服务运行在 `localhost:6379`。Redis 连接配置在 `application.yml`：

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      timeout: 5000
```

### 5. 启动后端

```bash
cd lantu_web_java
mvn spring-boot:run
```

后端运行在 `http://localhost:8081`。

### 6. 启动前端

打开另一个终端：

```bash
cd lantu_web
npm install
npm run dev
```

前端运行在 `http://localhost:5173`。

### 7. 一键启动（Windows）

双击 `start.bat` 即可启动 Vite 开发服务器。

---

## 页面路由

| 路径 | 页面 | 说明 | 需登录 |
|------|------|------|--------|
| `/` | 首页 | Hero + 特点 + 活动 + 数据 + 动态 + 成员说 + 合作组织 | ❌ |
| `/about` | 关于我们 | 故事 + 使命愿景 + 发展历程 + 价值观 + 成就 | ❌ |
| `/activities` | 活动 | 活动列表（按分类筛选）+ 活动形式 + 参与方式 | ❌ |
| `/projects` | 项目 | 项目展示 + 技术栈标签 + 贡献指南 | ❌ |
| `/join` | 加入我们 | 加入理由 + 流程 + 成员心声 + 联系方式 + 预报名表单 | ❌ |
| `/login` | 登录 | 手机号/账号 + 密码登录 | ❌ |
| `/register` | 注册 | 填写信息注册账号 | ❌ |
| `/birthday` | 设置生日 | 注册后设置生日（选填） | ✅ |
| `/register-complete` | 注册成功 | 展示账号，30 秒倒计时后自动跳转首页 | ✅ |
| `/profile` | 个人中心 | 查看个人信息、头像、签到积分 | ✅ |
| `/profile/edit` | 修改信息 | 修改姓名/昵称/手机号/邮箱/生日/头像 | ✅ |
| `/checkin` | 每日签到 | 日历视图 + 签到按钮 + 积分统计 + 积分规则 + 排行榜 | ✅ |
| `/change-pwd` | 修改密码 | 选择验证方式 | ✅ |
| `/change-pwd/old` | 原密码改密 | 通过原密码修改密码 | ✅ |
| `/change-pwd/email` | 邮箱改密 | 通过邮箱验证修改密码 | ✅ |
| `/admin` | 管理后台 | 仪表盘 + 用户管理（仅管理员） | ✅ (管理员) |
| `/:pathMatch(.*)*` | 404 | 未匹配路径显示 404 页面 | ❌ |

---

## 后端 API

### 认证模块

| 方法 | 路径 | 说明 | 需认证 |
|------|------|------|--------|
| POST | `/api/auth/login` | 登录 | ❌ |
| POST | `/api/auth/register` | 注册 | ❌ |
| PUT | `/api/auth/birthday` | 设置生日 | ✅ |
| PUT | `/api/auth/profile` | 更新个人信息 | ✅ |
| PUT | `/api/auth/password/old` | 原密码修改密码 | ✅ |
| PUT | `/api/auth/password/email` | 邮箱验证修改密码 | ❌ |
| POST | `/api/auth/password/email/code` | 发送邮箱验证码（模拟） | ❌ |

### 头像

| 方法 | 路径 | 说明 | 需认证 |
|------|------|------|--------|
| POST | `/api/avatar/upload` | 上传头像 | ✅ |
| GET | `/api/avatar/{userId}` | 获取头像路径 | ❌ |

### 预报名

| 方法 | 路径 | 说明 | 需认证 |
|------|------|------|--------|
| POST | `/api/preregister` | 加入预报名 | ❌ |

### 签到系统

| 方法 | 路径 | 说明 | 需认证 |
|------|------|------|--------|
| POST | `/api/checkin` | 执行签到 | ✅ |
| GET | `/api/checkin/status` | 获取签到状态（总积分/连续天数/本月签到） | ✅ |
| GET | `/api/checkin/month?yearMonth=2026-06` | 获取指定月份签到日期列表 | ✅ |

### 排行榜

| 方法 | 路径 | 说明 | 需认证 |
|------|------|------|--------|
| GET | `/api/checkin/ranking?topN=20` | 获取积分排行榜 | ❌ |
| GET | `/api/checkin/ranking/me` | 获取当前用户排名 | ✅ |

### 管理后台

| 方法 | 路径 | 说明 | 需认证 |
|------|------|------|--------|
| GET | `/api/admin/dashboard` | 仪表盘统计（总用户/今日签到/本月签到） | ✅ (管理员) |
| GET | `/api/admin/users?page=1&size=20&keyword=` | 用户列表（分页+搜索） | ✅ (管理员) |
| GET | `/api/admin/users/{id}` | 用户详情 | ✅ (管理员) |
| PUT | `/api/admin/users/{id}/role` | 切换用户角色 | ✅ (管理员) |
| DELETE | `/api/admin/users/{id}` | 删除用户 | ✅ (管理员) |

所有 API 统一返回格式：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": { ... }
}
```

---

## 用户系统流程

```
注册 → 设置生日（选填） → 注册成功页（展示账号） → 首页
                                                ↓
                                          30秒后自动跳转
```

- 注册后自动登录（返回 JWT token，存 sessionStorage）
- 10 分钟无操作自动退出登录（监听 mousedown/keydown/touchstart/scroll/wheel）
- 支持通过原密码或邮箱两种方式修改密码
- 个人信息（含头像）可在个人中心编辑
- 每日签到获取积分，在个人中心可查看积分概况
- 管理后台仅限管理员角色（role_id = 1）访问

---

## 管理后台

管理后台入口 `/admin`，仅 `role_id = 1`（管理员）用户可见可访问。

### 仪表盘
- 总用户数统计
- 今日签到人数
- 本月签到总数
- 今日签到率（ECharts 半圆仪表盘）
- 近 7 天签到趋势（ECharts 柱状图）

### 用户管理
- 用户分页列表
- 关键词搜索（按姓名/昵称/手机号/邮箱）
- 角色切换下拉菜单
- 删除用户确认弹窗

---

## 积分规则

| 行为 | 积分 | 说明 |
|------|------|------|
| 每日签到 | +1 | 每天一次，UTC+8 自然日 |
| 连续 7 天签到 | +2 | 额外奖励 |
| 连续 30 天签到 | +10 | 额外奖励 |
| 总积分 | - | `SELECT SUM(points) FROM check_ins` 实时计算 |

---

## 常见操作

### 添加新页面

1. 在 `lantu_web/src/views/` 新建 `XxxPage.vue`
2. 在 `lantu_web/src/router/index.ts` 添加懒加载路由和导航守卫配置
3. 如需导航栏入口，在 `AppLayout.vue` 的 `navItems` 中添加

### 修改导航栏

编辑 `lantu_web/src/layouts/AppLayout.vue` 中的 `navItems` 数组。

### 使用滚动动画

在元素上添加 `class="reveal"`：

```html
<div class="reveal">从下往上淡入</div>
<div class="reveal reveal-delay-2">延迟 0.2s 出现</div>
```

其他动画类：`reveal-scale`（缩放）、`reveal-left`（从左）、`reveal-right`（从右）。

### 修改全局样式

编辑 `lantu_web/src/style.css`。

### 调整数据库连接

编辑 `lantu_web_java/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/lantu_web?...
    username: your_username
    password: your_password
```

---

## 团队约定

- 页面组件放 `src/views/`，公共组件放 `src/components/`
- 页面使用 `<script setup lang="ts">` 写法
- 新页面添加 `reveal` 动画类和响应式布局
- 不提交 `node_modules/`、`dist/`、`target/` 到版本库
- 后端统一使用 `ApiResponse<T>` 作为响应格式
- Controller 使用 try-catch 捕获异常并返回友好提示
- 数据库建表 SQL 统一放在 `db/` 目录，命名使用 `表名.sql`
- MyBatis Mapper XML 放在 `resources/mapper/` 目录
- 积分通过 `SELECT SUM(points) FROM check_ins` 实时计算，不冗余存储
- 前端登录态使用 `sessionStorage` 存储（关标签页即退出），非 `localStorage`
- 角色权限通过 `role_id` 字段控制，管理后台仅管理员可访问
- 排行榜优先使用 Redis ZSET，Redis 不可用时自动回退 MySQL 查询
