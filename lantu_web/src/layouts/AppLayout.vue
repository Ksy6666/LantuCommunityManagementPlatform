<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router'
import { computed, ref, h } from 'vue'
import type { MenuOption } from 'naive-ui'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()

const mobileMenuOpen = ref(false)

const navItems: MenuOption[] = [
  { label: '首页', key: '/' },
  { label: '活动', key: '/activities' },
  { label: '项目', key: '/projects' },
  { label: '加入', key: '/join' },
  { label: '关于', key: '/about' },
]

const activeKey = computed(() => route.path)

function handleSelect(key: string) {
  mobileMenuOpen.value = false
  router.push(key)
}

function toggleMobileMenu() {
  mobileMenuOpen.value = !mobileMenuOpen.value
}

function closeMobileMenu() {
  mobileMenuOpen.value = false
}

router.afterEach(() => {
  mobileMenuOpen.value = false
})

const userMenuOptions = computed(() => [
  {
    label: '个人中心',
    key: 'profile',
    icon: () => h('div', { style: 'font-size:16px' }, '👤'),
  },
  {
    label: '每日签到',
    key: 'checkin',
    icon: () => h('div', { style: 'font-size:16px' }, '📅'),
  },
  {
    label: '修改资料',
    key: 'profile-edit',
    icon: () => h('div', { style: 'font-size:16px' }, '✏️'),
  },
  {
    label: '修改密码',
    key: 'change-pwd',
    icon: () => h('div', { style: 'font-size:16px' }, '🔑'),
  },
  ...(auth.roleId === 1 ? [{
    label: '管理后台',
    key: 'admin',
    icon: () => h('div', { style: 'font-size:16px' }, '⚙️'),
  }] : []),
  {
    type: 'divider' as const,
    key: 'd1',
  },
  {
    label: '退出登录',
    key: 'logout',
    icon: () => h('div', { style: 'font-size:16px' }, '↩'),
  },
])

function handleUserMenuSelect(key: string) {
  if (key === 'profile') {
    router.push('/profile')
  } else if (key === 'checkin') {
    router.push('/checkin')
  } else if (key === 'profile-edit') {
    router.push('/profile/edit')
  } else if (key === 'change-pwd') {
    router.push('/change-pwd')
  } else if (key === 'admin') {
    router.push('/admin')
  } else if (key === 'logout') {
    auth.logout()
    router.push('/')
  }
}
</script>

