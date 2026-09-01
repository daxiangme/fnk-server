<#assign entityName = entity?replace("DO", "")>
<#if controllerMappingHyphenStyle??>
  <#assign controllerPath = controllerMappingHyphen?replace("-","/")?replace("_","/")>
<#else>
  <#assign controllerPath = table.entityPath?replace("-","/")?replace("_","/")>
</#if>
<#assign permissionPrefix = table.entityPath?replace("-","_")?replace("_",":")>
<#if package.ModuleName?? && package.ModuleName != "">
  <#assign permissionPrefix = package.ModuleName + ":" + permissionPrefix>
</#if>
<template>
  <div class="art-full-height">
    <ArtSearchBar
      v-show="showSearchBar"
      v-model="query"
      :items="searchItems"
      :show-expand="false"
      @search="loadData"
      @reset="resetQuery"
    />

    <ElCard class="art-table-card" :style="{ marginTop: showSearchBar ? '12px' : '0' }">
      <ArtTableHeader
        v-model:columns="columnChecks"
        v-model:showSearchBar="showSearchBar"
        :loading="loading"
        @refresh="refreshData"
      >
        <template #left>
          <ElButton type="primary" v-auth="'${permissionPrefix}:create'" @click="openCreate">
            新增
          </ElButton>
        </template>
      </ArtTableHeader>

      <FnkTable
        :loading="loading"
        :data="data"
        :columns="columns"
        :pagination="pagination"
        @pagination:size-change="handleSizeChange"
        @pagination:current-change="handleCurrentChange"
      />
    </ElCard>

    <ElDialog v-model="dialogVisible" :title="form.id ? '编辑${table.comment!entityName}' : '新增${table.comment!entityName}'" width="620px">
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
        <ElButton @click="dialogVisible = false">取消</ElButton>
        <ElButton type="primary" :loading="saving" @click="submit">保存</ElButton>
      </template>
    </ElDialog>
  </div>
</template>

<script setup lang="ts">
  import { h, ref } from 'vue'
  import { ElTag, type FormRules } from 'element-plus'
  import { useCrudTable } from '@/hooks/core/useCrudTable'
  import type { SearchFormItem } from '@/components/core/forms/art-search-bar/index.vue'
  import {
    fetchDelete${entityName},
    fetchGet${entityName}List,
    fetchSave${entityName},
    type ${entityName}Form,
    type ${entityName}Query,
    type ${entityName}Record
  } from './api'

  defineOptions({ name: '${entityName}' })

  const showSearchBar = ref(true)

  const searchItems: SearchFormItem[] = [
<#list table.fields as field>
<#if !field.keyFlag>
<#if field.propertyType == "Boolean" || field.propertyType == "boolean">
    {
      label: '${field.comment!field.propertyName}',
      key: '${field.propertyName}',
      type: 'select',
      props: {
        clearable: true,
        placeholder: '全部',
        options: [
          { label: '启用', value: true },
          { label: '禁用', value: false }
        ]
      }
    },
<#elseif field.propertyType == "Integer" || field.propertyType == "Long" || field.propertyType == "Float" || field.propertyType == "Double">
    {
      label: '${field.comment!field.propertyName}',
      key: '${field.propertyName}',
      type: 'number',
      props: { clearable: true, placeholder: '请输入${field.comment!field.propertyName}' }
    },
<#else>
    {
      label: '${field.comment!field.propertyName}',
      key: '${field.propertyName}',
      type: 'input',
      props: { clearable: true, placeholder: '请输入${field.comment!field.propertyName}' }
    },
</#if>
</#if>
</#list>
  ]

  const {
    loading,
    saving,
    dialogVisible,
    formRef,
    query,
    form,
    data,
    pagination,
    columns,
    columnChecks,
    loadData,
    resetQuery,
    openCreate,
    openEdit,
    submit,
    remove,
    handleSizeChange,
    handleCurrentChange,
    refreshData
  } = useCrudTable<${entityName}Record, ${entityName}Query, ${entityName}Form>({
    defaultQuery: () => ({
      page: 1,
      pageSize: 10,
<#list table.fields as field>
<#if !field.keyFlag>
      ${field.propertyName}: undefined,
</#if>
</#list>
    }),
    defaultForm: () => ({
      id: '',
<#list table.fields as field>
<#if !field.keyFlag>
<#if field.propertyType == "Boolean" || field.propertyType == "boolean">
      ${field.propertyName}: false,
<#elseif field.propertyType == "Integer" || field.propertyType == "Long" || field.propertyType == "Float" || field.propertyType == "Double">
      ${field.propertyName}: 0,
<#else>
      ${field.propertyName}: '',
</#if>
</#if>
</#list>
    }),
    listApi: fetchGet${entityName}List,
    saveApi: fetchSave${entityName},
    removeApi: (row) => fetchDelete${entityName}(row.id || ''),
    getEditForm: (row) => ({ ...row }),
    removeOptions: {
      message: (row) => '确定删除${table.comment!entityName}「' + (row.id || '') + '」吗？',
      successMessage: '删除成功'
    },
    columnsFactory: () => [
      { type: 'globalIndex', label: '序号', width: 70 },
<#list table.fields as field>
<#if !field.keyFlag>
<#if field.propertyType == "Boolean" || field.propertyType == "boolean">
      {
        prop: '${field.propertyName}',
        label: '${field.comment!field.propertyName}',
        minWidth: 120,
        formatter: (row) =>
          h(ElTag, { type: row.${field.propertyName} ? 'success' : 'info' }, () =>
            row.${field.propertyName} ? '启用' : '禁用'
          )
      },
<#else>
      { prop: '${field.propertyName}', label: '${field.comment!field.propertyName}', minWidth: 140 },
</#if>
</#if>
</#list>
      {
        type: 'operation',
        prop: 'operation',
        label: '操作',
        width: 160,
        fixed: 'right',
        actions: [
          {
            key: 'edit',
            label: '编辑',
            permission: '${permissionPrefix}:update',
            onClick: (row) => openEdit(row)
          },
          {
            key: 'delete',
            label: '删除',
            type: 'error',
            permission: '${permissionPrefix}:delete',
            onClick: (row) => remove(row)
          }
        ]
      }
    ]
  })

  const rules: FormRules = {
<#list table.fields as field>
<#if !field.keyFlag>
    ${field.propertyName}: [{ required: true, message: '请输入${field.comment!field.propertyName}', trigger: 'blur' }],
</#if>
</#list>
  }

</script>
