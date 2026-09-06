<script setup lang="ts">
const timeline = [
  {
    year: '2024 秋',
    title: '萌芽 · 初遇',
    content: '几位热爱编程的同学在学校技术社团活动中相识，发现大家都渴望一个更自由、更开放的技术交流空间。于是 Open_Lantu 的种子就此埋下。',
  },
  {
    year: '2025 春',
    title: '成长 · 破土',
    content: '成员从最初的 5 人扩展到 15+ 人。我们开始了每周技术分享会，有人讲 React 原理，有人分享逆向工程，还有人带大家写第一个 Rust 程序。',
  },
  {
    year: '2025 秋',
    title: '拓展 · 枝繁',
    content: '我们承接了校内多个信息化项目，与学生会、创新实验室等组织建立了合作。团队内部发起了第一个开源项目，成员在 GitHub 上相互 Code Review。',
  },
  {
    year: '2026',
    title: '展望 · 参天',
    content: '组织持续壮大，活跃成员突破 30 人。未来计划举办校园 Hackathon、邀请校友工程师分享、走出校园参与更广泛的开源社区。',
  },
]

const values = [
  {
    icon: 'MagicStick',
    title: '热爱分享',
    desc: '知识因分享而更有价值。每个人都有值得教给别人的东西，讲出来是最好的学习方式。',
  },
  {
    icon: 'UserFilled',
    title: '互相尊重',
    desc: '无论技术水平高低、来自什么专业，每个人都值得被倾听与尊重。没有傲慢，只有好奇。',
  },
  {
    icon: 'CircleCheckFilled',
    title: '脚踏实地',
    desc: '不画大饼，不说空话。动手写代码、做项目、解决问题，用实践检验一切。',
  },
  {
    icon: 'Link',
    title: '开放包容',
    desc: '欢迎所有热爱技术的同学。不问背景、不看成绩，只有对技术的一腔热情。',
  },
]

const achievements = [
  {
    icon: 'Trophy',
    title: 'Hackathon 三等奖',
    desc: '2025 秋季校园 Hackathon 中，团队项目"智能课表助手"荣获全校三等奖。',
    year: '2025',
  },
  {
    icon: 'FolderOpened',
    title: '3 个开源项目',
    desc: '团队在 GitHub 上维护了 3 个开源项目，累计获得 50+ Star，被多位校外开发者使用。',
    year: '2025-2026',
  },
  {
    icon: 'UserFilled',
    title: '成员突破 30 人',
    desc: '经过三个学期的积累，活跃成员从最初的 5 人扩展到 30+ 人，涵盖 6 个专业。',
    year: '2026',
  },
  {
    icon: 'Connection',
    title: '校企合作',
    desc: '与两家本地科技公司建立了合作，为成员争取到实习内推和技术交流的机会。',
    year: '2026',
  },
]

const faq = [
  {
    q: '需要什么基础才能加入？',
    a: '不需要任何基础。只要你热爱技术、愿意学习，我们就欢迎你。我们有学长学姐带路，也有完善的学习路线建议。',
  },
  {
    q: '活动时间怎么安排？',
    a: '技术分享会通常每两周一次（周末），项目协作灵活进行。不会占用太多课业时间，一切以学业为重。',
  },
  {
    q: '我不是计算机专业的可以吗？',
    a: '当然可以！我们的成员来自计科、软工、通信、数学、设计等各个专业。技术的世界不限于单一学科。',
  },
  {
    q: '加入后有什么收获？',
    a: '项目实战经验、团队协作能力、技术人脉、作品集、GitHub 绿点……当然还有一群志同道合的朋友。',
  },
]
</script>

