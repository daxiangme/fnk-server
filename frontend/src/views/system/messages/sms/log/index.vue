<template>
  <div class="art-full-height">
    <ArtSearchBar v-show="showSearchBar" v-model="query" :items="searchItems" :show-expand="false" @search="loadData" @reset="resetQuery" />
    <ElCard class="art-table-card" :style="{ marginTop: showSearchBar ? '12px' : '0' }">
      <ArtTableHeader v-model:columns="columnChecks" v-model:showSearchBar="showSearchBar" :loading="loading" @refresh="refreshData" />
      <FnkTable row-key="id" action-mode="menu" :loading="loading" :data="data" :columns="columns" :pagination="pagination" empty-text="暂无短信日志" @pagination:size-change="handleSizeChange" @pagination:current-change="handleCurrentChange" />
    </ElCard>

    <ElDialog v-model="detailVisible" title="短信日志详情" width="640px">
      <ElDescriptions :column="1" border>
        <ElDescriptionsItem label="模板编码">{{ detail?.templateCode }}</ElDescriptionsItem>
        <ElDescriptionsItem label="手机号">{{ detail?.mobile }}</ElDescriptionsItem>
        <ElDescriptionsItem label="发送状态">
          <ElTag :type="statusTagType(detail?.sendStatus)">{{ statusText(detail?.sendStatus) }}</ElTag>
        </ElDescriptionsItem>
        <ElDescriptionsItem label="发送时间">{{ detail?.sendTime }}</ElDescriptionsItem>
        <ElDescriptionsItem label="模板参数">
          <pre class="m-0 whitespace-pre-wrap">{{ stringifyParams(detail?.templateParams) }}</pre>
        </ElDescriptionsItem>
        <ElDescriptionsItem label="短信内容">{{ detail?.content }}</ElDescriptionsItem>
        <ElDescriptionsItem v-if="detail?.errorMsg" label="失败原因">{{ detail.errorMsg }}</ElDescriptionsItem>
      </ElDescriptions>
    </ElDialog>
  </div>
</template>

<script setup lang="ts">
  import { h } from 'vue'
  import { ElTag } from 'element-plus'
  import { fetchGetSmsLogList } from '@/api/messages'
  import { useCrudTable } from '@/hooks/core/useCrudTable'
  import type { SearchFormItem } from '@/components/core/forms/art-search-bar/index.vue'
  import { statusTagType, statusText, stringifyParams } from '../../utils'

  defineOptions({ name: 'SystemMessagesSmsLog' })

  const showSearchBar = ref(true)
  const detailVisible = ref(false)
  const detail = ref<Api.Messages.SmsLogItem>()

  const searchItems: SearchFormItem[] = [
    { label: '模板编码', key: 'templateCode', type: 'input', props: { clearable: true } },
    { label: '手机号', key: 'mobile', type: 'input', props: { clearable: true } },
    {
      label: '发送状态',
      key: 'sendStatus',
      type: 'select',
      props: {
        clearable: true,
        options: [
          { label: '成功', value: 'SUCCESS' },
          { label: '失败', value: 'FAILED' },
          { label: '待发送', value: 'INIT' }
        ]
      }
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
  } = useCrudTable<Api.Messages.SmsLogItem, Api.Messages.SmsLogSearchParams, Record<string, never>>({
    defaultQuery: () => ({ page: 1, pageSize: 10 }),
    defaultForm: () => ({}),
    listApi: fetchGetSmsLogList,
    columnsFactory: () => [
      { type: 'globalIndex', label: '序号', width: 70 },
      { prop: 'templateCode', label: '模板编码', minWidth: 150 },
      { prop: 'mobile', label: '手机号', width: 130 },
      { prop: 'content', label: '短信内容', minWidth: 240 },
      {
        prop: 'sendStatus',
        label: '状态',
        width: 100,
        formatter: (row) => h(ElTag, { type: statusTagType(row.sendStatus) }, () => statusText(row.sendStatus))
      },
      { prop: 'sendTime', label: '发送时间', minWidth: 170 },
      {
        type: 'operation',
        prop: 'operation',
        label: '操作',
        width: 90,
        fixed: 'right',
        actions: [{ key: 'detail', label: '详情', permission: 'system:sms-log:view', onClick: openDetail }]
      }
    ]
  })

  function openDetail(row: Api.Messages.SmsLogItem) {
    detail.value = row
    detailVisible.value = true
  }
</script>
