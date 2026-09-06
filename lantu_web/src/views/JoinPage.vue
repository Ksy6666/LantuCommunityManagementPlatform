<script setup lang="ts">
import { ref } from 'vue'
import { preregisterApi } from '../api'
import { useMessage } from 'naive-ui'

const message = useMessage()

const benefits = [
  {
    icon: 'UserFilled',
    title: '技术人脉',
    desc: '结识来自各个专业的优秀同学，在交流中拓展视野，找到志同道合的伙伴。',
  },
  {
    icon: 'FolderOpened',
    title: '项目经验',
    desc: '参与真实项目开发，从需求分析到上线部署，积累完整的工程实践经验。',
  },
  {
    icon: 'TrendCharts',
    title: '成长路径',
    desc: '有方向的学习路线、定期的技术分享、Code Review，帮你避开自学常见的弯路。',
  },
  {
    icon: 'Trophy',
    title: '竞赛机会',
    desc: '组队参加 Hackathon 和技术竞赛，在实战中锤炼能力，为简历增色。',
  },
]

const steps = [
  {
    title: '扫码添加联系人',
    desc: '添加下方微信，备注"加入 Open_Lantu"，我们会拉你进群。',
    detail: '微信号：Open_Lantu_Admin',
  },
  {
    title: '参加一次活动',
    desc: '来参加技术分享会或 Code Jam，感受我们的氛围和风格。',
    detail: '活动信息会提前在群里通知，选择你感兴趣的活动参加即可。',
  },
  {
    title: '找到你的方向',
    desc: '参与项目或活动，找到你感兴趣的技术方向，开始协作之旅。',
    detail: '不用担心基础——我们会帮你匹配合适的学习路线和 mentor。',
  },
]

const testimonials = [
  {
    quote: '加入 Open_Lantu 是我大学里做的最正确的决定之一。从一个连 Git 都不会用的萌新，到现在能独立负责模块开发，这里的每个人都给了我巨大的帮助。',
    author: '王同学',
    role: '大二 · 计科',
    color: '#2080f0',
  },
  {
    quote: '当初抱着试试看的心态加了群，结果一发不可收拾。在这里认识了超多厉害的人，每次分享会都能学到新东西。',
    author: '李同学',
    role: '大三 · 软工',
    color: '#36ad6a',
  },
  {
    quote: '作为一个设计专业的同学，我在这里找到了技术与设计的结合点。帮团队做 UI 设计的同时，也学会了写代码。',
    author: '赵同学',
    role: '大三 · 设计',
    color: '#f0a020',
  },
]

const faq = [
  {
    q: '加入需要面试吗？',
    a: '不需要正式面试。我们更看重兴趣和态度，而非当前的技术水平。参加几次活动后，如果双方都觉得合适就可以正式加入。',
  },
  {
    q: '有年级或专业限制吗？',
    a: '完全没有。从大一新生到研究生，从计算机到文科专业，我们都欢迎。技术的世界不设限。',
  },
  {
    q: '需要投入多少时间？',
    a: '完全看你自己。我们鼓励量力而行，学业永远是第一位的。偶尔参加分享会、或者深度参与项目——由你选择。',
  },
  {
    q: '女生可以加入吗？',
    a: '当然可以！我们有不少女性成员和贡献者，我们致力于营造一个包容、友善的社区环境。',
  },
  {
    q: '零基础能跟上吗？',
    a: '完全可以。我们有专门的新手引导计划和学长学姐一对一答疑，很多成员都是从零开始在组织中成长起来的。',
  },
  {
    q: '有线下活动场地吗？',
    a: '有的。我们在校内创新实验室 302 有固定活动场地，配有白板、投影和开发板等设备。',
  },
]

const form = ref({
  name: '',
  major: '',
  grade: null as string | null,
  intro: '',
})

const gradeOptions = [
  { label: '大一', value: '大一' },
  { label: '大二', value: '大二' },
  { label: '大三', value: '大三' },
  { label: '大四', value: '大四' },
  { label: '研究生', value: '研究生' },
]

const submitting = ref(false)

