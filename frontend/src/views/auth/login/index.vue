<template>
  <div class="flex w-full h-screen">
    <LoginLeftView />
    <div class="relative flex-1">
      <AuthTopBar />
      <div class="auth-right-wrap">
        <div class="form">
          <h3 class="title">后台管理系统</h3>
          <p class="sub-title">使用系统账号登录后加载你的菜单和权限</p>
          <ElForm
            ref="formRef"
            :model="formData"
            :rules="rules"
            @keyup.enter="handleSubmit"
            style="margin-top: 25px"
          >
            <ElFormItem prop="phone">
              <ElInput
                v-model.trim="formData.phone"
                class="custom-height"
                placeholder="手机号"
                autocomplete="username"
              />
            </ElFormItem>
            <ElFormItem prop="password">
              <ElInput
                v-model.trim="formData.password"
                class="custom-height"
                placeholder="密码"
                type="password"
                autocomplete="current-password"
                show-password
              />
            </ElFormItem>

            <div class="flex-cb mt-2 text-sm">
              <ElCheckbox v-model="formData.rememberPassword">记住登录</ElCheckbox>
            </div>

            <div style="margin-top: 30px">
              <ElButton
                class="w-full custom-height"
                type="primary"
                :loading="loading"
                @click="handleSubmit"
                v-ripple
              >
                登录
              </ElButton>
            </div>
          </ElForm>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { fetchLogin } from '@/api/auth'
  import AppConfig from '@/config'
  import { resetRouterState } from '@/router/guards/beforeEach'
  import { useUserStore } from '@/store/modules/user'
  import { HttpError } from '@/utils/http/error'
  import { ElMessage, ElNotification, type FormInstance, type FormRules } from 'element-plus'

  defineOptions({ name: 'Login' })

  const userStore = useUserStore()
  const router = useRouter()
  const route = useRoute()
  const formRef = ref<FormInstance>()
  const loading = ref(false)

  const formData = reactive({
    phone: '18888888888',
    password: '123456',
    rememberPassword: true
  })

  const rules: FormRules = {
    phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
  }

  const handleSubmit = async () => {
    if (!formRef.value) return

    try {
      const valid = await formRef.value.validate()
      if (!valid) return

      loading.value = true
      const { tokenValue } = await fetchLogin({
        phone: formData.phone,
        password: formData.password
      })

      if (!tokenValue) {
        throw new Error('登录失败，未返回 Token')
      }

      userStore.resetBeforeLogin()
      resetRouterState(0)
      userStore.setToken(tokenValue)
      userStore.setLoginStatus(true)

      ElNotification({
        title: '登录成功',
        message: `欢迎使用 ${AppConfig.systemInfo.name}`,
        type: 'success',
        duration: 2200
      })

      const redirect = route.query.redirect as string
      router.push(redirect || '/')
    } catch (error) {
      if (!(error instanceof HttpError)) {
        console.error('[Login] Unexpected error:', error)
        ElMessage.error(error instanceof Error ? error.message : '登录失败')
      }
    } finally {
      loading.value = false
    }
  }
</script>

<style scoped>
  @import './style.css';
</style>
