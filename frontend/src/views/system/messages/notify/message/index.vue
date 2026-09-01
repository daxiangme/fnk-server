<template>
  <div class="art-full-height">
    <ArtSearchBar v-show="showSearchBar" v-model="query" :items="searchItems" :show-expand="false" @search="loadData" @reset="resetQuery" />
    <ElCard class="art-table-card" :style="{ marginTop: showSearchBar ? '12px' : '0' }">
      <ArtTableHeader v-model:columns="columnChecks" v-model:showSearchBar="showSearchBar" :loading="loading" @refresh="refreshData" />
      <FnkTable row-key="id" action-mode="menu" :loading="loading" :data="data" :columns="columns" :pagination="pagination" empty-text="暂无站内信消息" @pagination:size-change="handleSizeChange" @pagination:current-change="handleCurrentChange" />
    </ElCard>

    <ElDialog v-model="detailVisible" title="站内信详情" width="680px">
      <ElDescriptions :column="1" border>
        <ElDescriptionsItem label="接收用户">{{ detail?.userId }}</ElDescriptionsItem>
        <ElDescriptionsItem label="模板编码">{{ detail?.templateCode }}</ElDescriptionsItem>
        <ElDescriptionsItem label="发送昵称">{{ detail?.templateNickname }}</ElDescriptionsItem>
        <ElDescriptionsItem label="消息类型">{{ detail?.templateType }}</ElDescriptionsItem>
        <ElDescriptionsItem label="已读状态">
          <ElTag :type="detail?.readStatus ? 'success' : 'info'">{{ detail?.readStatus ? '已读' : '未读' }}</ElTag>
        </ElDescriptionsItem>
        <ElDescriptionsItem label="读取时间">{{ detail?.readTime || '-' }}</ElDescriptionsItem>
        <ElDescriptionsItem label="模板参数">
          <pre class="m-0 whitespace-pre-wrap">{{ stringifyParams(detail?.templateParams) }}</pre>
        </ElDescriptionsItem>
        <ElDescriptionsItem label="消息内容">
          <pre class="m-0 whitespace-pre-wrap">{{ detail?.templateContent }}</pre>
        </ElDescriptionsItem>
      </ElDescriptions>
    </ElDialog>
  </div>
</template>

<script setup lang="ts">
  import { h } from 'vue'
  import { ElTag } from 'element-plus'
  import { fetchGetNotifyMessageList } from '@/api/messages'
  import { useCrudTable } from '@/hooks/core/useCrudTable'
  import type { SearchFormItem } from '@/components/core/forms/art-search-bar/index.vue'
  import { stringifyParams } from '../../utils'

  defineOptions({ name: 'SystemMessagesNotifyMessage' })

  const showSearchBar = ref(true)
  const detailVisible = ref(false)
  const detail = ref<Api.Messages.NotifyMessageItem>()
  const searchItems: SearchFormItem[] = [
    { label: '接收用户', key: 'userId', type: 'input', props: { clearable: true } },
    { label: '模板编码', key: 'templateCode', type: 'input', props: { clearable: true } },
    {
      label: '已读状态',
      key: 'readStatus',
      type: 'select',
      props: { clearable: true, options: [{ label: '已读', value: true }, { label: '未读', value: false }] }
    }
  ]

  const {
    loading,
    query,
    data,
    pagination,
    columns,
    columnChecks,
    loadData,
    resetQuery,
    handleSizeChange,
    handleCurrentChange,
    refreshData
  } = useCrudTable<Api.Messages.NotifyMessageItem, Api.Messages.NotifyMessageSearchParams, Record<string, never>>({
    defaultQuery: () => ({ page: 1, pageSize: 10 }),
    defaultForm: () => ({}),
    listApi: fetchGetNotifyMessageList,
    columnsFactory: () => [
      { type: 'globalIndex', label: '序号', width: 70 },
      { prop: 'userId', label: '接收用户', minWidth: 160 },
      { prop: 'templateCode', label: '模板编码', minWidth: 160 },
      { prop: 'templateNickname', label: '发送昵称', width: 120 },
      { prop: 'templateContent', label: '消息内容', minWidth: 260 },
      {
        prop: 'readStatus',
        label: '状态',
        width: 90,
        formatter: (row) => h(ElTag, { type: row.readStatus ? 'success' : 'info' }, () => (row.readStatus ? '已读' : '未读'))
      },
      { prop: 'createTime', label: '发送时间', minWidth: 170 },
      {
        type: 'operation',
        prop: 'operation',
        label: '操作',
        width: 90,
        fixed: 'right',
        actions: [{ key: 'detail', label: '详情', permission: 'system:notify-message:view', onClick: openDetail }]
      }
    ]
  })

  function openDetail(row: Api.Messages.NotifyMessageItem) {
    detail.value = row
    detailVisible.value = true
  }
</script>
