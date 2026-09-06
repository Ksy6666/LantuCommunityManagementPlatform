<script setup lang="ts">
interface Project {
  name: string
  desc: string
  techs: string[]
  status: 'active' | 'maintenance' | 'archived'
  github?: string
  icon: string
  color: string
  bg: string
}

const projects: Project[] = [
  {
    name: 'Lantu Website',
    desc: '组织官网项目，基于 Vue 3 + TypeScript 构建，展示组织信息、活动和项目。完全开源，欢迎贡献。',
    techs: ['Vue 3', 'TypeScript', 'Vite', 'Element Plus'],
    status: 'active',
    icon: 'Monitor',
    color: '#2080f0',
    bg: '#ecf5ff',
  },
  {
    name: 'Lantu Bot',
    desc: '团队内部效率工具机器人，支持活动通知、签到统计、技术文章推荐等功能。',
    techs: ['Python', 'NoneBot', 'SQLite'],
    status: 'active',
    icon: 'Robot',
    color: '#36ad6a',
    bg: '#f0f9eb',
  },
  {
    name: 'OJ 题目助手',
    desc: '面向算法初学者的在线判题辅助工具，提供题目分类、题解聚合、学习路线推荐。',
    techs: ['React', 'Node.js', 'MongoDB'],
    status: 'active',
    icon: 'DataBoard',
    color: '#f0a020',
    bg: '#fdf6ec',
  },
  {
    name: '校园课程表',
    desc: '跨平台课表 App，支持课程导入、自习室查询、考试倒计时等校园常用功能。',
    techs: ['Flutter', 'Dart', 'Firebase'],
    status: 'maintenance',
    icon: 'Calendar',
    color: '#6366f1',
    bg: '#eef2ff',
  },
  {
    name: '内推信息聚合',
    desc: '收集整理校友在各公司的内推信息，帮助学弟学妹找到实习和全职机会。',
    techs: ['Vue 3', 'Express', 'PostgreSQL'],
    status: 'active',
    icon: 'Connection',
    color: '#f56c6c',
    bg: '#fef0f0',
  },
  {
    name: '学习资源导航',
    desc: '精选编程学习资源，按语言/方向分类，附带学长学姐的路线图和学习建议。',
    techs: ['HTML', 'CSS', 'JavaScript'],
    status: 'archived',
    icon: 'Reading',
    color: '#909399',
    bg: '#f4f4f5',
  },
]

function statusLabel(s: string) {
  const map: Record<string, string> = {
    active: '进行中',
    maintenance: '维护中',
    archived: '已归档',
  }
  return map[s] ?? s
}

function statusClass(s: string) {
  const map: Record<string, string> = {
    active: 's-active',
    maintenance: 's-maintenance',
    archived: 's-archived',
  }
  return map[s] ?? ''
}

function openLink(url: string) {
  window.open(url, '_blank')
}
</script>

<template>
  <div class="projects-page">
    <!-- ════════════════ Hero ════════════════ -->
    <section class="page-hero">
      <div class="ph-bg">
        <div class="ph-shape s1"></div>
        <div class="ph-shape s2"></div>
      </div>
      <div class="ph-content">
        <span class="ph-badge">Projects</span>
        <h1>开源项目</h1>
        <p class="ph-desc">我们做过的项目，每一行代码都是成长的见证</p>
      </div>
    </section>

    <!-- ════════════════ 介绍 ════════════════ -->
    <section class="section section-alt intro-section reveal">
      <div class="section-inner">
        <div class="intro-box">
          <n-icon :size="32" color="#1d1e1f"><Github /></n-icon>
          <h2>开源 · 协作 · 共赢</h2>
          <p>
            所有项目均开源在 GitHub 和 Gitee 上，欢迎 Star、Fork 和 PR。
            无论你是想学习项目架构、参与实际开发，还是寻找合作机会，这里都向你敞开。
          </p>
          <p style="margin-top: 12px; font-size: 14px; color: #909399;">
            Gitee 组织：<a href="https://gitee.com/open_lanTuDream" target="_blank" rel="noopener noreferrer" style="color: #2080f0; text-decoration: none;">gitee.com/open_lanTuDream</a>
          </p>
        </div>
      </div>
    </section>

    <!-- ════════════════ 项目列表 ════════════════ -->
    <section class="section project-section reveal">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">Projects</span>
          <h2 class="section-title">项目一览</h2>
          <p class="section-desc">每个项目都是一次成长的机会</p>
        </div>

        <div class="project-grid">
          <div
            v-for="(item, i) in projects"
            :key="i"
            class="project-card card reveal"
            :class="`reveal-delay-${Math.min(i + 1, 5)}`"
            :style="{ '--accent': item.color, '--accent-bg': item.bg }"
          >
            <div class="pc-top">
              <div class="pc-icon">
                <n-icon :size="24"><component :is="item.icon" /></n-icon>
              </div>
              <span class="pc-status" :class="statusClass(item.status)">{{ statusLabel(item.status) }}</span>
            </div>

            <h3>{{ item.name }}</h3>
            <p>{{ item.desc }}</p>

            <div class="pc-techs">
              <span v-for="tech in item.techs" :key="tech" class="tech-tag">{{ tech }}</span>
            </div>

            <div class="pc-actions">
              <n-button
                v-if="item.github"
                size="small"
                round
                quaternary
                @click="openLink(item.github!)"
              >
                <template #icon>
                  <n-icon><Github /></n-icon>
                </template>
                查看源码
              </n-button>
              <span v-else class="pc-soon">即将开源</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ════════════════ 贡献指南 ════════════════ -->
    <section class="section section-alt contribute-section reveal">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">Contribute</span>
          <h2 class="section-title">如何贡献代码</h2>
          <p class="section-desc">参与开源比你想象的简单</p>
        </div>

        <div class="contribute-steps">
          <div class="cont-step card">
            <div class="cs-num">1</div>
            <div class="cs-body">
              <h3>找到感兴趣的项目</h3>
              <p>浏览上面的项目列表，看看哪个项目的技术栈或功能方向让你感兴趣。</p>
            </div>
          </div>
          <div class="cont-step card">
            <div class="cs-num">2</div>
            <div class="cs-body">
              <h3>阅读贡献指南</h3>
              <p>每个项目都有 CONTRIBUTING.md，说明如何设置开发环境、提交 PR 的规范等。</p>
            </div>
          </div>
          <div class="cont-step card">
            <div class="cs-num">3</div>
            <div class="cs-body">
              <h3>从 Issue 开始</h3>
              <p>找带有 "good first issue" 标签的 Issue 入手，在评论区告知你想认领，我们会协助你。</p>
            </div>
          </div>
          <div class="cont-step card">
            <div class="cs-num">4</div>
            <div class="cs-body">
              <h3>提交 PR</h3>
              <p>Fork 项目 → 创建分支 → 编写代码 → 提交 PR。我们会在 Code Review 中提供详细反馈。</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ════════════════ CTA ════════════════ -->
    <section class="section cta-section reveal">
      <div class="section-inner">
        <div class="cta-card">
          <h2>想贡献代码？</h2>
          <p>所有项目都在 GitHub 上开放 Issue 和 PR，欢迎你的第一行贡献。</p>
          <n-button type="primary" size="large" round>
            <template #icon>
              <n-icon><Github /></n-icon>
            </template>
            前往 GitHub
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
  background: linear-gradient(160deg, #e8f4ff 0%, #f0f0ff 30%, #f5f3ff 70%, #fce4ec 100%);
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
  background: radial-gradient(circle, rgba(99, 102, 241, 0.15), transparent);
  top: -100px;
  left: -80px;
}

