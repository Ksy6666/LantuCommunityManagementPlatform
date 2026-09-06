<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { updateBirthdayApi } from '../api'
import { useAuthStore } from '../stores/auth'
import { useMessage } from 'naive-ui'

const router = useRouter()
const message = useMessage()
const auth = useAuthStore()

const birthday = ref<number | null>(null)
const month = ref<number | null>(null)
const year = ref<number | null>(null)
const saving = ref(false)

const years = Array.from({ length: 80 }, (_, i) => {
  const y = new Date().getFullYear() - i
  return { label: `${y}年`, value: y }
})

const months = Array.from({ length: 12 }, (_, i) => ({
  label: `${i + 1}月`,
  value: i + 1,
}))

const days = Array.from({ length: 31 }, (_, i) => ({
  label: `${i + 1}日`,
  value: i + 1,
}))

import { computed } from 'vue'

function getDaysInMonth() {
  if (!year.value || !month.value) return 31
  return new Date(year.value, month.value, 0).getDate()
}

const filteredDays = computed(() => {
  const max = getDaysInMonth()
  return days.filter((d) => d.value <= max)
})

async function handleSave() {
  if (!year.value || !month.value || !birthday.value) {
    message.warning('请选择完整的生日')
    return
  }
  const dateStr = `${year.value}-${String(month.value).padStart(2, '0')}-${String(birthday.value).padStart(2, '0')}`
  saving.value = true
  try {
    const res = await updateBirthdayApi(dateStr)
    if (res.data.code === 200) {
      auth.updateUser(res.data.data as any)
      message.success('生日设置成功 🎂')
      router.push('/register-complete')
    }
  } catch (err: any) {
    const msg = err?.response?.data?.message || '设置失败，请稍后重试'
    message.error(msg)
  } finally {
    saving.value = false
  }
}

function handleSkip() {
  router.push('/')
}
</script>

<template>
  <div class="birthday-page">
    <!-- 背景装饰 -->
    <div class="birthday-bg">
      <div class="bg-circle c1"></div>
      <div class="bg-circle c2"></div>
      <div class="bg-circle c3"></div>
      <div class="bg-circle c4"></div>
      <div class="bg-circle c5"></div>
    </div>

    <div class="birthday-card">
      <!-- 蛋糕装饰 -->
      <div class="cake-decoration">
        <div class="cake-emoji">🎂</div>
        <div class="candle" v-for="n in 3" :key="n" :style="{ '--delay': `${n * 0.15}s` }">
          <div class="candle-flame"></div>
        </div>
      </div>

      <!-- 标题 -->
      <div class="bc-header">
        <h1>你的生日是哪天？</h1>
        <p>设置后可以收到专属祝福哦 🎉</p>
      </div>

      <!-- 生日选择器 -->
      <div class="birthday-picker">
        <div class="picker-row">
          <div class="picker-item">
            <label>年份</label>
            <n-select
              v-model:value="year"
              placeholder="选择"
              :options="years"
              size="large"
              :consistent-menu-width="false"
            />
          </div>
          <div class="picker-item">
            <label>月份</label>
            <n-select
              v-model:value="month"
              placeholder="选择"
              :options="months"
              size="large"
              :consistent-menu-width="false"
            />
          </div>
          <div class="picker-item">
            <label>日期</label>
            <n-select
              v-model:value="birthday"
              placeholder="选择"
              :options="filteredDays"
              size="large"
              :consistent-menu-width="false"
            />
          </div>
        </div>
      </div>

      <!-- 按钮组 -->
      <div class="bc-actions">
        <n-button
          type="primary"
          size="large"
          round
          block
          :loading="saving"
          @click="handleSave"
          class="btn-save"
        >
          <template #icon>
            <n-icon><Check /></n-icon>
          </template>
          保存生日
        </n-button>

        <n-button
          size="large"
          round
          block
          ghost
          @click="handleSkip"
          class="btn-skip"
        >
          暂时跳过
        </n-button>
      </div>

      <!-- 提示 -->
      <p class="bc-hint">生日信息仅用于社区活动祝福，不会对外公开</p>
    </div>
  </div>
</template>

