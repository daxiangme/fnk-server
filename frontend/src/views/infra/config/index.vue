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
          <ElButton type="primary" v-auth="'infra:config:create'" @click="openCreate">
            新增参数
          </ElButton>
        </template>
      </ArtTableHeader>

      <FnkTable
        action-mode="menu"
        row-key="id"
        :loading="loading"
        :data="data"
        :columns="columns"
        :pagination="pagination"
        empty-text="暂无系统参数"
        @pagination:size-change="handleSizeChange"
        @pagination:current-change="handleCurrentChange"
      />
    </ElCard>

    <ElDialog v-model="dialogVisible" :title="form.id ? '编辑参数' : '新增参数'" width="640px">
      <ElForm ref="formRef" :model="form" :rules="rules" label-width="90px">
        <ElFormItem label="参数名称" prop="configName">
          <ElInput v-model="form.configName" placeholder="请输入参数名称" />
        </ElFormItem>
        <ElFormItem label="参数键" prop="configKey">
          <ElInput v-model="form.configKey" placeholder="例如 infra.file.local-path" />
        </ElFormItem>
        <ElFormItem label="参数分组" prop="groupCode">
          <ElInput v-model="form.groupCode" placeholder="例如 file / system" />
        </ElFormItem>
        <ElFormItem label="值类型" prop="valueType">
          <ElSelect v-model="form.valueType" class="w-full" placeholder="请选择值类型">
            <ElOption label="字符串" value="string" />
            <ElOption label="数字" value="number" />
            <ElOption label="布尔" value="boolean" />
            <ElOption label="JSON" value="json" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="参数值">
          <ElInput v-model="form.configValue" type="textarea" :rows="4" placeholder="请输入参数值" />
        </ElFormItem>
        <ElFormItem label="状态">
          <ElSwitch v-model="form.status" active-text="启用" inactive-text="停用" />
        </ElFormItem>
        <ElFormItem label="前端可见">
          <ElSwitch v-model="form.visible" active-text="可见" inactive-text="隐藏" />
        </ElFormItem>
        <ElFormItem label="备注">
          <ElInput v-model="form.remark" type="textarea" :rows="3" />
        </ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="dialogVisible = false">取消</ElButton>
        <ElButton type="primary" :loading="saving" @click="submit">保存</ElButton>
      </template>
    </ElDialog>
  </div>
</template>

<script setup lang="ts">
  import { h } from 'vue'
  import { ElTag, type FormRules } from 'element-plus'
  import {
    fetchDeleteInfraConfig,
    fetchGetInfraConfigList,
    fetchSaveInfraConfig
  } from '@/api/infra'
  import { useCrudTable } from '@/hooks/core/useCrudTable'
  import type { SearchFormItem } from '@/components/core/forms/art-search-bar/index.vue'

  defineOptions({ name: 'InfraConfig' })

  type ConfigForm = Pick<
    Api.Infra.ConfigItem,
    'configName' | 'configKey' | 'groupCode' | 'valueType' | 'visible' | 'status'
  > &
    Partial<Pick<Api.Infra.ConfigItem, 'id' | 'configValue' | 'remark'>>

  const showSearchBar = ref(true)

  const searchItems: SearchFormItem[] = [
    { label: '参数名称', key: 'configName', type: 'input', props: { clearable: true } },
    { label: '参数键', key: 'configKey', type: 'input', props: { clearable: true } },
    { label: '参数分组', key: 'groupCode', type: 'input', props: { clearable: true } },
    {
      label: '状态',
      key: 'status',
      type: 'select',
      props: {
        clearable: true,
        options: [
          { label: '启用', value: true },
          { label: '停用', value: false }
        ]
      }
    }
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
  } = useCrudTable<Api.Infra.ConfigItem, Api.Infra.ConfigSearchParams, ConfigForm>({
    defaultQuery: () => ({
      page: 1,
      pageSize: 10,
      configName: undefined,
      configKey: undefined,
      groupCode: undefined,
      status: undefined
    }),
    defaultForm: () => ({
      id: '',
      configName: '',
      configKey: '',
      configValue: '',
      groupCode: 'system',
      valueType: 'string',
      visible: true,
      status: true,
      remark: ''
    }),
    listApi: fetchGetInfraConfigList,
    saveApi: fetchSaveInfraConfig,
    removeApi: (row) => fetchDeleteInfraConfig(row.id),
    getEditForm: (row) => ({ ...row }),
    removeOptions: {
      message: (row) => `确定删除参数「${row.configName}」吗？`
    },
    columnsFactory: () => [
      { type: 'globalIndex', label: '序号', width: 70 },
      { prop: 'configName', label: '参数名称', minWidth: 160 },
      { prop: 'configKey', label: '参数键', minWidth: 220 },
      { prop: 'groupCode', label: '分组', width: 110 },
      { prop: 'valueType', label: '值类型', width: 110 },
      {
        prop: 'status',
        label: '状态',
        width: 100,
        formatter: (row) =>
          h(ElTag, { type: row.status ? 'success' : 'info' }, () => (row.status ? '启用' : '停用'))
      },
      {
        prop: 'visible',
        label: '可见',
        width: 100,
        formatter: (row) =>
          h(ElTag, { type: row.visible ? 'success' : 'info' }, () => (row.visible ? '可见' : '隐藏'))
      },
      { prop: 'remark', label: '备注', minWidth: 180 },
      { prop: 'updateTime', label: '更新时间', minWidth: 170 },
      {
        type: 'operation',
        prop: 'operation',
        label: '操作',
        width: 150,
        fixed: 'right',
        actions: [
          {
            key: 'edit',
            label: '编辑',
            permission: 'infra:config:update',
            onClick: (row) => openEdit(row)
          },
          {
            key: 'delete',
            label: '删除',
            type: 'error',
            permission: 'infra:config:delete',
            onClick: (row) => remove(row)
          }
        ]
      }
    ]
  })

  const rules: FormRules = {
    configName: [{ required: true, message: '请输入参数名称', trigger: 'blur' }],
    configKey: [{ required: true, message: '请输入参数键', trigger: 'blur' }],
    groupCode: [{ required: true, message: '请输入参数分组', trigger: 'blur' }],
    valueType: [{ required: true, message: '请选择值类型', trigger: 'change' }]
  }
</script>
