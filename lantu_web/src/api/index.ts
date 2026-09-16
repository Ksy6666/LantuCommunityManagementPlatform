import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
})

export interface LoginRequest {
  account: string
  password: string
}

export interface RegisterRequest {
  name: string
  nickname?: string
  phone: string
  password: string
  email: string
}

export interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

export interface UserInfo {
  id: number
  account: string
  nickname: string
  name?: string
  phone?: string
  email?: string
  birthday?: string
  avatarUrl?: string
  roleId?: number
  roleName?: string
}

export interface PreregisterRequest {
  name: string
  major: string
  grade: string
  intro?: string
}

/** 登录 */
export function loginApi(data: LoginRequest) {
  return api.post<ApiResponse<UserInfo & { token: string }>>('/auth/login', data)
}

/** 注册 */
export function registerApi(data: RegisterRequest) {
  return api.post<ApiResponse<UserInfo & { token: string }>>('/auth/register', data)
}

/** 预报名 */
export function preregisterApi(data: PreregisterRequest) {
  return api.post<ApiResponse<Record<string, unknown>>>('/preregister', data)
}

/** 设置生日 */
export function updateBirthdayApi(birthday: string) {
  const token = sessionStorage.getItem('token')
  return api.put<ApiResponse<Record<string, unknown>>>(
    '/auth/birthday',
    { birthday },
    { headers: { Authorization: `Bearer ${token}` } },
  )
}

/** 更新个人信息 */
export function updateProfileApi(data: {
  name?: string
  nickname?: string
  phone?: string
  email?: string
  birthday?: string
}) {
  const token = sessionStorage.getItem('token')
  return api.put<ApiResponse<UserInfo>>('/auth/profile', data, {
    headers: { Authorization: `Bearer ${token}` },
  })
}

/** 通过原密码修改密码 */
export function changePasswordOldApi(data: {
  oldPassword: string
  newPassword: string
  confirmPassword: string
}) {
  const token = sessionStorage.getItem('token')
  return api.put<ApiResponse<Record<string, unknown>>>('/auth/password/old', data, {
    headers: { Authorization: `Bearer ${token}` },
  })
}

/** 发送邮箱验证码（模拟） */
export function sendEmailCodeApi(email: string) {
  return api.post<ApiResponse<Record<string, unknown>>>('/auth/password/email/code', { email })
}

/** 通过邮箱验证码修改密码 */
export function changePasswordEmailApi(data: {
  email: string
  code: string
  newPassword: string
  confirmPassword: string
}) {
  return api.put<ApiResponse<Record<string, unknown>>>('/auth/password/email', data)
}

/** 上传头像 */
export function uploadAvatarApi(file: File, token?: string) {
  const authToken = token || sessionStorage.getItem('token')
  const formData = new FormData()
  formData.append('file', file)
  return api.post<ApiResponse<{ avatarUrl: string }>>('/avatar/upload', formData, {
    headers: {
      Authorization: `Bearer ${authToken}`,
      'Content-Type': null, // 覆盖实例默认的 application/json，让 axios 自动检测 FormData → multipart/form-data
    },
  })
}

/** ══════════════════════════════════════
 *  签到系统 API
 *  ══════════════════════════════════════ */

export interface CheckInStatus {
  totalPoints: number
  consecutiveDays: number
  monthlyCount: number
  checkedInToday: boolean
  checkInDates: string[]
  yearMonth: string
}

export interface CheckInResult {
  points: number
  totalPoints: number
  consecutiveDays: number
  streakBonus: number
  checkInDate: string
}

/** 签到 */
export function doCheckInApi() {
  const token = sessionStorage.getItem('token')
  return api.post<ApiResponse<CheckInResult>>('/checkin', {}, {
    headers: { Authorization: `Bearer ${token}` },
  })
}

/** 获取签到状态 */
export function getCheckInStatusApi() {
  const token = sessionStorage.getItem('token')
  return api.get<ApiResponse<CheckInStatus>>('/checkin/status', {
    headers: { Authorization: `Bearer ${token}` },
  })
}

/** 获取指定月份签到记录 */
export function getCheckInMonthApi(yearMonth: string) {
  const token = sessionStorage.getItem('token')
  return api.get<ApiResponse<{ checkInDates: string[]; yearMonth: string; count: number }>>('/checkin/month', {
    params: { yearMonth },
    headers: { Authorization: `Bearer ${token}` },
  })
}

/** ══════════════════════════════════════
 *  排行榜 API
 *  ══════════════════════════════════════ */

export interface RankingEntry {
  rank: number
  userId: number
  nickname: string
  avatarUrl: string | null
  totalPoints: number
  roleName?: string | null
}

/** 获取积分排行榜 */
export function getRankingApi(topN = 20) {
  return api.get<ApiResponse<RankingEntry[]>>('/checkin/ranking', {
    params: { topN },
  })
}

/** 获取我的排名 */
export function getMyRankingApi() {
  const token = sessionStorage.getItem('token')
  return api.get<ApiResponse<RankingEntry>>('/checkin/ranking/me', {
    headers: { Authorization: `Bearer ${token}` },
  })
}

/** ══════════════════════════════════════
 *  管理后台 API
 *  ══════════════════════════════════════ */

export interface DashboardData {
  totalUsers: number
  todayCheckins: number
  monthCheckins: number
}

export interface AdminUserItem {
  id: number
  account: string
  name: string
  nickname: string
  phone: string
  email: string
  birthday: string
  roleId: number
  roleName: string
  createdAt: string
}

