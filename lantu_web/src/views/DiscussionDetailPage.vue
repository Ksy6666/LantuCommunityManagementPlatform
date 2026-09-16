<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useMessage } from 'naive-ui'
import {
  getDiscussionPostApi,
  getDiscussionCommentsApi,
  addDiscussionCommentApi,
  togglePostLikeApi,
  togglePostFavoriteApi,
} from '../api'
import type { DiscussionPost, DiscussionComment } from '../api'
import { useAuthStore } from '../stores/auth'

const route = useRoute()
const router = useRouter()
const message = useMessage()
const auth = useAuthStore()

const postId = Number(route.params.id)
const post = ref<DiscussionPost | null>(null)
const comments = ref<DiscussionComment[]>([])
const myComment = ref('')
const loading = ref(true)
const submitting = ref(false)
const liked = ref(false)
const favorited = ref(false)
const likeCount = ref(0)
const favoriteCount = ref(0)

const replyTarget = ref<DiscussionComment | null>(null)
const replyContent = ref('')

const topComments = computed(() => comments.value.filter(c => !c.parentId || c.parentId === 0))

function ensureLoggedIn(): boolean {
  if (!auth.isLoggedIn) {
    message.warning('请先登录后再操作')
    router.push({ path: '/login', query: { redirect: route.fullPath } })
    return false
  }
  return true
}

async function loadPost() {
  try {
    const res = await getDiscussionPostApi(postId)
    post.value = res.data.data
    likeCount.value = post.value.likeCount
    favoriteCount.value = post.value.favoriteCount
  } catch (e: any) {
    message.error(e?.response?.data?.message || '帖子不存在或加载失败')
    router.replace('/discussion')
  } finally {
    loading.value = false
  }
}

async function loadComments() {
  try {
    const res = await getDiscussionCommentsApi(postId)
    comments.value = res.data.data
  } catch {
    // 评论加载失败不阻塞页面
  }
}

function toggleLike() {
  if (!ensureLoggedIn()) return
  togglePostLikeApi(postId).then(res => {
    liked.value = res.data.data.liked
    likeCount.value = res.data.data.likeCount
    if (post.value) post.value.likeCount = likeCount.value
  }).catch((e: any) => {
    message.error(e?.response?.data?.message || '操作失败')
  })
}

function toggleFavorite() {
  if (!ensureLoggedIn()) return
  togglePostFavoriteApi(postId).then(res => {
    favorited.value = res.data.data.favorited
    favoriteCount.value = res.data.data.favoriteCount
    if (post.value) post.value.favoriteCount = favoriteCount.value
  }).catch((e: any) => {
    message.error(e?.response?.data?.message || '操作失败')
  })
}

async function submitComment() {
  if (!ensureLoggedIn()) return
  const content = myComment.value.trim()
  if (!content) {
    message.warning('请输入评论内容')
    return
  }
  submitting.value = true
  try {
    await addDiscussionCommentApi(postId, content)
    myComment.value = ''
    message.success('评论成功')
    await loadComments()
    if (post.value) {
      post.value.commentCount++
    }
  } catch (e: any) {
    message.error(e?.response?.data?.message || '评论失败')
  } finally {
    submitting.value = false
  }
}

function startReply(comment: DiscussionComment) {
  if (!ensureLoggedIn()) return
  replyTarget.value = comment
  replyContent.value = ''
}

function cancelReply() {
  replyTarget.value = null
  replyContent.value = ''
}

async function submitReply() {
  if (!replyTarget.value) return
  const content = replyContent.value.trim()
  if (!content) {
    message.warning('请输入回复内容')
    return
  }
  submitting.value = true
  try {
    await addDiscussionCommentApi(postId, content, replyTarget.value.id)
    replyTarget.value = null
    replyContent.value = ''
    message.success('回复成功')
    await loadComments()
    if (post.value) {
      post.value.commentCount++
    }
  } catch (e: any) {
    message.error(e?.response?.data?.message || '回复失败')
  } finally {
    submitting.value = false
  }
}

function parentText(comment: DiscussionComment): string {
  const parent = comments.value.find(c => c.id === comment.parentId)
  return parent ? (parent.authorNickname || '匿名用户') : ''
}

function formatDate(s: string) {
  if (!s) return ''
  return s.replace('T', ' ').slice(0, 16)
}

onMounted(async () => {
  await loadPost()
  await loadComments()
})
</script>

