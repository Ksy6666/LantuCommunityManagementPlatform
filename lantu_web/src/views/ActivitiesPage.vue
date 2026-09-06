<script setup lang="ts">
import { ref, computed } from 'vue'

interface Activity {
  title: string
  desc: string
  date: string
  tag: string
  category: string
  status: 'upcoming' | 'ongoing' | 'done'
  icon: string
}

const activities: Activity[] = [
  {
    title: 'Rust 入门工作坊',
    desc: '从零开始学习 Rust 基础语法、所有权模型和常用库，完成一个命令行工具项目。适合零基础同学。',
    date: '2026.06.15',
    tag: '工作坊',
    category: 'workshop',
    status: 'upcoming',
    icon: 'Tools',
  },
  {
    title: '夏季 Code Jam',
    desc: '组队在 48 小时内完成一个完整项目，从前端到后端再到部署，体验全流程开发的快感。',
    date: '2026.07.01',
    tag: '竞赛',
    category: 'contest',
    status: 'upcoming',
    icon: 'Trophy',
  },
  {
    title: '技术分享：React 原理深入',
    desc: '由团队成员分享 React 的 Fiber 架构、调度机制和 Hooks 实现原理。',
    date: '2026.05.25',
    tag: '分享会',
    category: 'talk',
    status: 'ongoing',
    icon: 'Presentation',
  },
  {
    title: '开源项目 Sprint',
    desc: '集中攻坚组织维护的开源项目，分工协作完成 Issue 和 PR，体验真实开源贡献流程。',
    date: '2026.05.10',
    tag: '项目',
    category: 'project',
    status: 'done',
    icon: 'FolderOpened',
  },
  {
    title: '技术分享：逆向工程入门',
    desc: '了解二进制逆向分析的基础工具和方法，通过动手实验理解程序执行的底层原理。',
    date: '2026.04.20',
    tag: '分享会',
    category: 'talk',
    status: 'done',
    icon: 'Monitor',
  },
  {
    title: '春招经验分享会',
    desc: '邀请拿到大厂 offer 的学长学姐分享面试准备、简历撰写和求职时间线规划。',
    date: '2026.03.15',
    tag: '分享会',
    category: 'talk',
    status: 'done',
    icon: 'ChatDotSquare',
  },
  {
    title: 'Hackathon 校内选拔赛',
    desc: '为校外 Hackathon 预热，校内组队选拔，胜出队伍将代表学校参加 regional 决赛。',
    date: '2026.02.28',
    tag: '竞赛',
    category: 'contest',
    status: 'done',
    icon: 'Lightning',
  },
  {
    title: '圆桌讨论：AI 对程序员的影响',
    desc: '围绕 ChatGPT、Copilot 等 AI 工具对开发者工作方式的影响展开自由讨论。',
    date: '2026.01.20',
    tag: '圆桌',
    category: 'talk',
    status: 'done',
    icon: 'ChatLineSquare',
  },
]

const categories = [
  { key: 'all', label: '全部' },
  { key: 'talk', label: '分享会' },
  { key: 'workshop', label: '工作坊' },
  { key: 'contest', label: '竞赛' },
  { key: 'project', label: '项目' },
]

const activeCategory = ref('all')

const filteredActivities = computed(() =>
  activeCategory.value === 'all'
    ? activities
    : activities.filter((a) => a.category === activeCategory.value),
)

interface FriendSite {
  name: string
  desc: string
  url: string
  icon: string
}

const friendSites = ref<FriendSite[]>([
  {
    name: '力扣（LeetCode）',
    desc: '全球极客挚爱的技术成长平台。海量技术面试题库，涵盖算法、数据结构、系统设计等方向，助力程序员提升编程能力，备战技术面试。',
    url: 'https://leetcode.cn/',
    icon: '/src/assets/log_img-Other/avatar_1582018938.png',
  },
])

const showFriendSites = ref(false)

function selectCategory(key: string) {
  activeCategory.value = key
  showFriendSites.value = false
}

function toggleFriends() {
  showFriendSites.value = !showFriendSites.value
  if (showFriendSites.value) {
    activeCategory.value = '__friends__'
  } else {
    activeCategory.value = 'all'
  }
}

function openSite(url: string) {
  window.open(url, '_blank')
}
</script>

