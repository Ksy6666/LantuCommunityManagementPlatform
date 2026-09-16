<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage } from 'naive-ui'
import { publishPostApi } from '../api'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const message = useMessage()
const auth = useAuthStore()

const categories = [
  { key: '技术分享', label: '技术分享' },
  { key: '技术问答', label: '技术问答' },
  { key: '经验交流', label: '经验交流' },
  { key: '求助', label: '求助' },
]

const title = ref('')
const category = ref('技术分享')
const tags = ref('')
const content = ref('')
const submitting = ref(false)

onMounted(() => {
  if (!auth.isLoggedIn) {
    message.warning('请先登录后再发布帖子')
    router.push({ path: '/login', query: { redirect: '/discussion/publish' } })
  }
})

async function submit() {
  if (!title.value.trim()) {
    message.warning('请输入标题')
    return
  }
  if (!content.value.trim()) {
    message.warning('请输入正文内容')
    return
  }
  submitting.value = true
  try {
    const res = await publishPostApi({
      title: title.value.trim(),
      content: content.value.trim(),
      category: category.value,
      tags: tags.value.trim(),
    })
    message.success('发布成功')
    router.push(`/discussion/${res.data.data.id}`)
  } catch (e: any) {
    message.error(e?.response?.data?.message || '发布失败')
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <div class="discussion-publish-page">
    <!-- ════════════════ Hero ════════════════ -->
    <section class="page-hero">
      <div class="ph-bg">
        <div class="ph-shape s1"></div>
        <div class="ph-shape s2"></div>
      </div>
      <div class="ph-content">
        <span class="ph-badge">Publish</span>
        <h1 class="ph-title">发布帖子</h1>
        <p class="ph-desc">分享技术经验、提出疑问，让知识流动起来</p>
      </div>
    </section>

    <!-- ════════════════ 发布表单 ════════════════ -->
    <section class="section publish-section reveal">
      <div class="section-inner">
        <div class="publish-card card">
          <div class="form-group">
            <label class="form-label">标题 <span class="required">*</span></label>
            <input
              v-model="title"
              class="form-input"
              type="text"
              placeholder="一句话概括你的帖子主题（例如：Spring Boot 自动配置原理详解）"
              maxlength="200"
            />
          </div>

          <div class="form-row">
            <div class="form-group half">
              <label class="form-label">分类 <span class="required">*</span></label>
              <div class="category-select">
                <button
                  v-for="cat in categories"
                  :key="cat.key"
                  class="category-btn"
                  :class="{ active: category === cat.key }"
                  @click="category = cat.key"
                >
                  {{ cat.label }}
                </button>
              </div>
            </div>
            <div class="form-group half">
              <label class="form-label">标签</label>
              <input
                v-model="tags"
                class="form-input"
                type="text"
                placeholder="多个标签用逗号分隔，如：Java,Spring,MySQL"
                maxlength="200"
              />
            </div>
          </div>

          <div class="form-group">
            <label class="form-label">正文内容 <span class="required">*</span></label>
            <textarea
              v-model="content"
              class="form-textarea"
              rows="10"
              placeholder="详细描述你的技术问题或分享内容。\n建议包含：背景、关键代码/步骤、遇到的问题、期望的结果。"
            ></textarea>
          </div>

          <div class="publish-actions">
            <button class="cancel-btn" @click="router.back()">取消</button>
            <button class="submit-btn" :disabled="submitting" @click="submit">
              {{ submitting ? '发布中...' : '🚀 发布帖子' }}
            </button>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.discussion-publish-page {
  min-height: 60vh;
}

.publish-section {
  max-width: 680px;
}

.publish-card {
  padding: 36px;
}

.form-group {
  margin-bottom: 24px;
}
.form-row {
  display: flex;
  gap: 24px;
}
.form-group.half {
  flex: 1;
  min-width: 0;
}
.form-label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary, #1a1a1a);
}
.required {
  color: #d03050;
}

.form-input {
  width: 100%;
  padding: 11px 16px;
  font-size: 14px;
  color: var(--text-primary, #1a1a1a);
  background: var(--card-bg, #fff);
  border: 1px solid var(--border-color, #e0e0e0);
  border-radius: 10px;
  outline: none;
  box-sizing: border-box;
  transition: border-color 0.25s, box-shadow 0.25s;
}
.form-input:focus {
  border-color: #2080f0;
  box-shadow: 0 0 0 3px rgba(32, 128, 240, 0.12);
}

.category-select {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
.category-btn {
  padding: 8px 18px;
  font-size: 14px;
  color: var(--text-secondary, #666);
  background: transparent;
  border: 1px solid var(--border-color, #e0e0e0);
  border-radius: 999px;
  cursor: pointer;
  transition: all 0.25s;
}
.category-btn:hover {
  border-color: #2080f0;
  color: #2080f0;
}
.category-btn.active {
  color: #fff;
  background: linear-gradient(135deg, #2080f0, #6366f1);
  border-color: transparent;
  box-shadow: 0 4px 14px rgba(32, 128, 240, 0.3);
}

.form-textarea {
  width: 100%;
  padding: 12px 16px;
  font-size: 14px;
  font-family: inherit;
  line-height: 1.7;
  color: var(--text-primary, #1a1a1a);
  background: var(--card-bg, #fff);
  border: 1px solid var(--border-color, #e0e0e0);
  border-radius: 10px;
  outline: none;
  resize: vertical;
  box-sizing: border-box;
  transition: border-color 0.25s, box-shadow 0.25s;
}
.form-textarea:focus {
  border-color: #2080f0;
  box-shadow: 0 0 0 3px rgba(32, 128, 240, 0.12);
}

.publish-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 8px;
}
.cancel-btn {
  padding: 10px 26px;
  font-size: 14px;
  color: var(--text-secondary, #666);
  background: transparent;
  border: 1px solid var(--border-color, #e0e0e0);
  border-radius: 10px;
  cursor: pointer;
  transition: border-color 0.25s, color 0.25s;
}
.cancel-btn:hover {
  border-color: #d03050;
  color: #d03050;
}
.submit-btn {
  padding: 10px 30px;
  font-size: 15px;
  color: #fff;
  background: linear-gradient(135deg, #2080f0, #6366f1);
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: opacity 0.25s, transform 0.25s;
  box-shadow: 0 6px 18px rgba(32, 128, 240, 0.3);
}
.submit-btn:hover {
  opacity: 0.9;
  transform: translateY(-2px);
}
.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

@media (max-width: 768px) {
  .form-row {
    flex-direction: column;
    gap: 0;
  }
  .publish-card {
    padding: 24px;
  }
}
</style>