<template>
  <div class="discussion-detail-page">
    <section class="page-hero">
      <div class="ph-bg">
        <div class="ph-shape s1"></div>
        <div class="ph-shape s2"></div>
      </div>
      <div class="ph-content">
        <span class="ph-badge">Discussion Detail</span>
        <h1 class="ph-title">帖子详情</h1>
        <p class="ph-desc">认真阅读，真诚交流，让每次讨论都有价值</p>
      </div>
    </section>

    <section class="section detail-section reveal">
      <div class="section-inner">
        <n-spin :show="loading">
          <div v-if="post" class="detail-card card">
            <!-- 作者与标题 -->
            <div class="detail-header">
              <div class="detail-author">
                <span class="detail-avatar">{{ (post.authorNickname || '客').charAt(0) }}</span>
                <div class="detail-author-info">
                  <span class="detail-nickname">{{ post.authorNickname || '匿名用户' }}</span>
                  <span class="detail-time">{{ formatDate(post.createdAt) }}</span>
                </div>
              </div>
              <div class="detail-title-row">
                <span class="detail-category">{{ post.category }}</span>
                <h1 class="detail-title">{{ post.title }}</h1>
              </div>
            </div>

            <!-- 标签 -->
            <div v-if="post.tags" class="detail-tags">
              <span v-for="tag in post.tags.split(',').map((t: string) => t.trim()).filter(Boolean)" :key="tag" class="tag-item">
                # {{ tag }}
              </span>
            </div>

            <!-- 正文 -->
            <div class="detail-content">{{ post.content }}</div>

            <!-- 操作栏 -->
            <div class="detail-actions">
              <button class="action-btn" :class="{ active: liked }" @click="toggleLike">
                👍 {{ liked ? '已点赞' : '点赞' }} ({{ likeCount }})
              </button>
              <button class="action-btn" :class="{ active: favorited }" @click="toggleFavorite">
                ⭐ {{ favorited ? '已收藏' : '收藏' }} ({{ favoriteCount }})
              </button>
              <span class="stat-chip">👁 {{ post.viewCount }} 浏览</span>
              <span class="stat-chip">💬 {{ post.commentCount }} 评论</span>
            </div>
          </div>
        </n-spin>

        <!-- 发表评论 -->
        <div class="comment-editor card">
          <h3 class="comment-section-title">💬 发表回答 / 评论</h3>
          <textarea
            v-model="myComment"
            class="comment-input"
            rows="4"
            placeholder="写下你的回答或评论，支持 Markdown 风格纯文本..."
          ></textarea>
          <div class="comment-actions">
            <button class="submit-btn" :disabled="submitting" @click="submitComment">
              {{ submitting ? '提交中...' : '发表评论' }}
            </button>
          </div>
        </div>

        <!-- 评论列表 -->
        <div class="comment-list">
          <h3 class="comment-section-title">
            💬 全部回答与评论（{{ comments.length }}）
          </h3>

          <div v-for="comment in topComments" :key="comment.id" class="comment-item card">
            <div class="comment-body">
              <span class="comment-avatar">{{ (comment.authorNickname || '客').charAt(0) }}</span>
              <div class="comment-content">
                <div class="comment-meta">
                  <span class="comment-nickname">{{ comment.authorNickname || '匿名用户' }}</span>
                  <span class="comment-time">{{ formatDate(comment.createdAt) }}</span>
                </div>
                <p class="comment-text">{{ comment.content }}</p>
                <button class="reply-btn" @click="startReply(comment)">↩ 回复</button>

                <!-- 回复列表 -->
                <div v-for="sub in comments.filter(c => c.parentId === comment.id)" :key="sub.id" class="sub-comment">
                  <span class="comment-avatar small">{{ (sub.authorNickname || '客').charAt(0) }}</span>
                  <div class="comment-content">
                    <div class="comment-meta">
                      <span class="comment-nickname">{{ sub.authorNickname || '匿名用户' }}</span>
                      <span v-if="parentText(sub)" class="comment-reply-to">回复 {{ parentText(sub) }}</span>
                      <span class="comment-time">{{ formatDate(sub.createdAt) }}</span>
                    </div>
                    <p class="comment-text">{{ sub.content }}</p>
                    <button class="reply-btn" @click="startReply(sub)">↩ 回复</button>
                  </div>
                </div>
              </div>
            </div>

            <!-- 内联回复框 -->
            <div v-if="replyTarget && replyTarget.id === comment.id" class="reply-box">
              <textarea
                v-model="replyContent"
                class="comment-input compact"
                rows="2"
                placeholder="回复内容..."
              ></textarea>
              <div class="comment-actions">
                <button class="submit-btn small" :disabled="submitting" @click="submitReply">提交回复</button>
                <button class="cancel-btn" @click="cancelReply">取消</button>
              </div>
            </div>
          </div>

          <n-empty v-if="!loading && comments.length === 0" description="还没有评论，来抢沙发吧" />
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.discussion-detail-page {
  min-height: 60vh;
}

.detail-section {
  max-width: 900px;
}

.detail-card {
  padding: 32px;
  margin-bottom: 24px;
}

.detail-header {
  margin-bottom: 18px;
}

