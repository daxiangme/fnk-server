<template>
  <div class="art-full-height">
    <ArtSearchBar
      v-show="showSearchBar"
      v-model="query"
      :items="searchItems"
      :label-width="100"
      :show-expand="false"
      @search="loadData"
      @reset="resetQuery"
    />

    <ElCard class="art-table-card" :style="{ marginTop: showSearchBar ? '12px' : '0' }">
      <ArtTableHeader
        v-model:columns="columnChecks"
        v-model:showSearchBar="showSearchBar"
        :loading="loading"
        @refresh="loadData"
      >
        <template #left>
          <ElUpload
            :show-file-list="false"
            :http-request="uploadFile"
            :disabled="uploading"
            v-auth="'infra:file:upload'"
          >
            <ElButton type="primary" :loading="uploading">上传文件</ElButton>
          </ElUpload>
        </template>
      </ArtTableHeader>

      <FnkTable
        action-mode="menu"
        row-key="id"
        :loading="loading"
        :data="tableData"
        :columns="columns"
        :pagination="pagination"
        empty-text="暂无文件资源"
        @pagination:size-change="handleSizeChange"
        @pagination:current-change="handleCurrentChange"
      />
    </ElCard>
  </div>
</template>

<script setup lang="ts">
  import { h } from 'vue'
  import { ElMessage, ElMessageBox, ElTag, type UploadRequestOptions } from 'element-plus'
  import type { SearchFormItem } from '@/components/core/forms/art-search-bar/index.vue'
  import { useTableColumns } from '@/hooks/core/useTableColumns'
  import {
    fetchDeleteInfraFile,
    fetchDownloadInfraFile,
    fetchGetInfraFileList,
    fetchUploadInfraFile
  } from '@/api/infra'
  import type { ColumnOption } from '@/types/component'

  defineOptions({ name: 'InfraFile' })

  const showSearchBar = ref(true)
  const loading = ref(false)
  const uploading = ref(false)
  const tableData = ref<Api.Infra.FileItem[]>([])
  const total = ref(0)
  const query = reactive<Api.Infra.FileSearchParams>({
    page: 1,
    pageSize: 10,
    originalName: undefined,
    contentType: undefined,
    storageType: undefined
  })

  const searchItems: SearchFormItem[] = [
    { label: '原始文件名', key: 'originalName', type: 'input', props: { clearable: true } },
    { label: '内容类型', key: 'contentType', type: 'input', props: { clearable: true } },
    {
      label: '存储类型',
      key: 'storageType',
      type: 'select',
      props: {
        clearable: true,
        options: [
          { label: '本地存储', value: 'local' },
          { label: 'S3 兼容存储', value: 's3' }
        ]
      }
    }
  ]

  const tableColumnFactory = (): ColumnOption<Api.Infra.FileItem>[] => [
    { type: 'globalIndex', label: '序号', width: 70 },
    { prop: 'originalName', label: '原始文件名', minWidth: 220 },
    { prop: 'fileName', label: '存储文件名', minWidth: 260 },
    { prop: 'contentType', label: '内容类型', minWidth: 160 },
    {
      prop: 'fileSize',
      label: '大小',
      width: 110,
      formatter: (row) => formatSize(row.fileSize)
    },
    {
      prop: 'storageType',
      label: '存储',
      width: 120,
      formatter: (row) =>
        h(ElTag, null, () => (row.storageType === 'local' ? '本地存储' : row.storageType))
    },
    { prop: 'configName', label: '文件配置', minWidth: 160 },
    { prop: 'createTime', label: '上传时间', minWidth: 170 },
    {
      type: 'operation',
      prop: 'operation',
      label: '操作',
      width: 150,
      fixed: 'right',
      actions: [
        {
          key: 'download',
          label: '下载',
          permission: 'infra:file:view',
          onClick: (row) => downloadFile(row)
        },
        {
          key: 'delete',
          label: '删除',
          type: 'error',
          permission: 'infra:file:delete',
          onClick: (row) => removeFile(row)
        }
      ]
    }
  ]

  const { columns, columnChecks } = useTableColumns<Api.Infra.FileItem>(tableColumnFactory)
  const pagination = computed(() => ({
    current: query.page || 1,
    size: query.pageSize || 10,
    total: total.value
  }))

  onMounted(loadData)

  async function loadData() {
    loading.value = true
    try {
      const page = await fetchGetInfraFileList(query)
      tableData.value = page.records || []
      total.value = page.total || 0
    } finally {
      loading.value = false
    }
  }

  function resetQuery() {
    Object.assign(query, {
      page: 1,
      pageSize: 10,
      originalName: undefined,
      contentType: undefined,
      storageType: undefined
    })
    loadData()
  }

  function handleSizeChange(size: number) {
    query.pageSize = size
    query.page = 1
    loadData()
  }

  function handleCurrentChange(page: number) {
    query.page = page
    loadData()
  }

  async function uploadFile(options: UploadRequestOptions) {
    uploading.value = true
    try {
      const file = options.file as File
      const result = await fetchUploadInfraFile(file)
      options.onSuccess?.(result)
      ElMessage.success('上传成功')
      loadData()
    } catch (error) {
      options.onError?.(error as any)
    } finally {
      uploading.value = false
    }
  }

  async function downloadFile(row: Api.Infra.FileItem) {
    const blob = await fetchDownloadInfraFile(row.id)
    const url = URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = row.originalName || row.fileName
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    setTimeout(() => URL.revokeObjectURL(url), 0)
  }

  function removeFile(row: Api.Infra.FileItem) {
    ElMessageBox.confirm(`确定删除文件「${row.originalName}」吗？`, '删除确认', { type: 'warning' })
      .then(async () => {
        await fetchDeleteInfraFile(row.id)
        ElMessage.success('删除成功')
        loadData()
      })
      .catch(() => undefined)
  }

  function formatSize(size?: number) {
    if (!size) return '0 B'
    if (size < 1024) return `${size} B`
    if (size < 1024 * 1024) return `${(size / 1024).toFixed(1)} KB`
    return `${(size / 1024 / 1024).toFixed(1)} MB`
  }
</script>
