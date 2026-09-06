<script setup lang="ts">
import { ref, onMounted, watch, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'
import { getDashboardApi } from '../api'
import type { DashboardData } from '../api'
import AdminUsersSection from '../components/AdminUsersSection.vue'

const router = useRouter()
const loading = ref(true)
const error = ref('')
const dashboard = ref<DashboardData | null>(null)

const activeTab = ref<'dashboard' | 'users'>('dashboard')
const tabs = [
  { key: 'dashboard', label: '仪表盘' },
  { key: 'users', label: '用户管理' },
] as const

const gaugeRef = ref<HTMLDivElement>()
const barRef = ref<HTMLDivElement>()
let gaugeChart: echarts.ECharts | null = null
let barChart: echarts.ECharts | null = null

// 不管本地 roleId 是什么，交给服务端鉴权
loadDashboard()

async function loadDashboard() {
  loading.value = true
  try {
    const res = await getDashboardApi()
    if (res.data.code === 200) {
      dashboard.value = res.data.data
      loading.value = false
      await nextTick()
      initCharts()
    } else {
      error.value = res.data.message
    }
  } catch (err: any) {
    error.value = err?.response?.data?.message || '加载失败'
  } finally {
    loading.value = false
  }
}

function initCharts() {
  if (!dashboard.value) return
  const d = dashboard.value

  // --- 今日签到率仪表盘（半圆 gauge） ---
  if (gaugeRef.value) {
    gaugeChart?.dispose()
    gaugeChart = echarts.init(gaugeRef.value)
    const rate = d.totalUsers > 0 ? Math.round((d.todayCheckins / d.totalUsers) * 100) : 0
    gaugeChart.setOption({
      series: [{
        type: 'gauge',
        startAngle: 180,
        endAngle: 0,
        min: 0,
        max: 100,
        splitNumber: 5,
        progress: { show: true, width: 12 },
        axisLine: {
          lineStyle: { width: 12, color: [[1, '#e9ecef']] },
        },
        axisTick: { show: false },
        splitLine: { show: false },
        axisLabel: { show: false },
        detail: {
          offsetCenter: [0, '40%'],
          fontSize: 28,
          fontWeight: 700,
          lineHeight: 40,
          formatter: `{value}%\n{label|今日签到率}`,
          rich: { label: { fontSize: 16, color: '#909399', fontWeight: 500, lineHeight: 30 } },
        },
        pointer: { show: false },
        data: [{ value: rate }],
      }],
      graphic: [
        { type: 'text', left: 'center', top: '75%', style: { text: `${d.todayCheckins}/${d.totalUsers} 人`, fill: '#606266', fontSize: 12 } },
      ],
    })
    window.addEventListener('resize', () => gaugeChart?.resize())
  }

  // --- 双柱对比图（今日 vs 本月日均） ---
  if (barRef.value) {
    barChart?.dispose()
    barChart = echarts.init(barRef.value)
    const daysInMonth = new Date().getDate()
    const dailyAvg = daysInMonth > 0 ? +(d.monthCheckins / daysInMonth).toFixed(1) : 0
    barChart.setOption({
      tooltip: { trigger: 'axis' },
      grid: { left: '10%', right: '10%', bottom: '15%', top: '15%', containLabel: true },
      xAxis: {
        type: 'category',
        data: ['今日签到', '本月日均'],
        axisTick: { alignWithLabel: true },
        axisLabel: { fontSize: 12, color: '#606266' },
      },
      yAxis: { type: 'value', splitLine: { lineStyle: { color: '#f0f0f0' } }, axisLabel: { fontSize: 11 } },
      series: [{
        type: 'bar',
        barWidth: '40%',
        data: [
          { value: d.todayCheckins, itemStyle: { color: '#409eff', borderRadius: [6, 6, 0, 0] } },
          { value: dailyAvg, itemStyle: { color: '#67c23a', borderRadius: [6, 6, 0, 0] } },
        ],
        label: {
          show: true,
          position: 'top',
          fontSize: 14,
          fontWeight: 600,
          formatter: (p: any) => p.value,
        },
      }],
    })
    window.addEventListener('resize', () => barChart?.resize())
  }
}

onMounted(() => {
  if (dashboard.value) {
    nextTick(initCharts)
  }
})
</script>

<template>
  <div class="admin-page">
    <div class="admin-header">
      <h1>管理后台</h1>
      <div class="admin-nav">
        <button
          v-for="tab in tabs"
          :key="tab.key"
          :class="['nav-btn', { active: activeTab === tab.key }]"
          @click="activeTab = tab.key"
        >
          {{ tab.label }}
        </button>
      </div>
    </div>

    <div v-if="error" class="admin-error">
      <h2>{{ error }}</h2>
      <p>需要管理员权限才能访问此页面。</p>
      <button class="back-btn" @click="router.push('/')">返回首页</button>
    </div>

    <!-- 仪表盘 -->
    <div v-else-if="activeTab === 'dashboard'">
      <div v-if="loading" class="loading">加载中...</div>
      <template v-else>
        <!-- KPI 卡片 -->
        <div class="dashboard-cards">
          <div class="card card-users">
            <div class="card-icon">👥</div>
            <div class="card-body">
              <div class="card-value">{{ dashboard?.totalUsers ?? 0 }}</div>
              <div class="card-label">用户总数</div>
            </div>
          </div>
          <div class="card card-today">
            <div class="card-icon">📅</div>
            <div class="card-body">
              <div class="card-value">{{ dashboard?.todayCheckins ?? 0 }}</div>
              <div class="card-label">今日签到</div>
            </div>
          </div>
          <div class="card card-month">
            <div class="card-icon">📊</div>
            <div class="card-body">
              <div class="card-value">{{ dashboard?.monthCheckins ?? 0 }}</div>
              <div class="card-label">本月签到</div>
            </div>
          </div>
        </div>

        <!-- ECharts 图表 -->
        <div class="charts-row">
          <div class="chart-box">
            <h3 class="chart-title">今日签到率</h3>
            <div ref="gaugeRef" class="chart-container"></div>
          </div>
          <div class="chart-box">
            <h3 class="chart-title">签到对比</h3>
            <div ref="barRef" class="chart-container"></div>
          </div>
        </div>
      </template>
    </div>

    <!-- 用户管理 -->
    <div v-else-if="activeTab === 'users' && !error">
      <AdminUsersSection />
    </div>
  </div>
</template>

<style scoped>
.admin-page {
  min-height: calc(100vh - 64px);
  background: #f5f7fa;
  padding: 24px;
}

.admin-header {
  max-width: 1200px;
  margin: 0 auto 24px;
}

.admin-header h1 {
  font-size: 24px;
  font-weight: 700;
  color: #1d1e1f;
  margin: 0 0 16px;
}

.admin-nav {
  display: flex;
  gap: 4px;
  border-bottom: 2px solid #e8eaed;
}

.nav-btn {
  padding: 10px 20px;
  border: none;
  background: none;
  font-size: 15px;
  color: #606266;
  cursor: pointer;
  position: relative;
  font-weight: 500;
  transition: color 0.2s;
}

.nav-btn:hover { color: #409eff; }

.nav-btn.active { color: #409eff; }

.nav-btn.active::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  right: 0;
  height: 2px;
  background: #409eff;
}

.admin-error {
  max-width: 500px;
  margin: 80px auto;
  text-align: center;
  background: #fff;
  padding: 48px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}

.admin-error h2 { color: #e74c3c; margin: 0 0 12px; }
.admin-error p { color: #909399; margin: 0 0 24px; }

.back-btn {
  padding: 10px 32px;
  background: #409eff;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
}

.back-btn:hover { background: #337ecc; }

/* ── KPI 卡片 ── */
.dashboard-cards {
  max-width: 1200px;
  margin: 0 auto 24px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
}

.card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.04);
  transition: transform 0.2s, box-shadow 0.2s;
}

.card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(0,0,0,0.08);
}

.card-icon {
  width: 64px;
  height: 64px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
}

.card-users .card-icon { background: #ecf5ff; }
.card-today .card-icon { background: #f0f9eb; }
.card-month .card-icon { background: #fdf6ec; }

.card-value {
  font-size: 32px;
  font-weight: 700;
  color: #1d1e1f;
  line-height: 1.2;
}

.card-label { font-size: 14px; color: #909399; margin-top: 4px; }

/* ── 图表 ── */
.charts-row {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.chart-box {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.04);
}

.chart-title {
  margin: 0 0 8px;
  font-size: 15px;
  font-weight: 600;
  color: #1d1e1f;
}

.chart-container {
  width: 100%;
  height: 260px;
}

.loading {
  text-align: center;
  padding: 60px;
  color: #909399;
  font-size: 15px;
}

@media (max-width: 768px) {
  .charts-row {
    grid-template-columns: 1fr;
  }
}
</style>
