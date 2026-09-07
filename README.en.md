# Open_Lantu Website

> A complete student club website project featuring frontend presentation, user authentication, a check-in points system, leaderboard, and admin dashboard.
>
> **Organization Gitee:** [https://gitee.com/open_lanTuDream](https://gitee.com/open_lanTuDream)

---

## Project Structure

```
Open_Lantu/
├── lantu_web/                      # Frontend (Vue 3 + Vite + TypeScript)
│   ├── src/
│   │   ├── api/                    # API request wrappers (axios)
│   │   │   └── index.ts            # All API endpoint definitions
│   │   ├── assets/                 # Static assets
│   │   ├── components/             # Shared components
│   │   │   └── AdminUsersSection.vue  # Admin user management component
│   │   ├── composables/            # Reusable logic
│   │   │   ├── useScrollReveal.ts  # Scroll-triggered entrance animation
│   │   │   └── useAutoLogout.ts    # Auto logout on inactivity
│   │   ├── layouts/                # Page shell
│   │   │   └── AppLayout.vue       # Navbar + User menu + Footer + Back-to-top
│   │   ├── router/                 # Route configuration
│   │   │   └── index.ts            # Route table + navigation guard
│   │   ├── stores/                 # Global state (Pinia)
│   │   │   ├── auth.ts             # User auth state (with role/avatar/profile)
│   │   │   └── app.ts              # Application-wide state
│   │   ├── views/                  # Page components
│   │   │   ├── HomePage.vue        # Homepage
│   │   │   ├── AboutPage.vue       # About Us
│   │   │   ├── ActivitiesPage.vue  # Activities
│   │   │   ├── ProjectsPage.vue    # Projects
│   │   │   ├── JoinPage.vue        # Join Us (with pre-registration form)
│   │   │   ├── LoginPage.vue       # Login
│   │   │   ├── RegisterPage.vue    # Register
│   │   │   ├── BirthdayPage.vue    # Set Birthday (optional)
│   │   │   ├── RegistrationComplete.vue  # Registration Success
│   │   │   ├── ProfilePage.vue     # Profile (with check-in points)
│   │   │   ├── ProfileEditPage.vue # Edit Profile
│   │   │   ├── ChangePwdPage.vue   # Change Password (choose method)
│   │   │   ├── ChangePwdOld.vue    # Change via Old Password
│   │   │   ├── ChangePwdPhone.vue  # Change via Email Verification
│   │   │   ├── CheckinPage.vue     # Daily Check-in (calendar + points + ranking)
│   │   │   ├── AdminPage.vue       # Admin Dashboard (ECharts visualizations)
│   │   │   └── NotFoundPage.vue    # 404 page
│   │   ├── style.css               # Global styles (animations, utilities)
│   │   ├── App.vue                 # Root component
│   │   └── main.ts                 # Entry file (global component registration)
│   ├── index.html
│   └── package.json
│
├── lantu_web_java/                 # Backend (Spring Boot 3 + MyBatis + MySQL + Redis)
│   └── src/main/java/com/example/lantu_web_java/
│       ├── config/                 # Configuration
│       │   ├── SecurityConfig.java         # BCrypt password encoding
│       │   ├── WebConfig.java              # CORS + static resources + JWT interceptor
│       │   ├── JwtAuthInterceptor.java     # JWT token validation interceptor
│       │   └── DataInitializer.java        # Auto-create roles table & seed data on startup
│       ├── controller/             # API controllers
│       │   ├── AuthController.java             # Auth (login/register/password/profile)
│       │   ├── AvatarController.java           # Avatar upload
│       │   ├── PreregistrationController.java  # Pre-registration
│       │   ├── CheckInController.java          # Check-in system + leaderboard
│       │   └── AdminController.java            # Admin dashboard & user management
│       ├── dto/                    # Request/response DTOs
│       │   ├── ApiResponse.java             # Unified response format
│       │   ├── LoginRequest.java
│       │   ├── RegisterRequest.java
│       │   ├── ChangePasswordOldRequest.java
│       │   ├── ChangePasswordEmailRequest.java
│       │   └── PreregistrationRequest.java
│       ├── entity/                 # Data entities
│       │   ├── User.java               # User (with roleId/roleName)
│       │   ├── UserAvatar.java         # Avatar record
│       │   ├── Preregistration.java     # Pre-registration
│       │   ├── CheckIn.java            # Check-in record
│       │   └── Role.java               # Role
│       ├── mapper/                 # MyBatis data access interfaces
│       │   ├── UserMapper.java
│       │   ├── UserAvatarMapper.java
│       │   ├── PreregistrationMapper.java
│       │   ├── CheckInMapper.java
│       │   └── RoleMapper.java
│       ├── service/                # Business logic layer
│       │   ├── UserService.java
│       │   ├── AvatarService.java
│       │   ├── PreregistrationService.java
│       │   ├── CheckInService.java      # Check-in & points logic
│       │   └── RankingService.java      # Leaderboard (Redis ZSET + MySQL fallback)
│       └── util/
│           └── JwtUtil.java            # JWT generation & validation
│
├── LantuAppMg/                    # Mobile admin app (HarmonyOS + ArkTS)
│   ├── AppScope/                  # App-level config (bundleName, icon, version)
│   ├── entry/                     # Main module (src/main/ets + resources)
│   ├── build-profile.json5        # Build config
│   ├── oh-package.json5           # Dependency config
│   └── hvigor/                    # Build toolchain
│
├── db/                             # Database SQL schema files
│   ├── users.sql                   # Users table
│   ├── user_avatars.sql            # Avatars table
│   ├── preregistrations.sql        # Pre-registrations table
│   ├── check_ins.sql               # Check-in records table
│   ├── roles.sql                   # Roles table + seed data
│   ├── alter_users_add_role.sql    # Add role_id column to users table
│   └── migrate_nickname_unique.sql # Unique index migration
│
├── userTX/                         # User avatar upload storage directory
├── start.bat                       # Windows quick start script
├── README.md                       # Chinese documentation
└── README.en.md                    # English documentation
```

---

## Tech Stack

### Frontend

| Technology | Purpose |
|------------|---------|
| Vue 3 + TypeScript | Framework |
| Vite | Build tool |
| Vue Router 4 | Routing |
| Pinia | State management |
| Axios | HTTP client |
| Element Plus | UI library (icons) |
| Naive UI | UI library (navigation, forms, notifications, data tables) |
| ECharts | Admin dashboard data visualization |

### Backend

| Technology | Purpose |
|------------|---------|
| Spring Boot 3.2 | Web framework |
| MyBatis | ORM / Data access |
| Spring Validation | Bean validation |
| MySQL 8 | Relational database |
| Redis | Leaderboard caching (optional, auto-falls back to MySQL) |
| JWT (jjwt 0.12) | Authentication tokens |
| BCrypt | Password hashing |
| Lombok | Annotation-based boilerplate reduction for Java DTOs/entities |
| Maven | Build management |

---

## Features

### 👤 User System
- Register and login via phone number or account
- Auto-generated unique 11-digit numeric account
- JWT token authentication (24h validity)
- Auto-logout after 10 minutes of inactivity (event-based monitoring)
- Profile editing (name, nickname, phone, email, birthday, avatar upload)
- Two password change methods (old password / email verification)
- Session persisted in `sessionStorage` (log out on tab close)

### 📅 Check-in Points System
- Daily check-in: +1 point each time
- 7-day streak: +2 bonus points
- 30-day streak: +10 bonus points
- Points overview shown on profile page
- Dedicated check-in page with calendar view, stats cards, and success animation
- Leaderboard (Redis ZSET with automatic MySQL fallback)

### 🏆 Leaderboard
- Global points ranking (configurable TopN)
- Current user rank query
- Displays nickname, avatar, points, role badge
- Redis ZSET for high-performance sorting

### 👑 Role & Permission System
- Roles: Admin / Teacher / Student / Regular User
- Auto-creates `roles` table and seed data on application startup
- Admin dashboard accessible only to admins (role_id = 1)
- Dynamic menu items based on user role

### 📋 Pre-registration System
- Pre-registration form on the Join Us page
- Submit name, major, grade, and self-introduction

### ⚙️ Admin Dashboard
- ECharts dashboard: total users, today's check-ins, monthly check-ins, check-in rate
- User management: paginated list, keyword search, role switching, delete user

---

## Quick Start

### 1. Prerequisites

Ensure the following are installed:

- **Node.js** >= 18
- **Java** >= 17
- **Maven** >= 3.8
- **MySQL** >= 8.0
- **Redis** (optional — ranking falls back to MySQL automatically)

### 2. Create Database

```sql
CREATE DATABASE lantu_web CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

Database connection settings are in `lantu_web_java/src/main/resources/application.yml`. Update username and password as needed.

### 3. Initialize Tables

Execute the SQL files in the `db/` directory in the following order:

```bash
# Manual import
mysql -u root -p lantu_web < db/users.sql
mysql -u root -p lantu_web < db/user_avatars.sql
mysql -u root -p lantu_web < db/preregistrations.sql
mysql -u root -p lantu_web < db/check_ins.sql
mysql -u root -p lantu_web < db/roles.sql
mysql -u root -p lantu_web < db/alter_users_add_role.sql
```

> Note: `roles.sql` and `alter_users_add_role.sql` can also be executed automatically by `DataInitializer.java` on application startup.

### 4. Configure Redis (Optional)

To enable Redis-based leaderboard caching, ensure Redis is running on `localhost:6379`. Redis connection settings are in `application.yml`:

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      timeout: 5000
```

### 5. Start Backend

```bash
cd lantu_web_java
mvn spring-boot:run
```

The backend runs on `http://localhost:8081`.

### 6. Start Frontend

Open another terminal:

```bash
cd lantu_web
npm install
npm run dev
```

The frontend runs on `http://localhost:5173`.

### 7. One-Click Start (Windows)

Double-click `start.bat` to launch the Vite dev server.

---

## Page Routes

| Path | Page | Description | Auth Required |
|------|------|-------------|---------------|
| `/` | Homepage | Hero + Features + Activities + Stats + Updates + Testimonials + Partners | ❌ |
| `/about` | About Us | Story + Mission & Vision + Timeline + Values + Achievements | ❌ |
| `/activities` | Activities | Activity list (category filter) + Formats + How to Join | ❌ |
| `/projects` | Projects | Project showcase + Tech stack tags + Contribution guide | ❌ |
| `/join` | Join Us | Why join + Process + Member voices + Contact + Pre-registration form | ❌ |
| `/login` | Login | Phone/account + password login | ❌ |
| `/register` | Register | Fill in info to create an account | ❌ |
| `/birthday` | Set Birthday | Optional birthday setup after registration | ✅ |
| `/register-complete` | Registration Success | Displays account, auto-redirects to home after 30s | ✅ |
| `/profile` | Profile | View personal info, avatar, and check-in points | ✅ |
| `/profile/edit` | Edit Profile | Edit name/nickname/phone/email/birthday/avatar | ✅ |
| `/checkin` | Daily Check-in | Calendar view + check-in button + points stats + rules + ranking | ✅ |
| `/change-pwd` | Change Password | Choose verification method | ✅ |
| `/change-pwd/old` | Change via Old Password | Change using current password | ✅ |
| `/change-pwd/email` | Change via Email | Change through email verification | ✅ |
| `/admin` | Admin Dashboard | Dashboard charts + user management (admin only) | ✅ (Admin) |
| `/:pathMatch(.*)*` | 404 | Catch-all for unmatched routes | ❌ |

---

## Backend API

### Authentication

| Method | Path | Description | Auth Required |
|--------|------|-------------|---------------|
| POST | `/api/auth/login` | Login | ❌ |
| POST | `/api/auth/register` | Register | ❌ |
| PUT | `/api/auth/birthday` | Set birthday | ✅ |
| PUT | `/api/auth/profile` | Update profile | ✅ |
| PUT | `/api/auth/password/old` | Change password via old password | ✅ |
| PUT | `/api/auth/password/email` | Change password via email | ❌ |
| POST | `/api/auth/password/email/code` | Send email code (simulated) | ❌ |

### Avatar

| Method | Path | Description | Auth Required |
|--------|------|-------------|---------------|
| POST | `/api/avatar/upload` | Upload avatar | ✅ |
| GET | `/api/avatar/{userId}` | Get avatar path | ❌ |

### Pre-registration

| Method | Path | Description | Auth Required |
|--------|------|-------------|---------------|
| POST | `/api/preregister` | Pre-registration form | ❌ |

### Check-in

| Method | Path | Description | Auth Required |
|--------|------|-------------|---------------|
| POST | `/api/checkin` | Perform daily check-in | ✅ |
| GET | `/api/checkin/status` | Get check-in status (total points, streak, monthly count) | ✅ |
| GET | `/api/checkin/month?yearMonth=2026-06` | Get check-in dates for a given month | ✅ |

### Leaderboard

| Method | Path | Description | Auth Required |
|--------|------|-------------|---------------|
| GET | `/api/checkin/ranking?topN=20` | Get points leaderboard | ❌ |
| GET | `/api/checkin/ranking/me` | Get current user's rank | ✅ |

### Admin

| Method | Path | Description | Auth Required |
|--------|------|-------------|---------------|
| GET | `/api/admin/dashboard` | Dashboard stats (total users/today check-ins/monthly check-ins) | ✅ (Admin) |
| GET | `/api/admin/users?page=1&size=20&keyword=` | User list (paginated + search) | ✅ (Admin) |
| GET | `/api/admin/users/{id}` | User detail | ✅ (Admin) |
| PUT | `/api/admin/users/{id}/role` | Switch user role | ✅ (Admin) |
| DELETE | `/api/admin/users/{id}` | Delete user | ✅ (Admin) |

All APIs return a unified response format:

```json
{
  "code": 200,
  "message": "Success",
  "data": { ... }
}
```

---

## User System Flow

```
Register → Set Birthday (optional) → Registration Success Page → Homepage
                                                                    ↓
                                                             Auto-redirect after 30s
```

- Auto-login after registration (JWT token stored in sessionStorage)
- Auto-logout after 10 minutes of inactivity (monitors mousedown/keydown/touchstart/scroll/wheel)
- Password can be changed via either old password or email verification
- Profile (including avatar) is editable in the profile center
- Daily check-in earns points; points overview displayed on the profile page
- Admin dashboard accessible only to admin users (role_id = 1)

---

## Admin Dashboard

The admin dashboard is accessible at `/admin` and requires `role_id = 1` (Admin).

### Dashboard
- Total user count
- Today's check-in count
- Monthly check-in total
- Today's check-in rate (ECharts gauge chart)
- Last 7 days check-in trend (ECharts bar chart)

### User Management
- Paginated user list
- Keyword search (name/nickname/phone/email)
- Role switching dropdown
- Delete user with confirmation dialog

---

## Points Rules

| Action | Points | Description |
|--------|--------|-------------|
| Daily check-in | +1 | Once per day, UTC+8 calendar day |
| 7-day streak | +2 | Bonus reward |
| 30-day streak | +10 | Bonus reward |
| Total points | - | Real-time `SELECT SUM(points) FROM check_ins` |

---

## Common Operations

### Adding a New Page

1. Create `XxxPage.vue` in `lantu_web/src/views/`
2. Add a lazy-loaded route in `lantu_web/src/router/index.ts` with navigation guard config
3. Add a nav item in `AppLayout.vue`'s `navItems` array if needed

### Modifying the Navigation Bar

Edit the `navItems` array in `lantu_web/src/layouts/AppLayout.vue`.

### Using Scroll Animations

Add `class="reveal"` to any element:

```html
<div class="reveal">Fade in from bottom to top</div>
<div class="reveal reveal-delay-2">Appear with 0.2s delay</div>
```

Other animation classes: `reveal-scale` (scale), `reveal-left` (from left), `reveal-right` (from right).

### Modifying Global Styles

Edit `lantu_web/src/style.css`.

### Adjusting Database Connection

Edit `lantu_web_java/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/lantu_web?...
    username: your_username
    password: your_password
```

---

## Team Conventions

- Page components go in `src/views/`, shared components in `src/components/`
- Use `<script setup lang="ts">` for page components
- Add `reveal` animation class and responsive layout for new pages
- Do not commit `node_modules/`, `dist/`, or `target/` to version control
- Backend consistently uses `ApiResponse<T>` as the response format
- Controllers use try-catch to handle exceptions and return user-friendly messages
- Database SQL schema files go in `db/` directory, named after the table (`tablename.sql`)
- MyBatis Mapper XML files are placed in `resources/mapper/`
- Total points are computed in real-time via `SELECT SUM(points) FROM check_ins` (no redundant storage)
- Frontend uses `sessionStorage` for auth persistence (expires on tab close), not `localStorage`
- Role-based access is controlled via `role_id`; admin-only sections check on both frontend and backend
- Leaderboard prefers Redis ZSET; automatically falls back to MySQL when Redis is unavailable
