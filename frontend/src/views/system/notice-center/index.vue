<template>
  <div class="art-full-height">
    <ArtSearchBar
      v-show="showSearchBar"
      v-model="query"
      :items="searchItems"
      :show-expand="false"
      @search="refreshAll"
      @reset="resetQuery"
    />

    <ElCard class="art-table-card" :style="{ marginTop: showSearchBar ? '12px' : '0' }">
      <ArtTableHeader
        v-model:columns="columnChecks"
        v-model:showSearchBar="showSearchBar"
        :loading="loading"
        @refresh="refreshAll"
      >
        <template #left>
          <div class="notice-summary">
            <strong>消息中心</strong>
            <span>未读 {{ unreadCount }} 条</span>
          </div>
          <ElButton type="primary" :disabled="unreadCount === 0" @click="readAll">全部已读</ElButton>
        </template>
      </ArtTableHeader>

      <FnkTable
        action-mode="menu"
        row-key="id"
        :loading="loading"
        :data="data"
        :columns="columns"
        :pagination="pagination"
        empty-text="暂无站内通知"
        @pagination:size-change="handleSizeChange"
        @pagination:current-change="handleCurrentChange"
      />
    </ElCard>

    <ElDialog v-model="detailVisible" title="通知详情" width="640px">
      <div v-if="currentNotice" class="notice-detail">
        <div class="notice-detail__title">{{ currentNotice.title }}</div>
        <div class="notice-detail__meta">
          <ElTag>{{ typeText(currentNotice.noticeType) }}</ElTag>
          <span>{{ currentNotice.createTime }}</span>
        </div>
        <div class="notice-detail__content">{{ currentNotice.content || '暂无内容' }}</div>
      </div>
      <template #footer>
        <ElButton type="primary" @click="detailVisible = false">关闭</ElButton>
      </template>
    </ElDialog>
  </div>
</template>

<script setup lang="ts">
  import { h } from 'vue'
  import { ElMessage, ElTag } from 'element-plus'
  import type { SearchFormItem } from '@/components/core/forms/art-search-bar/index.vue'
  import { useCrudTable } from '@/hooks/core/useCrudTable'
  import {
    fetchGetMyNoticeList,
    fetchGetMyUnreadNoticeCount,
    fetchReadAllMyNotices,
    fetchReadMyNotice
  } from '@/api/foundation'

  defineOptions({ name: 'SystemNoticeCenter' })

  const showSearchBar = ref(true)
  const unreadCount = ref(0)
  const detailVisible = ref(false)
  const currentNotice = ref<Api.Foundation.UserNoticeItem>()

  const searchItems: SearchFormItem[] = [
    { label: '标题', key: 'title', type: 'input', props: { clearable: true } },
    {
      label: '类型',
      key: 'noticeType',
      type: 'select',
      props: {
        clearable: true,
        options: [
          { label: '通知', value: 'NOTICE' },
          { label: '公告', value: 'ANNOUNCEMENT' }
        ]
      }
    },
    {
      label: '状态',
      key: 'readStatus',
      type: 'select',
      props: {
        clearable: true,
        options: [
          { label: '未读', value: false },
          { label: '已读', value: true }
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
    handleCurrentChange
  } = useCrudTable<Api.Foundation.UserNoticeItem, Api.Foundation.UserNoticeSearchParams, Record<string, never>>({
    defaultQuery: () => ({
      page: 1,
      pageSize: 10,
      title: undefined,
      noticeType: undefined,
      readStatus: undefined
    }),
    defaultForm: () => ({}),
    listApi: fetchGetMyNoticeList,
    columnsFactory: () => [
      { type: 'globalIndex', label: '序号', width: 70 },
      { prop: 'title', label: '标题', minWidth: 220 },
      {
        prop: 'noticeType',
        label: '类型',
        width: 110,
        formatter: (row) => h(ElTag, null, () => typeText(row.noticeType))
      },
      {
        prop: 'readStatus',
        label: '状态',
        width: 110,
        formatter: (row) =>
          h(ElTag, { type: row.readStatus ? 'info' : 'danger' }, () =>
            row.readStatus ? '已读' : '未读'
          )
      },
      { prop: 'createTime', label: '接收时间', minWidth: 170 },
      { prop: 'readTime', label: '阅读时间', minWidth: 170 },
      {
        type: 'operation',
        prop: 'operation',
        label: '操作',
        width: 150,
        fixed: 'right',
        actions: [
          {
            key: 'detail',
            label: '查看',
            onClick: (row) => openDetail(row)
          },
          {
            key: 'read',
            label: '已读',
            type: 'success',
            visible: (row) => !row.readStatus,
            onClick: (row) => read(row)
          }
        ]
      }
    ]
  })

  onMounted(loadUnreadCount)

  async function refreshAll() {
    await loadData()
    await loadUnreadCount()
  }

  async function loadUnreadCount() {
    unreadCount.value = await fetchGetMyUnreadNoticeCount()
  }

  async function read(row: Api.Foundation.UserNoticeItem) {
    await fetchReadMyNotice(row.id)
    ElMessage.success('已标记为已读')
    await refreshAll()
  }

  async function readAll() {
    await fetchReadAllMyNotices()
    ElMessage.success('已全部标记为已读')
    await refreshAll()
  }

  async function openDetail(row: Api.Foundation.UserNoticeItem) {
    currentNotice.value = row
    detailVisible.value = true
    if (!row.readStatus) {
      await fetchReadMyNotice(row.id)
      await refreshAll()
    }
  }

  function typeText(type: string) {
    return type === 'ANNOUNCEMENT' ? '公告' : '通知'
  }
</script>

<style scoped lang="scss">
  .notice-summary {
    display: inline-flex;
    align-items: baseline;
    gap: 10px;
    margin-right: 12px;

    strong {
      font-size: 14px;
      color: var(--art-text-gray-900);
    }

    span {
      font-size: 13px;
      color: var(--art-text-gray-500);
    }
  }

  .notice-detail {
    min-height: 180px;
  }

  .notice-detail__title {
    font-size: 16px;
    font-weight: 600;
    color: var(--art-text-gray-900);
  }

  .notice-detail__meta {
    display: flex;
    gap: 10px;
    align-items: center;
    margin: 12px 0;
    color: var(--art-text-gray-500);
  }

  .notice-detail__content {
    white-space: pre-wrap;
    line-height: 1.7;
    color: var(--art-text-gray-800);
  }
</style>