<template>
  <div class="about">
    <!-- ════════════════ Page Hero ════════════════ -->
    <section class="page-hero">
      <div class="ph-bg">
        <div class="ph-shape s1"></div>
        <div class="ph-shape s2"></div>
      </div>
      <div class="ph-content">
        <span class="ph-badge">About</span>
        <h1>关于我们</h1>
        <p class="ph-desc">一群热爱技术的年轻人，想把写代码变成一件有趣的事。</p>
      </div>
    </section>

    <!-- ════════════════ 故事 ════════════════ -->
    <section class="section story reveal">
      <div class="section-inner">
        <div class="story-grid">
          <div class="story-text reveal reveal-delay-1">
            <span class="section-tag">Our Story</span>
            <h2>我们的故事</h2>
            <p>
              Open_Lantu 诞生于一次再普通不过的课后闲聊。几个朋友抱怨学校里缺少一个纯粹的技术交流圈子——不为了比赛加分，不为了综测排名，只是想有人一起讨论技术、一起写点有意思的东西。
            </p>
            <p>
              于是我们说干就干。从最开始在教室里围成一圈分享技术文章，到后来有了固定的分享会、有了第一个团队项目、有了来自不同专业的成员加入。Open_Lantu 从一个念头，慢慢长成了现在的样子。
            </p>
            <p class="story-quote">"我们相信，最好的学习方式是教别人，最快的成长路径是与人协作。"</p>
          </div>
          <div class="story-stats">
            <div class="story-stat-card card reveal reveal-delay-2">
              <div class="ssc-value">30+</div>
              <div class="ssc-label">活跃成员</div>
            </div>
            <div class="story-stat-card card">
              <div class="ssc-value">12+</div>
              <div class="ssc-label">落地项目</div>
            </div>
            <div class="story-stat-card card">
              <div class="ssc-value">20+</div>
              <div class="ssc-label">技术分享</div>
            </div>
            <div class="story-stat-card card">
              <div class="ssc-value">3</div>
              <div class="ssc-label">学期历程</div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ════════════════ 使命愿景 ════════════════ -->
    <section class="section section-alt mission reveal">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">Mission</span>
          <h2 class="section-title">使命 · 愿景 · 价值观</h2>
          <p class="section-desc">我们为什么存在，我们要去往哪里</p>
        </div>

        <div class="mission-grid">
          <div class="mission-card primary card reveal reveal-delay-1">
            <div class="mission-icon">
              <n-icon :size="28"><Flag /></n-icon>
            </div>
            <h3>使命</h3>
            <p>打造一个让每位成员都能找到方向、获得成长、留下作品的校园技术社区。</p>
          </div>
          <div class="mission-card success card reveal reveal-delay-2">
            <div class="mission-icon">
              <n-icon :size="28"><Sunrise /></n-icon>
            </div>
            <h3>愿景</h3>
            <p>成为校内最具影响力的学生技术组织，让"Open_Lantu"成为技术热情的代名词。</p>
          </div>
          <div class="mission-card warning card reveal reveal-delay-3">
            <div class="mission-icon">
              <n-icon :size="28"><Star /></n-icon>
            </div>
            <h3>价值观</h3>
            <p>分享 · 协作 · 实践 · 成长</p>
          </div>
        </div>
      </div>
    </section>

    <!-- ════════════════ 发展历程 ════════════════ -->
    <section class="section timeline-section reveal">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">Timeline</span>
          <h2 class="section-title">发展历程</h2>
          <p class="section-desc">每一步都走得不算快，但每一步都很踏实</p>
        </div>

        <div class="custom-timeline">
          <div
            v-for="(item, i) in timeline"
            :key="i"
            class="tl-item reveal"
            :class="`reveal-delay-${i + 1}`"
          >
            <div class="tl-dot"></div>
            <div class="tl-line" v-if="i < timeline.length - 1"></div>
            <div class="tl-year">{{ item.year }}</div>
            <div class="tl-card card">
              <h3>{{ item.title }}</h3>
              <p>{{ item.content }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ════════════════ 价值观 ════════════════ -->
    <section class="section section-alt values-section reveal">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">Values</span>
          <h2 class="section-title">我们的价值观</h2>
          <p class="section-desc">这四个词，是我们做每一件事的准则</p>
        </div>

        <div class="values-grid">
          <div
            v-for="(item, i) in values"
            :key="i"
            class="value-card card reveal"
            :class="`reveal-delay-${i + 1}`"
          >
            <div class="value-icon">
              <n-icon :size="24"><component :is="item.icon" /></n-icon>
            </div>
            <h3>{{ item.title }}</h3>
            <p>{{ item.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- ════════════════ 成就 ════════════════ -->
    <section class="section achievements-section reveal">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">Achievements</span>
          <h2 class="section-title">我们的成就</h2>
          <p class="section-desc">每一个里程碑，都是大家一起走出来的</p>
        </div>

        <div class="achievements-list">
          <div
            v-for="(item, i) in achievements"
            :key="i"
            class="achievement-card card reveal"
            :class="`reveal-delay-${i + 1}`"
          >
            <div class="ac-year">{{ item.year }}</div>
            <div class="ac-icon">
              <n-icon :size="22"><component :is="item.icon" /></n-icon>
            </div>
            <div class="ac-body">
              <h3>{{ item.title }}</h3>
              <p>{{ item.desc }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ════════════════ FAQ ════════════════ -->
    <section class="section section-alt faq-section reveal">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">FAQ</span>
          <h2 class="section-title">常见问题</h2>
          <p class="section-desc">可能你想知道的</p>
        </div>

        <div class="faq-list">
          <div
            v-for="(item, i) in faq"
            :key="i"
            class="faq-item card reveal"
            :class="`reveal-delay-${i + 1}`"
          >
            <div class="faq-q">
              <span class="faq-marker" style="background:#2080f0;color:#fff">Q</span>
              <span>{{ item.q }}</span>
            </div>
            <div class="faq-a">
              <span class="faq-marker" style="background:#f0f0f0;color:#909399">A</span>
              <span>{{ item.a }}</span>
            </div>
          </div>
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
  background: linear-gradient(160deg, #eef2ff 0%, #ecf5ff 50%, #f5f3ff 100%);
  z-index: 0;
}

.ph-shape {
  position: absolute;
  border-radius: 50%;
  opacity: 0.25;
}

.s1 {
  width: 350px;
  height: 350px;
  background: radial-gradient(circle, rgba(99, 102, 241, 0.15), transparent);
  top: -100px;
  left: -80px;
}

.s2 {
  width: 250px;
  height: 250px;
  background: radial-gradient(circle, rgba(32, 128, 240, 0.12), transparent);
  bottom: -60px;
  right: -40px;
}

.ph-badge {
  color: #6366f1;
  background: rgba(99, 102, 241, 0.1);
}

/* ════════════════════════════════════════
   Story
   ════════════════════════════════════════ */
.story-grid {
  display: grid;
  grid-template-columns: 1.3fr 1fr;
  gap: 48px;
  align-items: start;
}

.story-text .section-tag {
  margin-bottom: 12px;
}

.story-text h2 {
  font-size: 28px;
  font-weight: 700;
  color: #1d1e1f;
  margin: 0 0 20px;
}

.story-text p {
  font-size: 15px;
  color: #606266;
  line-height: 1.9;
  margin: 0 0 18px;
}

.story-quote {
  font-style: italic;
  color: #2080f0 !important;
  font-size: 15px !important;
  padding: 16px 20px;
  background: rgba(32, 128, 240, 0.06);
  border-radius: 10px;
  border-left: 3px solid #2080f0;
  margin-top: 8px !important;
}

.story-stats {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.story-stat-card {
  padding: 28px 20px;
  text-align: center;
}

.story-stat-card:hover {
  border-color: #2080f0;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.06);
}

.ssc-value {
  font-size: 32px;
  font-weight: 800;
  background: linear-gradient(135deg, #2080f0, #6366f1);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  line-height: 1;
  margin-bottom: 6px;
}

.ssc-label {
  font-size: 14px;
  color: #909399;
}

/* ════════════════════════════════════════
   Mission
   ════════════════════════════════════════ */
.mission-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.mission-card {
  padding: 36px 24px;
  text-align: center;
  border: none;
}

.mission-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.06);
}

.mission-icon {
  margin-bottom: 16px;
}

.mission-card.primary {
  background: linear-gradient(135deg, #ecf5ff, #e8f4ff);
}
.mission-card.primary .mission-icon { color: #2080f0; }
.mission-card.primary h3 { color: #2080f0; }

.mission-card.success {
  background: linear-gradient(135deg, #f0f9eb, #ecfdf5);
}
.mission-card.success .mission-icon { color: #36ad6a; }
.mission-card.success h3 { color: #36ad6a; }

.mission-card.warning {
  background: linear-gradient(135deg, #fdf6ec, #fffbeb);
}
.mission-card.warning .mission-icon { color: #f0a020; }
.mission-card.warning h3 { color: #f0a020; }

.mission-card h3 {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 10px;
}

.mission-card p {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  margin: 0;
}

/* ════════════════════════════════════════
   Custom Timeline
   ════════════════════════════════════════ */
.timeline-section {
  background: #f5f7fa;
}

.custom-timeline {
  max-width: 720px;
  margin: 0 auto;
  position: relative;
}

.tl-item {
  display: grid;
  grid-template-columns: 40px 1fr;
  column-gap: 24px;
  row-gap: 4px;
  position: relative;
  padding-bottom: 32px;
}

.tl-item:last-child {
  padding-bottom: 0;
}

.tl-dot {
  grid-row: 1 / 3;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: linear-gradient(135deg, #2080f0, #6366f1);
  border: 3px solid #fff;
  box-shadow: 0 0 0 2px #2080f0;
  z-index: 1;
  justify-self: center;
  margin-top: 6px;
}

.tl-line {
  position: absolute;
  left: 19px;
  top: 24px;
  width: 2px;
  height: calc(100% - 8px);
  background: #e0e4ea;
}

.tl-year {
  font-size: 13px;
  font-weight: 600;
  color: #2080f0;
  background: rgba(32, 128, 240, 0.08);
  padding: 2px 12px;
  border-radius: 6px;
  align-self: start;
  justify-self: start;
}

.tl-card {
  padding: 20px 24px;
  grid-column: 2;
}

.tl-card:hover {
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
  border-color: #2080f0;
}

.tl-card h3 {
  font-size: 16px;
  font-weight: 600;
  color: #1d1e1f;
  margin: 0 0 8px;
}

.tl-card p {
  font-size: 14px;
  color: #606266;
  line-height: 1.7;
  margin: 0;
}

/* ════════════════════════════════════════
   Values
   ════════════════════════════════════════ */
.values-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.value-card {
  text-align: center;
  padding: 36px 20px;
}

.value-card:hover {
  border-color: #2080f0;
  box-shadow: 0 8px 28px rgba(32, 128, 240, 0.1);
}

.value-icon {
  width: 52px;
  height: 52px;
  background: #ecf5ff;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #2080f0;
  margin: 0 auto 16px;
  transition: transform 0.3s;
}

.value-card:hover .value-icon {
  transform: scale(1.12) rotate(-4deg);
}

.value-card h3 {
  font-size: 16px;
  font-weight: 600;
  color: #1d1e1f;
  margin: 0 0 10px;
}

.value-card p {
  font-size: 13px;
  color: #909399;
  line-height: 1.7;
  margin: 0;
}

/* ════════════════════════════════════════
   Achievements
   ════════════════════════════════════════ */
.achievements-section {
  background: #f5f7fa;
}

.achievements-list {
  max-width: 720px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.achievement-card {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 24px;
}

.achievement-card:hover {
  border-color: #2080f0;
  box-shadow: 0 4px 16px rgba(32, 128, 240, 0.06);
  transform: translateX(4px);
}

.ac-year {
  flex-shrink: 0;
  font-size: 13px;
  font-weight: 600;
  color: #2080f0;
  background: rgba(32, 128, 240, 0.08);
  padding: 4px 12px;
  border-radius: 8px;
  min-width: 72px;
  text-align: center;
}

.ac-icon {
  flex-shrink: 0;
  width: 44px;
  height: 44px;
  background: #ecf5ff;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #2080f0;
}

.ac-body {
  flex: 1;
}

.ac-body h3 {
  font-size: 16px;
  font-weight: 600;
  color: #1d1e1f;
  margin: 0 0 4px;
}

.ac-body p {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  margin: 0;
}

/* ════════════════════════════════════════
   FAQ
   ════════════════════════════════════════ */
.faq-list {
  max-width: 720px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.faq-item {
  padding: 20px 24px;
}

.faq-item:hover {
  border-color: #e0e0e0;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04);
}

.faq-q {
  display: flex;
  gap: 12px;
  font-size: 15px;
  font-weight: 600;
  color: #1d1e1f;
  margin-bottom: 10px;
}

.faq-a {
  display: flex;
  gap: 12px;
  font-size: 14px;
  color: #606266;
  line-height: 1.7;
}

.faq-marker {
  flex-shrink: 0;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 700;
}

/* ════════════════════════════════════════
   Responsive
   ════════════════════════════════════════ */
@media (max-width: 900px) {
  .mission-grid {
    grid-template-columns: 1fr;
  }

  .values-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .story-grid {
    grid-template-columns: 1fr;
    gap: 32px;
  }

  .ph-content h1 {
    font-size: 32px;
  }
}

@media (max-width: 480px) {
  .values-grid {
    grid-template-columns: 1fr;
  }

  .story-stats {
    grid-template-columns: 1fr;
  }
}
</style>
