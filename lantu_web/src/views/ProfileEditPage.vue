<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage } from 'naive-ui'
import { useAuthStore } from '../stores/auth'
import { updateProfileApi, uploadAvatarApi } from '../api'

const router = useRouter()
const message = useMessage()
const auth = useAuthStore()

const fields = [
  { label: '姓名', key: 'name', type: 'text', placeholder: '请输入姓名' },
  { label: '昵称', key: 'nickname', type: 'text', placeholder: '请输入昵称' },
  { label: '手机号', key: 'phone', type: 'tel', placeholder: '请输入手机号', maxlength: 11 },
  { label: '邮箱', key: 'email', type: 'email', placeholder: '请输入邮箱' },
  { label: '生日', key: 'birthday', type: 'date', placeholder: '' },
] as const

const form = reactive<Record<string, string>>(
  Object.fromEntries(fields.map(f => [f.key, '']))
)

const fieldErrors = reactive<Record<string, string>>({})

const loading = ref(false)
const uploadLoading = ref(false)
const fileInputRef = ref<HTMLInputElement>()
const previewUrl = ref('')

onMounted(() => {
  previewUrl.value = auth.avatarUrl
  for (const f of fields) {
    form[f.key] = (auth as any)[f.key] ?? ''
  }
})

async function handleSave() {
  if (!form.name) {
    message.warning('姓名不能为空')
    return
  }
  if (form.phone && !/^1\d{10}$/.test(form.phone)) {
    message.warning('请输入正确的11位手机号')
    return
  }
  if (form.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) {
    message.warning('请输入正确的邮箱地址')
    return
  }
  loading.value = true
  try {
    const payload: Record<string, string | undefined> = {}
    for (const f of fields) {
      payload[f.key] = form[f.key] || undefined
    }
    const res = await updateProfileApi(payload as any)
    if (res.data.code === 200) {
      auth.updateUser(res.data.data)
      message.success('个人信息更新成功')
      router.push('/profile')
    }
  } catch (err: any) {
    const msg = err?.response?.data?.message || '更新失败'
    message.error(msg)
  } finally {
    loading.value = false
  }
}

function onFieldInput(e: Event, key: string) {
  const input = e.target as HTMLInputElement
  let val = input.value
  if (key === 'phone') {
    val = val.replace(/\D/g, '')
  } else if (key === 'email') {
    val = val.replace(/\s+/g, '')
  }
  form[key] = val
  // 实时校验反馈
  if (key === 'phone') {
    fieldErrors.phone = val && !/^1\d{10}$/.test(val) ? '请输入正确的11位手机号' : ''
  } else if (key === 'email') {
    fieldErrors.email = val && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(val) ? '邮箱格式不正确' : ''
  } else {
    fieldErrors[key] = ''
  }
}

function goBack() {
  router.push('/profile')
}

function triggerFileInput() {
  fileInputRef.value?.click()
}

async function handleFileChange(e: Event) {
  const input = e.target as HTMLInputElement
  const file = input.files?.[0]
  if (!file) return

  // 本地预览
  previewUrl.value = URL.createObjectURL(file)

  uploadLoading.value = true
  try {
    const res = await uploadAvatarApi(file)
    if (res.data.code === 200) {
      auth.updateUser({ avatarUrl: res.data.data.avatarUrl })
      message.success('头像已更新')
    }
  } catch (err: any) {
    // 上传失败，恢复为原头像
    previewUrl.value = auth.avatarUrl
    const msg = err?.response?.data?.message || '头像上传失败'
    message.error(msg)
  } finally {
    uploadLoading.value = false
    input.value = '' // 清空 input，允许重复选择同一文件
  }
}
</script>