interface PaginatedUsers {
  list: AdminUserItem[]
  total: number
  page: number
  size: number
}

/** 获取管理员仪表盘 */
export function getDashboardApi() {
  const token = sessionStorage.getItem('token')
  return api.get<ApiResponse<DashboardData>>('/admin/dashboard', {
    headers: { Authorization: `Bearer ${token}` },
  })
}

/** 获取用户列表（分页+搜索） */
export function getAdminUsersApi(page = 1, size = 20, keyword = '') {
  const token = sessionStorage.getItem('token')
  return api.get<ApiResponse<PaginatedUsers>>('/admin/users', {
    params: { page, size, keyword },
    headers: { Authorization: `Bearer ${token}` },
  })
}

/** 获取用户详情 */
export function getAdminUserDetailApi(id: number) {
  const token = sessionStorage.getItem('token')
  return api.get<ApiResponse<AdminUserItem>>(`/admin/users/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  })
}

/** 切换用户角色 */
export function updateUserRoleApi(userId: number, roleId: number) {
  const token = sessionStorage.getItem('token')
  return api.put<ApiResponse<{ roleName: string }>>(
    `/admin/users/${userId}/role`,
    { roleId },
    { headers: { Authorization: `Bearer ${token}` } },
  )
}

/** 删除用户 */
export function deleteUserApi(userId: number) {
  const token = sessionStorage.getItem('token')
  return api.delete<ApiResponse<Record<string, unknown>>>(`/admin/users/${userId}`, {
    headers: { Authorization: `Bearer ${token}` },
  })
}

/** ══════════════════════════════════════
 *  技术讨论 API
 *  ══════════════════════════════════════ */

export interface DiscussionPost {
  id: number
  userId: number
  title: string
  content: string
  category: string
  tags: string | null
  viewCount: number
  likeCount: number
  favoriteCount: number
  commentCount: number
  status: number
  createdAt: string
  updatedAt: string
  authorNickname?: string
  authorAvatar?: string | null
}

export interface DiscussionComment {
  id: number
  postId: number
  userId: number
  parentId: number
  content: string
  likeCount: number
  status: number
  createdAt: string
  authorNickname?: string
  authorAvatar?: string | null
}

export interface DiscussionPageData {
  list: DiscussionPost[]
  total: number
  page: number
  size: number
}

/** 发布帖子（需登录） */
export function publishPostApi(data: {
  title: string
  content: string
  category?: string
  tags?: string
}) {
  const token = sessionStorage.getItem('token')
  return api.post<ApiResponse<DiscussionPost>>('/discussion/posts', data, {
    headers: { Authorization: `Bearer ${token}` },
  })
}

/** 帖子列表（分页+分类+关键词） */
export function getDiscussionPostsApi(
  page = 1,
  size = 10,
  category = '',
  keyword = '',
) {
  return api.get<ApiResponse<DiscussionPageData>>('/discussion/posts', {
    params: { page, size, category, keyword },
  })
}

/** 帖子详情（浏览量+1） */
export function getDiscussionPostApi(id: number) {
  return api.get<ApiResponse<DiscussionPost>>(`/discussion/posts/${id}`)
}

/** 帖子评论列表 */
export function getDiscussionCommentsApi(postId: number) {
  return api.get<ApiResponse<DiscussionComment[]>>(`/discussion/posts/${postId}/comments`)
}

/** 发表评论/回答（需登录） */
export function addDiscussionCommentApi(postId: number, content: string, parentId?: number) {
  const token = sessionStorage.getItem('token')
  return api.post<ApiResponse<DiscussionComment>>(
    `/discussion/posts/${postId}/comments`,
    { content, parentId: parentId || 0 },
    { headers: { Authorization: `Bearer ${token}` } },
  )
}

/** 点赞/取消点赞帖子（需登录） */
export function togglePostLikeApi(postId: number) {
  const token = sessionStorage.getItem('token')
  return api.post<ApiResponse<{ liked: boolean; likeCount: number }>>(
    `/discussion/posts/${postId}/like`,
    {},
    { headers: { Authorization: `Bearer ${token}` } },
  )
}

/** 点赞/取消点赞评论（需登录） */
export function toggleCommentLikeApi(commentId: number) {
  const token = sessionStorage.getItem('token')
  return api.post<ApiResponse<{ likeCount: number }>>(
    `/discussion/comments/${commentId}/like`,
    {},
    { headers: { Authorization: `Bearer ${token}` } },
  )
}

/** 收藏/取消收藏帖子（需登录） */
export function togglePostFavoriteApi(postId: number) {
  const token = sessionStorage.getItem('token')
  return api.post<ApiResponse<{ favorited: boolean; favoriteCount: number }>>(
    `/discussion/posts/${postId}/favorite`,
    {},
    { headers: { Authorization: `Bearer ${token}` } },
  )
}

/** 我发布的帖子（需登录） */
export function getMyPostsApi() {
  const token = sessionStorage.getItem('token')
  return api.get<ApiResponse<DiscussionPost[]>>('/discussion/my/posts', {
    headers: { Authorization: `Bearer ${token}` },
  })
}

/** 我收藏的帖子（需登录） */
export function getMyFavoritesApi() {
  const token = sessionStorage.getItem('token')
  return api.get<ApiResponse<DiscussionPost[]>>('/discussion/my/favorites', {
    headers: { Authorization: `Bearer ${token}` },
  })
}

/** 热点帖子 TopN */
export function getHotPostsApi(limit = 10) {
  return api.get<ApiResponse<DiscussionPost[]>>('/discussion/hot', {
    params: { limit },
  })
}

export default api
