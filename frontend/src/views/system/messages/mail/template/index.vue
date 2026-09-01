<template>
  <div class="art-full-height">
    <ArtSearchBar v-show="showSearchBar" v-model="query" :items="searchItems" :show-expand="false" @search="loadData" @reset="resetQuery" />
    <ElCard class="art-table-card" :style="{ marginTop: showSearchBar ? '12px' : '0' }">
      <ArtTableHeader v-model:columns="columnChecks" v-model:showSearchBar="showSearchBar" :loading="loading" @refresh="refreshData">
        <template #left>
          <ElButton type="primary" v-auth="'system:mail-template:create'" @click="openCreate">新增模板</ElButton>
        </template>
      </ArtTableHeader>
      <FnkTable row-key="id" action-mode="menu" :loading="loading" :data="data" :columns="columns" :pagination="pagination" empty-text="暂无邮件模板" @pagination:size-change="handleSizeChange" @pagination:current-change="handleCurrentChange" />
    </ElCard>

    <ElDialog v-model="dialogVisible" :title="form.id ? '编辑邮件模板' : '新增邮件模板'" width="760px">
      <ElForm ref="formRef" :model="form" :rules="rules" label-width="104px">
        <ElFormItem label="邮箱账号" prop="accountId">
          <ElSelect v-model="form.accountId" class="w-full" placeholder="请选择邮箱账号">
            <ElOption v-for="item in accountOptions" :key="item.id" :label="item.mail" :value="item.id" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="模板名称" prop="name">
          <ElInput v-model="form.name" />
        </ElFormItem>
        <ElFormItem label="模板编码" prop="code">
          <ElInput v-model="form.code" placeholder="例如 ORDER_NOTICE" />
        </ElFormItem>
        <ElFormItem label="发件昵称">
          <ElInput v-model="form.fromName" />
        </ElFormItem>
        <ElFormItem label="邮件标题" prop="title">
          <ElInput v-model="form.title" placeholder="支持 {name} 形式占位符" />
        </ElFormItem>
        <ElFormItem label="邮件内容" prop="content">
          <ElInput v-model="form.content" type="textarea" :rows="6" placeholder="支持 HTML 和 {name} 形式占位符" />
        </ElFormItem>
        <ElFormItem label="参数列表">
          <ElInput v-model="form.paramsText" type="textarea" :rows="3" placeholder="每行一个参数；留空则由标题和内容自动提取" />
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

    <ElDialog v-model="sendVisible" title="发送测试邮件" width="560px">
      <ElForm label-width="96px">
        <ElFormItem label="收件邮箱" required>
          <ElInput v-model="sendForm.toMail" />
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
    fetchDeleteMailTemplate,
    fetchGetEnabledMailAccounts,
    fetchGetMailTemplateList,
    fetchSaveMailTemplate,
    fetchSendMailTemplate
  } from '@/api/messages'
  import { useCrudTable } from '@/hooks/core/useCrudTable'
  import type { SearchFormItem } from '@/components/core/forms/art-search-bar/index.vue'
  import { joinText, paramsFromKeys, splitText, statusText } from '../../utils'

  defineOptions({ name: 'SystemMessagesMailTemplate' })

  type TemplateForm = Partial<Api.Messages.MailTemplateItem> & { paramsText?: string }
  const showSearchBar = ref(true)
  const accountOptions = ref<Api.Messages.MailAccountItem[]>([])
  const sendVisible = ref(false)
  const sending = ref(false)
  const activeTemplate = ref<Api.Messages.MailTemplateItem>()
  const sendForm = reactive({ toMail: '', params: {} as Record<string, string> })

  const searchItems: SearchFormItem[] = [
    { label: '模板名称', key: 'name', type: 'input', props: { clearable: true } },
    { label: '模板编码', key: 'code', type: 'input', props: { clearable: true } },
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
  } = useCrudTable<Api.Messages.MailTemplateItem, Api.Messages.MailTemplateSearchParams, TemplateForm>({
    defaultQuery: () => ({ page: 1, pageSize: 10 }),
    defaultForm: () => ({
      id: '',
      accountId: '',
      name: '',
      code: '',
      fromName: '',
      title: '',
      content: '',
      params: [],
      paramsText: '',
      status: true,
      remark: ''
    }),
    listApi: fetchGetMailTemplateList,
    saveApi: (payload) => fetchSaveMailTemplate({ ...payload, params: splitText(payload.paramsText) }),
    removeApi: (row) => fetchDeleteMailTemplate(row.id),
    getEditForm: (row) => ({ ...row, paramsText: joinText(row.params) }),
    removeOptions: { message: (row) => `确定删除邮件模板「${row.name}」吗？` },
    columnsFactory: () => [
      { type: 'globalIndex', label: '序号', width: 70 },
      { prop: 'name', label: '模板名称', minWidth: 160 },
      { prop: 'code', label: '模板编码', minWidth: 160 },
      { prop: 'title', label: '邮件标题', minWidth: 220 },
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
          { key: 'send', label: '测试', type: 'success', permission: 'system:mail-template:send', onClick: openSend },
          { key: 'edit', label: '编辑', permission: 'system:mail-template:update', onClick: (row) => openEdit(row) },
          { key: 'delete', label: '删除', type: 'error', permission: 'system:mail-template:delete', onClick: (row) => remove(row) }
        ]
      }
    ]
  })

  const rules: FormRules = {
    accountId: [{ required: true, message: '请选择邮箱账号', trigger: 'change' }],
    name: [{ required: true, message: '请输入模板名称', trigger: 'blur' }],
    code: [{ required: true, message: '请输入模板编码', trigger: 'blur' }],
    title: [{ required: true, message: '请输入邮件标题', trigger: 'blur' }],
    content: [{ required: true, message: '请输入邮件内容', trigger: 'blur' }]
  }

  onMounted(async () => {
    accountOptions.value = await fetchGetEnabledMailAccounts()
  })

  function openSend(row: Api.Messages.MailTemplateItem) {
    activeTemplate.value = row
    sendForm.toMail = ''
    sendForm.params = paramsFromKeys(row.params)
    sendVisible.value = true
  }

  async function sendTemplate() {
    if (!activeTemplate.value) return
    sending.value = true
    try {
      const log = await fetchSendMailTemplate({
        templateCode: activeTemplate.value.code,
        toMail: sendForm.toMail,
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
