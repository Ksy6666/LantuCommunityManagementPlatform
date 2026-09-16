<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage } from 'naive-ui'
import { getDiscussionPostsApi, getHotPostsApi } from '../api'
import type { DiscussionPost } from '../api'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const message = useMessage()
const auth = useAuthStore()

const categories = [
  { key: '', label: '全部' },
  { key: '技术分享', label: '技术分享' },
  { key: '技术问答', label: '技术问答' },
  { key: '经验交流', label: '经验交流' },
  { key: '求助', label: '求助' },
]

const posts = ref<DiscussionPost[]>([])
const hotPosts = ref<DiscussionPost[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const loading = ref(false)
const activeCategory = ref('')
const keyword = ref('')

const totalPages = computed(() => Math.max(1, Math.ceil(total.value / size.value)))

async function loadPosts() {
  loading.value = true
  try {
    const res = await getDiscussionPostsApi(page.value, size.value, activeCategory.value, keyword.value)
    posts.value = res.data.data.list
    total.value = res.data.data.total
  } catch (e: any) {
    message.error(e?.response?.data?.message || '加载帖子列表失败')
  } finally {
    loading.value = false
  }
}

async function loadHotPosts() {
  try {
    const res = await getHotPostsApi(10)
    hotPosts.value = res.data.data
  } catch {
    // 热点帖加载失败不阻塞页面
  }
}

function selectCategory(cat: string) {
  activeCategory.value = cat
  page.value = 1
  loadPosts()
}

function search() {
  page.value = 1
  loadPosts()
}

function goDetail(id: number) {
  router.push(`/discussion/${id}`)
}

function goPublish() {
  if (!auth.isLoggedIn) {
    message.warning('请先登录后再发布帖子')
    router.push({ path: '/login', query: { redirect: '/discussion/publish' } })
    return
  }
  router.push('/discussion/publish')
}

function formatDate(s: string) {
  if (!s) return ''
  return s.replace('T', ' ').slice(0, 16)
}

onMounted(() => {
  loadPosts()
  loadHotPosts()
})
</script>

<template>
  <div class="discussion-page">
    <!-- ════════════════ Hero ════════════════ -->
    <section class="page-hero">
      <div class="ph-bg">
        <div class="ph-shape s1"></div>
        <div class="ph-shape s2"></div>
      </div>
      <div class="ph-content">
        <span class="ph-badge">Discussion</span>
        <h1 class="ph-title">技术讨论</h1>
        <p class="ph-desc">发布技术问题、分享实践经验、与伙伴交流成长</p>
        <button class="ph-btn" @click="goPublish">📝 发布帖子</button>
      </div>
    </section>

    <!-- ════════════════ 帖子列表 ════════════════ -->
    <section class="section discussion-section reveal">
      <div class="section-inner discussion-inner">
        <!-- 左侧：列表 -->
        <div class="discussion-main">
          <div class="filter-bar">
            <button
              v-for="cat in categories"
              :key="cat.key"
              class="filter-btn"
              :class="{ active: activeCategory === cat.key }"
              @click="selectCategory(cat.key)"
            >
              {{ cat.label }}
            </button>
          </div>

          <div class="search-bar">
            <input
              v-model="keyword"
              class="search-input"
              type="text"
              placeholder="搜索标题或内容关键词..."
              @keyup.enter="search"
            />
            <button class="search-btn" @click="search">搜索</button>
          </div>

          <n-spin :show="loading">
            <div class="post-list">
              <div
                v-for="post in posts"
                :key="post.id"
                class="post-card card reveal"
                @click="goDetail(post.id)"
              >
                <div class="post-main">
                  <div class="post-title-row">
                    <span class="post-category">{{ post.category }}</span>
                    <h3 class="post-title">{{ post.title }}</h3>
                  </div>
                  <p class="post-excerpt">{{ post.content.slice(0, 120) }}...</p>
                  <div class="post-meta">
                    <span class="post-author">
                      <span class="author-avatar">{{ (post.authorNickname || '客').charAt(0) }}</span>
                      {{ post.authorNickname || '匿名用户' }}
                    </span>
                    <span class="meta-item">👁 {{ post.viewCount }}</span>
                    <span class="meta-item">👍 {{ post.likeCount }}</span>
                    <span class="meta-item">⭐ {{ post.favoriteCount }}</span>
                    <span class="meta-item">💬 {{ post.commentCount }}</span>
                    <span class="meta-item">{{ formatDate(post.createdAt) }}</span>
                  </div>
                </div>
              </div>

              <n-empty v-if="!loading && posts.length === 0" description="暂无帖子，来发布第一篇吧" />
            </div>
          </n-spin>

          <div v-if="totalPages > 1" class="pagination">
            <button class="page-btn" :disabled="page <= 1" @click="page--; loadPosts()">‹</button>
            <span class="page-info">{{ page }} / {{ totalPages }}</span>
            <button class="page-btn" :disabled="page >= totalPages" @click="page++; loadPosts()">›</button>
          </div>
        </div>

        <!-- 右侧：热点排行 -->
        <aside class="discussion-side">
          <div class="side-card card">
            <h3 class="side-title">🔥 热门帖子</h3>
            <div v-for="(post, i) in hotPosts" :key="post.id" class="hot-item" @click="goDetail(post.id)">
              <span class="hot-rank" :class="{ top3: i < 3 }">{{ i + 1 }}</span>
              <span class="hot-title">{{ post.title }}</span>
            </div>
            <n-empty v-if="hotPosts.length === 0" description="暂无数据" :show="false" />
          </div>

          <div class="side-card card publish-card" @click="goPublish">
            <h3 class="side-title">✏️ 想要分享？</h3>
            <p class="side-desc">把你的技术心得、踩坑经验发布出来，帮助更多人！</p>
            <button class="side-btn">立即发布</button>
          </div>
        </aside>
      </div>
    </section>
  </div>
</template>

<style scoped>
.discussion-page {
  min-height: 60vh;
}

/* Hero 复用全局 page-hero 样式 */
.ph-btn {
  margin-top: 24px;
  padding: 12px 28px;
  font-size: 16px;
  color: #fff;
  background: linear-gradient(135deg, #2080f0, #6366f1);
  border: none;
  border-radius: 999px;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  box-shadow: 0 8px 24px rgba(99, 102, 241, 0.35);
}
.ph-btn:hover {
  transform: translateY(-3px) scale(1.03);
  box-shadow: 0 12px 32px rgba(99, 102, 241, 0.5);
}

.discussion-inner {
  display: flex;
  gap: 28px;
  align-items: flex-start;
}
.discussion-main {
  flex: 1;
  min-width: 0;
}
.discussion-side {
  width: 300px;
  flex-shrink: 0;
}

.filter-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 16px;
}
.filter-btn {
  padding: 7px 18px;
  font-size: 14px;
  color: var(--text-secondary, #666);
  background: transparent;
  border: 1px solid var(--border-color, #e0e0e0);
  border-radius: 999px;
  cursor: pointer;
  transition: all 0.25s;
}
.filter-btn:hover {
  border-color: #2080f0;
  color: #2080f0;
}
.filter-btn.active {
  color: #fff;
  background: linear-gradient(135deg, #2080f0, #6366f1);
  border-color: transparent;
  box-shadow: 0 4px 14px rgba(32, 128, 240, 0.3);
}

.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}
.search-input {
  flex: 1;
  padding: 10px 16px;
  font-size: 14px;
  border: 1px solid var(--border-color, #e0e0e0);
  border-radius: 10px;
  outline: none;
  transition: border-color 0.25s, box-shadow 0.25s;
  background: var(--card-bg, #fff);
  color: var(--text-primary, #1a1a1a);
}
.search-input:focus {
  border-color: #2080f0;
  box-shadow: 0 0 0 3px rgba(32, 128, 240, 0.12);
}
.search-btn {
  padding: 10px 24px;
  font-size: 14px;
  color: #fff;
  background: linear-gradient(135deg, #2080f0, #6366f1);
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: opacity 0.25s;
}
.search-btn:hover {
  opacity: 0.88;
}

.post-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}
.post-card {
  padding: 20px 24px;
  cursor: pointer;
  transition: transform 0.25s, box-shadow 0.25s;
}
.post-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 28px rgba(0, 0, 0, 0.08);
}
.post-title-row {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}
.post-category {
  padding: 2px 10px;
  font-size: 12px;
  color: #2080f0;
  background: rgba(32, 128, 240, 0.1);
  border-radius: 999px;
  white-space: nowrap;
}
.post-title {
  margin: 0;
  font-size: 17px;
  font-weight: 600;
  color: var(--text-primary, #1a1a1a);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.post-excerpt {
  margin: 0 0 12px;
  font-size: 14px;
  color: var(--text-secondary, #666);
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.post-meta {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 14px;
  font-size: 13px;
  color: var(--text-tertiary, #999);
}
.post-author {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  color: var(--text-secondary, #666);
}
.author-avatar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 22px;
  height: 22px;
  font-size: 12px;
  color: #fff;
  background: linear-gradient(135deg, #2080f0, #6366f1);
  border-radius: 50%;
}
.meta-item {
  display: inline-flex;
  align-items: center;
  gap: 2px;
}

.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  margin-top: 28px;
}
.page-btn {
  width: 36px;
  height: 36px;
  font-size: 18px;
  color: var(--text-secondary, #666);
  background: var(--card-bg, #fff);
  border: 1px solid var(--border-color, #e0e0e0);
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.25s;
}
.page-btn:hover:not(:disabled) {
  border-color: #2080f0;
  color: #2080f0;
}
.page-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}
.page-info {
  font-size: 14px;
  color: var(--text-secondary, #666);
}

.side-card {
  padding: 20px;
  margin-bottom: 20px;
}
.side-title {
  margin: 0 0 14px;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary, #1a1a1a);
}
.hot-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 9px 0;
  cursor: pointer;
  border-bottom: 1px dashed var(--border-color, #eee);
  transition: color 0.2s;
}
.hot-item:last-child {
  border-bottom: none;
}
.hot-item:hover .hot-title {
  color: #2080f0;
}
.hot-rank {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 22px;
  height: 22px;
  flex-shrink: 0;
  font-size: 12px;
  font-weight: 600;
  color: #fff;
  background: #bbb;
  border-radius: 6px;
}
.hot-rank.top3 {
  background: linear-gradient(135deg, #f0a020, #ff6b35);
}
.hot-title {
  font-size: 14px;
  color: var(--text-secondary, #555);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: color 0.2s;
}

.publish-card {
  text-align: center;
}
.side-desc {
  margin: 0 0 16px;
  font-size: 13px;
  color: var(--text-secondary, #777);
  line-height: 1.6;
}
.side-btn {
  padding: 9px 26px;
  font-size: 14px;
  color: #fff;
  background: linear-gradient(135deg, #36ad6a, #2080f0);
  border: none;
  border-radius: 999px;
  cursor: pointer;
  transition: transform 0.25s, box-shadow 0.25s;
  box-shadow: 0 6px 18px rgba(54, 173, 106, 0.3);
}
.side-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 24px rgba(54, 173, 106, 0.4);
}

@media (max-width: 900px) {
  .discussion-inner {
    flex-direction: column;
  }
  .discussion-side {
    width: 100%;
  }
}
</style>
