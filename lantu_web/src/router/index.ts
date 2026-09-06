import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '../views/HomePage.vue'
import { useAuthStore } from '../stores/auth'

const router = createRouter({
  history: createWebHistory(),
  scrollBehavior() {
    return { top: 0, behavior: 'smooth' }
  },
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomePage,
      meta: { title: '首页' },
    },
    {
      path: '/activities',
      name: 'activities',
      component: () => import('../views/ActivitiesPage.vue'),
      meta: { title: '活动' },
    },
    {
      path: '/projects',
      name: 'projects',
      component: () => import('../views/ProjectsPage.vue'),
      meta: { title: '项目' },
    },
    {
      path: '/join',
      name: 'join',
      component: () => import('../views/JoinPage.vue'),
      meta: { title: '加入我们' },
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutPage.vue'),
      meta: { title: '关于我们' },
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginPage.vue'),
      meta: { title: '登录' },
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/RegisterPage.vue'),
      meta: { title: '注册' },
    },
    {
      path: '/birthday',
      name: 'birthday',
      component: () => import('../views/BirthdayPage.vue'),
      meta: { title: '设置生日', requiresAuth: true },
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('../views/ProfilePage.vue'),
      meta: { title: '个人中心', requiresAuth: true },
    },
    {
      path: '/profile/edit',
      name: 'profile-edit',
      component: () => import('../views/ProfileEditPage.vue'),
      meta: { title: '修改个人信息', requiresAuth: true },
    },
    {
      path: '/register-complete',
      name: 'register-complete',
      component: () => import('../views/RegistrationComplete.vue'),
      meta: { title: '注册成功', requiresAuth: true },
    },
    {
      path: '/change-pwd',
      name: 'change-pwd',
      component: () => import('../views/ChangePwdPage.vue'),
      meta: { title: '修改密码', requiresAuth: true },
    },
    {
      path: '/change-pwd/email',
      name: 'change-pwd-email',
      component: () => import('../views/ChangePwdPhone.vue'),
      meta: { title: '通过邮箱验证修改密码', requiresAuth: true },
    },
    {
      path: '/change-pwd/old',
      name: 'change-pwd-old',
      component: () => import('../views/ChangePwdOld.vue'),
      meta: { title: '通过原密码修改密码', requiresAuth: true },
    },
    {
      path: '/checkin',
      name: 'checkin',
      component: () => import('../views/CheckinPage.vue'),
      meta: { title: '每日签到', requiresAuth: true },
    },
    {
      path: '/admin',
      name: 'admin',
      component: () => import('../views/AdminPage.vue'),
      meta: { title: '管理后台', requiresAuth: true },
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: () => import('../views/NotFoundPage.vue'),
      meta: { title: '页面未找到' },
    },
  ],
})

router.beforeEach((to, _from, next) => {
  // 更新页面标题
  const title = to.meta.title as string
  if (title) {
    document.title = `${title} - Open_Lantu`
  }

  // 游客模式：需要登录的页面但未登录 → 跳转登录
  if (to.meta.requiresAuth) {
    const auth = useAuthStore()
    if (!auth.isLoggedIn) {
      next({ name: 'login', query: { redirect: to.fullPath } })
      return
    }
  }

  next()
})

export default router
