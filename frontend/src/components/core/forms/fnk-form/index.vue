<template>
  <ElForm ref="formRef" :model="model" :rules="rules" :label-width="labelWidth" v-bind="$attrs">
    <ElRow :gutter="gutter">
      <ElCol
        v-for="item in visibleItems"
        :key="item.key"
        :xs="24"
        :sm="item.span || span"
        :md="item.span || span"
        :lg="item.span || span"
        :xl="item.span || span"
      >
        <ElFormItem :label="item.label" :prop="item.key">
          <slot :name="item.key" :item="item" :model="model">
            <component
              :is="getComponent(item)"
              v-model="model[item.key]"
              v-bind="getProps(item)"
              :disabled="resolveState(item.disabled)"
            >
              <template v-if="hasOptions(item)">
                <ElOption
                  v-for="option in item.options"
                  :key="String(option.value)"
                  :label="option.label"
                  :value="option.value"
                />
              </template>
            </component>
          </slot>
        </ElFormItem>
      </ElCol>
    </ElRow>
  </ElForm>
</template>

<script setup lang="ts">
  import type { FormInstance, FormRules } from 'element-plus'
  import {
    ElDatePicker,
    ElInput,
    ElInputNumber,
    ElSelect,
    ElSwitch,
    ElUpload
  } from 'element-plus'

  defineOptions({
    name: 'FnkForm',
    inheritAttrs: false
  })

  export type FnkFormType =
    | 'input'
    | 'textarea'
    | 'number'
    | 'select'
    | 'dict-select'
    | 'switch'
    | 'date'
    | 'datetime'
    | 'daterange'
    | 'upload'
    | 'richtext'

  export interface FnkFormOption {
    label: string
    value: string | number | boolean
  }

  export interface FnkFormItem {
    key: string
    label: string
    type?: FnkFormType
    span?: number
    required?: boolean
    props?: Record<string, any>
    options?: FnkFormOption[]
    dictCode?: string
    hidden?: boolean | ((model: Record<string, any>) => boolean)
    disabled?: boolean | ((model: Record<string, any>) => boolean)
  }

  const model = defineModel<Record<string, any>>({ default: {} })

  const props = withDefaults(
    defineProps<{
      items: FnkFormItem[]
      rules?: FormRules
      labelWidth?: string | number
      span?: number
      gutter?: number
    }>(),
    {
      items: () => [],
      rules: () => ({}),
      labelWidth: '100px',
      span: 24,
      gutter: 12
    }
  )

  const formRef = ref<FormInstance>()

  const componentMap = {
    input: ElInput,
    textarea: ElInput,
    number: ElInputNumber,
    select: ElSelect,
    'dict-select': ElSelect,
    switch: ElSwitch,
    date: ElDatePicker,
    datetime: ElDatePicker,
    daterange: ElDatePicker,
    upload: ElUpload,
    richtext: ElInput
  }

  const resolveState = (value: FnkFormItem['hidden'] | FnkFormItem['disabled']) => {
    if (typeof value === 'function') return value(model.value)
    return value === true
  }

  const visibleItems = computed(() => props.items.filter((item) => !resolveState(item.hidden)))

  const getComponent = (item: FnkFormItem) => {
    return componentMap[item.type || 'input'] || ElInput
  }

  const getProps = (item: FnkFormItem) => {
    const baseProps: Record<string, any> = {
      clearable: true,
      placeholder: item.props?.placeholder || buildPlaceholder(item)
    }

    if (item.type === 'textarea' || item.type === 'richtext') {
      baseProps.type = 'textarea'
      baseProps.rows = item.type === 'richtext' ? 6 : 3
    }
    if (item.type === 'date') {
      baseProps.type = 'date'
      baseProps.valueFormat = 'YYYY-MM-DD'
    }
    if (item.type === 'datetime') {
      baseProps.type = 'datetime'
      baseProps.valueFormat = 'YYYY-MM-DD HH:mm:ss'
    }
    if (item.type === 'daterange') {
      baseProps.type = 'datetimerange'
      baseProps.valueFormat = 'YYYY-MM-DD HH:mm:ss'
    }

    return { ...baseProps, ...(item.props || {}) }
  }

  const buildPlaceholder = (item: FnkFormItem) => {
    if (item.type === 'select' || item.type === 'dict-select' || item.type === 'date') {
      return `请选择${item.label}`
    }
    return `请输入${item.label}`
  }

  const hasOptions = (item: FnkFormItem) => {
    return (item.type === 'select' || item.type === 'dict-select') && Boolean(item.options?.length)
  }

  defineExpose({
    formRef,
    validate: () => formRef.value?.validate(),
    clearValidate: () => formRef.value?.clearValidate(),
    resetFields: () => formRef.value?.resetFields()
  })
</script>
