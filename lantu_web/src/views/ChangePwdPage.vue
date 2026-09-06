<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<template>
  <div class="change-pwd-page">
    <div class="cp-header">
      <h1 class="cp-title">修改密码</h1>
      <p class="cp-desc">请选择一种验证方式</p>
    </div>

    <div class="cp-cards">
      <div class="cp-card" @click="router.push('/change-pwd/email')">
        <div class="cp-card-icon email-icon">
          <svg width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
            <rect x="2" y="4" width="20" height="16" rx="2"/>
            <path d="M22 4L12 13 2 4"/>
          </svg>
        </div>
        <div class="cp-card-body">
          <h2 class="cp-card-title">通过邮箱验证</h2>
          <p class="cp-card-desc">通过已绑定的邮箱接收验证码，验证身份后修改密码（模拟验证码：1234）</p>
        </div>
        <div class="cp-card-arrow">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="9 18 15 12 9 6"/>
          </svg>
        </div>
      </div>

      <div class="cp-card" @click="router.push('/change-pwd/old')">
        <div class="cp-card-icon lock-icon">
          <svg width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
            <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
            <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
          </svg>
        </div>
        <div class="cp-card-body">
          <h2 class="cp-card-title">通过原密码修改</h2>
          <p class="cp-card-desc">输入当前使用的密码，验证通过后设置新密码</p>
        </div>
        <div class="cp-card-arrow">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="9 18 15 12 9 6"/>
          </svg>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.change-pwd-page {
  min-height: calc(100vh - 64px);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 48px 24px;
  background: linear-gradient(180deg, #f0f2f5 0%, #f8f9fc 100%);
}

.cp-header {
  text-align: center;
  margin-bottom: 48px;
}

.cp-title {
  font-size: 28px;
  font-weight: 700;
  color: #1d2939;
  margin: 0 0 8px;
}

.cp-desc {
  font-size: 15px;
  color: #667085;
  margin: 0;
}

.cp-cards {
  display: flex;
  gap: 24px;
  width: 100%;
  max-width: 640px;
}

.cp-card {
  flex: 1;
  display: flex;
  align-items: flex-start;
  gap: 16px;
  padding: 28px 24px;
  background: #fff;
  border-radius: 16px;
  cursor: pointer;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
  transition: all 0.25s ease;
}

.cp-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.cp-card-icon {
  flex-shrink: 0;
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.email-icon {
  background: #eef2ff;
  color: #6366f1;
}

.lock-icon {
  background: #fef3f2;
  color: #ef4444;
}

.cp-card-body {
  flex: 1;
  min-width: 0;
}

.cp-card-title {
  font-size: 16px;
  font-weight: 600;
  color: #1d2939;
  margin: 0 0 6px;
}

.cp-card-desc {
  font-size: 13px;
  color: #98a2b3;
  line-height: 1.5;
  margin: 0;
}

.cp-card-arrow {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  color: #d0d5dd;
  margin-top: 6px;
  transition: color 0.2s;
}

.cp-card:hover .cp-card-arrow {
  color: #667085;
}

@media (max-width: 520px) {
  .cp-cards {
    flex-direction: column;
  }

  .cp-title {
    font-size: 22px;
  }
}
</style>
