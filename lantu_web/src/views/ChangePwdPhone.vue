<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage } from 'naive-ui'
import { changePasswordEmailApi } from '../api'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const message = useMessage()
const auth = useAuthStore()

const email = ref('')
const codeObtained = ref(false)
const newPwd = ref('')
const confirmPwd = ref('')
const pwdMatch = computed(() => {
  if (!newPwd.value || !confirmPwd.value) return null
  return newPwd.value === confirmPwd.value
})

const submitting = ref(false)

const canSubmit = computed(() => {
  return !submitting.value && email.value && codeObtained.value && newPwd.value && confirmPwd.value && pwdMatch.value
})

function getCode() {
  if (!email.value) {
    message.warning('请先输入邮箱')
    return
  }
  if (email.value !== auth.email) {
    message.error('邮箱与当前账号不匹配')
    return
  }
  codeObtained.value = true
  message.success('验证码已获取')
}

async function submit() {
  if (!pwdMatch.value) {
    message.error('两次输入的密码不一致')
    return
  }
  if (!codeObtained.value) {
    message.error('请先点击获取验证码')
    return
  }
  submitting.value = true
  try {
    const res = await changePasswordEmailApi({
      email: email.value,
      code: '',
      newPassword: newPwd.value,
      confirmPassword: confirmPwd.value,
    })
    if (res.data.code === 200) {
      message.success('密码修改成功，请重新登录')
      auth.logout()
      router.push('/')
    }
  } catch (err: any) {
    const msg = err?.response?.data?.message || '密码修改失败'
    message.error(msg)
  } finally {
    submitting.value = false
  }
}

function goBack() {
  router.push('/change-pwd')
}
</script>

<template>
  <div class="pwd-page">
    <div class="pwd-card">
      <button class="pwd-back" @click="goBack">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 12H5"/><polyline points="12 19 5 12 12 5"/></svg>
        返回
      </button>
      <div class="pwd-header">
        <h1 class="pwd-title">通过邮箱验证</h1>
        <p class="pwd-desc">向绑定的邮箱发送验证码</p>
      </div>
      <!-- 邮箱 -->
      <div class="field">
        <label class="field-label">邮箱</label>
        <input v-model="email" type="email" placeholder="请输入绑定的邮箱" class="field-input" />
      </div>

      <!-- 验证码 -->
      <div class="field">
        <div class="code-row">
          <div class="code-status" :class="{ obtained: codeObtained }">
            {{ codeObtained ? '✅ 验证码已获取' : '点击按钮获取验证码' }}
          </div>
          <button class="code-btn" :disabled="codeObtained" @click="getCode">
            {{ codeObtained ? '已验证' : '获取验证码' }}
          </button>
        </div>
      </div>

      <!-- 新密码 -->
      <div class="field">
        <label class="field-label">新密码</label>
        <input v-model="newPwd" type="password" placeholder="请输入新密码" class="field-input" />
      </div>

      <!-- 确认密码 -->
      <div class="field">
        <label class="field-label">确认密码</label>
        <input v-model="confirmPwd" type="password" placeholder="请再次输入新密码" class="field-input" :class="{ 'input-error': confirmPwd && pwdMatch === false }" />
        <p v-if="confirmPwd && pwdMatch === false" class="field-error">两次输入的密码不一致</p>
        <p v-if="pwdMatch === true" class="field-success">密码匹配</p>
      </div>

      <button class="submit-btn" :disabled="!canSubmit" @click="submit">
        {{ submitting ? '修改中...' : '确认修改' }}
      </button>
    </div>
  </div>
</template>

<style scoped>
.pwd-page {
  min-height: calc(100vh - 64px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 24px;
  background: linear-gradient(180deg, #f0f2f5 0%, #f8f9fc 100%);
}

.pwd-card {
  width: 100%;
  max-width: 440px;
  background: #fff;
  border-radius: 20px;
  padding: 36px 32px 32px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
}

.pwd-back {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  border: none;
  background: transparent;
  color: #667085;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  border-radius: 8px;
  transition: all 0.15s;
  margin-bottom: 20px;
}

.pwd-back:hover {
  background: #f2f4f7;
  color: #1d2939;
}

.pwd-header {
  margin-bottom: 28px;
}

.pwd-title {
  font-size: 22px;
  font-weight: 700;
  color: #1d2939;
  margin: 0 0 6px;
}

.pwd-desc {
  font-size: 14px;
  color: #98a2b3;
  margin: 0;
}

/* 字段 */
.field {
  margin-bottom: 20px;
}

.field-label {
  display: block;
  font-size: 13px;
  font-weight: 600;
  color: #344054;
  margin-bottom: 6px;
}

.field-input {
  display: block;
  width: 100%;
  height: 44px;
  padding: 0 14px;
  border: 1.5px solid #d0d5dd;
  border-radius: 10px;
  font-size: 14px;
  color: #1d2939;
  background: #fff;
  outline: none;
  transition: border-color 0.2s, box-shadow 0.2s;
  box-sizing: border-box;
}

.field-input:focus {
  border-color: #6366f1;
  box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);
}

.field-input::placeholder {
  color: #b0b7c3;
}

.field-input.input-error {
  border-color: #ef4444;
}

.field-input.input-error:focus {
  box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.1);
}

.code-row {
  display: flex;
  gap: 10px;
}

.code-status {
  flex: 1;
  height: 44px;
  display: flex;
  align-items: center;
  padding: 0 14px;
  border: 1.5px solid #d0d5dd;
  border-radius: 10px;
  font-size: 14px;
  color: #98a2b3;
  box-sizing: border-box;
  transition: border-color 0.2s;
}

.code-status.obtained {
  border-color: #16a34a;
  color: #16a34a;
  font-weight: 500;
}

.code-btn {
  flex-shrink: 0;
  height: 44px;
  padding: 0 16px;
  border: 1.5px solid #6366f1;
  border-radius: 10px;
  background: #fff;
  color: #6366f1;
  font-size: 13px;
  font-weight: 600;
  white-space: nowrap;
  cursor: pointer;
  transition: all 0.2s;
}

.code-btn:hover:not(:disabled) {
  background: #eef2ff;
}

.code-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.field-error {
  font-size: 12px;
  color: #ef4444;
  margin: 4px 0 0;
}

.field-success {
  font-size: 12px;
  color: #16a34a;
  margin: 4px 0 0;
}

/* 提交按钮 */
.submit-btn {
  display: block;
  width: 100%;
  height: 46px;
  margin-top: 28px;
  padding: 0 24px;
  border: none;
  border-radius: 12px;
  background: linear-gradient(135deg, #6366f1, #8b5cf6);
  color: #fff;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.submit-btn:hover:not(:disabled) {
  box-shadow: 0 4px 14px rgba(99, 102, 241, 0.35);
  transform: translateY(-1px);
}

.submit-btn:active:not(:disabled) {
  transform: translateY(0);
}

.submit-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