<template>
  <n-message-provider>
    <n-dialog-provider>
      <div class="layout" @click="closeMobileMenu">
        <!-- 导航栏 -->
        <header class="header">
          <div class="header-inner">
            <div class="logo" @click="router.push('/')">
              <img class="logo-icon" src="/favicon.png" alt="Open_Lantu" />
              <span class="logo-text">Open_Lantu</span>
            </div>

            <!-- 桌面端导航 -->
            <n-menu
              mode="horizontal"
              :options="navItems"
              :value="activeKey"
              @update:value="handleSelect"
              class="desktop-nav"
            />

            <!-- 桌面端登录/注册（未登录） -->
            <div v-if="!auth.isLoggedIn" class="auth-actions desktop-auth">
              <n-button text size="small" @click="router.push('/login')" class="auth-link">
                登录
              </n-button>
              <span class="auth-divider">|</span>
              <n-button text size="small" @click="router.push('/register')" class="auth-link">
                注册
              </n-button>
            </div>

            <!-- 桌面端用户信息（已登录） -->
            <div v-else class="user-info desktop-auth">
              <n-dropdown
                trigger="click"
                :options="userMenuOptions"
                @select="handleUserMenuSelect"
              >
                <div class="user-trigger">
                  <div class="user-avatar">
                    <img v-if="auth.avatarUrl" :src="auth.avatarUrl" class="avatar-img" />
                    <n-icon v-else size="20"><UserFilled /></n-icon>
                  </div>
                  <span class="user-nickname">{{ auth.nickname }}</span>
                  <n-icon size="14" class="user-arrow"><ArrowDown /></n-icon>
                </div>
              </n-dropdown>
            </div>

            <!-- 移动端汉堡按钮 -->
            <button
              class="hamburger"
              :class="{ active: mobileMenuOpen }"
              @click.stop="toggleMobileMenu"
              aria-label="菜单"
            >
              <span></span>
              <span></span>
              <span></span>
            </button>
          </div>

          <!-- 移动端下拉菜单 -->
          <Transition name="mobile-nav">
            <div v-if="mobileMenuOpen" class="mobile-nav" @click.stop>
              <a
                v-for="item in navItems"
                :key="item.key as string"
                class="mobile-nav-item"
                :class="{ active: activeKey === item.key }"
                @click="handleSelect(item.key as string)"
              >
                {{ item.label }}
              </a>
              <div class="mobile-nav-divider"></div>
              <!-- 移动端：未登录 -->
              <template v-if="!auth.isLoggedIn">
                <a
                  class="mobile-nav-item"
                  :class="{ active: activeKey === '/login' }"
                  @click="handleSelect('/login')"
                >
                  登录
                </a>
                <a
                  class="mobile-nav-item"
                  :class="{ active: activeKey === '/register' }"
                  @click="handleSelect('/register')"
                >
                  注册
                </a>
              </template>
              <!-- 移动端：已登录 -->
              <template v-else>
                <div class="mobile-user-info">
                  <div class="mobile-user-avatar">
                    <img v-if="auth.avatarUrl" :src="auth.avatarUrl" class="avatar-img" />
                    <n-icon v-else size="20"><UserFilled /></n-icon>
                  </div>
                  <span class="mobile-user-nickname">{{ auth.nickname }}</span>
                </div>
                <a class="mobile-nav-item" @click="handleSelect('/profile')">
                  个人中心
                </a>
                <a class="mobile-nav-item" @click="handleSelect('/checkin')">
                  每日签到
                </a>
                <a class="mobile-nav-item" @click="handleSelect('/change-pwd')">
                  修改密码
                </a>
                <a class="mobile-nav-item mobile-logout" @click="auth.logout(); closeMobileMenu()">
                  退出登录
                </a>
              </template>
            </div>
          </Transition>
        </header>

        <!-- 主内容区 -->
        <main class="main">
          <router-view v-slot="{ Component }">
            <Transition name="page" mode="out-in">
              <component :is="Component" />
            </Transition>
          </router-view>
        </main>

        <!-- 回到顶部 -->
        <n-back-top :visibility-height="300" :bottom="100" :right="40">
          <div class="back-top-btn">
            <svg class="back-top-arrow" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
              <path d="M12 19V5" />
              <polyline points="5 12 12 5 19 12" />
            </svg>
          </div>
        </n-back-top>

        <!-- 页脚 -->
        <footer class="footer">
          <div class="footer-wave">
            <svg viewBox="0 0 1200 40" preserveAspectRatio="none">
              <path d="M0,20 C300,40 600,0 1200,20 L1200,40 L0,40 Z" fill="currentColor"/>
            </svg>
          </div>
          <div class="footer-inner">
            <div class="footer-col brand">
              <div class="footer-logo">
                <img class="fl-icon" src="/favicon.png" alt="Open_Lantu" />
                <span>Open_Lantu</span>
              </div>
              <p class="footer-desc">学生技术学习与协作社区</p>
              <div class="footer-social">
                <span class="social-dot" style="--c:#2080f0"></span>
                <span class="social-dot" style="--c:#36ad6a"></span>
                <span class="social-dot" style="--c:#f0a020"></span>
                <span class="social-dot" style="--c:#6366f1"></span>
              </div>
            </div>

            <div class="footer-col">
              <h4>导航</h4>
              <a @click="router.push('/')">首页</a>
              <a @click="router.push('/about')">关于我们</a>
            </div>

            <div class="footer-col">
              <h4>活动</h4>
              <a>技术分享会</a>
              <a>Code Jam</a>
              <a>开源项目</a>
            </div>

            <div class="footer-col">
              <h4>联系</h4>
              <p>Open_LanTu@outlook.com</p>
              <p>校内创新实验室 302</p>
              <a href="https://gitee.com/open_lanTuDream" target="_blank" rel="noopener noreferrer">Gitee 组织</a>
            </div>
          </div>
          <div class="footer-bottom">
            <p>&copy; {{ new Date().getFullYear() }} Open_Lantu 学生技术组织</p>
          </div>
        </footer>
      </div>
    </n-dialog-provider>
    <n-notification-provider>
    </n-notification-provider>
  </n-message-provider>
