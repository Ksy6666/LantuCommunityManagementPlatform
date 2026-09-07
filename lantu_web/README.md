# lantu_web — 前端项目

> Open_Lantu 社团官网前端项目，基于 Vue 3 + TypeScript + Vite 构建。

## 技术栈

| 技术 | 用途 |
|------|------|
| Vue 3 + TypeScript | 框架（Composition API + `<script setup>`） |
| Vite | 构建工具（开发代理配置到后端 8081） |
| Vue Router 4 | 路由（懒加载 + 导航守卫） |
| Pinia | 状态管理（setup store 语法） |
| Axios | HTTP 请求封装 |
| Naive UI | 主要 UI 组件库 |
| Element Plus | 图标库 |
| ECharts | 管理后台图表 |

## 项目结构

```
src/
├── api/                # Axios API 封装（所有接口定义）
├── assets/             # 静态资源
├── components/         # 公共组件
├── composables/        # 可复用逻辑
│   ├── useScrollReveal.ts   # 滚动入场动画（IntersectionObserver）
│   └── useAutoLogout.ts     # 无操作 10 分钟自动登出
├── layouts/            # 页面外壳（导航栏 + 页脚 + 回到顶部）
├── router/             # 路由配置 + 导航守卫
├── stores/             # Pinia 全局状态
│   ├── auth.ts         # 用户认证状态
│   └── app.ts          # 应用配置
├── views/              # 页面组件（17 个页面）
├── style.css           # 全局样式
├── App.vue             # 根组件
└── main.ts             # 入口文件
```

## 页面列表

| 路径 | 组件 | 说明 |
|------|------|------|
| `/` | HomePage.vue | 首页 |
| `/about` | AboutPage.vue | 关于我们 |
| `/activities` | ActivitiesPage.vue | 活动 |
| `/projects` | ProjectsPage.vue | 项目 |
| `/join` | JoinPage.vue | 加入我们（含预报名表单） |
| `/login` | LoginPage.vue | 登录 |
| `/register` | RegisterPage.vue | 注册 |
| `/birthday` | BirthdayPage.vue | 设置生日（需登录） |
| `/register-complete` | RegistrationComplete.vue | 注册成功（需登录） |
| `/profile` | ProfilePage.vue | 个人中心（需登录） |
| `/profile/edit` | ProfileEditPage.vue | 修改个人信息（需登录） |
| `/checkin` | CheckinPage.vue | 每日签到 + 排行榜（需登录） |
| `/change-pwd` | ChangePwdPage.vue | 修改密码（需登录） |
| `/change-pwd/old` | ChangePwdOld.vue | 原密码修改（需登录） |
| `/change-pwd/email` | ChangePwdPhone.vue | 邮箱验证修改（需登录） |
| `/admin` | AdminPage.vue | 管理后台（需管理员） |
| `/:pathMatch(.*)*` | NotFoundPage.vue | 404 页面 |

## 开发

```bash
# 安装依赖
npm install

# 启动开发服务器（Vite，端口 5173）
npm run dev

# 生产构建
npm run build

# 预览构建结果
npm run preview
```

开发服务器已配置 Vite Proxy，将 `/api` 和 `/avatars` 请求转发到 `http://localhost:8081`（后端）。

> 项目还配置了 `allowedHosts`（如 `68356lmot458.vicp.fun`）以支持内网穿透访问开发服务器。

## 构建

```bash
npm run build
# 或
npm run build  # 包含 vue-tsc 类型检查
```

详细项目说明请参阅根目录 [README.md](../README.md)。
