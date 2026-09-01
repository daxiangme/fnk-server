<template>
  <div class="art-full-height">
    <ArtSearchBar
      v-show="showSearchBar"
      v-model="query"
      :items="searchItems"
      :label-width="90"
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
          <ElButton type="primary" v-auth="'infra:file-config:create'" @click="openCreate">
            新增文件配置
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
        empty-text="暂无文件配置"
        @pagination:size-change="handleSizeChange"
        @pagination:current-change="handleCurrentChange"
      />
    </ElCard>

    <ElDialog
      v-model="dialogVisible"
      :title="form.id ? '编辑文件配置' : '新增文件配置'"
      width="760px"
    >
      <ElForm ref="formRef" :model="form" :rules="rules" label-width="130px">
        <ElFormItem label="配置名称" prop="name">
          <ElInput v-model="form.name" placeholder="请输入配置名称" />
        </ElFormItem>
        <ElFormItem label="存储类型" prop="storageType">
          <ElSelect
            v-model="form.storageType"
            class="w-full"
            :disabled="Boolean(form.id)"
            placeholder="请选择存储类型"
          >
            <ElOption label="本地存储" value="local" />
            <ElOption label="S3 兼容存储" value="s3" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="最大上传大小" prop="maxSizeMb">
          <ElInputNumber v-model="form.maxSizeMb" :min="1" :max="10240" class="w-full">
            <template #suffix>MB</template>
          </ElInputNumber>
        </ElFormItem>

        <template v-if="form.storageType === 'local'">
          <ElFormItem label="本地存储路径" prop="basePath">
            <ElInput v-model="form.basePath" placeholder="例如 dev/uploads" />
          </ElFormItem>
          <ElFormItem label="访问域名">
            <ElInput v-model="form.domain" placeholder="不填则使用当前后端地址" />
          </ElFormItem>
        </template>

        <template v-if="form.storageType === 's3'">
          <ElFormItem label="Endpoint" prop="endpoint">
            <ElInput v-model="form.endpoint" placeholder="例如 http://127.0.0.1:9000" />
          </ElFormItem>
          <ElFormItem label="Bucket" prop="bucket">
            <ElInput v-model="form.bucket" placeholder="请输入 Bucket" />
          </ElFormItem>
          <ElFormItem label="Access Key" prop="accessKey">
            <ElInput v-model="form.accessKey" placeholder="请输入 Access Key" />
          </ElFormItem>
          <ElFormItem label="Access Secret" prop="accessSecret">
            <ElInput
              v-model="form.accessSecret"
              type="password"
              show-password
              placeholder="请输入 Access Secret"
            />
          </ElFormItem>
          <ElFormItem label="Path Style">
            <ElSwitch
              v-model="form.enablePathStyleAccess"
              active-text="启用"
              inactive-text="关闭"
            />
          </ElFormItem>
          <ElFormItem label="访问域名">
            <ElInput v-model="form.domain" placeholder="可选，自定义文件访问域名" />
          </ElFormItem>
        </template>

        <ElFormItem label="备注">
          <ElInput v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
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
  import { ElMessage, ElMessageBox, ElTag, type FormRules } from 'element-plus'
  import type { SearchFormItem } from '@/components/core/forms/art-search-bar/index.vue'
  import {
    fetchDeleteInfraFileConfig,
    fetchGetInfraFileConfigList,
    fetchSaveInfraFileConfig,
    fetchSetInfraFileConfigMaster,
    fetchTestInfraFileConfig
  } from '@/api/infra'
  import { useCrudTable } from '@/hooks/core/useCrudTable'

  defineOptions({ name: 'InfraFileConfig' })

  type FileConfigForm = Partial<Api.Infra.FileConfigItem>

  const showSearchBar = ref(true)

  const storageOptions = [
    { label: '本地存储', value: 'local' },
    { label: 'S3 兼容存储', value: 's3' }
  ]

  const searchItems: SearchFormItem[] = [
    { label: '配置名称', key: 'name', type: 'input', props: { clearable: true } },
    {
      label: '存储类型',
      key: 'storageType',
      type: 'select',
      props: { clearable: true, options: storageOptions }
    },
    {
      label: '主配置',
      key: 'master',
      type: 'select',
      props: {
        clearable: true,
        options: [
          { label: '是', value: true },
          { label: '否', value: false }
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
  } = useCrudTable<Api.Infra.FileConfigItem, Api.Infra.FileConfigSearchParams, FileConfigForm>({
    defaultQuery: () => ({
      page: 1,
      pageSize: 10,
      name: undefined,
      storageType: undefined,
      master: undefined
    }),
    defaultForm: () => ({
      id: '',
      name: '',
      storageType: 'local',
      master: false,
      basePath: 'dev/uploads',
      domain: '',
      maxSizeMb: 50,
      endpoint: '',
      bucket: '',
      accessKey: '',
      accessSecret: '',
      enablePathStyleAccess: false,
      remark: ''
    }),
    listApi: fetchGetInfraFileConfigList,
    saveApi: fetchSaveInfraFileConfig,
    removeApi: (row) => fetchDeleteInfraFileConfig(row.id),
    getEditForm: (row) => ({ ...row }),
    removeOptions: {
      message: (row) => `确定删除文件配置「${row.name}」吗？`
    },
    columnsFactory: () => [
      { type: 'globalIndex', label: '序号', width: 70 },
      { prop: 'name', label: '配置名称', minWidth: 180 },
      {
        prop: 'storageType',
        label: '存储类型',
        width: 130,
        formatter: (row) =>
          h(ElTag, null, () => (row.storageType === 'local' ? '本地存储' : 'S3 兼容'))
      },
      {
        prop: 'master',
        label: '主配置',
        width: 100,
        formatter: (row) =>
          h(ElTag, { type: row.master ? 'success' : 'info' }, () => (row.master ? '是' : '否'))
      },
      { prop: 'maxSizeMb', label: '最大大小(MB)', width: 130 },
      {
        prop: 'basePath',
        label: '存储路径/桶',
        minWidth: 220,
        formatter: (row) => row.basePath || row.bucket || '-'
      },
      { prop: 'domain', label: '访问域名', minWidth: 220 },
      { prop: 'remark', label: '备注', minWidth: 180 },
      { prop: 'updateTime', label: '更新时间', minWidth: 170 },
      {
        type: 'operation',
        prop: 'operation',
        label: '操作',
        width: 180,
        fixed: 'right',
        actions: [
          {
            key: 'master',
            label: '设为主配置',
            permission: 'infra:file-config:master',
            visible: (row) => !row.master && row.storageType === 'local',
            onClick: (row) => setMaster(row)
          },
          {
            key: 'test',
            label: '测试',
            permission: 'infra:file-config:test',
            onClick: (row) => testConfig(row)
          },
          {
            key: 'edit',
            label: '编辑',
            permission: 'infra:file-config:update',
            onClick: (row) => openEdit(row)
          },
          {
            key: 'delete',
            label: '删除',
            type: 'error',
            permission: 'infra:file-config:delete',
            visible: (row) => !row.master,
            onClick: (row) => remove(row)
          }
        ]
      }
    ]
  })

  const rules: FormRules = {
    name: [{ required: true, message: '请输入配置名称', trigger: 'blur' }],
    storageType: [{ required: true, message: '请选择存储类型', trigger: 'change' }],
    maxSizeMb: [{ required: true, message: '请输入最大上传大小', trigger: 'blur' }],
    basePath: [{ required: true, message: '请输入本地存储路径', trigger: 'blur' }],
    endpoint: [{ required: true, message: '请输入 Endpoint', trigger: 'blur' }],
    bucket: [{ required: true, message: '请输入 Bucket', trigger: 'blur' }],
    accessKey: [{ required: true, message: '请输入 Access Key', trigger: 'blur' }],
    accessSecret: [{ required: true, message: '请输入 Access Secret', trigger: 'blur' }]
  }

  async function setMaster(row: Api.Infra.FileConfigItem) {
    await ElMessageBox.confirm(`确定将「${row.name}」设置为主文件配置吗？`, '设置主配置', {
      type: 'warning'
    })
    await fetchSetInfraFileConfigMaster(row.id)
    ElMessage.success('设置成功')
    loadData()
  }

  async function testConfig(row: Api.Infra.FileConfigItem) {
    const message = await fetchTestInfraFileConfig(row.id)
    ElMessage.success(message || '测试通过')
  }
</script>