</template>

<style scoped>
.layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

/* ── Header ── */
.header {
  position: sticky;
  top: 0;
  z-index: 100;
  /* 静态渐变：以 #e3e3e3 为基底，顶部不透明 → 底部透明，毛玻璃效果 */
  background: linear-gradient(
    to bottom,
    rgba(227, 227, 227, 1)    0%,
    rgba(227, 227, 227, 0.85) 25%,
    rgba(227, 227, 227, 0.6)  50%,
    rgba(227, 227, 227, 0.3)  75%,
    rgba(227, 227, 227, 0)    100%
  );
  backdrop-filter: blur(6px) saturate(150%);
  -webkit-backdrop-filter: blur(6px) saturate(150%);
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
}

.header-inner {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
  padding: 0 24px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  user-select: none;
  flex-shrink: 0;
}

.logo-icon {
  width: 36px;
  height: 36px;
  border-radius: 8px;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.logo:hover .logo-icon {
  transform: rotate(-6deg) scale(1.05);
}

.logo-text {
  font-size: 20px;
  font-weight: 700;
  color: #1d1e1f;
  letter-spacing: -0.3px;
}

.desktop-nav {
  flex: 1;
  justify-content: flex-end;
  background: transparent !important;
}

/* ── Auth Buttons ── */
.auth-actions {
  display: flex;
  align-items: center;
  gap: 2px;
  flex-shrink: 0;
  margin-left: 28px;
  padding-left: 20px;
  border-left: 1px solid #eaecf0;
}

.auth-link {
  font-size: 14px;
  color: #606266 !important;
  padding: 0 4px !important;
  transition: color 0.2s;
}

.auth-link:hover {
  color: #2080f0 !important;
}

.auth-divider {
  color: #dcdfe6;
  font-size: 12px;
  user-select: none;
}

.mobile-nav-divider {
  height: 1px;
  background: #f0f0f0;
  margin: 6px 16px;
}

.mobile-nav-item.auth-highlight {
  color: #2080f0;
  font-weight: 600;
}

/* ── User Info (Desktop) ── */
.user-info {
  display: flex;
  align-items: center;
  flex-shrink: 0;
  margin-left: 28px;
  padding-left: 20px;
  border-left: 1px solid #eaecf0;
}

.user-trigger {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 8px;
  transition: background 0.2s;
  user-select: none;
}

.user-trigger:hover {
  background: rgba(0, 0, 0, 0.04);
}

.user-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: linear-gradient(135deg, #2080f0, #6366f1);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  overflow: hidden;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
}

.user-nickname {
  font-size: 14px;
  font-weight: 500;
  color: #1d1e1f;
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.user-arrow {
  color: #909399;
  transition: transform 0.2s;
}

.user-trigger:hover .user-arrow {
  transform: rotate(180deg);
}

/* ── User Info (Mobile) ── */
.mobile-user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 24px;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 4px;
}

.mobile-user-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: linear-gradient(135deg, #2080f0, #6366f1);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  overflow: hidden;
}

.mobile-user-nickname {
  font-size: 15px;
  font-weight: 600;
  color: #1d1e1f;
}

.mobile-logout {
  color: #909399 !important;
  font-size: 14px !important;
}

/* ── Main ── */
.main {
  flex: 1;
}

/* ── Footer ── */
.footer {
  background: #1a1a2e;
  color: #a0a0b8;
  position: relative;
  padding-top: 0;
}

.footer-wave {
  color: #1a1a2e;
  line-height: 0;
  margin-top: -1px;
}

.footer-wave svg {
  display: block;
  width: 100%;
  height: 40px;
}

.footer-inner {
  max-width: 1100px;
  margin: 0 auto;
  display: flex;
  gap: 60px;
  padding: 40px 24px 32px;
}

.footer-col {
  flex: 1;
}

.footer-col.brand {
  flex: 1.5;
}

.footer-logo {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 20px;
  font-weight: 600;
  color: #fff;
  margin-bottom: 12px;
}

.fl-icon {
  width: 32px;
  height: 32px;
  border-radius: 6px;
  object-fit: cover;
}

.footer-desc {
  font-size: 14px;
  color: #6b6b8a;
  line-height: 1.6;
  margin: 0 0 16px;
}

.footer-social {
  display: flex;
  gap: 10px;
}

.social-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: var(--c);
  opacity: 0.7;
  transition: opacity 0.2s;
  cursor: default;
}