<template>
  <div class="edit-page">
    <div class="edit-card">
      <button class="edit-back" @click="goBack">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 12H5"/><polyline points="12 19 5 12 12 5"/></svg>
        返回
      </button>

      <div class="edit-header">
        <h1 class="edit-title">修改个人信息</h1>
        <p class="edit-desc">修改后点击保存即可更新</p>
      </div>

      <!-- 头像 -->
      <div class="avatar-section">
        <div class="avatar-preview" :class="{ uploading: uploadLoading }" @click="triggerFileInput">
          <img v-if="previewUrl" :src="previewUrl" class="avatar-img" />
          <span v-else class="avatar-fallback">{{ auth.nickname?.charAt(0) || '?' }}</span>
          <div class="avatar-overlay">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"/><circle cx="12" cy="13" r="4"/></svg>
            <span>{{ uploadLoading ? '上传中...' : '更换头像' }}</span>
          </div>
        </div>
      </div>
      <input ref="fileInputRef" type="file" accept="image/*" hidden @change="handleFileChange" />

      <div v-for="f in fields" :key="f.key" class="field">
        <label class="field-label">{{ f.label }}</label>
        <input
          :value="form[f.key]"
          @input="onFieldInput($event, f.key)"
          :type="f.type"
          :placeholder="f.placeholder"
          class="field-input"
          :class="{ 'field-input-error': fieldErrors[f.key] }"
          :maxlength="(f as any).maxlength"
          :inputmode="f.key === 'phone' ? 'numeric' : undefined"
        />
        <span v-if="fieldErrors[f.key]" class="field-error">{{ fieldErrors[f.key] }}</span>
      </div>

      <button class="save-btn" :disabled="loading" @click="handleSave">
        {{ loading ? '保存中...' : '保存修改' }}
      </button>
    </div>
  </div>
</template>

<style scoped>
.edit-page {
  min-height: calc(100vh - 64px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 24px;
  background: linear-gradient(180deg, #f0f2f5 0%, #f8f9fc 100%);
}

.edit-card {
  width: 100%;
  max-width: 440px;
  background: #fff;
  border-radius: 20px;
  padding: 36px 32px 32px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
}

.edit-back {
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

.edit-back:hover {
  background: #f2f4f7;
  color: #1d2939;
}

.edit-header {
  margin-bottom: 28px;
}

.edit-title {
  font-size: 22px;
  font-weight: 700;
  color: #1d2939;
  margin: 0 0 6px;
}

.edit-desc {
  font-size: 14px;
  color: #98a2b3;
  margin: 0;
}

/* 头像 */
.avatar-section {
  display: flex;
  justify-content: center;
  margin-bottom: 24px;
}

.avatar-preview {
  position: relative;
  width: 96px;
  height: 96px;
  border-radius: 50%;
  background: #f0f2f5;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  cursor: pointer;
  transition: box-shadow 0.2s;
}

.avatar-preview:hover {
  box-shadow: 0 0 0 4px rgba(99, 102, 241, 0.2);
}

.avatar-preview.uploading {
  opacity: 0.6;
  pointer-events: none;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-fallback {
  font-size: 36px;
  font-weight: 700;
  color: #98a2b3;
  font-family: 'Inter', system-ui, sans-serif;
}

.avatar-overlay {
  position: absolute;
  inset: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 2px;
  background: rgba(0, 0, 0, 0.45);
  color: #fff;
  font-size: 11px;
  font-weight: 500;
  opacity: 0;
  transition: opacity 0.2s;
}

.avatar-preview:hover .avatar-overlay {
  opacity: 1;
}

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

.field-input.field-input-error {
  border-color: #f56c6c;
}

.field-input.field-input-error:focus {
  border-color: #f56c6c;
  box-shadow: 0 0 0 3px rgba(245, 108, 108, 0.1);
}

.field-error {
  display: block;
  font-size: 12px;
  color: #f56c6c;
  margin-top: 4px;
  line-height: 1.2;
}

.field-input::placeholder {
  color: #b0b7c3;
}

.save-btn {
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

.save-btn:hover:not(:disabled) {
  box-shadow: 0 4px 14px rgba(99, 102, 241, 0.35);
  transform: translateY(-1px);
}

.save-btn:active:not(:disabled) {
  transform: translateY(0);
}

.save-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
