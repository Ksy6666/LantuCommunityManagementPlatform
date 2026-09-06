<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { registerApi, uploadAvatarApi } from '../api'
import { useAuthStore } from '../stores/auth'
import { useMessage } from 'naive-ui'

const router = useRouter()
const message = useMessage()
const auth = useAuthStore()

const form = ref({
  name: '',
  nickname: '',
  phone: '',
  email: '',
  password: '',
  confirmPassword: '',
})

const loading = ref(false)
const avatarFile = ref<File | null>(null)
const avatarPreview = ref<string>('')

const emailStatus = computed(() => {
  const v = form.value.email
  if (!v) return undefined
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v) ? 'success' : 'error'
})

const phoneStatus = computed(() => {
  const v = form.value.phone
  if (!v) return undefined
  return /^1\d{10}$/.test(v) ? 'success' : 'error'
})

function stripNonDigits(value: string): string {
  return value.replace(/\D/g, '')
}

function onEmailInput(value: string) {
  form.value.email = value.replace(/\s+/g, '')
}

function handleFileChange(event: Event) {
  const input = event.target as HTMLInputElement
  const file = input.files?.[0]
  if (!file) return

  // 校验文件类型
  if (!file.type.startsWith('image/')) {
    message.warning('请选择图片文件')
    return
  }

  // 校验文件大小（最大 5MB）
  if (file.size > 5 * 1024 * 1024) {
    message.warning('图片大小不能超过 5MB')
    return
  }

  avatarFile.value = file

  // 生成预览
  const reader = new FileReader()
  reader.onload = (e) => {
    avatarPreview.value = e.target?.result as string
  }
  reader.readAsDataURL(file)
}

function triggerFileInput() {
  document.getElementById('avatar-input')?.click()
}

async function handleRegister() {
  if (!form.value.name.trim()) {
    message.warning('请填写姓名')
    return
  }
  if (!form.value.phone.trim()) {
    message.warning('请填写手机号')
    return
  }
  if (!form.value.email.trim()) {
    message.warning('请填写邮箱')
    return
  }
  if (!/^1\d{10}$/.test(form.value.phone.trim())) {
    message.warning('请输入正确的手机号')
    return
  }
  if (!form.value.password) {
    message.warning('请填写密码')
    return
  }
  if (form.value.password.length < 6) {
    message.warning('密码不能少于6位')
    return
  }
  if (form.value.password !== form.value.confirmPassword) {
    message.warning('两次密码不一致')
    return
  }
  loading.value = true
  try {
    const res = await registerApi({
      name: form.value.name.trim(),
      nickname: form.value.nickname.trim() || undefined,
      phone: form.value.phone.trim(),
      email: form.value.email.trim(),
      password: form.value.password,
    })
    if (res.data.code === 200) {
      const userData = res.data.data
      auth.login(userData)

      // 如果有选头像，直接用注册返回的 token 上传
      if (avatarFile.value) {
        try {
          const avatarRes = await uploadAvatarApi(avatarFile.value!, userData.token)
          // 上传成功后更新 store 中的 avatarUrl
          if (avatarRes.data.code === 200 && avatarRes.data.data?.avatarUrl) {
            userData.avatarUrl = avatarRes.data.data.avatarUrl
            auth.login(userData)
          }
        } catch (err: any) {
          const msg = err?.response?.data?.message || '头像上传失败，可稍后重试'
          message.warning(msg)
        }
      }

      message.success('注册成功')
      // 跳转到生日填写页
      router.push('/birthday')
    }
  } catch (err: any) {
    const msg = err?.response?.data?.message || '注册失败，请检查网络'
    message.error(msg)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="register-page">
    <!-- 背景装饰 -->
    <div class="register-bg">
      <div class="bg-shape s1"></div>
      <div class="bg-shape s2"></div>
      <div class="bg-shape s3"></div>
    </div>

    <div class="register-card">
      <!-- 头部 -->
      <div class="rc-header">
        <div class="rc-logo" @click="router.push('/')">
          <img src="/favicon.png" alt="Open_Lantu" />
        </div>
        <h1>创建账号</h1>
        <p>加入 Open_Lantu，开启技术之旅</p>
      </div>

      <!-- 头像上传 -->
      <div class="avatar-section">
        <div class="avatar-uploader" @click="triggerFileInput">
          <div v-if="avatarPreview" class="avatar-preview">
            <img :src="avatarPreview" alt="头像预览" />
            <div class="avatar-overlay">
              <n-icon size="20"><Camera /></n-icon>
              <span>更换头像</span>
            </div>
          </div>
          <div v-else class="avatar-placeholder">
            <n-icon size="32"><CameraAdd /></n-icon>
            <span>点击选择头像</span>
          </div>
        </div>
        <input
          id="avatar-input"
          type="file"
          accept="image/*"
          style="display: none"
          @change="handleFileChange"
        />
      </div>

      <!-- 表单 -->
      <n-form :model="form" class="rc-form">
        <n-form-item>
          <n-input
            v-model:value="form.name"
            placeholder="姓名"
            size="large"
            clearable
          >
            <template #prefix>
              <n-icon><UserFilled /></n-icon>
            </template>
          </n-input>
        </n-form-item>

        <n-form-item>
          <n-input
            v-model:value="form.nickname"
            placeholder="昵称（选填，不填将自动生成）"
            size="large"
            clearable
          >
            <template #prefix>
              <n-icon><Stamp /></n-icon>
            </template>
          </n-input>
        </n-form-item>

        <n-form-item :validation-status="phoneStatus">
          <n-input
            :value="form.phone"
            @update:value="(v: string) => form.phone = stripNonDigits(v)"
            placeholder="手机号"
            size="large"
            clearable
            :maxlength="11"
            :input-props="{ type: 'tel', inputmode: 'numeric' }"
            :status="phoneStatus"
          >
            <template #prefix>
              <n-icon><Iphone /></n-icon>
            </template>
          </n-input>
          <template #feedback>
            <span v-if="phoneStatus === 'error'" style="font-size: 12px;">请输入11位手机号</span>
          </template>
        </n-form-item>

        <n-form-item :validation-status="emailStatus">
          <n-input
            :value="form.email"
            @update:value="onEmailInput"
            placeholder="邮箱"
            size="large"
            clearable
            :input-props="{ type: 'email' }"
            :status="emailStatus"
          >
            <template #prefix>
              <n-icon><Message /></n-icon>
            </template>
          </n-input>
          <template #feedback>
            <span v-if="emailStatus === 'error'" style="font-size: 12px;">邮箱格式不正确</span>
          </template>
        </n-form-item>

        <n-form-item>
          <n-input
            v-model:value="form.password"
            type="password"
            placeholder="密码（至少6位）"
            size="large"
            show-password-on="click"
            clearable
          >
            <template #prefix>
              <n-icon><Lock /></n-icon>
            </template>
          </n-input>
        </n-form-item>

        <n-form-item>
          <n-input
            v-model:value="form.confirmPassword"
            type="password"
            placeholder="确认密码"
            size="large"
            show-password-on="click"
            clearable
          >
            <template #prefix>
              <n-icon><Lock /></n-icon>
            </template>
          </n-input>
        </n-form-item>

        <n-button
          type="primary"
          size="large"
          round
          block
          :loading="loading"
          @click="handleRegister"
        >
          注册
        </n-button>
      </n-form>

      <!-- 登录引导 -->
      <div class="rc-footer">
        已有账号？
        <n-button text @click="router.push('/login')" class="rc-login-link">
          立即登录
        </n-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.register-page {
  min-height: calc(100vh - 64px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 24px;
  position: relative;
  overflow: hidden;
}

.register-bg {
  position: absolute;
  inset: 0;
  background: linear-gradient(160deg, #ecfdf5 0%, #e8f4ff 30%, #f0f0ff 70%, #fdf6ec 100%);
  z-index: 0;
}

.bg-shape {
  position: absolute;
  border-radius: 50%;
  opacity: 0.2;
  animation: float 8s ease-in-out infinite;
}

.s1 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(54, 173, 106, 0.12), transparent);
  top: -120px;
  left: -80px;
  animation-delay: 0s;
}

.s2 {
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, rgba(99, 102, 241, 0.1), transparent);
  bottom: -80px;
  right: -60px;
  animation-delay: -3s;
}

.s3 {
  width: 200px;
  height: 200px;
  background: radial-gradient(circle, rgba(32, 128, 240, 0.08), transparent);
  top: 30%;
  right: 20%;
  animation-delay: -6s;
}

@keyframes float {
  0%, 100% { transform: translateY(0) scale(1); }
  50% { transform: translateY(-20px) scale(1.03); }
}

.register-card {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 420px;
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border-radius: 20px;
  padding: 32px 36px 36px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.5);
}

