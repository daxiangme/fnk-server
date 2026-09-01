<template>
  <ArtSearchBar
    v-model="formData"
    :items="formItems"
    :show-expand="false"
    @reset="emit('reset')"
    @search="handleSearch"
  />
</template>

<script setup lang="ts">
  import type { SearchFormItem } from '@/components/core/forms/art-search-bar/index.vue'

  defineOptions({ name: 'UserSearch' })

  const props = defineProps<{
    modelValue: Api.SystemManage.UserSearchParams
  }>()

  const emit = defineEmits<{
    (e: 'update:modelValue', value: Api.SystemManage.UserSearchParams): void
    (e: 'search', value: Api.SystemManage.UserSearchParams): void
    (e: 'reset'): void
  }>()

  const formData = computed({
    get: () => props.modelValue,
    set: (value) => emit('update:modelValue', value)
  })

  const formItems: SearchFormItem[] = [
    {
      label: '用户名',
      key: 'username',
      type: 'input',
      props: { clearable: true, placeholder: '请输入用户名' }
    },
    {
      label: '手机号',
      key: 'phone',
      type: 'input',
      props: { clearable: true, placeholder: '请输入手机号' }
    },
    {
      label: '状态',
      key: 'status',
      type: 'select',
      props: {
        clearable: true,
        placeholder: '全部',
        options: [
          { label: '启用', value: true },
          { label: '禁用', value: false }
        ]
      }
    }
  ]

  function handleSearch(value: Api.SystemManage.UserSearchParams) {
    emit('search', value)
  }
</script>
