<#assign entityName = entity?replace("DO", "")>
<template>
  <ElDialog v-model="visible" :title="title" width="620px" @closed="handleClosed">
    <ElForm ref="formRef" :model="form" :rules="rules" label-width="110px">
<#list table.fields as field>
<#if !field.keyFlag>
      <ElFormItem label="${field.comment!field.propertyName}" prop="${field.propertyName}">
<#if field.propertyType == "Boolean" || field.propertyType == "boolean">
        <ElSwitch v-model="form.${field.propertyName}" />
<#elseif field.propertyType == "Integer" || field.propertyType == "Long" || field.propertyType == "Float" || field.propertyType == "Double">
        <ElInputNumber v-model="form.${field.propertyName}" :min="0" />
<#elseif field.propertyType == "Date" || field.propertyType == "LocalDate" || field.propertyType == "LocalDateTime">
        <ElDatePicker v-model="form.${field.propertyName}" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" />
<#else>
        <ElInput v-model="form.${field.propertyName}" placeholder="请输入${field.comment!field.propertyName}" />
</#if>
      </ElFormItem>
</#if>
</#list>
    </ElForm>
    <template #footer>
      <ElButton @click="visible = false">取消</ElButton>
      <ElButton type="primary" :loading="loading" @click="handleSubmit">保存</ElButton>
    </template>
  </ElDialog>
</template>

<script setup lang="ts">
  import type { FormInstance, FormRules } from 'element-plus'
  import type { ${entityName}Form } from './api'

  const visible = defineModel<boolean>('visible', { default: false })
  const form = defineModel<${entityName}Form>('form', { required: true })

  const props = withDefaults(
    defineProps<{
      title?: string
      loading?: boolean
      rules?: FormRules
    }>(),
    {
      title: '${table.comment!entityName}',
      loading: false,
      rules: () => ({})
    }
  )

  const emit = defineEmits<{
    submit: []
    closed: []
  }>()

  const formRef = ref<FormInstance>()
  const rules = computed(() => props.rules)

  async function handleSubmit() {
    if (!formRef.value) return
    await formRef.value.validate()
    emit('submit')
  }

  function handleClosed() {
    formRef.value?.clearValidate()
    emit('closed')
  }
</script>
