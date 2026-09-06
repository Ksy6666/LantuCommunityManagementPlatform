<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<template>
  <div class="not-found">
    <div class="nf-content">
      <div class="nf-code">404</div>
      <h1 class="nf-title">页面未找到</h1>
      <p class="nf-desc">抱歉，您访问的页面不存在或已被移除。</p>
      <n-button type="primary" size="large" round @click="router.push('/')">
        返回首页
      </n-button>
    </div>
  </div>
</template>

<style scoped>
.not-found {
  min-height: calc(100vh - 64px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 24px;
  background: linear-gradient(180deg, #f0f2f5 0%, #f8f9fc 100%);
}

.nf-content {
  text-align: center;
  max-width: 420px;
}

.nf-code {
  font-size: 96px;
  font-weight: 800;
  line-height: 1;
  background: linear-gradient(135deg, #2080f0, #6366f1, #8b5cf6);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  margin-bottom: 16px;
}

.nf-title {
  font-size: 24px;
  font-weight: 700;
  color: #1d1e1f;
  margin: 0 0 12px;
}

.nf-desc {
  font-size: 15px;
  color: #909399;
  margin: 0 0 32px;
  line-height: 1.6;
}
</style>
