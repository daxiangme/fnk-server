<template>
  <div class="art-full-height">
    <ElRow :gutter="16">
      <ElCol :span="8" :xs="24">
        <ElCard>
          <template #header>当前用户</template>
          <ElDescriptions :column="1" border>
            <ElDescriptionsItem label="账号">{{ userInfo.username || '-' }}</ElDescriptionsItem>
            <ElDescriptionsItem label="手机号">{{ userInfo.phone || '-' }}</ElDescriptionsItem>
            <ElDescriptionsItem label="角色">
              <ElSpace wrap>
                <ElTag v-for="role in userInfo.roles || []" :key="role">{{ role }}</ElTag>
              </ElSpace>
            </ElDescriptionsItem>
          </ElDescriptions>
        </ElCard>
      </ElCol>
      <ElCol :span="16" :xs="24">
        <ElCard>
          <template #header>权限概览</template>
          <ElSpace wrap>
            <ElTag
              v-for="permission in userInfo.permissions || []"
              :key="permission"
              type="info"
              effect="plain"
            >
              {{ permission }}
            </ElTag>
          </ElSpace>
          <ElEmpty v-if="!userInfo.permissions?.length" description="暂无权限" />
        </ElCard>
      </ElCol>
    </ElRow>
  </div>
</template>

<script setup lang="ts">
  import { useUserStore } from '@/store/modules/user'

  defineOptions({ name: 'Home' })

  const userStore = useUserStore()
  const userInfo = computed(() => userStore.getUserInfo)
</script>
