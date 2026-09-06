<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { getCheckInStatusApi } from '../api'
import type { CheckInStatus } from '../api'

const router = useRouter()
const auth = useAuthStore()

const checkinStatus = ref<CheckInStatus | null>(null)
const loadingCheckin = ref(true)

const fields = [
  { label: '姓名', key: 'name', fallback: '未填写' },
  { label: '昵称', key: 'nickname', fallback: '未设置' },
  { label: '手机号', key: 'phone', fallback: '未填写' },
  { label: '邮箱', key: 'email', fallback: '未填写' },
  { label: '生日', key: 'birthday', fallback: '未设置' },
  { label: '角色', key: 'roleName', fallback: '普通用户' },
]

function val(key: string): string {
  return (auth as any)[key] ?? ''
}

onMounted(async () => {
  try {
    const res = await getCheckInStatusApi()
    if (res.data.code === 200) {
      checkinStatus.value = res.data.data
    }
  } catch {
    // ignore
  } finally {
    loadingCheckin.value = false
  }
})
</script>

<template>
  <div class="profile-page">
    <!-- 顶部大 Banner -->
    <div class="banner">
      <div class="banner-bg" />
      <div class="banner-content">
        <div class="avatar-ring">
          <div class="avatar">
            <img v-if="auth.avatarUrl" :src="auth.avatarUrl" class="avatar-img" />
            <span v-else class="avatar-fallback">{{ auth.nickname?.charAt(0) || '?' }}</span>
          </div>
        </div>
        <h1 class="banner-name">{{ auth.nickname || '未设置昵称' }}</h1>
        <p class="banner-account">@{{ auth.user?.account }}</p>
      </div>
    </div>

    <!-- 信息区 -->
    <div class="info-section">
      <div class="profile-cols">
        <!-- 基本信息 -->
        <div class="profile-col">
          <div class="section-header">
            <span class="section-line" />
            <span class="section-title">基本信息</span>
            <span class="section-line" />
          </div>

          <div class="info-grid">
            <div v-for="f in fields" :key="f.key" class="info-item">
              <span class="info-label">{{ f.label }}</span>
              <span class="info-value">{{ val(f.key) || f.fallback }}</span>
            </div>
          </div>
        </div>

        <!-- 签到积分 -->
        <div class="profile-col">
          <div class="section-header">
            <span class="section-line" />
            <span class="section-title">签到积分</span>
            <span class="section-line" />
          </div>

          <div v-if="loadingCheckin" class="checkin-loading">加载中...</div>
          <div v-else-if="checkinStatus" class="checkin-cards">
            <div class="checkin-card" style="--c:#f0a020">
              <span class="checkin-num gradient-gold">{{ checkinStatus.totalPoints }}</span>
              <span class="checkin-label">总积分</span>
            </div>
            <div class="checkin-card" style="--c:#6366f1">
              <span class="checkin-num gradient-blue">{{ checkinStatus.consecutiveDays }}</span>
              <span class="checkin-label">连续签到</span>
            </div>
            <div class="checkin-card" style="--c:#36ad6a">
              <span class="checkin-num gradient-green">{{ checkinStatus.monthlyCount }}</span>
              <span class="checkin-label">本月签到</span>
            </div>
            <div class="checkin-card checkin-more" @click="router.push('/checkin')">
              <span class="checkin-more-icon">
                <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="9 18 15 12 9 6"/></svg>
              </span>
              <span class="checkin-label">详情</span>
            </div>
          </div>
        </div>
      </div>

      <div class="action-btns">
        <button class="action-btn" @click="router.push('/')">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 12H5"/><polyline points="12 19 5 12 12 5"/></svg>
          返回首页
        </button>
        <button class="action-btn action-btn-primary" @click="router.push('/profile/edit')">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
          修改个人信息
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* ═════════════════════════════════════════════
   个人主页 — 开放式全宽布局
   顶部渐变 Banner + 下方自由信息区
   ═════════════════════════════════════════════ */

