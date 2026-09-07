# 蓝图管理员控制APP

> 基于HarmonyOS开发的蓝图社团管理员移动端控制应用，提供用户管理、数据统计、签到查看等功能。

---

## 项目简介

本项目是[蓝图社团官网项目](../README.md)的移动端管理员应用，采用HarmonyOS原生开发，为管理员提供便捷的移动端管理能力。管理员可以通过此APP实时查看社团数据、管理用户信息、调整用户权限等。

### 主要功能

- **登录认证**：管理员账号密码登录，JWT Token认证
- **数据仪表盘**：实时查看用户总数、签到统计、签到率等关键指标
- **用户管理**：用户列表查看、搜索、角色调整、删除用户
- **权限控制**：仅限管理员角色访问，确保数据安全

---

## 技术栈

| 技术 | 说明 |
|------|------|
| HarmonyOS | 华为鸿蒙操作系统 |
| ArkTS | HarmonyOS应用开发语言（TypeScript扩展） |
| ArkUI | HarmonyOS声明式UI框架 |
| @kit.NetworkKit | 网络请求能力 |
| @kit.ArkData | 数据存储能力（Preferences） |

---

## 项目结构

```
LantuAppMg/
├── AppScope/                           # 应用级配置（bundleName、图标、版本）
│   └── resources/base/media/           # 应用图标资源
├── entry/                              # 主模块
│   ├── src/main/ets/
│   │   ├── common/                     # 公共模块
│   │   │   ├── constants/              # 常量定义
│   │   │   │   └── Constants.ets       # 常量、角色枚举（BASE_URL/角色）
│   │   │   ├── utils/                  # 工具类
│   │   │   │   ├── HttpUtil.ets        # HTTP请求封装
│   │   │   │   └── PreferencesUtil.ets # 本地存储工具
│   │   │   └── api/                    # API接口
│   │   │       └── Api.ets             # 所有API接口定义
│   │   ├── model/                      # 数据模型
│   │   │   ├── User.ets                # 用户相关模型
│   │   │   ├── Dashboard.ets           # 仪表盘数据模型
│   │   │   └── ApiResponse.ets         # API响应模型
│   │   ├── pages/                      # 页面组件
│   │   │   ├── Index.ets               # 启动页（登录检查）
│   │   │   ├── LoginPage.ets           # 登录页面
│   │   │   ├── MainPage.ets            # 主页面（Tab导航）
│   │   │   ├── DashboardPage.ets       # 仪表盘页（MainPage Tab 内容）
│   │   │   └── UserManagePage.ets      # 用户管理页（MainPage Tab 内容）
│   │   ├── entryability/               # 应用入口
│   │   │   └── EntryAbility.ets        # Ability生命周期
│   │   └── entrybackupability/         # 备份能力
│   │       └── EntryBackupAbility.ets  # 应用备份/恢复
│   ├── src/main/resources/             # 资源文件（element/media/profile）
│   │   └── base/profile/               # main_pages.json / network_config.json 等
│   ├── src/main/module.json5           # 模块配置（权限、入口、网络）
│   ├── src/mock/mock-config.json5      # 本地 Mock 配置
│   ├── src/ohosTest/                   # 端到端测试
│   └── src/test/                       # 单元测试
├── hvigor/                             # 构建脚本工具链
├── build-profile.json5                 # 构建配置
├── code-linter.json5                   # 代码检查规则
├── oh-package.json5                    # 依赖配置
└── README.md                           # 本文档
```

---

## 快速开始

### 前置要求

1. **DevEco Studio**：已安装DevEco Studio 3.1或更高版本
2. **HarmonyOS SDK**：API Version 6.0.2(22)或更高
3. **后端服务**：确保Spring Boot后端服务正在运行

### 启动后端服务

```bash
# 进入后端项目目录
cd ../lantu_web_java

# 启动Spring Boot应用
mvn spring-boot:run
```

后端服务默认运行在 `http://localhost:8081`

### 运行APP

1. 使用DevEco Studio打开项目 `LantuAppMg`
2. 连接HarmonyOS设备或启动模拟器
3. 点击运行按钮启动应用

### 默认管理员账号

首次使用需要通过Web端创建管理员账号，或使用数据库中已有的管理员账号登录。

---

## 配置说明

