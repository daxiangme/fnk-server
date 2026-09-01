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
          <ElButton type="primary" v-auth="'system:notice:create'" @click="openCreate">
            新增通知
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

    <ElDialog v-model="dialogVisible" :title="form.id ? '编辑通知' : '新增通知'" width="640px">
      <ElForm ref="formRef" :model="form" :rules="rules" label-width="90px">
        <ElFormItem label="标题" prop="title">
          <ElInput v-model="form.title" placeholder="请输入标题" />
        </ElFormItem>
        <ElFormItem label="类型" prop="noticeType">
          <ElSelect v-model="form.noticeType" placeholder="请选择类型">
            <ElOption label="通知" value="NOTICE" />
            <ElOption label="公告" value="ANNOUNCEMENT" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="发布状态">
          <ElSwitch v-model="form.publishStatus" active-text="保存后发布" inactive-text="草稿" />
        </ElFormItem>
        <ElFormItem label="内容">
          <ElInput v-model="form.content" type="textarea" :rows="6" placeholder="请输入内容" />
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
  import { ElMessage, ElTag, type FormRules } from 'element-plus'
  import {
    fetchDeleteNotice,
    fetchGetNoticeList,
    fetchPublishNotice,
    fetchRevokeNotice,
    fetchSaveNotice
  } from '@/api/foundation'
  import { useCrudTable } from '@/hooks/core/useCrudTable'
  import type { SearchFormItem } from '@/components/core/forms/art-search-bar/index.vue'

  defineOptions({ name: 'SystemNotice' })

  type NoticeForm = Pick<Api.Foundation.NoticeItem, 'title' | 'noticeType' | 'publishStatus'> &
    Partial<Pick<Api.Foundation.NoticeItem, 'id' | 'content'>>

  const showSearchBar = ref(true)

  const searchItems: SearchFormItem[] = [
    {
      label: '标题',
      key: 'title',
      type: 'input',
      props: { clearable: true, placeholder: '请输入标题' }
    },
    {
      label: '类型',
      key: 'noticeType',
      type: 'select',
      props: {
        clearable: true,
        placeholder: '全部',
        options: [
          { label: '通知', value: 'NOTICE' },
          { label: '公告', value: 'ANNOUNCEMENT' }
        ]
      }
    },
    {
      label: '发布状态',
      key: 'publishStatus',
      type: 'select',
      props: {
        clearable: true,
        placeholder: '全部',
        options: [
          { label: '已发布', value: true },
          { label: '草稿', value: false }
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
  } = useCrudTable<Api.Foundation.NoticeItem, Api.Foundation.NoticeSearchParams, NoticeForm>({
    defaultQuery: () => ({
      page: 1,
      pageSize: 10,
      title: undefined,
      noticeType: undefined,
      publishStatus: undefined
    }),
    defaultForm: () => ({
      id: '',
      title: '',
      noticeType: 'NOTICE',
      publishStatus: false,
      content: ''
    }),
    listApi: fetchGetNoticeList,
    saveApi: fetchSaveNotice,
    removeApi: (row) => fetchDeleteNotice(row.id),
    getEditForm: (row) => ({ ...row }),
    removeOptions: {
      message: (row) => `确定删除通知「${row.title}」吗？`
    },
    columnsFactory: () => [
      { type: 'globalIndex', label: '序号', width: 70 },
      { prop: 'title', label: '标题', minWidth: 180 },
      {
        prop: 'noticeType',
        label: '类型',
        width: 110,
        formatter: (row) => h(ElTag, null, () => typeText(row.noticeType))
      },
      {
        prop: 'publishStatus',
        label: '发布状态',
        width: 110,
        formatter: (row) =>
          h(ElTag, { type: row.publishStatus ? 'success' : 'info' }, () =>
            row.publishStatus ? '已发布' : '草稿'
          )
      },
      { prop: 'publishTime', label: '发布时间', minWidth: 170 },
      {
        type: 'operation',
        prop: 'operation',
        label: '操作',
        width: 260,
        fixed: 'right',
        actions: [
          {
            key: 'publish',
            label: '发布',
            type: 'success',
            permission: 'system:notice:publish',
            visible: (row) => !row.publishStatus,
            onClick: (row) => publish(row)
          },
          {
            key: 'revoke',
            label: '撤回',
            type: 'warning',
            permission: 'system:notice:revoke',
            visible: (row) => row.publishStatus,
            onClick: (row) => revoke(row)
          },
          {
            key: 'edit',
            label: '编辑',
            permission: 'system:notice:update',
            onClick: (row) => openEdit(row)
          },
          {
            key: 'delete',
            label: '删除',
            type: 'error',
            permission: 'system:notice:delete',
            onClick: (row) => remove(row)
          }
        ]
      }
    ]
  })

  const rules: FormRules = {
    title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
    noticeType: [{ required: true, message: '请选择类型', trigger: 'change' }]
  }

  async function publish(row: Api.Foundation.NoticeItem) {
    await fetchPublishNotice(row.id)
    ElMessage.success('发布成功')
    loadData()
  }

  async function revoke(row: Api.Foundation.NoticeItem) {
    await fetchRevokeNotice(row.id)
    ElMessage.success('撤回成功')
    loadData()
  }

  function typeText(type: string) {
    return type === 'ANNOUNCEMENT' ? '公告' : '通知'
  }

</script>
