<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ref, onMounted } from 'vue'

const router = useRouter()

const features = [
  {
    icon: 'UserFilled',
    title: '互助学习',
    desc: '成员之间互相帮助，共同攻克技术难题。无论遇到什么问题，总有人愿意伸出援手。',
    color: '#2080f0',
    bg: '#ecf5ff',
  },
  {
    icon: 'Connection',
    title: '协作编程',
    desc: '通过结对编程和 Code Review 培养工程习惯，在真实的团队协作中快速成长。',
    color: '#36ad6a',
    bg: '#f0f9eb',
  },
  {
    icon: 'Lightning',
    title: '项目实战',
    desc: '从 idea 到上线，完整经历项目开发流程，积累作品集，为未来铺路。',
    color: '#f0a020',
    bg: '#fdf6ec',
  },
]

const activities = [
  {
    icon: 'Reading',
    title: '技术分享会',
    desc: '每两周一次轮流分享，从基础到前沿，每个人都可以是讲师。',
    tag: '双周',
  },
  {
    icon: 'Monitor',
    title: 'Code Jam',
    desc: '限时编程挑战赛，组队完成一个完整项目，体验从零到一的创造过程。',
    tag: '每月',
  },
  {
    icon: 'Files',
    title: '开源项目',
    desc: '一起维护和组织相关的开源项目，贡献代码，积累 GitHub 绿点。',
    tag: '长期',
  },
  {
    icon: 'ChatDotSquare',
    title: '圆桌讨论',
    desc: '围绕技术热点、学习路线、求职经验等话题自由交流，碰撞思维火花。',
    tag: '不定期',
  },
]

const stats = [
  { value: '30+', label: '活跃成员', icon: 'User' },
  { value: '12+', label: '落地项目', icon: 'FolderOpened' },
  { value: '20+', label: '技术分享', icon: 'Presentation' },
  { value: '5+', label: '合作组织', icon: 'Share' },
]

const announcements = [
  {
    icon: 'Bell',
    title: '夏季 Code Jam 报名开启',
    desc: '2026 年 7 月 1 日 · 48 小时极限编程挑战，组队报名现已开放，不限年级专业。',
    color: '#f0a020',
    date: '2026.06',
  },
  {
    icon: 'Reading',
    title: '技术分享会 Vol.12 预告',
    desc: '本期主题："从零搭建一个前端工程化脚手架"，由大三学长分享实战经验。',
    color: '#2080f0',
    date: '2026.06',
  },
  {
    icon: 'UserFilled',
    title: '春季招新圆满结束',
    desc: '本期共吸纳 12 位新成员，来自计科、软工、通信、设计 4 个专业。',
    color: '#36ad6a',
    date: '2026.05',
  },
]

const partners = [
  { name: '校学生会技术部', icon: 'School' },
  { name: '创新实验室', icon: 'Monitor' },
  { name: '开源社团', icon: 'Share' },
  { name: 'ACM 集训队', icon: 'Trophy' },
  { name: '设计协会', icon: 'MagicStick' },
  { name: '创业孵化器', icon: 'Lightning' },
]

const testimonials = [
  {
    quote: '在这里我从一个写不出完整页面的小白，成长到能独立负责一个项目的前端开发。最重要的是，我找到了愿意一起熬夜改 Bug 的伙伴。',
    author: '陈同学',
    role: '大二 · 计科',
    color: '#2080f0',
  },
  {
    quote: '加入 Open_Lantu 之前我几乎没写过实际项目。在 Code Jam 里被队友带着飞了一次，从此打开了新世界的大门。',
    author: '林同学',
    role: '大一 · 软工',
    color: '#36ad6a',
  },
  {
    quote: '作为一个非 CS 专业的学生，我一直担心自己跟不上。但这里的氛围让我知道：只要愿意学，永远有人愿意教。',
    author: '张同学',
    role: '大二 · 通信',
    color: '#f0a020',
  },
]

const showScrollHint = ref(true)

onMounted(() => {
  const handleScroll = () => {
    if (window.scrollY > 100) showScrollHint.value = false
  }
  window.addEventListener('scroll', handleScroll)
})
</script>

