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
          <ElButton type="primary" v-auth="'system:sms-channel:create'" @click="openCreate">
            新增渠道
          </ElButton>
        </template>
      </ArtTableHeader>

      <FnkTable
        row-key="id"
        action-mode="menu"
        :loading="loading"
        :data="data"
        :columns="columns"
        :pagination="pagination"
        empty-text="暂无短信渠道"
        @pagination:size-change="handleSizeChange"
        @pagination:current-change="handleCurrentChange"
      />
    </ElCard>

    <ElDialog v-model="dialogVisible" :title="form.id ? '编辑短信渠道' : '新增短信渠道'" width="680px">
      <ElForm ref="formRef" :model="form" :rules="rules" label-width="96px">
        <ElFormItem label="渠道名称" prop="channelName">
          <ElInput v-model="form.channelName" placeholder="请输入渠道名称" />
        </ElFormItem>
        <ElFormItem label="渠道编码" prop="channelCode">
          <ElSelect v-model="form.channelCode" filterable allow-create default-first-option>
            <ElOption label="DEBUG" value="DEBUG" />
            <ElOption label="阿里云" value="ALIYUN" />
            <ElOption label="腾讯云" value="TENCENT" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="AccessKey">
          <ElInput v-model="form.accessKey" placeholder="请输入 AccessKey" />
        </ElFormItem>
        <ElFormItem label="AccessSecret">
          <ElInput v-model="form.accessSecret" show-password placeholder="请输入 AccessSecret" />
        </ElFormItem>
        <ElFormItem label="短信签名">
          <ElInput v-model="form.signature" placeholder="请输入短信签名" />
        </ElFormItem>
        <ElFormItem label="服务端点">
          <ElInput v-model="form.endpoint" placeholder="请输入服务端点" />
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
    fetchDeleteSmsChannel,
    fetchGetSmsChannelList,
    fetchSaveSmsChannel
  } from '@/api/messages'
  import { useCrudTable } from '@/hooks/core/useCrudTable'
  import type { SearchFormItem } from '@/components/core/forms/art-search-bar/index.vue'

  defineOptions({ name: 'SystemMessagesSmsChannel' })

  type ChannelForm = Partial<Api.Messages.SmsChannelItem>
  const showSearchBar = ref(true)

  const searchItems: SearchFormItem[] = [
    { label: '渠道名称', key: 'channelName', type: 'input', props: { clearable: true } },
    { label: '渠道编码', key: 'channelCode', type: 'input', props: { clearable: true } },
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
  } = useCrudTable<Api.Messages.SmsChannelItem, Api.Messages.SmsChannelSearchParams, ChannelForm>({
    defaultQuery: () => ({ page: 1, pageSize: 10 }),
    defaultForm: () => ({
      id: '',
      channelName: '',
      channelCode: 'DEBUG',
      accessKey: '',
      accessSecret: '',
      signature: '',
      endpoint: '',
      status: true,
      remark: ''
    }),
    listApi: fetchGetSmsChannelList,
    saveApi: fetchSaveSmsChannel,
    removeApi: (row) => fetchDeleteSmsChannel(row.id),
    getEditForm: (row) => ({ ...row }),
    removeOptions: { message: (row) => `确定删除短信渠道「${row.channelName}」吗？` },
    columnsFactory: () => [
      { type: 'globalIndex', label: '序号', width: 70 },
      { prop: 'channelName', label: '渠道名称', minWidth: 160 },
      { prop: 'channelCode', label: '渠道编码', width: 130 },
      { prop: 'signature', label: '短信签名', minWidth: 130 },
      {
        prop: 'status',
        label: '状态',
        width: 100,
        formatter: (row) =>
          h(ElTag, { type: row.status ? 'success' : 'info' }, () => (row.status ? '启用' : '停用'))
      },
      { prop: 'updateTime', label: '更新时间', minWidth: 170 },
      {
        type: 'operation',
        prop: 'operation',
        label: '操作',
        width: 150,
        fixed: 'right',
        actions: [
          { key: 'edit', label: '编辑', permission: 'system:sms-channel:update', onClick: (row) => openEdit(row) },
          {
            key: 'delete',
            label: '删除',
            type: 'error',
            permission: 'system:sms-channel:delete',
            onClick: (row) => remove(row)
          }
        ]
      }
    ]
  })

  const rules: FormRules = {
    channelName: [{ required: true, message: '请输入渠道名称', trigger: 'blur' }],
    channelCode: [{ required: true, message: '请输入渠道编码', trigger: 'change' }]
  }
</script>
