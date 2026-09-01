<template>
  <div class="art-full-height">
    <ArtSearchBar v-show="showSearchBar" v-model="query" :items="searchItems" :show-expand="false" @search="loadData" @reset="resetQuery" />
    <ElCard class="art-table-card" :style="{ marginTop: showSearchBar ? '12px' : '0' }">
      <ArtTableHeader v-model:columns="columnChecks" v-model:showSearchBar="showSearchBar" :loading="loading" @refresh="refreshData">
        <template #left>
          <ElButton type="primary" v-auth="'system:mail-account:create'" @click="openCreate">新增账号</ElButton>
        </template>
      </ArtTableHeader>
      <FnkTable row-key="id" action-mode="menu" :loading="loading" :data="data" :columns="columns" :pagination="pagination" empty-text="暂无邮箱账号" @pagination:size-change="handleSizeChange" @pagination:current-change="handleCurrentChange" />
    </ElCard>

    <ElDialog v-model="dialogVisible" :title="form.id ? '编辑邮箱账号' : '新增邮箱账号'" width="680px">
      <ElForm ref="formRef" :model="form" :rules="rules" label-width="104px">
        <ElFormItem label="邮箱地址" prop="mail">
          <ElInput v-model="form.mail" />
        </ElFormItem>
        <ElFormItem label="SMTP 用户" prop="username">
          <ElInput v-model="form.username" />
        </ElFormItem>
        <ElFormItem label="SMTP 密码">
          <ElInput v-model="form.password" show-password placeholder="编辑时留空表示沿用原密码" />
        </ElFormItem>
        <ElFormItem label="SMTP 主机" prop="host">
          <ElInput v-model="form.host" placeholder="例如 smtp.example.com" />
        </ElFormItem>
        <ElFormItem label="SMTP 端口" prop="port">
          <ElInputNumber v-model="form.port" :min="1" :max="65535" class="w-full" />
        </ElFormItem>
        <ElFormItem label="SSL">
          <ElSwitch v-model="form.sslEnable" active-text="启用" inactive-text="关闭" />
        </ElFormItem>
        <ElFormItem label="STARTTLS">
          <ElSwitch v-model="form.starttlsEnable" active-text="启用" inactive-text="关闭" />
        </ElFormItem>
        <ElFormItem label="状态">
          <ElSwitch v-model="form.status" active-text="启用" inactive-text="停用" />
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
    fetchDeleteMailAccount,
    fetchGetMailAccountList,
    fetchSaveMailAccount
  } from '@/api/messages'
  import { useCrudTable } from '@/hooks/core/useCrudTable'
  import type { SearchFormItem } from '@/components/core/forms/art-search-bar/index.vue'

  defineOptions({ name: 'SystemMessagesMailAccount' })

  type AccountForm = Partial<Api.Messages.MailAccountItem>
  const showSearchBar = ref(true)
  const searchItems: SearchFormItem[] = [
    { label: '邮箱地址', key: 'mail', type: 'input', props: { clearable: true } },
    {
      label: '状态',
      key: 'status',
      type: 'select',
      props: { clearable: true, options: [{ label: '启用', value: true }, { label: '停用', value: false }] }
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
  } = useCrudTable<Api.Messages.MailAccountItem, Api.Messages.MailAccountSearchParams, AccountForm>({
    defaultQuery: () => ({ page: 1, pageSize: 10 }),
    defaultForm: () => ({
      id: '',
      mail: '',
      username: '',
      password: '',
      host: '',
      port: 465,
      sslEnable: true,
      starttlsEnable: false,
      status: true,
      remark: ''
    }),
    listApi: fetchGetMailAccountList,
    saveApi: fetchSaveMailAccount,
    removeApi: (row) => fetchDeleteMailAccount(row.id),
    getEditForm: (row) => ({ ...row, password: '' }),
    removeOptions: { message: (row) => `确定删除邮箱账号「${row.mail}」吗？` },
    columnsFactory: () => [
      { type: 'globalIndex', label: '序号', width: 70 },
      { prop: 'mail', label: '邮箱地址', minWidth: 180 },
      { prop: 'host', label: 'SMTP 主机', minWidth: 180 },
      { prop: 'port', label: '端口', width: 90 },
      {
        prop: 'sslEnable',
        label: 'SSL',
        width: 90,
        formatter: (row) => h(ElTag, { type: row.sslEnable ? 'success' : 'info' }, () => (row.sslEnable ? '启用' : '关闭'))
      },
      {
        prop: 'status',
        label: '状态',
        width: 90,
        formatter: (row) => h(ElTag, { type: row.status ? 'success' : 'info' }, () => (row.status ? '启用' : '停用'))
      },
      { prop: 'updateTime', label: '更新时间', minWidth: 170 },
      {
        type: 'operation',
        prop: 'operation',
        label: '操作',
        width: 150,
        fixed: 'right',
        actions: [
          { key: 'edit', label: '编辑', permission: 'system:mail-account:update', onClick: (row) => openEdit(row) },
          { key: 'delete', label: '删除', type: 'error', permission: 'system:mail-account:delete', onClick: (row) => remove(row) }
        ]
      }
    ]
  })

  const rules: FormRules = {
    mail: [{ required: true, message: '请输入邮箱地址', trigger: 'blur' }],
    username: [{ required: true, message: '请输入 SMTP 用户', trigger: 'blur' }],
    host: [{ required: true, message: '请输入 SMTP 主机', trigger: 'blur' }],
    port: [{ required: true, message: '请输入 SMTP 端口', trigger: 'change' }]
  }
</script>