.s2 {
  width: 250px;
  height: 250px;
  background: radial-gradient(circle, rgba(245, 108, 108, 0.1), transparent);
  bottom: -60px;
  right: -40px;
}

.ph-badge {
  color: #6366f1;
  background: rgba(99, 102, 241, 0.1);
}

/* ── Intro ── */
.intro-box {
  text-align: center;
  max-width: 640px;
  margin: 0 auto;
  padding: 32px 24px;
}

.intro-box .n-icon {
  margin-bottom: 16px;
}

.intro-box h2 {
  font-size: 24px;
  font-weight: 700;
  color: #1d1e1f;
  margin: 0 0 12px;
}

.intro-box p {
  font-size: 15px;
  color: #606266;
  line-height: 1.8;
  margin: 0;
}

/* ── Project Grid ── */
.project-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.project-card {
  padding: 28px 24px;
  display: flex;
  flex-direction: column;
}

.project-card:hover {
  border-color: var(--accent);
  box-shadow: 0 16px 40px rgba(0, 0, 0, 0.07);
}

.pc-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.pc-icon {
  width: 48px;
  height: 48px;
  background: var(--accent-bg);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--accent);
}

.pc-status {
  font-size: 11px;
  font-weight: 600;
  padding: 2px 10px;
  border-radius: 6px;
}

.s-active {
  background: rgba(54, 173, 106, 0.1);
  color: #36ad6a;
}

.s-maintenance {
  background: rgba(240, 160, 32, 0.1);
  color: #f0a020;
}

.s-archived {
  background: #f4f4f5;
  color: #909399;
}

.project-card h3 {
  font-size: 17px;
  font-weight: 600;
  color: #1d1e1f;
  margin: 0 0 8px;
}

.project-card > p {
  font-size: 14px;
  color: #606266;
  line-height: 1.7;
  margin: 0 0 16px;
  flex: 1;
}

.pc-techs {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 16px;
}

.tech-tag {
  font-size: 11px;
  font-weight: 500;
  color: #2080f0;
  background: rgba(32, 128, 240, 0.06);
  padding: 2px 10px;
  border-radius: 6px;
  border: 1px solid rgba(32, 128, 240, 0.12);
}

.pc-actions {
  margin-top: auto;
}

.pc-soon {
  font-size: 13px;
  color: #c0c4cc;
  font-style: italic;
}

/* ── Contribute ── */
.contribute-steps {
  max-width: 720px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.cont-step {
  display: flex;
  align-items: flex-start;
  gap: 20px;
  padding: 24px;
}

.cont-step:hover {
  border-color: #2080f0;
  box-shadow: 0 4px 16px rgba(32, 128, 240, 0.06);
  transform: translateX(4px);
}

.cs-num {
  flex-shrink: 0;
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #2080f0, #6366f1);
  color: #fff;
  font-size: 16px;
  font-weight: 700;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.cs-body {
  flex: 1;
}

.cs-body h3 {
  font-size: 16px;
  font-weight: 600;
  color: #1d1e1f;
  margin: 0 0 4px;
}

.cs-body p {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  margin: 0;
}

/* ── CTA ── */
.cta-card {
  background: linear-gradient(135deg, #e8f4ff, #f0f0ff, #f5f3ff);
  border-radius: 20px;
  text-align: center;
  padding: 64px 40px;
  border: 1px solid rgba(99, 102, 241, 0.1);
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
  .project-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .project-grid {
    grid-template-columns: 1fr;
  }

  .ph-content h1 {
    font-size: 32px;
  }

  .cta-card {
    padding: 40px 24px;
  }
}
</style>
