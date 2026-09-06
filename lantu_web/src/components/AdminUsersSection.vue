<script setup lang="ts">
import { ref, onMounted, h } from 'vue'
import { useMessage } from 'naive-ui'
import {
  getAdminUsersApi,
  updateUserRoleApi,
  deleteUserApi,
} from '../api'
import type { AdminUserItem } from '../api'

const message = useMessage()
const users = ref<AdminUserItem[]>([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(20)
const keyword = ref('')
const loading = ref(false)

const roleOptions = [
  { label: '管理员', value: 1 },
  { label: '教师', value: 2 },
  { label: '学生', value: 3 },
  { label: '普通用户', value: 4 },
]

function loadUsers() {
  loading.value = true
  getAdminUsersApi(page.value, pageSize.value, keyword.value)
    .then((res) => {
      if (res.data.code === 200) {
        users.value = res.data.data.list
        total.value = res.data.data.total
      } else {
        message.warning(res.data.message)
      }
    })
    .catch((err: any) => {
      message.error(err?.response?.data?.message || '加载用户列表失败')
    })
    .finally(() => {
      loading.value = false
    })
}

function onSearch() {
  page.value = 1
  loadUsers()
}

function onPageChange(p: number) {
  page.value = p
  loadUsers()
}

function onChangeRole(userId: number, roleId: number) {
  updateUserRoleApi(userId, roleId)
    .then((res) => {
      if (res.data.code === 200) {
        message.success('角色已更新')
        loadUsers()
      } else {
        message.warning(res.data.message)
      }
    })
    .catch((err: any) => {
      message.error(err?.response?.data?.message || '角色更新失败')
    })
}

function onDelete(userId: number, nickname: string) {
  if (!confirm(`确定要删除用户「${nickname}」吗？此操作不可恢复。`)) return
  deleteUserApi(userId)
    .then((res) => {
      if (res.data.code === 200) {
        message.success('用户已删除')
        loadUsers()
      } else {
        message.warning(res.data.message)
      }
    })
    .catch((err: any) => {
      message.error(err?.response?.data?.message || '删除失败')
    })
}

onMounted(loadUsers)
</script>

<template>
  <div class="users-section">
    <div class="users-toolbar">
      <input
        v-model="keyword"
        class="search-input"
        placeholder="搜索账号/昵称/姓名/手机号"
        @keyup.enter="onSearch"
      />
      <button class="search-btn" @click="onSearch">搜索</button>
      <span class="total-info">共 {{ total }} 条</span>
    </div>

    <div class="users-table-wrapper">
      <div v-if="loading" class="loading">加载中...</div>
      <table v-else class="users-table">
        <thead>
          <tr>
            <th style="width:70px">ID</th>
            <th style="width:130px">账号</th>
            <th style="width:100px">姓名</th>
            <th style="width:120px">昵称</th>
            <th style="width:130px">手机号</th>
            <th style="min-width:160px">邮箱</th>
            <th style="width:120px">角色</th>
            <th style="width:170px">创建时间</th>
            <th style="width:80px">操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in users" :key="row.id">
            <td>{{ row.id }}</td>
            <td>{{ row.account }}</td>
            <td>{{ row.name || '-' }}</td>
            <td>{{ row.nickname || '-' }}</td>
            <td>{{ row.phone }}</td>
            <td>{{ row.email || '-' }}</td>
            <td>
              <select
                class="role-select"
                :value="row.roleId"
                @change="onChangeRole(row.id, Number(($event.target as HTMLSelectElement).value))"
              >
                <option
                  v-for="opt in roleOptions"
                  :key="opt.value"
                  :value="opt.value"
                  :selected="row.roleId === opt.value"
                >
                  {{ opt.label }}
                </option>
              </select>
            </td>
            <td>{{ row.createdAt || '-' }}</td>
            <td>
              <button class="delete-btn" @click="onDelete(row.id, row.nickname || row.name)">
                删除
              </button>
            </td>
          </tr>
          <tr v-if="users.length === 0">
            <td colspan="9" class="empty-row">暂无数据</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="users-pagination">
      <button
        class="page-btn"
        :disabled="page <= 1"
        @click="onPageChange(page - 1)"
      >
        上一页
      </button>
      <span class="page-info">{{ page }} / {{ Math.ceil(total / pageSize) || 1 }}</span>
      <button
        class="page-btn"
        :disabled="page >= Math.ceil(total / pageSize)"
        @click="onPageChange(page + 1)"
      >
        下一页
      </button>
    </div>
  </div>
</template>

<style scoped>
.users-section {
  max-width: 1200px;
  margin: 0 auto;
}

.users-toolbar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.search-input {
  flex: 1;
  max-width: 320px;
  height: 36px;
  padding: 0 12px;
  border: 1px solid #dcdfe6;
  border-radius: 6px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.2s;
}

.search-input:focus {
  border-color: #409eff;
}

.search-btn {
  height: 36px;
  padding: 0 20px;
  background: #409eff;
  color: #fff;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
}

.search-btn:hover {
  background: #337ecc;
}

.total-info {
  margin-left: auto;
  font-size: 14px;
  color: #909399;
}

.users-table-wrapper {
  overflow-x: auto;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.04);
}

.users-table {
  width: 100%;
  border-collapse: collapse;
}

.users-table th {
  background: #f5f7fa;
  padding: 12px 16px;
  text-align: left;
  font-size: 13px;
  font-weight: 600;
  color: #606266;
  white-space: nowrap;
  border-bottom: 1px solid #e8eaed;
}

.users-table td {
  padding: 12px 16px;
  font-size: 14px;
  color: #1d1e1f;
  border-bottom: 1px solid #f0f0f0;
}

.users-table tr:hover td {
  background: #f5f7fa;
}

.empty-row {
  text-align: center;
  color: #c0c4cc;
  padding: 40px 16px !important;
}

.role-select {
  padding: 4px 8px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 13px;
  outline: none;
  cursor: pointer;
}

.role-select:focus {
  border-color: #409eff;
}

.delete-btn {
  padding: 4px 12px;
  background: none;
  color: #e74c3c;
  border: 1px solid #e74c3c;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.delete-btn:hover {
  background: #e74c3c;
  color: #fff;
}

.users-pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-top: 16px;
}

.page-btn {
  padding: 6px 16px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background: #fff;
  font-size: 13px;
  cursor: pointer;
  color: #606266;
}

.page-btn:hover:not(:disabled) {
  border-color: #409eff;
  color: #409eff;
}

.page-btn:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.page-info {
  font-size: 13px;
  color: #909399;
}

.loading {
  text-align: center;
  padding: 60px;
  color: #909399;
  font-size: 15px;
}
</style>
