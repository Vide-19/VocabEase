<template>
  <div class="user-management">
    <el-card shadow="never">
      <template #header>
        <div class="header">
          <!-- 1. 新增 Tab 切换 -->
          <div class="tabs">
            <el-button
                :type="activeTab === 'admin' ? 'primary' : 'default'"
                @click="switchTab('admin')"
            >
              后端管理员
            </el-button>
            <el-button
                :type="activeTab === 'applet' ? 'primary' : 'default'"
                @click="switchTab('applet')"
            >
              小程序用户
            </el-button>
          </div>

          <!-- 2. 条件渲染：仅在管理后端管理员时显示“新增”和“角色搜索” -->
          <div v-if="activeTab === 'admin'" class="admin-actions">
            <el-button type="primary" @click="openAddDialog">新增用户</el-button>
            <div class="search-box">
              <el-input
                  v-model="searchQuery"
                  placeholder="用户名"
                  style="width: 240px; margin-right: 10px;"
                  clearable
                  @keyup.enter="loadUsers"
              />
              <el-button type="primary" @click="loadUsers">搜索</el-button>
            </div>
          </div>
          <!-- 3. 小程序用户搜索（通常只需要手机号或昵称） -->
          <div v-else class="search-box">
            <el-input
                v-model="searchQuery"
                placeholder="请输入用户昵称"
                style="width: 240px; margin-right: 10px;"
                clearable
                @keyup.enter="loadUsers"
            />
            <el-button type="primary" @click="loadUsers">搜索</el-button>
          </div>
        </div>
      </template>

      <!-- 表格 -->
      <el-table :data="userList" style="width: 100%" v-loading="loading">
        <el-table-column prop="userId" label="ID" width="120"/>
        <el-table-column prop="openId" label="openID" width="120" v-if="activeTab === 'applet'"/>
        <el-table-column prop="email" label="邮箱" width="120" v-if="activeTab === 'applet'"/>
        <el-table-column prop="nickName" label="昵称" width="120" v-if="activeTab === 'applet'"/>
        <el-table-column prop="gender" label="性别" width="70" v-if="activeTab === 'applet'">
          <template #default="{ row }">
            <el-tag :type="row.gender === 0 ? 'danger' : 'brand'">
              {{ row.gender === 0 ? '女生' : '男生' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="用户名" width="120" v-if="activeTab === 'admin'"/>
        <el-table-column prop="phone" label="手机号" width="120" v-if="activeTab === 'admin'"/>

        <!-- 4. 仅后端管理员显示角色列 -->
        <el-table-column v-if="activeTab === 'admin'" prop="roleNames" label="角色" width="70">
          <template #default="{ row }">
            <el-tag>
              {{ row.roles === '1' ? '超管' : '管理' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="状态" width="70">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'warning'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="createTime" label="创建时间" width="170"/>
        <el-table-column prop="lastLoginTime" label="上传登录时间" width="155" v-if="activeTab === 'applet'"/>
        <el-table-column prop="lastUseDeviceBrand" label="上传登录设备" width="155" v-if="activeTab === 'applet'"/>

        <el-table-column label="操作" fixed="right" width="220" v-if="activeTab === 'admin'">
          <template #default="{ row }">
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
                :type="row.status === 0 ? 'success' : 'warning'"
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
        <el-table-column label="操作" fixed="right" width="100" v-if="activeTab === 'applet'">
          <template #default="{ row }">
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
                :type="row.status === 0 ? 'success' : 'warning'"
                link
                @click="toggleStatus(row)"
            >
              {{ row.status === 1 ? '禁用' : '启用' }}
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

    <!-- 编辑后端管理员 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="400px" v-if="activeTab === 'admin'">
      <el-form :model="formAdmin" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="formAdmin.userName"/>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="formAdmin.phone"/>
        </el-form-item>
        <el-form-item v-if="!formAdmin.userId" label="密码" prop="password">
          <el-input v-model="formAdmin.password" type="password" show-password/>
        </el-form-item>
        <el-form-item label="角色" prop="roles">
          <el-select v-model="formAdmin.roles" multiple placeholder="请选择角色">
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
    <!-- 编辑小程序用户 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="400px" v-if="activeTab === 'applet'">
      <el-form :model="formApplet" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formApplet.email"/>
        </el-form-item>
        <el-form-item label="昵称" prop="nickName">
          <el-input v-model="formApplet.nickName"/>
        </el-form-item>
        <el-form-item label="角色" prop="gender">
          <el-select v-model="formApplet.gender" clearable placeholder="请选择性别">
            <el-option :label="'女生'" :value="0"/>
            <el-option :label="'男生'" :value="1"/>
            <el-option :label="'未知'" :value="2"/>
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
          <el-input v-model="pwdForm.newPassword" type="password" show-password/>
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
import axios from 'axios' // 1. 新增：当前激活的 Tab

// 1. 新增：当前激活的 Tab
const activeTab = ref('admin') // 默认为 admin

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

const formAdmin = ref({
  userId: null,
  userName: '',
  phone: '',
  password: '',
  roles: []
})

const formApplet = ref({
  userId: null,
  nickName: '',
  email: '',
  gender: ''
})

const pwdForm = ref({
  userId: null,
  newPassword: ''
})

const dialogTitle = ref('新增用户')
const roleOptions = ref([])

// 表单校验规则
const rules = {
  userName: [{required: true, message: '请输入用户名', trigger: 'blur'}],
  phone: [{required: true, message: '请输入手机号', trigger: 'blur'}],
  password: [{required: true, message: '请输入密码', trigger: 'blur'}]
}

const pwdRules = {
  newPassword: [{required: true, message: '请输入新密码', trigger: 'blur'}]
}

// 判断是否超管（保持不变）
const isSuperAdmin = (row) => {
  const superAdminPhones = import.meta.env.VITE_SUPER_ADMIN_PHONE?.split(',') || []
  return superAdminPhones.includes(row.phone)
}

// 2. 修改 loadUsers：根据 Tab 调用不同的接口或参数
const loadUsers = async () => {
  loading.value = true
  try {
    let res
    if (activeTab.value === 'admin') {
      res = await axios.post('/settings/loadAccountList', {
        pageNo: pageNo.value,
        pageSize: pageSize.value,
        userNameFuzzy: searchQuery.value
      })
    } else {
      res = await axios.post('/appAccount/loadAccountList', {
        pageNo: pageNo.value,
        pageSize: pageSize.value,
        nickNameFuzzy: searchQuery.value
      })
    }

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

// 加载角色列表（仅 admin 需要，但为了复用可以保留）
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

// 3. 新增：切换 Tab 的方法
const switchTab = (tab) => {
  activeTab.value = tab
  pageNo.value = 1 // 切换时重置页码
  loadUsers() // 重新加载数据
}

// 以下方法保持不变，但注意它们仅在 admin 模式下可用
const openAddDialog = () => {
  formAdmin.value = {
    userId: null,
    userName: '',
    phone: '',
    password: '',
    roles: []
  }
  dialogTitle.value = '新增用户'
  dialogVisible.value = true
}

const openEditDialog = (row) => {
  if  (activeTab.value === 'admin') {
    formAdmin.value = {
      userId: row.userId,
      userName: row.userName,
      phone: row.phone,
      password: '',
      roles: Array.isArray(row.roles) ? row.roles : (row.roles ? [row.roles] : [])
    }
  } else {
    formApplet.value = {
      userId: row.userId,
      nickName: row.nickName,
      email: row.email,
      gender: row.gender
    }
  }
  dialogTitle.value = '编辑用户'
  dialogVisible.value = true
}

const saveUser = async () => {
  await formRef.value.validate()
  try {
    if (activeTab.value === 'admin') {
      await axios.post('/settings/saveAccount', {
        userId: formAdmin.value.userId,
        userName: formAdmin.value.userName,
        phone: formAdmin.value.phone,
        password: formAdmin.value.password,
        roles: formAdmin.value.roles.join(',')
      })
    } else  {
      await axios.post('/appAccount/updateAppAccount', {
        userId: formApplet.value.userId,
        nickName: formApplet.value.nickName,
        email: formApplet.value.email,
        gender: formApplet.value.gender
      })
    }
    ElMessage.success('操作成功')
    dialogVisible.value = false
    loadUsers()
  } catch (error) {
    ElMessage.error('操作失败')
    console.error(error)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除用户【${row.userName}】？`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await axios.delete('/settings/deleteAccount', {
        params: {userId: row.userId}
      })
      ElMessage.success('删除成功')
      loadUsers()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

const toggleStatus = async (row) => {
  const newStatus = row.status === 1 ? 0 : 1
  try {
    if (activeTab.value === 'admin') {
      await axios.post('/settings/updateStatus', null, {
        params: {userId: row.userId, status: newStatus}
      })
    } else {
      await axios.post('/appAccount/updateStatus', null, {
        params: {userId: row.userId, status: newStatus}
      })
    }
    ElMessage.success('状态更新成功')
    loadUsers()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const openResetPwdDialog = (row) => {
  pwdForm.value = {userId: row.userId, newPassword: ''}
  pwdDialogVisible.value = true
}

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
  loadRoles() // 如果小程序用户不需要角色，这里只加载一次即可
})
</script>

<style scoped>
.user-management .header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

/* 4. 新增样式：Tab 样式 */
.tabs {
  display: flex;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.tabs .el-button {
  border-radius: 0;
  margin: 0;
  border: none;
}

.tabs .el-button:first-child {
  border-radius: 4px 0 0 4px;
}

.tabs .el-button:last-child {
  border-radius: 0 4px 4px 0;
}

.admin-actions {
  display: flex;
  align-items: center; /* 垂直居中对齐 */
  gap: 10px; /* 按钮和搜索框之间的间距 */
}

/* 搜索框样式 */
.search-box {
  display: flex;
  align-items: center;
}
</style>