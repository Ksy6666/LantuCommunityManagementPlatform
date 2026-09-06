<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { loginApi } from '../api'
import { useAuthStore } from '../stores/auth'
import { useMessage } from 'naive-ui'

const router = useRouter()
const message = useMessage()
const auth = useAuthStore()

const form = ref({
  account: '',
  password: '',
  remember: false,
})

const loading = ref(false)

async function handleLogin() {
  if (!form.value.account || !form.value.password) {
    message.warning('请填写手机号和密码')
    return
  }
  loading.value = true
  try {
    const res = await loginApi({
      account: form.value.account,
      password: form.value.password,
    })
    if (res.data.code === 200) {
      message.success('登录成功')
      auth.login(res.data.data)
      router.push('/')
    }
  } catch (err: any) {
    const msg = err?.response?.data?.message || '登录失败，请检查网络'
    message.error(msg)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <!-- 背景装饰 -->
    <div class="login-bg">
      <div class="bg-shape s1"></div>
      <div class="bg-shape s2"></div>
      <div class="bg-shape s3"></div>
    </div>

    <div class="login-card">
      <!-- 头部 -->
      <div class="lc-header">
        <div class="lc-logo" @click="router.push('/')">
          <img src="/favicon.png" alt="Open_Lantu" />
        </div>
        <h1>欢迎回来</h1>
        <p>登录你的 Open_Lantu 账号</p>
      </div>

      <!-- 表单 -->
      <n-form :model="form" class="lc-form">
        <n-form-item>
          <n-input
            v-model:value="form.account"
            placeholder="手机号 / 账号"
            size="large"
            clearable
          >
            <template #prefix>
              <n-icon><Iphone /></n-icon>
            </template>
          </n-input>
        </n-form-item>

        <n-form-item>
          <n-input
            v-model:value="form.password"
            type="password"
            placeholder="密码"
            size="large"
            show-password-on="click"
            clearable
          >
            <template #prefix>
              <n-icon><Lock /></n-icon>
            </template>
          </n-input>
        </n-form-item>

        <div class="lc-options">
          <n-checkbox v-model:checked="form.remember">记住我</n-checkbox>
        </div>

        <n-button
          type="primary"
          size="large"
          round
          block
          :loading="loading"
          @click="handleLogin"
        >
          登录
        </n-button>
      </n-form>

      <!-- 注册引导 -->
      <div class="lc-footer">
        还没有账号？
        <n-button text @click="router.push('/register')" class="lc-link">
          立即注册
        </n-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  min-height: calc(100vh - 64px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 24px;
  position: relative;
  overflow: hidden;
}

.login-bg {
  position: absolute;
  inset: 0;
  background: linear-gradient(160deg, #e8f4ff 0%, #f0f0ff 40%, #f5f3ff 70%, #ecfdf5 100%);
  z-index: 0;
}

.bg-shape {
  position: absolute;
  border-radius: 50%;
  opacity: 0.25;
  animation: float 8s ease-in-out infinite;
}

.s1 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(32, 128, 240, 0.12), transparent);
  top: -120px;
  right: -80px;
  animation-delay: 0s;
}

.s2 {
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, rgba(99, 102, 241, 0.1), transparent);
  bottom: -80px;
  left: -60px;
  animation-delay: -3s;
}

.s3 {
  width: 200px;
  height: 200px;
  background: radial-gradient(circle, rgba(240, 160, 32, 0.08), transparent);
  top: 50%;
  left: 60%;
  animation-delay: -6s;
}

@keyframes float {
  0%, 100% { transform: translateY(0) scale(1); }
  50% { transform: translateY(-20px) scale(1.03); }
}

.login-card {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 420px;
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border-radius: 20px;
  padding: 40px 36px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.5);
}

.lc-header {
  text-align: center;
  margin-bottom: 32px;
}

.lc-logo {
  cursor: pointer;
  margin-bottom: 16px;
}

.lc-logo img {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  object-fit: cover;
  transition: transform 0.3s;
}

.lc-logo img:hover {
  transform: rotate(-6deg) scale(1.05);
}

.lc-header h1 {
  font-size: 24px;
  font-weight: 700;
  color: #1d1e1f;
  margin: 0 0 6px;
}

.lc-header p {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.lc-form {
  margin-bottom: 24px;
}

.lc-form .n-form-item {
  margin-bottom: 4px;
}

.lc-options {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
}

.lc-forgot {
  font-size: 13px;
  color: #2080f0;
  text-decoration: none;
  transition: opacity 0.2s;
}

.lc-forgot:hover {
  opacity: 0.7;
}

.lc-footer {
  text-align: center;
  font-size: 14px;
  color: #909399;
}

.lc-link {
  font-size: 14px;
}

@media (max-width: 480px) {
  .login-card {
    padding: 32px 24px;
    border-radius: 16px;
  }
}
</style>