.profile-page {
  min-height: calc(100vh - 64px);
  display: flex;
  flex-direction: column;
  background: linear-gradient(180deg, #f0f2f5 0%, #f8f9fc 100%);
}

/* ═══════════════════════════════════
   顶部 Banner
   ═══════════════════════════════════ */
.banner {
  position: relative;
  width: 100%;
  padding: 64px 24px 56px;
  overflow: hidden;
}

.banner-bg {
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 40%, #0f3460 70%, #1a1a2e 100%);
  z-index: 0;
}

.banner-bg::before {
  content: '';
  position: absolute;
  inset: -50%;
  background:
    radial-gradient(ellipse 80% 60% at 10% 90%, rgba(99, 102, 241, 0.15) 0%, transparent 60%),
    radial-gradient(ellipse 60% 50% at 90% 20%, rgba(139, 92, 246, 0.12) 0%, transparent 60%),
    radial-gradient(ellipse 50% 40% at 50% 50%, rgba(56, 189, 248, 0.08) 0%, transparent 50%);
  animation: bgShift 20s ease-in-out infinite alternate;
  z-index: 0;
}

@keyframes bgShift {
  0% { transform: translate(0, 0) rotate(0deg); }
  100% { transform: translate(2%, 1%) rotate(3deg); }
}

.banner-content {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

/* 头像 */
.avatar-ring {
  position: relative;
  margin-bottom: 20px;
}

.avatar-ring::before {
  content: '';
  position: absolute;
  inset: -6px;
  border-radius: 50%;
  background: linear-gradient(135deg, #6366f1, #8b5cf6, #6366f1);
  animation: ringSpin 4s linear infinite;
  mask: radial-gradient(farthest-side, transparent calc(100% - 3px), #fff calc(100% - 3px));
  -webkit-mask: radial-gradient(farthest-side, transparent calc(100% - 3px), #fff calc(100% - 3px));
}

@keyframes ringSpin {
  to { transform: rotate(360deg); }
}

.avatar {
  width: 112px;
  height: 112px;
  border-radius: 50%;
  background: #1e293b;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  position: relative;
  z-index: 1;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-fallback {
  font-size: 42px;
  font-weight: 700;
  color: #e2e8f0;
  font-family: 'Inter', system-ui, sans-serif;
}

.banner-name {
  font-size: 28px;
  font-weight: 700;
  color: #fff;
  margin: 0 0 6px;
  letter-spacing: 0.5px;
}

.banner-account {
  font-size: 15px;
  color: rgba(255, 255, 255, 0.55);
  margin: 0;
  font-weight: 400;
}

/* ═══════════════════════════════════
   信息区
   ═══════════════════════════════════ */
.info-section {
  flex: 1;
  padding: 32px 24px 48px;
  max-width: 900px;
  width: 100%;
  margin: 0 auto;
}

.profile-cols {
  display: flex;
  gap: 24px;
}

.profile-col {
  flex: 1;
  min-width: 0;
}

/* 分区标题 */
.section-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 28px;
}

.section-line {
  flex: 1;
  height: 1px;
  background: linear-gradient(90deg, transparent, #d0d5dd, transparent);
}

.section-title {
  font-size: 14px;
  font-weight: 600;
  color: #98a2b3;
  letter-spacing: 2px;
  text-transform: uppercase;
  white-space: nowrap;
}

/* 信息网格 */
.info-grid {
  display: flex;
  flex-direction: column;
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 18px 24px;
  transition: background 0.2s ease;
}

.info-item:hover {
  background: #f8f9fc;
}

.info-item + .info-item {
  border-top: 1px solid #f0f2f5;
}

.info-label {
  font-size: 14px;
  font-weight: 500;
  color: #667085;
  min-width: 72px;
}

.info-value {
  font-size: 15px;
  font-weight: 500;
  color: #1d2939;
  text-align: right;
  word-break: break-all;
}

/* ═══════════════════════════════════
    签到积分
    ═══════════════════════════════════ */
.checkin-loading {
  text-align: center;
  padding: 24px;
  color: #909399;
  font-size: 14px;
}

.checkin-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  margin-top: 4px;
}

.checkin-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 6px;
  background: #fff;
  border-radius: 14px;
  padding: 20px 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
  border: 1px solid #f0f2f5;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.checkin-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.07);
}

.checkin-num {
  font-size: 26px;
  font-weight: 800;
  line-height: 1.2;
}

.checkin-label {
  font-size: 12px;
  font-weight: 500;
  color: #909399;
}

.gradient-gold {
  background: linear-gradient(135deg, #f0a020, #f7b731);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}

.gradient-blue {
  background: linear-gradient(135deg, #2080f0, #6366f1);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}

.gradient-green {
  background: linear-gradient(135deg, #36ad6a, #52c41a);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}

.checkin-more {
  cursor: pointer;
  border-color: #e8ecf1;
}

.checkin-more-icon {
  color: #909399;
  transition: transform 0.2s ease;
  display: flex;
}

.checkin-more:hover .checkin-more-icon {
  transform: translateX(3px);
  color: #2080f0;
}

/* ═══════════════════════════════════
    操作按钮
    ═══════════════════════════════════ */
.action-btns {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin-top: 32px;
}

.action-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 10px 28px;
  border: none;
  border-radius: 12px;
  background: #fff;
  color: #475569;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.06);
  transition: all 0.2s ease;
}

.action-btn:hover {
  background: #f1f5f9;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transform: translateY(-1px);
}

.action-btn:active {
  transform: translateY(0);
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.06);
}

.action-btn-primary {
  background: linear-gradient(135deg, #2080f0, #6366f1);
  color: #fff;
  box-shadow: 0 2px 10px rgba(32, 128, 240, 0.25);
}

.action-btn-primary:hover {
  background: linear-gradient(135deg, #2080f0, #6366f1);
  box-shadow: 0 4px 18px rgba(32, 128, 240, 0.35);
  transform: translateY(-1px);
}

.action-btn-primary:active {
  box-shadow: 0 1px 4px rgba(32, 128, 240, 0.2);
  transform: translateY(0);
}

/* ═══════════════════════════════════
   响应式
   ═══════════════════════════════════ */
@media (max-width: 480px) {
  .banner {
    padding: 48px 20px 44px;
  }

  .avatar {
    width: 88px;
    height: 88px;
  }

  .avatar-fallback {
    font-size: 34px;
  }

  .banner-name {
    font-size: 22px;
  }

  .info-section {
    padding: 24px 16px 40px;
  }

  .info-item {
    padding: 14px 18px;
  }

  .profile-cols {
    flex-direction: column;
  }

  .checkin-cards {
    grid-template-columns: repeat(2, 1fr);
  }

  .action-btns {
    flex-direction: column;
    align-items: stretch;
  }

  .action-btn {
    width: 100%;
  }
}
</style>
