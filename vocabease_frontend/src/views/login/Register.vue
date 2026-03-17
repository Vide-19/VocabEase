<script setup lang="ts">
import {computed, reactive, ref} from 'vue'
import {ElButton, ElDivider, ElForm, ElFormItem, ElIcon, ElInput, ElLink, ElMessage} from 'element-plus'
import {Lock, Picture, User} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {postWithParams} from '@/net/login' // 注意：注册也用带参数的 POST（或根据后端调整）

const router = useRouter()

// 表单数据
const form = reactive({
  username: '',
  password: '',
  password_repeat: '',
  phone: '',
  checkCode: ''
})

// 表单引用
const formRef = ref()

// 验证码时间戳（用于刷新）
const captchaKey = ref(Date.now())
const refreshCaptcha = () => {
  captchaKey.value = Date.now()
}
const captchaUrl = computed(() => `/api/checkCode?t=${captchaKey.value}`)

// 校验规则
const validateUsername = (rule: any, value: string, callback: any) => {
  if (!value) {
    callback(new Error('请输入用户名'))
  } else if (!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)) {
    callback(new Error('用户名不能包含特殊字符，只能是中英文'))
  } else {
    callback()
  }
}

const validatePasswordRepeat = (rule: any, value: string, callback: any) => {
  if (!value) {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.password) {
    callback(new Error('两次密码不一致'))
  } else {
    callback()
  }
}

const validatePhone = (rule: any, value: string, callback: any) => {
  if (!value) {
    callback(new Error('请输入手机号'))
  } else if (!/^1[3-9]\d{9}$/.test(value)) {
    callback(new Error('请输入正确的11位手机号'))
  } else {
    callback()
  }
}

const rules = {
  username: [{ validator: validateUsername, trigger: ['blur', 'change'] }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度需在6-20字符之间', trigger: ['blur', 'change'] }
  ],
  password_repeat: [{ validator: validatePasswordRepeat, trigger: ['blur', 'change'] }],
  phone: [{ validator: validatePhone, trigger: ['blur', 'change'] }],
  checkCode: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

// 注册提交
function register() {
  formRef.value?.validate((valid: boolean) => {
    if (!valid) return

    // 后端注册接口
    postWithParams(
        '/api/register',
        {
          username: form.username,
          password: form.password,
          phone: form.phone,
          checkCode: form.checkCode
        },
        () => {
          ElMessage.success('注册成功！')
          router.push('/login')
        },
        (message: string) => {
          ElMessage.error(message)
          refreshCaptcha() // 验证码错误时刷新
        }
    )
  })
}
</script>

<template>
  <div style="text-align: center; margin: 0 20px">
    <div style="margin-top: 100px">
      <div style="font-size: 25px; font-weight: bold">注册新用户</div>
      <div style="font-size: 15px; color: gray">欢迎注册，请填写您的相关信息</div>
    </div>

    <div style="margin-top: 30px; max-width: 300px; margin-left: auto; margin-right: auto;">
      <el-form :model="form" :rules="rules" ref="formRef" size="default">
        <!-- 用户名 -->
        <el-form-item prop="username">
          <el-input
              v-model="form.username"
              maxlength="10"
              placeholder="用户名"
          >
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 密码 -->
        <el-form-item prop="password">
          <el-input
              v-model="form.password"
              type="password"
              maxlength="20"
              placeholder="密码"
              show-password
          >
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 确认密码 -->
        <el-form-item prop="password_repeat">
          <el-input
              v-model="form.password_repeat"
              type="password"
              maxlength="20"
              placeholder="重复密码"
              show-password
          >
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 手机号 -->
        <el-form-item prop="phone">
          <el-input
              v-model="form.phone"
              maxlength="11"
              placeholder="手机号"
          >
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 图片验证码 -->
        <el-form-item prop="checkCode">
          <el-row :gutter="10" style="width: 100%">
            <el-col :span="17">
              <el-input
                  v-model="form.checkCode"
                  maxlength="6"
                  placeholder="验证码"
              >
                <template #prefix>
                  <el-icon><Picture /></el-icon>
                </template>
              </el-input>
            </el-col>
            <el-col :span="5">
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
                  border: 1px solid #dcdfe6;
                  border-radius: 4px;
                "
              />
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>

      <div style="margin-top: 20px">
        <el-button
            @click="register"
            style="width: 270px"
            type="warning"
            plain
        >
          立即注册
        </el-button>
      </div>

      <el-divider style="margin-top: 30px">
        <el-link @click="router.push('/login')" style="font-size: 13px; color: gray">
          已有账号？去登录
        </el-link>
      </el-divider>
    </div>
  </div>
</template>

<style scoped>
.captcha-img {
  display: inline-block;
}
</style>