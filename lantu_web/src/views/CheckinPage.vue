<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useMessage } from 'naive-ui'
import { doCheckInApi, getCheckInStatusApi, getRankingApi } from '../api'
import type { CheckInStatus, RankingEntry } from '../api'

const message = useMessage()

const status = ref<CheckInStatus | null>(null)
const loading = ref(false)
const checkInLoading = ref(false)
const currentMonth = ref(new Date().getMonth())
const currentYear = ref(new Date().getFullYear())

// 排行榜
const ranking = ref<RankingEntry[]>([])
const rankingLoading = ref(false)

// 月份名称
const monthNames = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']

// 获取当天日期
const today = new Date()
const todayStr = computed(() => formatDate(today))
const todayDate = today.getDate()

// 格式化日期为 YYYY-MM-DD
function formatDate(d: Date): string {
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${y}-${m}-${day}`
}

// 当前年-月字符串
const yearMonth = computed(() => {
  return `${currentYear.value}-${String(currentMonth.value + 1).padStart(2, '0')}`
})

// 是否为当前月份
const isCurrentMonth = computed(() => {
  const now = new Date()
  return currentYear.value === now.getFullYear() && currentMonth.value === now.getMonth()
})

// 已签到的日期集合
const checkedInSet = computed(() => {
  return new Set(status.value?.checkInDates ?? [])
})

// 日历数据
const calendarDays = computed(() => {
  const year = currentYear.value
  const month = currentMonth.value
  const firstDay = new Date(year, month, 1).getDay() // 0=周日
  const daysInMonth = new Date(year, month + 1, 0).getDate()

  const days: { date: number; isToday: boolean; isCheckedIn: boolean; isPast: boolean; dateStr: string }[] = []

  // 填充空白
  for (let i = 0; i < firstDay; i++) {
    days.push({ date: 0, isToday: false, isCheckedIn: false, isPast: false, dateStr: '' })
  }

  for (let d = 1; d <= daysInMonth; d++) {
    const dateStr = `${year}-${String(month + 1).padStart(2, '0')}-${String(d).padStart(2, '0')}`
    const dateObj = new Date(year, month, d)
    const isToday = dateStr === todayStr.value
    const isPast = dateObj < new Date(today.getFullYear(), today.getMonth(), today.getDate() + 1)
    const isCheckedIn = checkedInSet.value.has(dateStr)

    days.push({ date: d, isToday, isCheckedIn, isPast, dateStr })
  }

  return days
})

// 签到动画
const showAnimation = ref(false)

// 执行签到
async function doCheckIn() {
  if (checkInLoading.value) return
  if (status.value?.checkedInToday) {
    message.warning('今日已签到，明天再来吧 ~')
    return
  }

  checkInLoading.value = true
  try {
    const res = await doCheckInApi()
    if (res.data.code === 200) {
      message.success(`签到成功！+${res.data.data.points} 积分`)
      showAnimation.value = true
      setTimeout(() => { showAnimation.value = false }, 1500)
      // 重新获取状态
      await loadStatus()
    } else {
      message.error(res.data.message)
    }
  } catch (e: any) {
    message.error(e?.response?.data?.message || '签到失败，请稍后重试')
  } finally {
    checkInLoading.value = false
  }
}

// 加载排行榜
async function loadRanking() {
  rankingLoading.value = true
  try {
    const res = await getRankingApi(20)
    if (res.data.code === 200) {
      ranking.value = res.data.data
    }
  } catch {
    // ignore
  } finally {
    rankingLoading.value = false
  }
}

// 加载签到状态
async function loadStatus() {
  loading.value = true
  try {
    const res = await getCheckInStatusApi()
    if (res.data.code === 200) {
      status.value = res.data.data
    }
  } catch {
    // ignore
  } finally {
    loading.value = false
  }
}

// 切换月份
function prevMonth() {
  if (currentMonth.value === 0) {
    currentMonth.value = 11
    currentYear.value--
  } else {
    currentMonth.value--
  }
}

function nextMonth() {
  if (currentMonth.value === 11) {
    currentMonth.value = 0
    currentYear.value++
  } else {
    currentMonth.value++
  }
}

const weekDays = ['日', '一', '二', '三', '四', '五', '六']

onMounted(() => {
  loadStatus()
  loadRanking()
})
</script>

<template>
  <div class="checkin-page">
    <!-- 顶部 Banner -->
    <div class="banner">
      <div class="banner-bg" />
      <div class="banner-content">
        <!-- <div class="banner-icon">
          <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
            <path d="M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2Z" fill="rgba(255,255,255,0.15)" stroke="#fff"/>
          </svg>
        </div> -->
        <h1 class="banner-title">每日签到</h1>
        <p class="banner-desc">坚持签到，积累积分，解锁更多精彩</p>
      </div>
    </div>

    <div class="checkin-body">
      <!-- 左侧：主内容区 -->
      <div class="checkin-main">
        <!-- 统计卡片 -->
        <div class="stats-row reveal">
          <div class="stat-card">
            <div class="stat-value gradient-gold">{{ status?.totalPoints ?? 0 }}</div>
            <div class="stat-label">总积分</div>
          </div>
          <div class="stat-card">
            <div class="stat-value gradient-blue">{{ status?.consecutiveDays ?? 0 }}</div>
            <div class="stat-label">连续签到 (天)</div>
          </div>
          <div class="stat-card">
            <div class="stat-value gradient-green">{{ status?.monthlyCount ?? 0 }}</div>
            <div class="stat-label">本月签到</div>
          </div>
        </div>

        <!-- 签到按钮区 -->
        <div class="checkin-action reveal reveal-delay-1">
          <button
            class="checkin-btn"
            :class="{ checked: status?.checkedInToday, animating: showAnimation }"
            :disabled="checkInLoading"
            @click="doCheckIn"
          >
            <div class="btn-inner">
              <!-- 未签到 -->
              <template v-if="!status?.checkedInToday">
                <svg class="btn-icon" width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <polyline points="20 6 9 17 4 12" />
                </svg>
                <span class="btn-text">{{ checkInLoading ? '签到中...' : '签到' }}</span>
              </template>
              <!-- 已签到 -->
              <template v-else>
                <svg class="btn-icon" width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
                  <polyline points="22 4 12 14.01 9 11.01"/>
                </svg>
                <span class="btn-text">已签到</span>
              </template>
            </div>
            <div class="btn-points" v-if="!status?.checkedInToday">+1 积分</div>
          </button>

          <!-- 签到成功动画浮层 -->
          <Transition name="anim-pop">
            <div v-if="showAnimation" class="checkin-anim-overlay">
              <div class="checkin-anim">
                <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="#52c41a" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
                  <polyline points="22 4 12 14.01 9 11.01"/>
                </svg>
                <div class="anim-text">签到成功!</div>
              </div>
            </div>
          </Transition>
        </div>

        <!-- 日历卡片 -->
        <div class="calendar-card reveal reveal-delay-2">
          <div class="calendar-header">
            <button class="month-nav" @click="prevMonth">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="15 18 9 12 15 6"/></svg>
            </button>
            <span class="month-title">{{ currentYear }}年 {{ monthNames[currentMonth] }}</span>
            <button class="month-nav" @click="nextMonth">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="9 18 15 12 9 6"/></svg>
            </button>
          </div>

          <div class="calendar-weekdays">
            <span v-for="w in weekDays" :key="w" class="weekday">{{ w }}</span>
          </div>

          <div class="calendar-grid">
            <div
              v-for="(day, idx) in calendarDays"
              :key="idx"
              class="calendar-cell"
              :class="{
                'cell-empty': day.date === 0,
                'cell-today': day.isToday,
                'cell-checked': day.isCheckedIn && !day.isToday,
                'cell-future': !day.isPast && !day.isToday,
              }"
            >
              <span v-if="day.date > 0" class="cell-date">{{ day.date }}</span>
              <span v-if="day.isCheckedIn && !day.isToday" class="cell-dot">✓</span>
              <span v-if="day.isToday && status?.checkedInToday" class="cell-today-dot">✓</span>
            </div>
          </div>
        </div>

        <!-- 积分规则 -->
        <div class="rules-card reveal reveal-delay-3">
          <div class="rules-title">🏆 积分规则</div>
          <div class="rules-list">
            <div class="rule-item">
              <span class="rule-dot" style="--c:#f0a020"></span>
              <span>每日签到：<strong>+1</strong> 积分</span>
            </div>
            <div class="rule-item">
              <span class="rule-dot" style="--c:#6366f1"></span>
              <span>连续 7 天：额外 <strong>+2</strong> 积分</span>
            </div>
            <div class="rule-item">
              <span class="rule-dot" style="--c:#52c41a"></span>
              <span>连续 30 天：额外 <strong>+10</strong> 积分</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧：排行榜 -->
      <aside class="ranking-sidebar reveal reveal-delay-2">
        <div class="ranking-card">
          <div class="ranking-header">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M6 9H4.5a2.5 2.5 0 0 1 0-5C7 4 7 7 7 7"/>
              <path d="M18 9h1.5a2.5 2.5 0 0 0 0-5C17 4 17 7 17 7"/>
              <path d="M4 22h16"/>
              <path d="M10 22V2h4v20"/>
            </svg>
            <span>积分排行榜</span>
          </div>

          <div class="ranking-list" v-if="!rankingLoading && ranking.length > 0">
            <div
              v-for="(item, idx) in ranking"
              :key="item.userId"
              class="ranking-item"
              :class="{ 'top-1': idx === 0, 'top-2': idx === 1, 'top-3': idx === 2 }"
            >
              <span class="ranking-pos" :class="{ 'medal': idx < 3 }">
                <template v-if="idx === 0">🥇</template>
                <template v-else-if="idx === 1">🥈</template>
                <template v-else-if="idx === 2">🥉</template>
                <template v-else>{{ item.rank }}</template>
              </span>
              <div class="ranking-user">
                <img
                  v-if="item.avatarUrl"
                  :src="item.avatarUrl"
                  class="ranking-avatar"
                  alt="avatar"
                />
                <div v-else class="ranking-avatar ranking-avatar-placeholder">
                  {{ item.nickname?.charAt(0) ?? '?' }}
                </div>
                <span class="ranking-name">{{ item.nickname }}</span>
                <span
                  v-if="item.roleName && item.roleName !== '普通用户'"
                  class="ranking-role"
                  :class="'role-' + item.roleName"
                >{{ item.roleName }}</span>
              </div>
              <span class="ranking-points">{{ item.totalPoints }} 分</span>
            </div>
          </div>

          <div v-else-if="rankingLoading" class="ranking-empty">加载中...</div>
          <div v-else class="ranking-empty">暂无排行数据</div>
        </div>
      </aside>
    </div>
  </div>
</template>

<style scoped>
/* ═══════════════════════════════════
   签到页面
   ═══════════════════════════════════ */

.checkin-page {
  min-height: calc(100vh - 64px);
  background: linear-gradient(180deg, #f0f2f5 0%, #f8f9fc 100%);
  padding-bottom: 48px;
}

/* ═══════════════════════════════════
   Banner
   ═══════════════════════════════════ */
.banner {
  position: relative;
  width: 100%;
  padding: 56px 24px 48px;
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
    radial-gradient(ellipse 60% 50% at 90% 20%, rgba(240, 160, 32, 0.12) 0%, transparent 60%),
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

.banner-icon {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16px;
  backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.12);
}

.banner-title {
  font-size: 32px;
  font-weight: 800;
  color: #fff;
  margin: 0 0 8px;
  letter-spacing: 0.5px;
}

.banner-desc {
  font-size: 15px;
  color: rgba(255, 255, 255, 0.6);
  margin: 0;
}

/* ═══════════════════════════════════
   统计卡片
   ═══════════════════════════════════ */
.stats-row {
  display: flex;
  justify-content: center;
  gap: 16px;
  padding: 0 24px;
  margin-top: -20px;
  position: relative;
  z-index: 2;
}

.stat-card {
  flex: 1;
  max-width: 200px;
  background: #fff;
  border-radius: 16px;
  padding: 24px 20px;
  text-align: center;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.stat-value {
  font-size: 36px;
  font-weight: 800;
  line-height: 1.2;
  margin-bottom: 6px;
}

.stat-label {
  font-size: 13px;
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

/* ═══════════════════════════════════
   签到按钮
   ═══════════════════════════════════ */
.checkin-action {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 32px 24px 16px;
  position: relative;
}

.checkin-btn {
  position: relative;
  width: 160px;
  height: 160px;
  border-radius: 50%;
  border: none;
  cursor: pointer;
  background: linear-gradient(135deg, #2080f0, #6366f1);
  box-shadow: 0 8px 32px rgba(32, 128, 240, 0.35);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.checkin-btn:hover:not(:disabled) {
  transform: scale(1.05);
  box-shadow: 0 12px 40px rgba(32, 128, 240, 0.45);
}

.checkin-btn:active:not(:disabled) {
  transform: scale(0.95);
}

.checkin-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.checkin-btn.checked {
  background: linear-gradient(135deg, #52c41a, #36ad6a);
  box-shadow: 0 8px 32px rgba(82, 196, 26, 0.35);
}

.checkin-btn.checked:hover {
  box-shadow: 0 12px 40px rgba(82, 196, 26, 0.45);
}

.btn-inner {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.btn-icon {
  color: #fff;
}

.btn-text {
  font-size: 18px;
  font-weight: 700;
  color: #fff;
  letter-spacing: 1px;
}

.btn-points {
  position: absolute;
  bottom: -28px;
  font-size: 14px;
  font-weight: 600;
  color: #f0a020;
  background: rgba(240, 160, 32, 0.1);
  padding: 2px 14px;
  border-radius: 12px;
  white-space: nowrap;
}

/* 签到动画浮层 */
.checkin-anim-overlay {
  position: fixed;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  pointer-events: none;
}

.checkin-anim {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  background: rgba(255, 255, 255, 0.95);
  padding: 32px 48px;
  border-radius: 24px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
  backdrop-filter: blur(20px);
}

.anim-text {
  font-size: 24px;
  font-weight: 700;
  color: #52c41a;
}

.anim-pop-enter-active {
  transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.anim-pop-leave-active {
  transition: all 0.3s ease;
}

.anim-pop-enter-from {
  opacity: 0;
  transform: scale(0.5);
}

.anim-pop-leave-to {
  opacity: 0;
  transform: scale(0.8);
}

/* ═══════════════════════════════════
   日历卡片
   ═══════════════════════════════════ */
.calendar-card {
  max-width: 480px;
  margin: 24px auto 0;
  background: #fff;
  border-radius: 20px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.calendar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.month-title {
  font-size: 18px;
  font-weight: 700;
  color: #1d1e1f;
}

.month-nav {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: none;
  background: #f5f7fa;
  color: #606266;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.month-nav:hover {
  background: #e8ecf1;
  color: #2080f0;
}

.calendar-weekdays {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 4px;
  margin-bottom: 8px;
}

.weekday {
  text-align: center;
  font-size: 13px;
  font-weight: 600;
  color: #909399;
  padding: 8px 0;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 4px;
}

.calendar-cell {
  aspect-ratio: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 500;
  color: #1d2939;
  position: relative;
  transition: all 0.2s;
}

.cell-empty {
  visibility: hidden;
}

.cell-date {
  z-index: 1;
}

.cell-checked {
  color: #36ad6a;
}

.cell-checked .cell-date {
  font-weight: 600;
}

.cell-dot {
  position: absolute;
  bottom: 4px;
  font-size: 10px;
  font-weight: 700;
  color: #52c41a;
}

.cell-today {
  background: rgba(32, 128, 240, 0.08);
  font-weight: 700;
  color: #2080f0;
}

.cell-today-dot {
  position: absolute;
  bottom: 4px;
  font-size: 10px;
  font-weight: 700;
  color: #2080f0;
}

.cell-future {
  color: #c0c4cc;
}

.calendar-cell:not(.cell-empty):not(.cell-future):hover {
  background: #f5f7fa;
}

/* ═══════════════════════════════════
   积分规则
   ═══════════════════════════════════ */
.rules-card {
  max-width: 480px;
  margin: 16px auto 0;
  background: #fff;
  border-radius: 20px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.rules-title {
  font-size: 16px;
  font-weight: 700;
  color: #1d1e1f;
  margin-bottom: 16px;
}

.rules-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.rule-item {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  color: #606266;
}

.rule-item strong {
  color: #1d2939;
}

.rule-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: var(--c);
  flex-shrink: 0;
}

/* ═══════════════════════════════════
   响应式
   ═══════════════════════════════════ */
@media (max-width: 640px) {
  .banner {
    padding: 40px 20px 40px;
  }

  .banner-title {
    font-size: 26px;
  }

  .stats-row {
    gap: 10px;
    padding: 0 16px;
  }

  .stat-card {
    padding: 18px 12px;
  }

  .stat-value {
    font-size: 28px;
  }

  .checkin-btn {
    width: 130px;
    height: 130px;
  }

  .btn-text {
    font-size: 16px;
  }

  .calendar-card,
  .rules-card {
    margin-left: 16px;
    margin-right: 16px;
    padding: 18px;
  }
}

@media (max-width: 400px) {
  .stats-row {
    flex-direction: column;
    align-items: center;
  }

  .stat-card {
    max-width: 100%;
    width: 100%;
  }
}

/* ═══════════════════════════════════
   双栏布局
   ═══════════════════════════════════ */
.checkin-body {
  display: flex;
  gap: 24px;
  max-width: 960px;
  margin: 0 auto;
  padding: 0 24px;
  align-items: flex-start;
}

.checkin-main {
  flex: 1;
  min-width: 0;
}

/* ═══════════════════════════════════
   排行榜侧边栏
   ═══════════════════════════════════ */
.ranking-sidebar {
  width: 280px;
  flex-shrink: 0;
  position: sticky;
  top: 80px;
}

.ranking-card {
  background: #fff;
  border-radius: 20px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.ranking-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 700;
  color: #1d1e1f;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 12px;
}

.ranking-header svg {
  color: #f0a020;
  flex-shrink: 0;
}

.ranking-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.ranking-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 10px;
  border-radius: 12px;
  transition: background 0.2s;
}

.ranking-item:hover {
  background: #f5f7fa;
}

.ranking-item.top-1 { background: linear-gradient(135deg, #fff9e6, #fff3cc); }
.ranking-item.top-2 { background: linear-gradient(135deg, #f5f7fa, #eef1f5); }
.ranking-item.top-3 { background: linear-gradient(135deg, #fff4e6, #ffecd6); }

.ranking-pos {
  width: 24px;
  text-align: center;
  font-size: 14px;
  font-weight: 700;
  color: #909399;
  flex-shrink: 0;
}

.ranking-pos.medal {
  font-size: 18px;
}

.ranking-user {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 0;
}

.ranking-avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
}

.ranking-avatar-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #2080f0, #6366f1);
  color: #fff;
  font-size: 12px;
  font-weight: 700;
}

.ranking-name {
  font-size: 13px;
  font-weight: 600;
  color: #1d2939;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.ranking-role {
  font-size: 10px;
  font-weight: 600;
  padding: 1px 6px;
  border-radius: 4px;
  flex-shrink: 0;
  line-height: 1.4;
}

.ranking-role.role-管理员 {
  background: #fff1f0;
  color: #f5222d;
}

.ranking-role.role-教师 {
  background: #e6f7ff;
  color: #1890ff;
}

.ranking-role.role-学生 {
  background: #f6ffed;
  color: #52c41a;
}

.ranking-points {
  font-size: 13px;
  font-weight: 700;
  color: #f0a020;
  white-space: nowrap;
  flex-shrink: 0;
}

.ranking-empty {
  text-align: center;
  color: #c0c4cc;
  font-size: 14px;
  padding: 32px 0;
}

/* 响应式：小屏时排行榜放到下面 */
@media (max-width: 900px) {
  .checkin-body {
    flex-direction: column;
    padding: 0 16px;
  }

  .ranking-sidebar {
    width: 100%;
    position: static;
    margin-top: 0;
  }
}
</style>
