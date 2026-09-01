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

  defineOptions({ name: 'RoleSearch' })

  const props = defineProps<{
    modelValue: Api.SystemManage.RoleSearchParams
  }>()

  const emit = defineEmits<{
    (e: 'update:modelValue', value: Api.SystemManage.RoleSearchParams): void
    (e: 'search', value: Api.SystemManage.RoleSearchParams): void
    (e: 'reset'): void
  }>()

  const formData = computed({
    get: () => props.modelValue,
    set: (value) => emit('update:modelValue', value)
  })

  const formItems: SearchFormItem[] = [
    {
      label: '角色名称',
      key: 'roleName',
      type: 'input',
      props: { clearable: true, placeholder: '请输入角色名称' }
    },
    {
      label: '角色标识',
      key: 'roleKey',
      type: 'input',
      props: { clearable: true, placeholder: '请输入角色标识' }
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

  function handleSearch(value: Api.SystemManage.RoleSearchParams) {
    emit('search', value)
  }
</script>