.social-dot:hover {
  opacity: 1;
}

.footer-col h4 {
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  margin: 0 0 14px;
  letter-spacing: 1px;
}

.footer-col a {
  display: block;
  color: #6b6b8a;
  font-size: 14px;
  line-height: 2.2;
  cursor: pointer;
  text-decoration: none;
  transition: color 0.2s;
}

.footer-col a:hover {
  color: #f0a020;
}

.footer-col p {
  font-size: 14px;
  color: #6b6b8a;
  line-height: 2.2;
  margin: 0;
}

.footer-bottom {
  max-width: 1100px;
  margin: 0 auto;
  text-align: center;
  padding: 18px 24px;
  border-top: 1px solid rgba(255, 255, 255, 0.06);
  font-size: 13px;
  color: #4a4a6a;
}

.footer-bottom p {
  margin: 0;
}

/* ── Hamburger ── */
.hamburger {
  display: none;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 40px;
  height: 40px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 8px;
  gap: 5px;
  z-index: 110;
}

.hamburger span {
  display: block;
  width: 22px;
  height: 2px;
  background: #1d1e1f;
  border-radius: 2px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.hamburger.active span:nth-child(1) {
  transform: translateY(7px) rotate(45deg);
}

.hamburger.active span:nth-child(2) {
  opacity: 0;
  transform: scaleX(0);
}

.hamburger.active span:nth-child(3) {
  transform: translateY(-7px) rotate(-45deg);
}

/* ── Back to Top ── */
/* 自定义按钮（hover 动画直接挂在模板元素上，不用 :deep） */
.back-top-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: linear-gradient(135deg, #2080f0, #6366f1);
  color: #fff;
  cursor: pointer;
  box-shadow: 0 4px 16px rgba(32, 128, 240, 0.35);
  transition: transform 0.25s cubic-bezier(0.34, 1.56, 0.64, 1),
              box-shadow 0.25s ease;
}

.back-top-btn:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 24px rgba(32, 128, 240, 0.5);
}

.back-top-btn:active {
  transform: scale(0.9);
  box-shadow: 0 2px 8px rgba(32, 128, 240, 0.3);
}

/* ── 箭头图标 ── */
.back-top-arrow {
  display: block;
}

/* ── Mobile Nav Dropdown ── */
.mobile-nav {
  position: fixed;
  top: 64px;
  left: 0;
  right: 0;
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  z-index: 99;
  padding: 8px 0;
  border-top: 1px solid #f0f0f0;
}

.mobile-nav-item {
  display: block;
  padding: 14px 24px;
  font-size: 16px;
  font-weight: 500;
  color: #606266;
  cursor: pointer;
  transition: all 0.2s;
  text-decoration: none;
}

.mobile-nav-item:hover {
  background: rgba(32, 128, 240, 0.06);
  color: #2080f0;
}

.mobile-nav-item.active {
  color: #2080f0;
  background: rgba(32, 128, 240, 0.08);
  border-left: 3px solid #2080f0;
}

.mobile-nav-enter-active,
.mobile-nav-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.mobile-nav-enter-from,
.mobile-nav-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}

/* ── Responsive ── */
@media (max-width: 900px) {
  .desktop-nav {
    display: none !important;
  }

  .desktop-auth {
    display: none !important;
  }

  .hamburger {
    display: flex;
  }

  .footer-inner {
    flex-direction: column;
    gap: 28px;
  }

  .footer-col.brand {
    flex: 1;
  }
}

@media (max-width: 768px) {
  .footer-inner {
    gap: 24px;
  }
}
</style>

<!-- 非 scoped 兜底：确保 teleport 到 body 的 n-back-top 背景彻底透明 -->
<style>
.n-back-top {
  background: transparent !important;
  box-shadow: none !important;
  border-radius: 50% !important;
  height: auto !important;
  min-width: auto !important;
  padding: 0 !important;
  line-height: 0 !important;
  display: block !important;
}
</style>