### API地址配置

如需修改后端API地址，编辑 `entry/src/main/ets/common/constants/Constants.ets`：

```typescript
export class Constants {
  static readonly BASE_URL: string = 'http://10.0.2.2:8081/api';  // 模拟器访问宿主机：10.0.2.2 指向宿主机 localhost
  static readonly TOKEN_KEY: string = 'auth_token';
  static readonly USER_INFO_KEY: string = 'user_info';
  static readonly TIMEOUT: number = 30000;  // 请求超时时间（毫秒）
}
```

### 角色定义

系统支持四种角色：

| 角色ID | 角色名称 | 说明 |
|--------|----------|------|
| 1 | 管理员 | 拥有所有权限，可访问管理后台 |
| 2 | 教师 | 教师角色 |
| 3 | 学生 | 学生角色 |
| 4 | 普通用户 | 默认角色 |

---

## API接口说明

### 认证接口

#### 登录
```
POST /api/auth/login
请求体：{ account: string, password: string }
响应：{ code: 200, message: "成功", data: { token: string, user: User } }
```

### 管理接口

#### 获取仪表盘数据
```
GET /api/admin/dashboard
需要认证：是
响应：{ code: 200, data: { totalUsers, todayCheckins, monthCheckins, todayCheckinRate } }
```

#### 获取用户列表
```
GET /api/admin/users?page=1&size=20&keyword=
需要认证：是
响应：{ code: 200, data: { users: User[], total, page, size } }
```

#### 更新用户角色
```
PUT /api/admin/users/{id}/role
请求体：{ roleId: number }
需要认证：是
```

#### 删除用户
```
DELETE /api/admin/users/{id}
需要认证：是
```

---

## 页面说明

### 1. 启动页（Index.ets）

- **功能**：应用启动时检查登录状态
- **逻辑**：
  - 检查本地存储的Token
  - Token存在 → 跳转主页面
  - Token不存在 → 跳转登录页面

### 2. 登录页（LoginPage.ets）

- **功能**：管理员登录
- **特性**：
  - 账号密码输入
  - 密码显示/隐藏切换
  - 登录验证（仅管理员角色可登录）
  - 登录成功后保存Token和用户信息

### 3. 主页面（MainPage.ets）

- **功能**：应用主界面
- **布局**：
  - 顶部导航栏（应用标题、用户信息、退出按钮）
  - Tab导航（仪表盘、用户管理）
- **特性**：
  - Tab切换
  - 退出登录确认对话框

### 4. 仪表盘页（DashboardPage.ets，MainPage Tab 内容页）

- **功能**：数据统计展示
- **展示内容**：
  - 用户总数卡片
  - 今日签到卡片
  - 本月签到卡片
  - 今日签到率（环形进度条）
  - 签到统计对比（今日 vs 本月日均）

### 5. 用户管理页（UserManagePage.ets，MainPage Tab 内容页）

- **功能**：用户信息管理
- **特性**：
  - 用户列表展示（分页）
  - 关键词搜索（姓名/昵称/手机号/邮箱）
  - 角色切换（管理员/教师/学生/普通用户）
  - 删除用户（带确认对话框）
  - 角色颜色标识（管理员-红色、教师-蓝色、学生-绿色、普通用户-灰色）

---

## 开发指南

### 添加新页面

> 说明：路由只需注册「独立页面」。当前 `DashboardPage`、`UserManagePage` 是 `MainPage` 通过 Tab 内嵌的内容页，不在 `main_pages.json` 中单独注册；启动页、登录页、主页面注册如下：

1. 在 `entry/src/main/ets/pages/` 创建新页面文件
2. 在 `entry/src/main/resources/base/profile/main_pages.json` 注册路由：

```json
{
  "src": [
    "pages/Index",
    "pages/LoginPage",
    "pages/MainPage",
    "pages/NewPage"  // 添加新独立页面（若为 Tab 内容页则无需注册）
  ]
}
```

3. 使用路由跳转：

```typescript
import { router } from '@kit.ArkUI';

// 跳转页面
router.pushUrl({ url: 'pages/NewPage' });

// 替换当前页面
router.replaceUrl({ url: 'pages/NewPage' });
```

### 添加新API接口

在 `entry/src/main/ets/common/api/Api.ets` 中添加：