<style scoped>
.birthday-page {
  min-height: calc(100vh - 64px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 24px;
  position: relative;
  overflow: hidden;
}

/* ── 背景 ── */
.birthday-bg {
  position: absolute;
  inset: 0;
  background: linear-gradient(160deg, #fdf2f8 0%, #fce7f3 20%, #ede9fe 50%, #e0e7ff 80%, #dbeafe 100%);
  z-index: 0;
}

.bg-circle {
  position: absolute;
  border-radius: 50%;
  animation: floatBubble 10s ease-in-out infinite;
}

.c1 {
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, rgba(244, 114, 182, 0.12), transparent);
  top: -80px;
  right: -60px;
  animation-delay: 0s;
}

.c2 {
  width: 200px;
  height: 200px;
  background: radial-gradient(circle, rgba(167, 139, 250, 0.1), transparent);
  bottom: -40px;
  left: -40px;
  animation-delay: -3s;
}

.c3 {
  width: 150px;
  height: 150px;
  background: radial-gradient(circle, rgba(251, 191, 36, 0.08), transparent);
  top: 40%;
  left: 10%;
  animation-delay: -5s;
}

.c4 {
  width: 100px;
  height: 100px;
  background: radial-gradient(circle, rgba(52, 211, 153, 0.08), transparent);
  bottom: 20%;
  right: 15%;
  animation-delay: -7s;
}

.c5 {
  width: 180px;
  height: 180px;
  background: radial-gradient(circle, rgba(96, 165, 250, 0.08), transparent);
  top: 15%;
  left: 60%;
  animation-delay: -2s;
}

@keyframes floatBubble {
  0%, 100% { transform: translateY(0) scale(1); }
  50% { transform: translateY(-25px) scale(1.05); }
}

/* ── 卡片 ── */
.birthday-card {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 480px;
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border-radius: 24px;
  padding: 48px 40px 36px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.5);
  text-align: center;
}

/* ── 蛋糕装饰 ── */
.cake-decoration {
  position: relative;
  margin-bottom: 24px;
  height: 72px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.cake-emoji {
  font-size: 56px;
  line-height: 1;
  animation: bounceCake 2s ease-in-out infinite;
}

@keyframes bounceCake {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-8px); }
}

.candle {
  position: absolute;
  top: 2px;
  width: 4px;
  height: 20px;
  background: linear-gradient(to top, #f472b6, #fbbf24);
  border-radius: 2px 2px 0 0;
  animation: candleGlow 1.5s ease-in-out infinite;
  animation-delay: var(--delay);
}

.candle:nth-child(2) { left: calc(50% - 22px); }
.candle:nth-child(3) { left: calc(50% + 12px); }
.candle:nth-child(4) { left: calc(50% + 32px); }

.candle-flame {
  position: absolute;
  top: -8px;
  left: 50%;
  transform: translateX(-50%);
  width: 8px;
  height: 10px;
  background: radial-gradient(circle, #fbbf24, #f472b6);
  border-radius: 50% 50% 50% 50% / 60% 60% 40% 40%;
  animation: flicker 0.5s ease-in-out infinite alternate;
}

@keyframes candleGlow {
  0%, 100% { opacity: 0.6; }
  50% { opacity: 1; }
}

@keyframes flicker {
  0% { transform: translateX(-50%) scaleY(1); }
  100% { transform: translateX(-50%) scaleY(1.2) scaleX(0.8); }
}

/* ── 标题 ── */
.bc-header {
  margin-bottom: 32px;
}

.bc-header h1 {
  font-size: 26px;
  font-weight: 700;
  color: #1d1e1f;
  margin: 0 0 8px;
}

.bc-header p {
  font-size: 15px;
  color: #909399;
  margin: 0;
}

/* ── 选择器 ── */
.birthday-picker {
  margin-bottom: 32px;
}

.picker-row {
  display: flex;
  gap: 12px;
}

.picker-item {
  flex: 1;
}

.picker-item label {
  display: block;
  font-size: 13px;
  font-weight: 500;
  color: #606266;
  margin-bottom: 6px;
  text-align: left;
}

/* ── 按钮 ── */
.bc-actions {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 20px;
}

.btn-save {
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #f472b6, #a78bfa);
  border: none;
}

.btn-save:hover {
  background: linear-gradient(135deg, #ec4899, #8b5cf6);
}

.btn-skip {
  height: 44px;
  font-size: 14px;
  color: #909399;
  border-color: #e0e0e0;
}

.btn-skip:hover {
  color: #606266;
  border-color: #c0c0c0;
  background: rgba(0, 0, 0, 0.02);
}

/* ── 提示 ── */
.bc-hint {
  font-size: 12px;
  color: #c0c4cc;
  margin: 0;
}

/* ── 响应式 ── */
@media (max-width: 480px) {
  .birthday-card {
    padding: 36px 24px 28px;
    border-radius: 20px;
  }

  .picker-row {
    flex-direction: column;
    gap: 8px;
  }

  .bc-header h1 {
    font-size: 22px;
  }
}
</style>
