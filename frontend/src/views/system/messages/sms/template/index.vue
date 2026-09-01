<template>
  <div class="art-full-height">
    <ArtSearchBar v-show="showSearchBar" v-model="query" :items="searchItems" :show-expand="false" @search="loadData" @reset="resetQuery" />

    <ElCard class="art-table-card" :style="{ marginTop: showSearchBar ? '12px' : '0' }">
      <ArtTableHeader v-model:columns="columnChecks" v-model:showSearchBar="showSearchBar" :loading="loading" @refresh="refreshData">
        <template #left>
          <ElButton type="primary" v-auth="'system:sms-template:create'" @click="openCreate">
            新增模板
          </ElButton>
        </template>
      </ArtTableHeader>

      <FnkTable row-key="id" action-mode="menu" :loading="loading" :data="data" :columns="columns" :pagination="pagination" empty-text="暂无短信模板" @pagination:size-change="handleSizeChange" @pagination:current-change="handleCurrentChange" />
    </ElCard>

    <ElDialog v-model="dialogVisible" :title="form.id ? '编辑短信模板' : '新增短信模板'" width="720px">
      <ElForm ref="formRef" :model="form" :rules="rules" label-width="112px">
        <ElFormItem label="短信渠道" prop="channelId">
          <ElSelect v-model="form.channelId" class="w-full" placeholder="请选择短信渠道">
            <ElOption v-for="item in channelOptions" :key="item.id" :label="item.channelName" :value="item.id" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="模板名称" prop="templateName">
          <ElInput v-model="form.templateName" />
        </ElFormItem>
        <ElFormItem label="模板编码" prop="templateCode">
          <ElInput v-model="form.templateCode" placeholder="例如 LOGIN_CAPTCHA" />
        </ElFormItem>
        <ElFormItem label="供应商模板码">
          <ElInput v-model="form.providerTemplateCode" />
        </ElFormItem>
        <ElFormItem label="模板类型">
          <ElSelect v-model="form.templateType" class="w-full" clearable>
            <ElOption label="验证码" value="CAPTCHA" />
            <ElOption label="通知" value="NOTICE" />
            <ElOption label="营销" value="MARKETING" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="模板内容" prop="content">
          <ElInput v-model="form.content" type="textarea" :rows="5" placeholder="支持 {code} 形式占位符" />
        </ElFormItem>
        <ElFormItem label="参数列表">
          <ElInput v-model="form.paramsText" type="textarea" :rows="3" placeholder="每行一个参数；留空则由模板内容自动提取" />
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

    <ElDialog v-model="sendVisible" title="发送测试短信" width="560px">
      <ElForm label-width="96px">
        <ElFormItem label="手机号" required>
          <ElInput v-model="sendForm.mobile" />
        </ElFormItem>
        <ElFormItem v-for="param in activeTemplate?.params || []" :key="param" :label="param" required>
          <ElInput v-model="sendForm.params[param]" />
        </ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="sendVisible = false">取消</ElButton>
        <ElButton type="primary" :loading="sending" @click="sendTemplate">发送</ElButton>
      </template>
    </ElDialog>
  </div>
</template>

<script setup lang="ts">
  import { h } from 'vue'
  import { ElMessage, ElTag, type FormRules } from 'element-plus'
  import {
    fetchDeleteSmsTemplate,
    fetchGetEnabledSmsChannels,
    fetchGetSmsTemplateList,
    fetchSaveSmsTemplate,
    fetchSendSmsTemplate
  } from '@/api/messages'
  import { useCrudTable } from '@/hooks/core/useCrudTable'
  import type { SearchFormItem } from '@/components/core/forms/art-search-bar/index.vue'
  import { joinText, paramsFromKeys, splitText, statusText } from '../../utils'

  defineOptions({ name: 'SystemMessagesSmsTemplate' })

  type TemplateForm = Partial<Api.Messages.SmsTemplateItem> & { paramsText?: string }
  const showSearchBar = ref(true)
  const channelOptions = ref<Api.Messages.SmsChannelItem[]>([])
  const sendVisible = ref(false)
  const sending = ref(false)
  const activeTemplate = ref<Api.Messages.SmsTemplateItem>()
  const sendForm = reactive({ mobile: '', params: {} as Record<string, string> })

  const searchItems: SearchFormItem[] = [
    { label: '模板名称', key: 'templateName', type: 'input', props: { clearable: true } },
    { label: '模板编码', key: 'templateCode', type: 'input', props: { clearable: true } },
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
  } = useCrudTable<Api.Messages.SmsTemplateItem, Api.Messages.SmsTemplateSearchParams, TemplateForm>({
    defaultQuery: () => ({ page: 1, pageSize: 10 }),
    defaultForm: () => ({
      id: '',
      channelId: '',
      templateName: '',
      templateCode: '',
      providerTemplateCode: '',
      templateType: 'CAPTCHA',
      content: '',
      params: [],
      paramsText: '',
      status: true,
      remark: ''
    }),
    listApi: fetchGetSmsTemplateList,
    saveApi: (payload) => fetchSaveSmsTemplate({ ...payload, params: splitText(payload.paramsText) }),
    removeApi: (row) => fetchDeleteSmsTemplate(row.id),
    getEditForm: (row) => ({ ...row, paramsText: joinText(row.params) }),
    removeOptions: { message: (row) => `确定删除短信模板「${row.templateName}」吗？` },
    columnsFactory: () => [
      { type: 'globalIndex', label: '序号', width: 70 },
      { prop: 'templateName', label: '模板名称', minWidth: 160 },
      { prop: 'templateCode', label: '模板编码', minWidth: 160 },
      { prop: 'templateType', label: '类型', width: 100 },
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
        width: 210,
        fixed: 'right',
        actions: [
          { key: 'send', label: '测试', type: 'success', permission: 'system:sms-template:send', onClick: openSend },
          { key: 'edit', label: '编辑', permission: 'system:sms-template:update', onClick: (row) => openEdit(row) },
          { key: 'delete', label: '删除', type: 'error', permission: 'system:sms-template:delete', onClick: (row) => remove(row) }
        ]
      }
    ]
  })

  const rules: FormRules = {
    channelId: [{ required: true, message: '请选择短信渠道', trigger: 'change' }],
    templateName: [{ required: true, message: '请输入模板名称', trigger: 'blur' }],
    templateCode: [{ required: true, message: '请输入模板编码', trigger: 'blur' }],
    content: [{ required: true, message: '请输入模板内容', trigger: 'blur' }]
  }

  onMounted(async () => {
    channelOptions.value = await fetchGetEnabledSmsChannels()
  })

  function openSend(row: Api.Messages.SmsTemplateItem) {
    activeTemplate.value = row
    sendForm.mobile = ''
    sendForm.params = paramsFromKeys(row.params)
    sendVisible.value = true
  }

  async function sendTemplate() {
    if (!activeTemplate.value) return
    sending.value = true
    try {
      const log = await fetchSendSmsTemplate({
        templateCode: activeTemplate.value.templateCode,
        mobile: sendForm.mobile,
        params: sendForm.params
      })
      if (log.sendStatus === 'SUCCESS') {
        ElMessage.success(statusText(log.sendStatus))
        sendVisible.value = false
      } else {
        ElMessage.error(log.errorMsg || statusText(log.sendStatus))
      }
    } finally {
      sending.value = false
    }
  }
</script>
