<template>
  <ArtSearchBar
    class="dict-type-search"
    v-model="formData"
    :items="formItems"
    :span="24"
    :gutter="8"
    is-expand
    label-position="top"
    :label-width="70"
    :show-expand="false"
    @reset="emit('reset')"
    @search="handleSearch"
  />
</template>

<script setup lang="ts">
  import type { SearchFormItem } from '@/components/core/forms/art-search-bar/index.vue'

  defineOptions({ name: 'DictTypeSearch' })

  const props = defineProps<{
    modelValue: Api.Foundation.DictTypeSearchParams
  }>()

  const emit = defineEmits<{
    (e: 'update:modelValue', value: Api.Foundation.DictTypeSearchParams): void
    (e: 'search', value: Api.Foundation.DictTypeSearchParams): void
    (e: 'reset'): void
  }>()

  const formData = computed({
    get: () => props.modelValue,
    set: (value) => emit('update:modelValue', value)
  })

  const formItems: SearchFormItem[] = [
    {
      label: '编码',
      key: 'dictCode',
      type: 'input',
      props: { clearable: true, placeholder: '字典编码' }
    },
    {
      label: '名称',
      key: 'dictName',
      type: 'input',
      props: { clearable: true, placeholder: '字典名称' }
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

  function handleSearch(value: Api.Foundation.DictTypeSearchParams) {
    emit('search', value)
  }
</script>

<style scoped lang="scss">
  .dict-type-search {
    :deep(.el-form-item) {
      margin-bottom: 12px;
    }

    :deep(.el-form-item__label) {
      height: 20px;
      line-height: 20px;
      margin-bottom: 4px;
      color: var(--el-text-color-regular);
    }
  }
</style>