<template>
  <div class="activities-page">
    <!-- ════════════════ Hero ════════════════ -->
    <section class="page-hero">
      <div class="ph-bg">
        <div class="ph-shape s1"></div>
        <div class="ph-shape s2"></div>
      </div>
      <div class="ph-content">
        <span class="ph-badge">Activities</span>
        <h1>我们的活动</h1>
        <p class="ph-desc">技术分享、Code Jam、开源贡献……总有一款适合你</p>
      </div>
    </section>

    <!-- ════════════════ 分类筛选 ════════════════ -->
    <section class="filter-section reveal">
      <div class="filter-inner">
        <div class="filter-bar">
          <button
            v-for="cat in categories"
            :key="cat.key"
            class="filter-btn"
            :class="{ active: activeCategory === cat.key && !showFriendSites }"
            @click="selectCategory(cat.key)"
          >
            {{ cat.label }}
          </button>
          <button
            class="filter-btn friend-btn"
            :class="{ active: showFriendSites }"
            @click="toggleFriends"
          >
            友情网站
          </button>
        </div>
      </div>
    </section>

    <!-- ════════════════ 活动列表 ════════════════ -->
    <section class="section list-section reveal">
      <div class="section-inner">
        <!-- 友情网站列表 -->
        <template v-if="showFriendSites">
          <div class="friend-grid">
            <div
              v-for="(site, i) in friendSites"
              :key="i"
              class="friend-card card reveal"
              :class="`reveal-delay-${i + 1}`"
              @click="openSite(site.url)"
            >
              <div class="fc-icon">
                <img :src="site.icon" :alt="site.name" />
              </div>
              <div class="fc-body">
                <h3>{{ site.name }}</h3>
                <p>{{ site.desc }}</p>
                <span class="fc-link">前往网站 →</span>
              </div>
            </div>
          </div>
        </template>

        <!-- 活动列表 -->
        <template v-else>
          <div v-if="filteredActivities.length === 0" class="empty-state">
            <n-icon :size="48" color="#c0c4cc"><FolderDelete /></n-icon>
            <p>该分类暂无活动</p>
          </div>

          <div v-else class="activity-list">
            <div
              v-for="(item, i) in filteredActivities"
              :key="i"
              class="activity-item card reveal"
              :class="`reveal-delay-${Math.min(i + 1, 5)}`"
            >
              <div class="ai-left">
                <div
                  class="ai-status"
                  :class="item.status"
                >
                  <template v-if="item.status === 'upcoming'">即将</template>
                  <template v-else-if="item.status === 'ongoing'">进行</template>
                  <template v-else>已结束</template>
                </div>
              </div>

              <div class="ai-body">
                <div class="ai-meta">
                  <span class="ai-date">{{ item.date }}</span>
                  <span class="ai-tag">{{ item.tag }}</span>
                </div>
                <h3>{{ item.title }}</h3>
                <p>{{ item.desc }}</p>
              </div>

              <div class="ai-icon">
                <n-icon :size="24" color="#c0c4cc"><component :is="item.icon" /></n-icon>
              </div>
            </div>
          </div>
        </template>
      </div>
    </section>

    <!-- ════════════════ 活动形式 ════════════════ -->
    <section class="section section-alt format-section reveal">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">Formats</span>
          <h2 class="section-title">活动形式</h2>
          <p class="section-desc">多样化的活动，总有一种方式适合你参与</p>
        </div>

        <div class="format-grid">
          <div class="format-card card">
            <n-icon :size="28" color="#2080f0"><Reading /></n-icon>
            <h3>技术分享会</h3>
            <p>每两周一次，由成员轮流分享感兴趣的技术话题。可以是框架原理、工具推荐、踩坑经验——什么都可以讲。</p>
            <span class="format-freq">每双周 · 周末</span>
          </div>
          <div class="format-card card">
            <n-icon :size="28" color="#2080f0"><Monitor /></n-icon>
            <h3>Code Jam</h3>
            <p>限时编程挑战赛，小组在 48 小时内从零开始完成一个项目。结束后有 Demo 展示和互评环节。</p>
            <span class="format-freq">每月 · 周末</span>
          </div>
          <div class="format-card card">
            <n-icon :size="28" color="#2080f0"><ChatDotSquare /></n-icon>
            <h3>圆桌讨论</h3>
            <p>围绕一个话题自由发言，没有 PPT 没有 agenda。技术趋势、求职经验、学习路线——聊什么你来定。</p>
            <span class="format-freq">不定期</span>
          </div>
          <div class="format-card card">
            <n-icon :size="28" color="#2080f0"><Files /></n-icon>
            <h3>项目 Sprint</h3>
            <p>集中一段时间攻坚组织维护的开源项目，在真实协作中学习 Git 工作流和团队开发规范。</p>
            <span class="format-freq">每月 · 线上</span>
          </div>
        </div>
      </div>
    </section>

    <!-- ════════════════ 如何参与 ════════════════ -->
    <section class="section howto-section reveal">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">How to Join</span>
          <h2 class="section-title">如何参与</h2>
          <p class="section-desc">参加活动没有门槛，来就对了</p>
        </div>

        <div class="howto-grid">
          <div class="howto-step card">
            <div class="ht-number">1</div>
            <h3>加入社群</h3>
            <p>添加微信 Open_Lantu_Admin 进群，所有活动通知都在群里发布。</p>
          </div>
          <div class="howto-step card">
            <div class="ht-number">2</div>
            <h3>选择活动</h3>
            <p>看看日历，选一个你感兴趣的活动报名。线上或线下，随你方便。</p>
          </div>
          <div class="howto-step card">
            <div class="ht-number">3</div>
            <h3>直接来玩</h3>
            <p>不需要准备，不需要基础。人来就行，剩下的交给我们。</p>
          </div>
        </div>
      </div>
    </section>

    <!-- ════════════════ CTA ════════════════ -->
    <section class="section section-alt cta-section reveal">
      <div class="section-inner">
        <div class="cta-card">
          <h2>想参与我们的活动？</h2>
          <p>关注活动通知，第一时间获取活动信息和报名链接。</p>
          <n-button type="primary" size="large" round @click="$router.push('/join')">
            加入我们
          </n-button>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