```typescript
export class NewService {
  static async getData(): Promise<ApiResponse<SomeData>> {
    return await HttpUtil.get<SomeData>('/new/endpoint');
  }

  static async postData(data: SomeRequest): Promise<ApiResponse<SomeData>> {
    return await HttpUtil.post<SomeData>('/new/endpoint', data);
  }
}
```

### 使用本地存储

```typescript
import { PreferencesUtil } from '../common/utils/PreferencesUtil';
import { Constants } from '../common/constants/Constants';

// 保存数据
await PreferencesUtil.put('key', 'value');
await PreferencesUtil.put('user', userObject);

// 读取数据
const value = await PreferencesUtil.get('key', 'default');
const user = await PreferencesUtil.getObject<User>('user');

// 删除数据
await PreferencesUtil.delete('key');

// 清空所有数据
await PreferencesUtil.clear();
```

### 显示提示消息

```typescript
import { promptAction } from '@kit.ArkUI';

// 显示Toast
promptAction.showToast({ message: '操作成功' });

// 显示对话框
promptAction.showDialog({
  title: '提示',
  message: '确定要执行此操作吗？',
  buttons: [
    { text: '取消', color: '#999999' },
    { text: '确定', color: '#1890ff' }
  ]
});
```

---

## 注意事项

### 1. 权限要求

- 本应用仅限**管理员角色（roleId = 1）**登录使用
- 普通用户、教师、学生角色无法登录管理端
- 所有API请求都需要携带JWT Token进行认证

### 2. 网络配置

- 确保设备与后端服务在同一网络
- 如使用真机调试，需将 `localhost` 改为实际IP地址
- HTTP请求超时时间默认为30秒

### 3. 数据安全

- Token和用户信息存储在本地Preferences中
- 退出登录会清除所有本地认证信息
- 建议在生产环境使用HTTPS

### 4. ArkTS开发规范

- 不支持使用 `any` 或 `unknown` 类型
- 不支持类型断言 `as`
- 不支持动态属性访问 `obj[key]`
- 对象字面量必须显式声明类型
- 静态方法中不能使用 `this`

### 5. 已知限制

- 部分 `@kit.ArkUI` API 在新 SDK 中标记废弃（如 `router.replaceUrl()`、`promptAction.showToast()`），项目当前仍在使用旧 API，升级 SDK 时需同步迁移到新 API
- 对话框通过 `@State` 布尔标志控制的自定义组件实现（`DeleteDialog`/`RoleDialog`/`LogoutDialog`），而非系统 `showDialog` API

---

## 常见问题

### Q1: 登录失败提示"权限不足"

**原因**：当前账号不是管理员角色

**解决**：
1. 使用Web端登录管理员账号
2. 或在数据库中将用户的 `role_id` 改为 1

### Q2: 网络请求失败

**原因**：无法连接到后端服务

**解决**：
1. 确认后端服务正在运行
2. 检查 `Constants.BASE_URL` 配置是否正确
3. 如使用真机，将 `localhost` 改为电脑的局域网IP

### Q3: 构建失败

**原因**：ArkTS语法错误或类型不匹配

**解决**：
1. 查看构建日志中的ERROR信息
2. 参考[ArkTS开发规范](#4-arkts开发规范)
3. 使用DevEco Studio的代码检查功能

### Q4: 页面跳转失败

**原因**：路由未注册

**解决**：在 `main_pages.json` 中注册页面路径

---

## 更新日志

### v1.0.0 (2026-06-24)

- ✨ 初始版本发布
- ✨ 实现管理员登录功能
- ✨ 实现数据仪表盘
- ✨ 实现用户管理功能
- ✨ 实现角色权限控制
- 🎨 优化UI界面和交互体验

---

## 技术支持

- **项目主页**：[蓝图社团官网项目](../README.md)
- **后端项目**：[lantu_web_java](../lantu_web_java)
- **前端项目**：[lantu_web](../lantu_web)
- **组织Gitee**：[https://gitee.com/open_lanTuDream](https://gitee.com/open_lanTuDream)

---

## 许可证

本项目遵循 MIT 许可证。

---

## 贡献指南

欢迎提交Issue和Pull Request参与项目开发。

1. Fork本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交Pull Request

---

**Made with ❤️ by 蓝图社团**