async function handleSubmit() {
  if (!form.value.name) {
    message.warning('请填写姓名')
    return
  }
  if (!form.value.major) {
    message.warning('请填写专业')
    return
  }
  if (!form.value.grade) {
    message.warning('请选择年级')
    return
  }
  submitting.value = true
  try {
    const res = await preregisterApi({
      name: form.value.name,
      major: form.value.major,
      grade: form.value.grade,
      intro: form.value.intro || undefined,
    })
    if (res.data.code === 200) {
      message.success('预报名成功！我们会尽快联系你 📮')
      // 重置表单
      form.value = { name: '', major: '', grade: null, intro: '' }
    }
  } catch (err: any) {
    const msg = err?.response?.data?.message || '提交失败，请稍后重试'
    message.error(msg)
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <div class="join-page">
    <!-- ════════════════ Hero ════════════════ -->
    <section class="page-hero">
      <div class="ph-bg">
        <div class="ph-shape s1"></div>
        <div class="ph-shape s2"></div>
      </div>
      <div class="ph-content">
        <span class="ph-badge">Join Us</span>
        <h1>加入我们</h1>
        <p class="ph-desc">不需要很厉害才能开始，但你需要开始才能变得很厉害。</p>
      </div>
    </section>

    <!-- ════════════════ 为什么加入 ════════════════ -->
    <section class="section benefits-section reveal">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">Why Join</span>
          <h2 class="section-title">为什么加入 Open_Lantu？</h2>
          <p class="section-desc">这里不只是一个技术组织，更是一个成长的社区</p>
        </div>

        <div class="benefits-grid">
          <div
            v-for="(item, i) in benefits"
            :key="i"
            class="benefit-card card reveal"
            :class="`reveal-delay-${i + 1}`"
          >
            <div class="benefit-icon">
              <n-icon :size="24"><component :is="item.icon" /></n-icon>
            </div>
            <h3>{{ item.title }}</h3>
            <p>{{ item.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- ════════════════ 加入流程 ════════════════ -->
    <section class="section section-alt process-section reveal">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">Process</span>
          <h2 class="section-title">加入流程</h2>
          <p class="section-desc">三步成为 Open_Lantu 的一员</p>
        </div>

        <div class="process-list">
          <div
            v-for="(step, i) in steps"
            :key="i"
            class="process-step card reveal"
            :class="`reveal-delay-${i + 1}`"
          >
            <div class="ps-number">{{ i + 1 }}</div>
            <div class="ps-body">
              <h3>{{ step.title }}</h3>
              <p class="ps-desc">{{ step.desc }}</p>
              <p class="ps-detail">{{ step.detail }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- ════════════════ 成员心声 ════════════════ -->
    <section class="section testimonials-section reveal">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">Testimonials</span>
          <h2 class="section-title">成员心声</h2>
          <p class="section-desc">听听已经在组织中成长的他们怎么说</p>
        </div>

        <div class="testimonials-grid">
          <div
            v-for="(item, i) in testimonials"
            :key="i"
            class="testimonial-card card reveal"
            :class="`reveal-delay-${i + 1}`"
            :style="{ '--accent': item.color }"
          >
            <p class="t-text">{{ item.quote }}</p>
            <div class="t-author">
              <div class="t-avatar" :style="{ background: `${item.color}14`, color: item.color }">
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

    <!-- ════════════════ 联系方式 ════════════════ -->
    <section class="section section-alt contact-section reveal">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">Contact</span>
          <h2 class="section-title">联系我们</h2>
          <p class="section-desc">任何问题，随时联系我们</p>
        </div>

        <div class="contact-grid">
          <div class="contact-card card">
            <n-icon :size="28" color="#2080f0"><Message /></n-icon>
            <h3>邮箱</h3>
            <a href="mailto:Open_LanTu@outlook.com" class="contact-link">Open_LanTu@outlook.com</a>
          </div>
          <div class="contact-card card">
            <n-icon :size="28" color="#2080f0"><Location /></n-icon>
            <h3>地点</h3>
            <p>校内创新实验室 302</p>
          </div>
          <div class="contact-card card">
            <n-icon :size="28" color="#2080f0"><ChatLineSquare /></n-icon>
            <h3>微信</h3>
            <p>Open_Lantu_Admin</p>
            <p class="contact-note">添加时备注"加入 Open_Lantu"</p>
          </div>
          <div class="contact-card card">
            <n-icon :size="28" color="#2080f0"><Link /></n-icon>
            <h3>Gitee</h3>
            <a href="https://gitee.com/open_lanTuDream" target="_blank" rel="noopener noreferrer" class="contact-link">gitee.com/open_lanTuDream</a>
            <p class="contact-note">组织开源项目</p>
          </div>
        </div>
      </div>
    </section>

    <!-- ════════════════ 预报名表单 ════════════════ -->
    <section class="section form-section reveal">
      <div class="section-inner">
        <div class="section-header">
          <span class="section-tag">Pre-register</span>
          <h2 class="section-title">预报名</h2>
          <p class="section-desc">填写以下信息，我们会主动联系你</p>
        </div>

        <div class="form-wrap">
          <n-form :model="form" class="join-form">
            <n-form-item>
              <n-input
                v-model:value="form.name"
                placeholder="你的姓名"
                size="large"
                clearable
              />
            </n-form-item>
            <n-form-item>
              <n-input
                v-model:value="form.major"
                placeholder="专业"
                size="large"
                clearable
              />
            </n-form-item>
            <n-form-item>
              <n-select
                v-model:value="form.grade"
                placeholder="年级"
                size="large"
                :options="gradeOptions"
                clearable
              />
            </n-form-item>
            <n-form-item>
              <n-input
                v-model:value="form.intro"
                placeholder="简短自我介绍（技术栈/兴趣方向/想做的事）"
                type="textarea"
                :rows="3"
                size="large"
              />
            </n-form-item>
            <n-form-item>
          <n-button
            type="primary"
            size="large"
            round
            block
            :loading="submitting"
            @click="handleSubmit"
          >
            提交预报名
          </n-button>
            </n-form-item>
          </n-form>
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
  background: linear-gradient(160deg, #ecfdf5 0%, #e8f4ff 30%, #f0f0ff 70%, #fdf6ec 100%);
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
  background: radial-gradient(circle, rgba(54, 173, 106, 0.12), transparent);
  top: -100px;
  right: -80px;
}

.s2 {
  width: 250px;
  height: 250px;
  background: radial-gradient(circle, rgba(32, 128, 240, 0.1), transparent);
  bottom: -60px;
  left: -40px;
}

.ph-badge {
  color: #36ad6a;
  background: rgba(54, 173, 106, 0.1);
}

/* ── Benefits ── */
.benefits-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.benefit-card {
  text-align: center;
  padding: 36px 20px;
}

.benefit-card:hover {
  border-color: #2080f0;
  box-shadow: 0 8px 28px rgba(32, 128, 240, 0.1);
}

.benefit-icon {
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

.benefit-card:hover .benefit-icon {
  transform: scale(1.12) rotate(-4deg);
}

.benefit-card h3 {
  font-size: 16px;
  font-weight: 600;
  color: #1d1e1f;
  margin: 0 0 10px;
}

.benefit-card p {
  font-size: 13px;
  color: #909399;
  line-height: 1.7;
  margin: 0;
}

/* ── Process ── */
.process-list {
  max-width: 720px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.process-step {
  display: flex;
  gap: 20px;
  padding: 24px;
}

.process-step:hover {
  border-color: #2080f0;
  box-shadow: 0 4px 16px rgba(32, 128, 240, 0.06);
}

.ps-number {
  flex-shrink: 0;
  width: 44px;
  height: 44px;
  background: linear-gradient(135deg, #2080f0, #6366f1);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 18px;
  font-weight: 700;
}

.ps-body {
  flex: 1;
}

.ps-body h3 {
  font-size: 17px;
  font-weight: 600;
  color: #1d1e1f;
  margin: 0 0 4px;
}

.ps-desc {
  font-size: 14px;
  color: #606266;
  margin: 0 0 4px;
  line-height: 1.6;
}

.ps-detail {
  font-size: 13px;
  color: #909399;
  margin: 0;
}

/* ── Testimonials ── */
.testimonials-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.testimonial-card {
  padding: 28px 24px;
  display: flex;
  flex-direction: column;
}

.testimonial-card:hover {
  border-color: var(--accent);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.06);
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

/* ── Contact ── */
.contact-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.contact-card {
  text-align: center;
  padding: 32px 16px;
}

.contact-card:hover {
  border-color: #2080f0;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.06);
}

.contact-card .n-icon {
  margin-bottom: 14px;
}

.contact-card h3 {
  font-size: 16px;
  font-weight: 600;
  color: #1d1e1f;
  margin: 0 0 8px;
}

.contact-card p {
  font-size: 14px;
  color: #606266;
  margin: 0;
  line-height: 1.5;
}

.contact-link {
  font-size: 14px;
  color: #2080f0;
  text-decoration: none;
  transition: opacity 0.2s;
}

.contact-link:hover {
  opacity: 0.7;
}

.contact-note {
  font-size: 12px !important;
  color: #c0c4cc !important;
  margin-top: 4px !important;
}

/* ── Form ── */
.form-wrap {
  max-width: 480px;
  margin: 0 auto;
}

.join-form .n-form-item {
  margin-bottom: 18px;
}

/* ── FAQ ── */
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

/* ── Responsive ── */
@media (max-width: 900px) {
  .benefits-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .testimonials-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .contact-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .ph-content h1 {
    font-size: 32px;
  }

  .testimonials-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 480px) {
  .benefits-grid {
    grid-template-columns: 1fr;
  }

  .contact-grid {
    grid-template-columns: 1fr;
  }
}
</style>