/* ── Page Hero ── */
.ph-bg {
  position: absolute;
  inset: 0;
  background: linear-gradient(160deg, #fff7e6 0%, #fefce8 30%, #ecfdf5 70%, #e8f4ff 100%);
  z-index: 0;
}

.ph-shape {
  position: absolute;
  border-radius: 50%;
  opacity: 0.2;
}

.s1 {
  width: 350px;
  height: 350px;
  background: radial-gradient(circle, rgba(240, 160, 32, 0.15), transparent);
  top: -100px;
  right: -80px;
}

.s2 {
  width: 250px;
  height: 250px;
  background: radial-gradient(circle, rgba(32, 128, 240, 0.12), transparent);
  bottom: -60px;
  left: -40px;
}

.ph-badge {
  color: #f0a020;
  background: rgba(240, 160, 32, 0.1);
}

/* ── Filter ── */
.filter-section {
  background: #fafafa;
  padding: 24px 24px 0;
}

.filter-inner {
  max-width: 1100px;
  margin: 0 auto;
}

.filter-bar {
  display: flex;
  justify-content: center;
  gap: 12px;
  flex-wrap: wrap;
}

.filter-btn {
  padding: 8px 24px;
  border-radius: 20px;
  border: 1px solid #e4e7ed;
  background: #fff;
  font-size: 14px;
  color: #606266;
  cursor: pointer;
  transition: all 0.25s;
  font-family: inherit;
}

.filter-btn:hover {
  border-color: #2080f0;
  color: #2080f0;
}

.filter-btn.active {
  background: #2080f0;
  border-color: #2080f0;
  color: #fff;
}

.friend-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  border-color: #6366f1;
  color: #6366f1;
}

.friend-btn:hover {
  border-color: #6366f1 !important;
  color: #6366f1 !important;
  background: rgba(99, 102, 241, 0.06);
}

.friend-btn.active {
  background: #6366f1 !important;
  border-color: #6366f1 !important;
  color: #fff !important;
}

.friend-icon {
  width: 18px;
  height: 18px;
  border-radius: 4px;
  flex-shrink: 0;
}

