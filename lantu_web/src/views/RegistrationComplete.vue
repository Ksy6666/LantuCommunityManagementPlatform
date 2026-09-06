<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const auth = useAuthStore()

const countdown = ref(30)
let timer: ReturnType<typeof setInterval> | null = null

function goHome() {
  if (timer) {
    clearInterval(timer)
    timer = null
  }
  router.push('/')
}

onMounted(() => {
  timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      goHome()
    }
  }, 1000)
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
    timer = null
  }
})
</script>

<template>
  <div class="success-page">
    <div class="success-bg">
      <div class="bg-circle c1"></div>
      <div class="bg-circle c2"></div>
      <div class="bg-circle c3"></div>
    </div>

    <div class="success-card">
      <!-- 成功图标 -->
      <div class="success-icon">
        <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
          <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
          <polyline points="22 4 12 14.01 9 11.01" />
        </svg>
      </div>

      <div class="sc-header">
        <h1>账号注册成功 🎉</h1>
        <p class="sc-sub">欢迎加入 Open_Lantu</p>
      </div>

      <!-- 账号卡片 -->
      <div class="account-box">
        <span class="account-label">您的账号是</span>
        <span class="account-value">{{ auth.user?.account }}</span>
      </div>

      <p class="sc-hint">请牢记您的账号，后续登录需要使用</p>

      <button class="home-btn" @click="goHome">
        立即前往首页
      </button>

      <p class="sc-countdown">
        {{ countdown }} 秒后自动跳转首页
        <span class="countdown-bar" :style="{ width: (countdown / 30) * 100 + '%' }"></span>
      </p>
    </div>
  </div>
</template>

<style scoped>
.success-page {
  min-height: calc(100vh - 64px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 24px;
  position: relative;
  overflow: hidden;
}

/* ── 背景 ── */
.success-bg {
  position: absolute;
  inset: 0;
  background: linear-gradient(160deg, #f0fdf4 0%, #ecfdf5 20%, #e0f2fe 50%, #ede9fe 80%, #fdf2f8 100%);
  z-index: 0;
}

.bg-circle {
  position: absolute;
  border-radius: 50%;
  animation: floatBubble 12s ease-in-out infinite;
}

.c1 {
  width: 320px;
  height: 320px;
  background: radial-gradient(circle, rgba(52, 211, 153, 0.1), transparent);
  top: -100px;
  right: -80px;
  animation-delay: 0s;
}

.c2 {
  width: 220px;
  height: 220px;
  background: radial-gradient(circle, rgba(99, 102, 241, 0.08), transparent);
  bottom: -60px;
  left: -60px;
  animation-delay: -4s;
}

.c3 {
  width: 160px;
  height: 160px;
  background: radial-gradient(circle, rgba(251, 191, 36, 0.07), transparent);
  top: 30%;
  left: 5%;
  animation-delay: -7s;
}

@keyframes floatBubble {
  0%, 100% { transform: translateY(0) scale(1); }
  50% { transform: translateY(-30px) scale(1.05); }
}

/* ── 卡片 ── */
.success-card {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 440px;
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border-radius: 24px;
  padding: 48px 40px 36px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.5);
  text-align: center;
}

/* ── 成功图标 ── */
.success-icon {
  width: 88px;
  height: 88px;
  margin: 0 auto 20px;
  border-radius: 50%;
  background: linear-gradient(135deg, #34d399, #6366f1);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  animation: scaleIn 0.5s ease-out;
}

@keyframes scaleIn {
  0% { transform: scale(0); }
  60% { transform: scale(1.15); }
  100% { transform: scale(1); }
}

/* ── 标题 ── */
.sc-header {
  margin-bottom: 28px;
}

.sc-header h1 {
  font-size: 26px;
  font-weight: 700;
  color: #1d2939;
  margin: 0 0 6px;
}

.sc-sub {
  font-size: 15px;
  color: #98a2b3;
  margin: 0;
}

/* ── 账号卡片 ── */
.account-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 24px;
  margin-bottom: 16px;
  background: linear-gradient(135deg, #eef2ff, #f0fdf4);
  border-radius: 16px;
  border: 1.5px dashed #c7d2fe;
}

.account-label {
  font-size: 14px;
  color: #667085;
  font-weight: 500;
}

.account-value {
  font-size: 32px;
  font-weight: 800;
  color: #6366f1;
  letter-spacing: 4px;
  font-family: 'SF Mono', 'Fira Code', 'Inter', monospace;
}

/* ── 提示 ── */
.sc-hint {
  font-size: 13px;
  color: #f59e0b;
  margin: 0 0 24px;
  font-weight: 500;
}

/* ── 按钮 ── */
.home-btn {
  display: block;
  width: 100%;
  height: 48px;
  padding: 0 24px;
  border: none;
  border-radius: 12px;
  background: linear-gradient(135deg, #6366f1, #8b5cf6);
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  margin-bottom: 20px;
}

.home-btn:hover {
  box-shadow: 0 4px 14px rgba(99, 102, 241, 0.35);
  transform: translateY(-1px);
}

.home-btn:active {
  transform: translateY(0);
}

/* ── 倒计时 ── */
.sc-countdown {
  font-size: 13px;
  color: #98a2b3;
  margin: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.countdown-bar {
  height: 3px;
  background: linear-gradient(90deg, #6366f1, #34d399);
  border-radius: 2px;
  transition: width 1s linear;
  max-width: 200px;
}

/* ── 响应式 ── */
@media (max-width: 480px) {
  .success-card {
    padding: 36px 24px 28px;
    border-radius: 20px;
  }

  .account-value {
    font-size: 26px;
    letter-spacing: 3px;
  }
}
</style>
