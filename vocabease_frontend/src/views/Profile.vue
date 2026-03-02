<template>
  <div class="profile">
    <h1>个人中心</h1>
    <el-card shadow="never" style="margin-top: 20px;">
      <template #header>
        <span>个人信息</span>
        <el-button
            v-if="!isEditing"
            type="primary"
            size="small"
            style="float: right"
            @click="startEdit"
        >
          编辑
        </el-button>
        <div v-else style="float: right">
          <el-button size="small" @click="cancelEdit">取消</el-button>
          <el-button type="primary" size="small" @click="saveProfile">保存</el-button>
        </div>
      </template>

      <el-form :model="form" label-width="80px" v-if="isEditing" ref="formRef">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="form.userName" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" disabled />
        </el-form-item>
      </el-form>

      <div v-else>
        <p><strong>用户名:</strong> {{ form.userName }}</p>
        <p><strong>手机号:</strong> {{ form.phone }}</p>
      </div>
    </el-card>

    <!-- 修改密码 -->
    <el-card shadow="never" style="margin-top: 20px;">
      <template #header>
        <span>修改密码</span>
      </template>
      <el-form :model="pwdForm" label-width="80px" :rules="pwdRules" ref="pwdFormRef">
        <el-form-item label="新密码" prop="newPassword">
          <el-input
              v-model="pwdForm.newPassword"
              type="password"
              show-password
              placeholder="8-20位，含数字、字母和 ~!@#$%^&*_"
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
              v-model="pwdForm.confirmPassword"
              type="password"
              show-password
              placeholder="请再次输入新密码"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="updatePassword">更新密码</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import {reactive, ref} from 'vue'
import {ElMessage} from 'element-plus'
import axios from 'axios'
import {useRouter} from 'vue-router'

const router = useRouter()
// 与后端 VerifyRegexEnum.PASSWORD 完全一致的正则
const PASSWORD_REGEX = /^(?=.*\d)(?=.*[a-zA-Z])(?=.*[~!@#$%^&*_])[\da-zA-Z~!@#$%^&*_]{8,20}$/;

// 获取当前用户信息
const fetchCurrentUser = async () => {
  const res = await axios.get('/settings/getMyInfo')
  if (res.data.status === 'success') {
    return res.data.data // { userId, userName, phone, ... }
  } else {
    throw new Error(res.data.info || '获取用户信息失败')
  }
}

const form = reactive({
  userId: null,
  userName: '',
  phone: ''
})

const pwdForm = reactive({
  newPassword: '',
  confirmPassword: ''
})

const isEditing = ref(false)
const formRef = ref()
const pwdFormRef = ref()

// 自定义密码校验器
const validatePassword = (rule, value, callback) => {
  if (!value) {
    return callback(new Error('请输入新密码'))
  }
  if (value.length < 8 || value.length > 20) {
    callback(new Error('密码长度需为8-20位'))
  } else if (!/[a-zA-Z]/.test(value)) {
    callback(new Error('密码需包含至少一个字母'))
  } else if (!/\d/.test(value)) {
    callback(new Error('密码需包含至少一个数字'))
  } else if (!/[~!@#$%^&*_]/.test(value)) {
    callback(new Error('密码需包含特殊字符：~ ! @ # $ % ^ & * _'))
  } else if (!PASSWORD_REGEX.test(value)) {
    // 兜底（理论上不会走到这里）
    callback(new Error('密码格式不合法'))
  } else {
    callback()
  }
}

const validateConfirmPassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请确认密码'))
  } else if (value !== pwdForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

// 表单校验规则
const pwdRules = {
  newPassword: [{ validator: validatePassword, trigger: 'blur' }],
  confirmPassword: [{ validator: validateConfirmPassword, trigger: 'blur' }]
}

// 初始化用户数据
const init = async () => {
  const user = await fetchCurrentUser()
  Object.assign(form, user)
}
init()

const startEdit = () => {
  isEditing.value = true
}

const cancelEdit = () => {
  isEditing.value = false
  init() // 重置为原始值
}

const saveProfile = async () => {
  try {
    await axios.post('/settings/saveAccount', {
      userId: form.userId,
      userName: form.userName,
      phone: form.phone
    })
    ElMessage.success('个人信息更新成功')
    isEditing.value = false
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败，请重试')
  }
}

const updatePassword = async () => {
  try {
    await pwdFormRef.value.validate()

    await axios.post('/settings/updatePassword', null, {
      params: {
        userId: form.userId,
        password: pwdForm.newPassword
      }
    })

    ElMessage.success('密码修改成功，请重新登录')

    // 清除登录凭证
    localStorage.removeItem('token')
    // 如果有其他登录相关数据也一并清除
    // localStorage.removeItem('userId')

    // 跳转登录页
    router.push('/login')

  } catch (error) {
    if (error?.message?.includes('Error')) {
      return // Element Plus 表单校验错误，已自动提示
    }
    console.error('修改密码失败:', error)
    ElMessage.error('密码修改失败')
  }
}
</script>

<style scoped>
.profile {
  padding: 20px;
}

.el-card {
  margin-bottom: 20px;
}
</style>