/* ── Friend Sites Grid ── */
.friend-grid {
  max-width: 860px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.friend-card {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 24px;
  cursor: pointer;
  transition: all 0.25s;
}

.friend-card:hover {
  border-color: #6366f1;
  box-shadow: 0 8px 24px rgba(99, 102, 241, 0.1);
  transform: translateX(4px);
}

.fc-icon {
  flex-shrink: 0;
  width: 56px;
  height: 56px;
  border-radius: 12px;
  overflow: hidden;
  background: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.fc-icon img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.fc-body {
  flex: 1;
  min-width: 0;
}

.fc-body h3 {
  font-size: 17px;
  font-weight: 600;
  color: #1d1e1f;
  margin: 0 0 6px;
}

.fc-body p {
  font-size: 14px;
  color: #606266;
  line-height: 1.7;
  margin: 0 0 8px;
}

.fc-link {
  font-size: 13px;
  font-weight: 500;
  color: #6366f1;
  white-space: nowrap;
}

/* ── Activity List ── */
.list-section {
  background: #fff;
  padding-top: 0;
}

.activity-list {
  max-width: 860px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.activity-item {
  display: flex;
  align-items: stretch;
  gap: 0;
  overflow: hidden;
}

.activity-item:hover {
  border-color: #e0e0e0;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.05);
  transform: translateX(4px);
}

.ai-left {
  display: flex;
  align-items: center;
  padding: 0 16px;
  background: #fafafa;
  border-right: 1px solid #f0f0f0;
  min-width: 72px;
  justify-content: center;
}

.ai-status {
  font-size: 12px;
  font-weight: 600;
  padding: 3px 10px;
  border-radius: 8px;
  white-space: nowrap;
}

.ai-status.upcoming {
  background: rgba(240, 160, 32, 0.1);
  color: #f0a020;
}

.ai-status.ongoing {
  background: rgba(54, 173, 106, 0.1);
  color: #36ad6a;
}

.ai-status.done {
  background: #f0f0f0;
  color: #909399;
}

.ai-body {
  flex: 1;
  padding: 20px;
}

.ai-meta {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 6px;
}

.ai-date {
  font-size: 13px;
  color: #909399;
}

.ai-tag {
  font-size: 11px;
  font-weight: 500;
  color: #2080f0;
  background: rgba(32, 128, 240, 0.08);
  padding: 1px 10px;
  border-radius: 6px;
}

.ai-body h3 {
  font-size: 17px;
  font-weight: 600;
  color: #1d1e1f;
  margin: 0 0 6px;
}

.ai-body p {
  font-size: 14px;
  color: #606266;
  line-height: 1.7;
  margin: 0;
}

.ai-icon {
  display: flex;
  align-items: center;
  padding: 0 20px;
}

/* ── Empty ── */
.empty-state {
  text-align: center;
  padding: 60px 24px;
}

.empty-state p {
  margin-top: 12px;
  font-size: 15px;
  color: #c0c4cc;
}

/* ── Format Grid ── */
.format-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.format-card {
  padding: 28px 20px;
  display: flex;
  flex-direction: column;
  background: #fafafa;
}

.format-card:hover {
  background: #fff;
  border-color: #2080f0;
  box-shadow: 0 8px 24px rgba(32, 128, 240, 0.06);
}

.format-card .n-icon {
  margin-bottom: 14px;
}

.format-card h3 {
  font-size: 16px;
  font-weight: 600;
  color: #1d1e1f;
  margin: 0 0 8px;
}

.format-card p {
  font-size: 14px;
  color: #606266;
  line-height: 1.7;
  margin: 0 0 12px;
  flex: 1;
}

.format-freq {
  font-size: 12px;
  font-weight: 500;
  color: #2080f0;
  background: rgba(32, 128, 240, 0.06);
  padding: 3px 10px;
  border-radius: 6px;
  align-self: flex-start;
}

/* ── How-to ── */
.howto-section {
  background: #f5f7fa;
}

.howto-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  max-width: 860px;
  margin: 0 auto;
}

.howto-step {
  text-align: center;
  padding: 32px 20px;
}

.howto-step:hover {
  border-color: #2080f0;
  box-shadow: 0 8px 24px rgba(32, 128, 240, 0.08);
}

.ht-number {
  width: 44px;
  height: 44px;
  background: linear-gradient(135deg, #2080f0, #6366f1);
  color: #fff;
  font-size: 18px;
  font-weight: 700;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
}

.howto-step h3 {
  font-size: 16px;
  font-weight: 600;
  color: #1d1e1f;
  margin: 0 0 8px;
}

.howto-step p {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  margin: 0;
}

/* ── CTA ── */
.cta-card {
  background: linear-gradient(135deg, #fefce8, #ecfdf5, #e8f4ff);
  border-radius: 20px;
  text-align: center;
  padding: 64px 40px;
  border: 1px solid rgba(32, 128, 240, 0.1);
}

.cta-card h2 {
  font-size: 28px;
  font-weight: 700;
  color: #1d1e1f;
  margin: 0 0 12px;
}

.cta-card p {
  font-size: 16px;
  color: #606266;
  margin: 0 0 28px;
}

/* ── Responsive ── */
@media (max-width: 900px) {
  .format-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .activity-item {
    flex-direction: column;
  }

  .ai-left {
    padding: 10px 16px;
    border-right: none;
    border-bottom: 1px solid #f0f0f0;
    justify-content: flex-start;
  }

  .ai-icon {
    display: none;
  }

  .format-grid {
    grid-template-columns: 1fr;
  }

  .howto-grid {
    grid-template-columns: 1fr;
  }

  .ph-content h1 {
    font-size: 32px;
  }

  .cta-card {
    padding: 40px 24px;
  }

  .filter-btn {
    padding: 6px 16px;
    font-size: 13px;
  }

  .friend-card {
    flex-direction: column;
    text-align: center;
  }

  .fc-icon {
    width: 48px;
    height: 48px;
  }

  .fc-link {
    display: inline-block;
  }
}
</style>