.detail-author {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 16px;
}
.detail-avatar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  font-size: 16px;
  color: #fff;
  background: linear-gradient(135deg, #2080f0, #6366f1);
  border-radius: 50%;
}
.detail-author-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}
.detail-nickname {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary, #1a1a1a);
}
.detail-time {
  font-size: 12px;
  color: var(--text-tertiary, #999);
}
.detail-title-row {
  display: flex;
  align-items: center;
  gap: 12px;
}
.detail-category {
  padding: 3px 12px;
  font-size: 13px;
  color: #2080f0;
  background: rgba(32, 128, 240, 0.1);
  border-radius: 999px;
  white-space: nowrap;
}
.detail-title {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
  color: var(--text-primary, #1a1a1a);
  line-height: 1.4;
}

.detail-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 18px;
}
.tag-item {
  padding: 3px 10px;
  font-size: 12px;
  color: #6366f1;
  background: rgba(99, 102, 241, 0.1);
  border-radius: 6px;
}

.detail-content {
  font-size: 15px;
  line-height: 1.9;
  color: var(--text-primary, #222);
  white-space: pre-wrap;
  word-break: break-word;
  padding: 10px 0 24px;
  border-bottom: 1px dashed var(--border-color, #eee);
  margin-bottom: 18px;
}

.detail-actions {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
}
.action-btn {
  padding: 8px 18px;
  font-size: 14px;
  color: var(--text-secondary, #555);
  background: var(--card-bg, #fff);
  border: 1px solid var(--border-color, #e0e0e0);
  border-radius: 999px;
  cursor: pointer;
  transition: all 0.25s;
}
.action-btn:hover {
  border-color: #2080f0;
  color: #2080f0;
}
.action-btn.active {
  color: #fff;
  background: linear-gradient(135deg, #2080f0, #6366f1);
  border-color: transparent;
  box-shadow: 0 4px 14px rgba(32, 128, 240, 0.3);
}
.stat-chip {
  font-size: 13px;
  color: var(--text-tertiary, #999);
}

.comment-editor {
  padding: 24px;
  margin-bottom: 28px;
}
.comment-section-title {
  margin: 0 0 16px;
  font-size: 17px;
  font-weight: 600;
  color: var(--text-primary, #1a1a1a);
}
.comment-input {
  width: 100%;
  padding: 12px 16px;
  font-size: 14px;
  font-family: inherit;
  line-height: 1.6;
  color: var(--text-primary, #1a1a1a);
  background: var(--card-bg, #fff);
  border: 1px solid var(--border-color, #e0e0e0);
  border-radius: 10px;
  outline: none;
  resize: vertical;
  box-sizing: border-box;
  transition: border-color 0.25s, box-shadow 0.25s;
}
.comment-input:focus {
  border-color: #2080f0;
  box-shadow: 0 0 0 3px rgba(32, 128, 240, 0.12);
}
.comment-input.compact {
  min-height: 40px;
}
.comment-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 12px;
}
.submit-btn {
  padding: 9px 26px;
  font-size: 14px;
  color: #fff;
  background: linear-gradient(135deg, #2080f0, #6366f1);
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: opacity 0.25s, transform 0.25s;
  box-shadow: 0 4px 14px rgba(32, 128, 240, 0.25);
}
.submit-btn:hover {
  opacity: 0.88;
  transform: translateY(-1px);
}
.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
.submit-btn.small {
  padding: 6px 16px;
  font-size: 13px;
}
.cancel-btn {
  padding: 6px 16px;
  font-size: 13px;
  color: var(--text-secondary, #666);
  background: transparent;
  border: 1px solid var(--border-color, #e0e0e0);
  border-radius: 10px;
  cursor: pointer;
}
.cancel-btn:hover {
  border-color: #d03050;
  color: #d03050;
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.comment-item {
  padding: 20px 24px;
}
.comment-body {
  display: flex;
  gap: 12px;
}
.comment-avatar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  flex-shrink: 0;
  font-size: 14px;
  color: #fff;
  background: linear-gradient(135deg, #36ad6a, #2080f0);
  border-radius: 50%;
}
.comment-avatar.small {
  width: 28px;
  height: 28px;
  font-size: 12px;
}
.comment-content {
  flex: 1;
  min-width: 0;
}
.comment-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 6px;
}
.comment-nickname {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary, #1a1a1a);
}
.comment-reply-to {
  font-size: 12px;
  color: #2080f0;
}
.comment-time {
  font-size: 12px;
  color: var(--text-tertiary, #999);
}
.comment-text {
  margin: 0 0 8px;
  font-size: 14px;
  line-height: 1.7;
  color: var(--text-primary, #333);
  white-space: pre-wrap;
  word-break: break-word;
}
.reply-btn {
  padding: 0;
  font-size: 13px;
  color: #2080f0;
  background: none;
  border: none;
  cursor: pointer;
  opacity: 0.7;
  transition: opacity 0.2s;
}
.reply-btn:hover {
  opacity: 1;
}

.sub-comment {
  display: flex;
  gap: 10px;
  margin-top: 12px;
  padding: 12px 14px;
  background: var(--bg-secondary, rgba(0, 0, 0, 0.03));
  border-radius: 10px;
}

.reply-box {
  margin-top: 14px;
  padding: 14px;
  background: var(--bg-secondary, rgba(0, 0, 0, 0.03));
  border-radius: 10px;
}
</style>
