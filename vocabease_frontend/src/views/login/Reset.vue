<script setup lang="ts">
import {computed, reactive, ref} from 'vue'
import {ElButton, ElForm, ElFormItem, ElIcon, ElInput, ElMessage, ElStep, ElSteps} from 'element-plus'
import {Lock, Picture, User} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {postWithParams} from '@/net/login'

const router = useRouter()
const active = ref(0)

// 表单数据（两步共用）
const form = reactive({
  phone: '',
  checkCode: '',
  password: '',
  password_repeat: ''
})

const formRef = ref()

// 验证码刷新
const captchaKey = ref(Date.now())
const refreshCaptcha = () => {
  captchaKey.value = Date.now()
}
const captchaUrl = computed(() => `/api/checkCode?t=${captchaKey.value}`)

// 密码一致性校验
const validatePasswordRepeat = (rule: any, value: string, callback: any) => {
  if (!value) {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.password) {
    callback(new Error('两次密码不一致'))
  } else {
    callback()
  }
}

// 手机号校验
const validatePhone = (rule: any, value: string, callback: any) => {
  if (!value) {
    callback(new Error('请输入手机号'))
  } else if (!/^1[3-9]\d{9}$/.test(value)) {
    callback(new Error('请输入正确的11位手机号'))
  } else {
    callback()
  }
}

// 表单规则（动态切换）
const rules = computed(() => {
  if (active.value === 0) {
    return {
      phone: [{validator: validatePhone, trigger: ['blur', 'change']}],
      checkCode: [{required: true, message: '请输入验证码', trigger: 'blur'}]
    }
  } else {
    return {
      password: [
        {required: true, message: '请输入新密码', trigger: 'blur'},
        {min: 6, max: 20, message: '密码长度需在6-20字符之间', trigger: ['blur', 'change']}
      ],
      password_repeat: [{validator: validatePasswordRepeat, trigger: ['blur', 'change']}]
    }
  }
})

// 第一步：验证手机号 + 图片验证码
function confirmReset() {
  formRef.value?.validate((valid: boolean) => {
    if (!valid) return

    // 调用验证接口（后端校验手机号是否存在 + 验证码是否正确）
    postWithParams(
        '/api/reset-password/verify',
        {
          phone: form.phone,
          checkCode: form.checkCode
        },
        () => {
          ElMessage.success('验证成功')
          active.value = 1 // 进入第二步
        },
        (message: string) => {
          ElMessage.error(message)
          refreshCaptcha() // 验证失败，刷新验证码
        }
    )
  })
}

// 第二步：重置密码
function doReset() {
  formRef.value?.validate((valid: boolean) => {
    if (!valid) return

    postWithParams(
        '/api/reset-password/do',
        {
          phone: form.phone, // 必须传 phone，用于定位用户
          password: form.password
        },
        () => {
          ElMessage.success('密码重置成功，请登录')
          router.push('/login')
        },
        (message: string) => {
          ElMessage.error(message)
          // 可选：重置失败是否回到第一步？
        }
    )
  })
}
</script>

<template>
  <div style="text-align: center; margin: 0 20px">
    <div style="margin-top: 30px">
      <el-steps :active="active" finish-status="success" align-center>
        <el-step title="验证手机号"/>
        <el-step title="重置密码"/>
      </el-steps>
    </div>

    <!-- 第一步：验证手机号 + 图片验证码 -->
    <div v-if="active === 0" style="margin-top: 50px; max-width: 300px; margin-left: auto; margin-right: auto;">
      <div style="font-size: 25px; font-weight: bold">忘记密码</div>
      <div style="font-size: 15px; color: gray; margin-top: 10px">请输入注册手机号进行验证</div>

      <el-form :model="form" :rules="rules" ref="formRef" style="margin-top: 40px">
        <el-form-item prop="phone">
          <el-input
              v-model="form.phone"
              maxlength="11"
              placeholder="手机号"
          >
            <template #prefix>
              <el-icon>
                <User/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="checkCode">
          <el-row :gutter="10" style="width: 100%">
            <el-col :span="17">
              <el-input
                  v-model="form.checkCode"
                  maxlength="6"
                  placeholder="验证码"
              >
                <template #prefix>
                  <el-icon>
                    <Picture/>
                  </el-icon>
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

      <div style="margin-top: 30px">
        <el-button
            @click="confirmReset"
            type="success"
            style="width: 270px"
            plain
        >
          验证手机号
        </el-button>
      </div>
    </div>

    <!-- 第二步：设置新密码 -->
    <div v-if="active === 1" style="margin-top: 50px; max-width: 300px; margin-left: auto; margin-right: auto;">
      <div style="font-size: 25px; font-weight: bold">重置密码</div>
      <div style="font-size: 15px; color: gray; margin-top: 10px">请输入您的新密码</div>

      <el-form :model="form" :rules="rules" ref="formRef" style="margin-top: 40px">
        <el-form-item prop="password">
          <el-input
              v-model="form.password"
              type="password"
              maxlength="20"
              placeholder="新密码"
              show-password
          >
            <template #prefix>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password_repeat">
          <el-input
              v-model="form.password_repeat"
              type="password"
              maxlength="20"
              placeholder="重复新密码"
              show-password
          >
            <template #prefix>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
      </el-form>

      <div style="margin-top: 30px">
        <el-button
            @click="doReset"
            type="success"
            style="width: 270px"
            plain
        >
          立即重置
        </el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.captcha-img {
  display: inline-block;
}
</style>