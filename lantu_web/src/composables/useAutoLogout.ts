import { onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const IDLE_TIMEOUT = 60_000 * 10 // 10 分钟无操作视为超时
const CHECK_INTERVAL = 5_000 // 每 5 秒检查一次

export function useAutoLogout() {
  const router = useRouter()
  const auth = useAuthStore()

  let lastActivity = Date.now()
  let checkTimer: ReturnType<typeof setInterval> | null = null

  const events = ['mousedown', 'keydown', 'touchstart', 'scroll', 'wheel'] as const

  function onActivity() {
    lastActivity = Date.now()
  }

  function startMonitoring() {
    if (!auth.isLoggedIn) return

    // 记录初始时间
    lastActivity = Date.now()

    // 监听用户活动事件
    events.forEach((evt) => window.addEventListener(evt, onActivity, { passive: true }))

    // 定时检查是否超时
    checkTimer = setInterval(() => {
      // 如果用户已登出就不再检查
      if (!auth.isLoggedIn) {
        stopMonitoring()
        return
      }

      if (Date.now() - lastActivity >= IDLE_TIMEOUT) {
        auth.logout()
        router.push('/')
      }
    }, CHECK_INTERVAL)
  }

  function stopMonitoring() {
    events.forEach((evt) => window.removeEventListener(evt, onActivity))
    if (checkTimer) {
      clearInterval(checkTimer)
      checkTimer = null
    }
  }

  onMounted(() => {
    startMonitoring()
  })

  onUnmounted(() => {
    stopMonitoring()
  })
}
