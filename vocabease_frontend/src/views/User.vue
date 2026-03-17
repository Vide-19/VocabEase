<!-- src/views/settings/UserManagement.vue -->
<template>
  <div class="user-management">
    <el-card shadow="never">
      <template #header>
        <div class="header">
          <el-button type="primary" @click="openAddDialog">新增用户</el-button>
          <div class="search-box">
            <el-input
                v-model="searchQuery"
                placeholder="用户名或手机号"
                style="width: 240px; margin-right: 10px;"
                clearable
                @keyup.enter="loadUsers"
            />
            <el-button type="primary" @click="loadUsers">搜索</el-button>
          </div>
        </div>
      </template>

      <el-table :data="userList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="userName" label="用户名" width="120" />
        <el-table-column prop="phone" label="手机号" width="150" />
        <el-table-column prop="roleNames" label="角色" width="120" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="success">启用</el-tag>
            <el-tag v-else type="danger">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" fixed="right" width="220">
          <template #default="{ row }">
            <!-- 超管不能编辑/删除 -->
            <el-button
                size="small"
                type="primary"
                link
                @click="openEditDialog(row)"
                :disabled="isSuperAdmin(row)"
            >
              编辑
            </el-button>
            <el-button
                size="small"
                type="danger"
                link
                @click="handleDelete(row)"
                :disabled="isSuperAdmin(row)"
            >
              删除
            </el-button>
            <el-button
                size="small"
                type="warning"
                link
                @click="toggleStatus(row)"
            >
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button
                size="small"
                type="info"
                link
                @click="openResetPwdDialog(row)"
            >
              重置密码
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
          v-if="total > 0"
          background
          layout="total, prev, pager, next"
          :total="total"
          :current-page="pageNo"
          :page-size="pageSize"
          @current-change="handlePageChange"
          style="margin-top: 20px; text-align: right;"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="400px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="form.userName" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item v-if="!form.userId" label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password />
        </el-form-item>
        <el-form-item label="角色" prop="roles">
          <el-select v-model="form.roles" multiple placeholder="请选择角色">
            <el-option
                v-for="role in roleOptions"
                :key="role.roleId"
                :label="role.roleName"
                :value="role.roleId"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveUser">确定</el-button>
      </template>
    </el-dialog>

    <!-- 重置密码对话框 -->
    <el-dialog title="重置密码" v-model="pwdDialogVisible" width="400px">
      <el-form :model="pwdForm" :rules="pwdRules" ref="pwdFormRef" label-width="80px">
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="pwdForm.newPassword" type="password" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="pwdDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="resetPassword">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import axios from 'axios'

const searchQuery = ref('')
const userList = ref([])
const total = ref(0)
const pageNo = ref(1)
const pageSize = ref(15)
const loading = ref(false)

const dialogVisible = ref(false)
const pwdDialogVisible = ref(false)
const formRef = ref()
const pwdFormRef = ref()

const form = ref({
  userId: null,
  userName: '',
  phone: '',
  password: '',
  roles: []
})

const pwdForm = ref({
  userId: null,
  newPassword: ''
})

const dialogTitle = ref('新增用户')

// 角色选项（从 /settings/loadRolesList 获取）
const roleOptions = ref([])

// 表单校验规则
const rules = {
  userName: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const pwdRules = {
  newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }]
}

// 判断是否超管（手机号在 appConfig.superAdminPhone 中）
const isSuperAdmin = (row) => {
  const superAdminPhones = import.meta.env.VITE_SUPER_ADMIN_PHONE?.split(',') || []
  return superAdminPhones.includes(row.phone)
}

// 加载用户列表
const loadUsers = async () => {
  loading.value = true
  try {
    const res = await axios.post('/settings/loadAccountList', {
      pageNo: pageNo.value,
      pageSize: pageSize.value,
      userNameFuzzy: searchQuery.value,
      phone: searchQuery.value // 后端需支持模糊匹配
    })
    if (res.data.status === 'success') {
      const data = res.data.data
      userList.value = data.list || []
      total.value = data.totalCount || 0
    }
  } catch (error) {
    ElMessage.error('加载用户失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 加载角色列表
const loadRoles = async () => {
  try {
    const res = await axios.get('/settings/loadRolesList')
    if (res.data.status === 'success') {
      roleOptions.value = (res.data.data.list || []).map(item => ({
        roleId: item.roleId,
        roleName: item.roleName
      }))
    }
  } catch (error) {
    console.warn('加载角色失败，不影响主功能')
  }
}

// 分页切换
const handlePageChange = (page) => {
  pageNo.value = page
  loadUsers()
}

// 打开新增对话框
const openAddDialog = () => {
  form.value = {
    userId: null,
    userName: '',
    phone: '',
    password: '',
    roles: []
  }
  dialogTitle.value = '新增用户'
  dialogVisible.value = true
}

// 打开编辑对话框
const openEditDialog = (row) => {
  form.value = {
    userId: row.userId,
    userName: row.userName,
    phone: row.phone,
    password: '', // 编辑时不传密码
    roles: Array.isArray(row.roles) ? row.roles : (row.roles ? [row.roles] : [])
  }
  dialogTitle.value = '编辑用户'
  dialogVisible.value = true
}

// 保存用户
const saveUser = async () => {
  await formRef.value.validate()
  try {
    await axios.post('/settings/saveAccount', {
      userId: form.value.userId,
      userName: form.value.userName,
      phone: form.value.phone,
      password: form.value.password,
      roles: form.value.roles.join(',')
    })
    ElMessage.success('操作成功')
    dialogVisible.value = false
    loadUsers()
  } catch (error) {
    ElMessage.error('操作失败')
    console.error(error)
  }
}

// 删除用户
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除用户【${row.userName}】？`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await axios.delete('/settings/deleteAccount', {
        params: { userId: row.userId }
      })
      ElMessage.success('删除成功')
      loadUsers()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

// 启用/禁用
const toggleStatus = async (row) => {
  const newStatus = row.status === 1 ? 0 : 1
  try {
    await axios.post('/settings/updateStatus', null, {
      params: { userId: row.userId, status: newStatus }
    })
    ElMessage.success('状态更新成功')
    loadUsers()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 打开重置密码
const openResetPwdDialog = (row) => {
  pwdForm.value = { userId: row.userId, newPassword: '' }
  pwdDialogVisible.value = true
}

// 重置密码
const resetPassword = async () => {
  await pwdFormRef.value.validate()
  try {
    await axios.post('/settings/updatePassword', null, {
      params: {
        userId: pwdForm.value.userId,
        password: pwdForm.value.newPassword
      }
    })
    ElMessage.success('密码重置成功')
    pwdDialogVisible.value = false
  } catch (error) {
    ElMessage.error('重置失败')
  }
}

onMounted(() => {
  loadUsers()
  loadRoles()
})
</script>

<style scoped>
.user-management .header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>