<script setup lang="ts">
import {computed, reactive, ref} from 'vue'
import {ElButton, ElDivider, ElForm, ElFormItem, ElIcon, ElInput, ElLink, ElMessage} from 'element-plus'
import {Lock, Picture, User} from '@element-plus/icons-vue'
import {postWithParams} from '@/net/login'
import {useUserStore} from '@/stores/user'
import {useRouter} from 'vue-router'


// 表单数据
const loginForm = reactive({
  phone: '',
  password: '',
  checkCode: ''
})

// 表单引用
const loginFormRef = ref()
const router = useRouter()

// 验证码时间戳
const captchaKey = ref(Date.now())
const userStore = useUserStore() // ← 创建 store 实例


const refreshCaptcha = () => {
  captchaKey.value = Date.now()
}

// 验证码图片 URL（带 /api 前缀）
const captchaUrl = computed(() => `/api/checkCode?t=${captchaKey.value}`)

// 登录
const handleLogin = () => {
  loginFormRef.value?.validate((valid) => {
    if (!valid) return
    postWithParams(
        '/api/login',
        {
          phone: loginForm.phone,
          password: loginForm.password,
          checkCode: loginForm.checkCode
        },
        (data) => {
          // ✅ 正确使用 userStore
          userStore.setUserInfo(data)
          ElMessage.success('登录成功')
          router.push('/index')
        },
        (message) => {
          ElMessage.error(message)
          refreshCaptcha()
        }
    )
  })
}

</script>

<template>
  <div style="text-align: center; margin: 0 20px">
    <div style="margin-top: 100px">
      <div style="font-size: 25px; font-weight: bold">简词后台</div>
      <div style="font-size: 15px; color: gray">请输入手机号、密码和验证码进行登录</div>
    </div>

    <div style="margin-top: 40px; max-width: 300px; margin-left: auto; margin-right: auto;">
      <el-form :model="loginForm" :rules="{
        phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        checkCode: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
      }" ref="loginFormRef" size="default">

        <!-- 手机号 -->
        <el-form-item prop="phone">
          <el-input
              v-model="loginForm.phone"
              placeholder="手机号"
              maxlength="11"
          >
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 密码 -->
        <el-form-item prop="password">
          <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="密码"
              show-password
          >
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 验证码 -->
        <el-form-item prop="checkCode">
          <el-input
              v-model="loginForm.checkCode"
              placeholder="验证码"
              maxlength="6"
              style="width: 140px"
          >
            <template #prefix>
              <el-icon><Picture /></el-icon>
            </template>
          </el-input>
          <img
              :src="captchaUrl"
              @click="refreshCaptcha"
              alt="验证码"
              class="captcha-img"
              style="
              width: 100px;
              height: 36px;
              cursor: pointer;
              vertical-align: middle;
              margin-left: 8px;
              border: 1px solid #dcdfe6;
              border-radius: 4px;
            "
          />
        </el-form-item>

        <el-row>
          <el-col :span="12" style="text-align: left">
            <!-- 可后续扩展“记住我” -->
          </el-col>
          <el-col :span="12" style="text-align: right">
            <el-link @click="router.push('/reset')">忘记密码？</el-link>
          </el-col>
        </el-row>
      </el-form>

      <div style="margin-top: 20px">
        <el-button
            @click="handleLogin"
            style="width: 270px"
            type="success"
            plain
        >
          立即登录
        </el-button>
      </div>

      <el-divider style="margin-top: 35px">
        <span style="font-size: 13px; color: gray">没有账号</span>
      </el-divider>

      <div>
        <el-button
            @click="router.push('/register')"
            style="width: 270px"
            type="warning"
            plain
        >
          立即注册
        </el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 可选：微调验证码垂直对齐 */
.captcha-img {
  display: inline-block;
}
</style>