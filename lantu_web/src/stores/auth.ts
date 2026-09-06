import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { UserInfo } from '../api'

interface UserWithToken extends UserInfo {
  token: string
}

export const useAuthStore = defineStore('auth', () => {
  // 从 sessionStorage 恢复登录状态（关闭标签页即退出）
  const raw = sessionStorage.getItem('user')
  const saved: UserWithToken | null = raw ? JSON.parse(raw) : null

  const user = ref<UserWithToken | null>(saved)

  const isLoggedIn = computed(() => user.value !== null)

  const nickname = computed(() => user.value?.nickname ?? '')
  const name = computed(() => user.value?.name ?? '')
  const phone = computed(() => user.value?.phone ?? '')
  const email = computed(() => user.value?.email ?? '')
  const birthday = computed(() => user.value?.birthday ?? '')
  const avatarUrl = computed(() => user.value?.avatarUrl ?? '')
  const roleName = computed(() => user.value?.roleName ?? '普通用户')
  const roleId = computed(() => user.value?.roleId ?? 4)

  function login(userData: UserWithToken) {
    user.value = userData
    sessionStorage.setItem('user', JSON.stringify(userData))
    sessionStorage.setItem('token', userData.token)
  }

  function updateUser(data: Partial<UserWithToken>) {
    if (user.value) {
      user.value = { ...user.value, ...data }
      sessionStorage.setItem('user', JSON.stringify(user.value))
    }
  }

  function logout() {
    user.value = null
    sessionStorage.removeItem('user')
    sessionStorage.removeItem('token')
  }

  return {
    user,
    isLoggedIn,
    nickname,
    name,
    phone,
    email,
    birthday,
    avatarUrl,
    roleName,
    roleId,
    login,
    logout,
    updateUser,
  }
})