.rc-header {
  text-align: center;
  margin-bottom: 20px;
}

.rc-logo {
  cursor: pointer;
  margin-bottom: 12px;
}

.rc-logo img {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  object-fit: cover;
  transition: transform 0.3s;
}

.rc-logo img:hover {
  transform: rotate(-6deg) scale(1.05);
}

.rc-header h1 {
  font-size: 24px;
  font-weight: 700;
  color: #1d1e1f;
  margin: 0 0 4px;
}

.rc-header p {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

/* ── 头像上传 ── */
.avatar-section {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.avatar-uploader {
  cursor: pointer;
  position: relative;
  border-radius: 50%;
  overflow: hidden;
  transition: transform 0.2s;
}

.avatar-uploader:hover {
  transform: scale(1.05);
}

.avatar-preview {
  width: 88px;
  height: 88px;
  border-radius: 50%;
  overflow: hidden;
  position: relative;
  border: 3px solid #e0e0e0;
}

.avatar-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-overlay {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.45);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 11px;
  gap: 2px;
  opacity: 0;
  transition: opacity 0.2s;
}

.avatar-preview:hover .avatar-overlay {
  opacity: 1;
}

.avatar-placeholder {
  width: 88px;
  height: 88px;
  border-radius: 50%;
  border: 2px dashed #c0c4cc;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #909399;
  font-size: 12px;
  gap: 4px;
  background: #f5f7fa;
  transition: border-color 0.2s, color 0.2s;
}

.avatar-placeholder:hover {
  border-color: #2080f0;
  color: #2080f0;
}

/* ── 表单 ── */
.rc-form {
  margin-bottom: 20px;
}

.rc-form .n-form-item {
  margin-bottom: 4px;
}

.rc-footer {
  text-align: center;
  font-size: 14px;
  color: #909399;
}

.rc-login-link {
  font-size: 14px;
}

@media (max-width: 480px) {
  .register-card {
    padding: 24px 20px 28px;
    border-radius: 16px;
  }
}
</style>