<template>
  <div class="home">
    <!-- ════════════════ Hero ════════════════ -->
    <section class="hero">
      <div class="hero-bg">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
      </div>

      <div class="hero-content">
        <div class="hero-badge">学生技术组织</div>
        <h1 class="hero-title">
          <span class="hero-greeting">欢迎加入</span>
          <span class="hero-brand">Open_Lantu</span>
        </h1>
        <p class="hero-slogan">学习 · 协作 · 成长</p>
        <p class="hero-desc">
          一个由学生自发组成的技术学习与协作社区。<br />
          我们一起写代码、做项目、分享知识，在互帮互助中共同进步。
        </p>
        <div class="hero-actions">
          <n-button type="primary" size="large" round @click="router.push('/about')">
            了解我们
            <n-icon class="btn-icon"><ArrowRight /></n-icon>
          </n-button>
          <n-button size="large" round quaternary @click="router.push('/join')">
            加入我们
          </n-button>
        </div>
      </div>

      <div v-show="showScrollHint" class="scroll-hint">
        <span class="scroll-text">向下滚动</span>
        <n-icon><ArrowDown /></n-icon>
      </div>
    </section>

    <!-- ════════════════ 特点 ════════════════ -->
    <section class="section features reveal">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">Why Us</span>
          <h2 class="section-title">我们的特点</h2>
          <p class="section-desc">在这里，每个人都是学习者，也是分享者</p>
        </div>

        <div class="features-grid">
          <div
            v-for="(item, i) in features"
            :key="i"
            class="feature-card card reveal"
            :class="`reveal-delay-${i + 1}`"
            :style="{ '--accent': item.color, '--accent-bg': item.bg }"
          >
            <div class="feature-icon">
              <n-icon :size="26"><component :is="item.icon" /></n-icon>
            </div>
            <h3>{{ item.title }}</h3>
            <p>{{ item.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- ════════════════ 活动 ════════════════ -->
    <section class="section section-alt activities reveal">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">Activities</span>
          <h2 class="section-title">我们的活动</h2>
          <p class="section-desc">除了写代码，我们还有很多有趣的事</p>
        </div>

        <div class="activities-grid">
          <div
            v-for="(item, i) in activities"
            :key="i"
            class="activity-card card reveal"
            :class="`reveal-delay-${i + 1}`"
          >
            <div class="activity-tag">{{ item.tag }}</div>
            <div class="activity-icon">
              <n-icon :size="22"><component :is="item.icon" /></n-icon>
            </div>
            <h3>{{ item.title }}</h3>
            <p>{{ item.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- ════════════════ 数据 ════════════════ -->
    <section class="section stats reveal">
      <div class="section-inner">
        <div class="stats-grid">
          <div
            v-for="(stat, i) in stats"
            :key="i"
            class="stat-item reveal"
            :class="`reveal-delay-${i + 1}`"
          >
            <n-icon :size="28"><component :is="stat.icon" /></n-icon>
            <div class="stat-value">{{ stat.value }}</div>
            <div class="stat-label">{{ stat.label }}</div>
          </div>
        </div>
      </div>
    </section>

    <!-- ════════════════ 加入流程 ════════════════ -->
    <section class="section join-section reveal">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">Join Us</span>
          <h2 class="section-title">加入我们</h2>
          <p class="section-desc">无论基础如何，只要热爱技术，这里就有你的位置</p>
        </div>

        <div class="steps">
          <div class="step card reveal reveal-delay-1">
            <div class="step-number">1</div>
            <div class="step-content">
              <h3>扫码进群</h3>
              <p>添加联系人微信，备注"加入 Open_Lantu"</p>
            </div>
          </div>
          <div class="step-arrow">
            <n-icon><ArrowRightBold /></n-icon>
          </div>
          <div class="step card reveal reveal-delay-2">
            <div class="step-number">2</div>
            <div class="step-content">
              <h3>参加活动</h3>
              <p>来参加一次技术分享或 Code Jam，感受氛围</p>
            </div>
          </div>
          <div class="step-arrow">
            <n-icon><ArrowRightBold /></n-icon>
          </div>
          <div class="step card reveal reveal-delay-3">
            <div class="step-number">3</div>
            <div class="step-content">
              <h3>成为成员</h3>
              <p>参与项目协作，找到你的技术方向，一起成长</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ════════════════ 最新动态 ════════════════ -->
    <section class="section section-alt announcements reveal">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">News</span>
          <h2 class="section-title">最新动态</h2>
          <p class="section-desc">组织的每一步成长，都值得被记录</p>
        </div>

        <div class="announcements-grid">
          <div
            v-for="(item, i) in announcements"
            :key="i"
            class="announcement-card card reveal"
            :class="`reveal-delay-${i + 1}`"
            :style="{ '--accent': item.color }"
          >
            <div class="ann-header">
              <div class="ann-icon" :style="{ background: `${item.color}14`, color: item.color }">
                <n-icon :size="20"><component :is="item.icon" /></n-icon>
              </div>
              <span class="ann-date">{{ item.date }}</span>
            </div>
            <h3>{{ item.title }}</h3>
            <p>{{ item.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- ════════════════ 成员说 ════════════════ -->
    <section class="section testimonials reveal">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">Testimonials</span>
          <h2 class="section-title">成员说</h2>
          <p class="section-desc">听听在 Open_Lantu 成长的他们怎么说</p>
        </div>

        <div class="testimonials-grid">
          <div
            v-for="(item, i) in testimonials"
            :key="i"
            class="testimonial-card card reveal"
            :class="`reveal-delay-${i + 1}`"
            :style="{ '--accent': item.color }"
          >
            <div class="t-quote">"</div>
            <p class="t-text">{{ item.quote }}</p>
            <div class="t-author">
              <div class="t-avatar" :style="{ background: `${item.color}18`, color: item.color }">
                {{ item.author[0] }}
              </div>
              <div>
                <div class="t-name">{{ item.author }}</div>
                <div class="t-role">{{ item.role }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ════════════════ 合作组织 ════════════════ -->
    <section class="section partners-section section-alt reveal">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">Partners</span>
          <h2 class="section-title">合作组织</h2>
          <p class="section-desc">与我们一起，共建校园技术生态</p>
        </div>

        <div class="partners-grid">
          <div
            v-for="(item, i) in partners"
            :key="i"
            class="partner-card card reveal"
            :class="`reveal-delay-${Math.min(i + 1, 5)}`"
          >
            <n-icon :size="22"><component :is="item.icon" /></n-icon>
            <span>{{ item.name }}</span>
          </div>
        </div>
      </div>
    </section>

    <!-- ════════════════ CTA ════════════════ -->
    <section class="section cta reveal">
      <div class="section-inner">
        <div class="cta-card reveal reveal-delay-1">
          <h2>准备好了吗？</h2>
          <p>不需要很厉害才能开始，但你需要开始才能变得很厉害。</p>
          <n-button type="primary" size="large" round @click="router.push('/about')">
            了解更多
          </n-button>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
/* ════════════════════════════════════════
   Hero
   ════════════════════════════════════════ */
.hero {
  position: relative;
  overflow: hidden;
  min-height: 92vh;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 80px 24px 60px;
}

.hero-bg {
  position: absolute;
  inset: 0;
  background: linear-gradient(160deg, #e8f4ff 0%, #f0f0ff 40%, #f5f3ff 70%, #ecfdf5 100%);
  z-index: 0;
}

.shape {
  position: absolute;
  border-radius: 50%;
  opacity: 0.3;
  animation: float 8s ease-in-out infinite;
}

.shape-1 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(32,128,240,0.15), transparent);
  top: -100px;
  right: -80px;
  animation-delay: 0s;
}

.shape-2 {
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, rgba(54,173,106,0.12), transparent);
  bottom: -50px;
  left: -60px;
  animation-delay: -3s;
}

.shape-3 {
  width: 200px;
  height: 200px;
  background: radial-gradient(circle, rgba(240,160,32,0.12), transparent);
  top: 40%;
  left: 10%;
  animation-delay: -6s;
}

@keyframes float {
  0%, 100% { transform: translateY(0) scale(1); }
  50% { transform: translateY(-30px) scale(1.05); }
}

.hero-content {
  position: relative;
  z-index: 1;
  max-width: 720px;
}

.hero-badge {
  display: inline-block;
  background: rgba(32, 128, 240, 0.12);
  color: #2080f0;
  font-size: 13px;
  font-weight: 500;
  padding: 4px 16px;
  border-radius: 20px;
  margin-bottom: 24px;
  letter-spacing: 1px;
}

.hero-title {
  margin: 0 0 8px;
}

.hero-greeting {
  display: block;
  font-size: 24px;
  font-weight: 400;
  color: #606266;
  margin-bottom: 4px;
}

.hero-brand {
  font-size: 56px;
  font-weight: 800;
  background: linear-gradient(135deg, #2080f0, #6366f1, #8b5cf6);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  letter-spacing: -1px;
}

.hero-slogan {
  font-size: 20px;
  font-weight: 500;
  color: #606266;
  margin: 0 0 16px;
  letter-spacing: 8px;
}

.hero-desc {
  font-size: 16px;
  color: #909399;
  line-height: 1.8;
  margin: 0 0 36px;
}

.hero-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
}

.btn-icon {
  margin-left: 4px;
}

.scroll-hint {
  position: absolute;
  bottom: 28px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  color: #c0c4cc;
  font-size: 12px;
  animation: bounce-down 2s ease-in-out infinite;
  cursor: default;
}

@keyframes bounce-down {
  0%, 100% { transform: translateX(-50%) translateY(0); }
  50% { transform: translateX(-50%) translateY(8px); }
}

/* ════════════════════════════════════════
   Features
   ════════════════════════════════════════ */
.features-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.feature-card {
  padding: 40px 28px;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.feature-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: var(--accent);
  transform: scaleX(0);
  transition: transform 0.35s;
}

.feature-card:hover::before {
  transform: scaleX(1);
}

.feature-card:hover {
  border-color: var(--accent);
  box-shadow: 0 16px 40px rgba(0, 0, 0, 0.07);
}

.feature-icon {
  width: 56px;
  height: 56px;
  background: var(--accent-bg);
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--accent);
  margin: 0 auto 20px;
  transition: transform 0.35s;
}

.feature-card:hover .feature-icon {
  transform: scale(1.1) rotate(-4deg);
}

.feature-card h3 {
  font-size: 18px;
  font-weight: 600;
  color: #1d1e1f;
  margin: 0 0 10px;
}

.feature-card p {
  font-size: 14px;
  color: #909399;
  line-height: 1.7;
  margin: 0;
}

/* ════════════════════════════════════════
   Activities
   ════════════════════════════════════════ */
.activities-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.activity-card {
  padding: 32px 20px;
  text-align: center;
  position: relative;
}

.activity-card:hover {
  border-color: #2080f0;
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.06);
}

.activity-tag {
  position: absolute;
  top: 12px;
  right: 12px;
  font-size: 11px;
  color: #2080f0;
  background: rgba(32, 128, 240, 0.08);
  padding: 2px 10px;
  border-radius: 8px;
  font-weight: 500;
}

.activity-icon {
  width: 48px;
  height: 48px;
  background: #ecf5ff;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #2080f0;
  margin: 0 auto 16px;
}

.activity-card h3 {
  font-size: 16px;
  font-weight: 600;
  color: #1d1e1f;
  margin: 0 0 8px;
}

.activity-card p {
  font-size: 13px;
  color: #909399;
  line-height: 1.6;
  margin: 0;
}

/* ════════════════════════════════════════
   Stats
   ════════════════════════════════════════ */
.stats {
  background: linear-gradient(135deg, #2080f0 0%, #6366f1 100%) !important;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 32px;
}

.stat-item {
  text-align: center;
  color: #fff;
}

.stat-item .n-icon {
  margin-bottom: 12px;
  opacity: 0.85;
}

.stat-value {
  font-size: 40px;
  font-weight: 800;
  line-height: 1;
  margin-bottom: 6px;
}

.stat-label {
  font-size: 15px;
  opacity: 0.8;
}

/* ════════════════════════════════════════
   Join Steps
   ════════════════════════════════════════ */
.steps {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0;
  max-width: 860px;
  margin: 0 auto;
}

.step {
  flex: 1;
  text-align: center;
  padding: 32px 20px;
}

.step:hover {
  border-color: #2080f0;
  box-shadow: 0 8px 24px rgba(32, 128, 240, 0.1);
}

.step-number {
  width: 40px;
  height: 40px;
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

.step-content h3 {
  font-size: 16px;
  font-weight: 600;
  color: #1d1e1f;
  margin: 0 0 6px;
}

.step-content p {
  font-size: 13px;
  color: #909399;
  margin: 0;
  line-height: 1.5;
}

.step-arrow {
  color: #dcdfe6;
  font-size: 20px;
  flex-shrink: 0;
  margin: 0 12px;
}

/* ════════════════════════════════════════
   Announcements
   ════════════════════════════════════════ */
.announcements-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.announcement-card {
  padding: 28px 24px;
}

.announcement-card:hover {
  border-color: var(--accent);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.06);
}

.ann-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 14px;
}

.ann-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.ann-date {
  font-size: 12px;
  color: #c0c4cc;
}

.announcement-card h3 {
  font-size: 16px;
  font-weight: 600;
  color: #1d1e1f;
  margin: 0 0 8px;
}

.announcement-card p {
  font-size: 14px;
  color: #909399;
  line-height: 1.7;
  margin: 0;
}

/* ════════════════════════════════════════
   Testimonials
   ════════════════════════════════════════ */
.testimonials-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.testimonial-card {
  padding: 32px 24px;
  display: flex;
  flex-direction: column;
}

.testimonial-card:hover {
  border-color: var(--accent);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.06);
}

.t-quote {
  font-size: 48px;
  line-height: 1;
  color: var(--accent);
  opacity: 0.3;
  margin-bottom: -8px;
}

.t-text {
  font-size: 14px;
  color: #606266;
  line-height: 1.8;
  margin: 0 0 20px;
  flex: 1;
}

.t-author {
  display: flex;
  align-items: center;
  gap: 10px;
}

.t-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 600;
  flex-shrink: 0;
}

.t-name {
  font-size: 14px;
  font-weight: 600;
  color: #1d1e1f;
}

.t-role {
  font-size: 12px;
  color: #c0c4cc;
}

/* ════════════════════════════════════════
   Partners
   ════════════════════════════════════════ */
.partners-grid {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 16px;
}

.partner-card {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 16px 24px;
  font-size: 14px;
  font-weight: 500;
  color: #606266;
  cursor: default;
}

.partner-card:hover {
  border-color: #2080f0;
  color: #2080f0;
  box-shadow: 0 4px 16px rgba(32, 128, 240, 0.08);
}

.partner-card .n-icon {
  color: #2080f0;
}

/* ════════════════════════════════════════
   Responsive
   ════════════════════════════════════════ */
@media (max-width: 900px) {
  .activities-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .announcements-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .testimonials-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .steps {
    flex-direction: column;
    gap: 16px;
  }

  .step-arrow {
    transform: rotate(90deg);
    margin: 0;
  }

  .step {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .hero-brand {
    font-size: 38px;
  }
  .hero-greeting {
    font-size: 20px;
  }
  .hero-slogan {
    font-size: 17px;
    letter-spacing: 6px;
  }

  .features-grid {
    grid-template-columns: 1fr;
  }

  .announcements-grid {
    grid-template-columns: 1fr;
  }

  .testimonials-grid {
    grid-template-columns: 1fr;
  }

  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 24px;
  }

  .partners-grid {
    gap: 12px;
  }

  .partner-card {
    padding: 12px 18px;
    font-size: 13px;
  }
}

@media (max-width: 480px) {
  .activities-grid {
    grid-template-columns: 1fr;
  }
}
</style>
