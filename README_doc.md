# Open_Lantu 官网

#### 项目简介
Open_Lantu 是一个面向学生技术组织的完整社团官网项目，包含前端展示、用户系统、签到积分系统、排行榜与管理后台。

- **前端**：Vue 3 + TypeScript + Vite + Pinia + Naive UI + ECharts
- **后端**：Spring Boot 3.2 + MyBatis + MySQL + Redis（可选）
- **认证**：JWT + BCrypt 密码加密
- **源码仓库**：[https://gitee.com/open_lanTuDream](https://gitee.com/open_lanTuDream)

---

#### 软件架构

```
Open_Lantu/
├── lantu_web/              # 前端项目（Vue 3 + Vite + TypeScript）
│   ├── src/
│   │   ├── api/            # Axios API 封装
│   │   ├── components/     # 公共组件
│   │   ├── composables/    # 可复用逻辑（滚动动画/自动登出）
│   │   ├── layouts/        # 页面布局外壳
│   │   ├── router/         # 路由配置 + 导航守卫
│   │   ├── stores/         # Pinia 状态管理
│   │   └── views/          # 页面组件（17 个页面）
│   └── package.json
│
├── lantu_web_java/         # 后端项目（Spring Boot 3 + MyBatis + MySQL + Redis）
│   └── src/main/java/.../
│       ├── config/         # 配置（BCrypt/CORS/JWT 拦截器/数据初始化）
│       ├── controller/     # API 控制器（5 个）
│       ├── dto/            # 数据传输对象
│       ├── entity/         # 数据实体（5 个）
│       ├── mapper/         # MyBatis 数据访问（5 个）
│       ├── service/        # 业务逻辑（5 个）
│       └── util/           # 工具类
│
├── LantuAppMg/             # 移动端管理 App（HarmonyOS + ArkTS）
│   ├── AppScope/           # 应用级配置（bundleName、图标、版本）
│   ├── entry/              # 主模块（ets + resources）
│   ├── build-profile.json5 # 构建配置
│   └── oh-package.json5    # 依赖配置
│
├── db/                     # 数据库建表 SQL（7 个）
├── userTX/                 # 用户头像上传目录
└── start.bat               # Windows 一键启动脚本
```

---

#### 安装教程

**环境要求：**
- Node.js >= 18
- Java >= 17
- Maven >= 3.8
- MySQL >= 8.0
- Redis（可选，排行榜缓存）

**1. 创建数据库：**
```sql
CREATE DATABASE lantu_web CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

**2. 初始化表结构：**
```bash
mysql -u root -p lantu_web < db/users.sql
mysql -u root -p lantu_web < db/user_avatars.sql
mysql -u root -p lantu_web < db/preregistrations.sql
mysql -u root -p lantu_web < db/check_ins.sql
mysql -u root -p lantu_web < db/roles.sql
mysql -u root -p lantu_web < db/alter_users_add_role.sql
```

**3. 配置数据库连接：**
编辑 `lantu_web_java/src/main/resources/application.yml`，修改 MySQL 用户名和密码。

**4. 启动后端：**
```bash
cd lantu_web_java
mvn spring-boot:run
```
后端运行在 `http://localhost:8081`。

**5. 启动前端：**
```bash
cd lantu_web
npm install
npm run dev
```
前端运行在 `http://localhost:5173`。

---

#### 功能模块

| 模块 | 说明 |
|------|------|
| 👤 用户系统 | 注册/登录、JWT 认证、个人信息编辑、头像上传、密码修改 |
| 📅 签到积分 | 每日签到、连续签到奖励、日历视图、积分统计 |
| 🏆 排行榜 | 全站积分排名、Redis 缓存、MySQL 自动回退 |
| 👑 角色权限 | 管理员/教师/学生/普通用户，控制后台访问 |
| 📋 预报名 | 加入社团预报名表单 |
| ⚙️ 管理后台 | ECharts 仪表盘、用户管理、角色管理 |

---

#### 页面列表

| 路径 | 页面 | 权限 |
|------|------|------|
| `/` | 首页 | 公开 |
| `/about` | 关于我们 | 公开 |
| `/activities` | 活动 | 公开 |
| `/projects` | 项目 | 公开 |
| `/join` | 加入我们 | 公开 |
| `/login` | 登录 | 公开 |
| `/register` | 注册 | 公开 |
| `/profile` | 个人中心 | 登录 |
| `/checkin` | 每日签到 | 登录 |
| `/admin` | 管理后台 | 管理员 |

---

#### 技术栈详情

**前端：**
- Vue 3 + Composition API + `<script setup lang="ts">`
- Vite 构建工具
- Vue Router 4（懒加载路由、导航守卫）
- Pinia 状态管理（setup store 语法）
- Naive UI 组件库 + Element Plus 图标
- ECharts 数据可视化
- Axios HTTP 请求（请求拦截器自动携带 JWT）

**后端：**
- Spring Boot 3.2（JDK 17+）
- MyBatis + MySQL 8（Mapper XML + 注解混合）
- JWT 认证（jjwt 0.12，HMAC-SHA256 签名，24h 过期）
- BCrypt 密码加密（Spring Security Crypto）
- Lombok（注解简化实体与 DTO 样板代码）
- Redis（排行榜 ZSET 缓存，可选依赖）
- 统一响应格式 `ApiResponse<T>`

---

#### 参与贡献

1. Fork 本仓库
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request

详细文档请参阅 [README.md](README.md) 和 [README.en.md](README.en.md)。
