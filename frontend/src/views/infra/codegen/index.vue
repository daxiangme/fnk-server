<template>
  <div class="art-full-height codegen-page">
    <ArtSearchBar
      v-show="showSearchBar"
      v-model="query"
      :items="searchItems"
      :show-expand="false"
      @search="loadTables"
      @reset="resetQuery"
    />

    <ElCard class="art-table-card" :style="{ marginTop: showSearchBar ? '12px' : '0' }">
      <ArtTableHeader
        v-model:columns="columnChecks"
        v-model:showSearchBar="showSearchBar"
        :loading="loading"
        @refresh="loadTables"
      >
        <template #left>
          <ElButton type="primary" v-auth="'infra:codegen:import'" @click="openImport">
            导入数据表
          </ElButton>
        </template>
      </ArtTableHeader>

      <FnkTable
        row-key="id"
        :loading="loading"
        :data="tableData"
        :columns="columns"
        :pagination="pagination"
        empty-text="暂无代码生成配置"
        @pagination:size-change="handleSizeChange"
        @pagination:current-change="handleCurrentChange"
      />
    </ElCard>

    <ElDialog
      v-model="importVisible"
      title="导入数据表"
      width="min(860px, 92vw)"
      destroy-on-close
      @closed="resetImportDialog"
    >
      <div class="import-table-search">
        <ElInput
          v-model="databaseQuery.tableName"
          clearable
          placeholder="请输入数据表名称"
          @clear="loadImportableTables"
          @keyup.enter="loadImportableTables"
        />
        <ElButton type="primary" :loading="databaseLoading" @click="loadImportableTables">
          搜索
        </ElButton>
        <ElButton @click="resetDatabaseQuery">重置</ElButton>
      </div>

      <ElTable
        ref="databaseTableRef"
        v-loading="databaseLoading"
        :data="databaseTables"
        row-key="tableName"
        height="360"
        empty-text="暂无可导入的数据表"
        @row-click="handleImportRowClick"
        @selection-change="handleImportSelectionChange"
      >
        <ElTableColumn type="selection" width="52" />
        <ElTableColumn prop="tableName" label="表名称" min-width="210" show-overflow-tooltip />
        <ElTableColumn prop="tableComment" label="表描述" min-width="240" show-overflow-tooltip />
        <ElTableColumn prop="engine" label="引擎" width="110" />
        <ElTableColumn prop="columnCount" label="字段数" width="90" align="center" />
      </ElTable>

      <div class="import-selection-summary">已选择 {{ selectedTableNames.length }} 张数据表</div>
      <template #footer>
        <ElButton @click="importVisible = false">取消</ElButton>
        <ElButton
          type="primary"
          :disabled="selectedTableNames.length === 0"
          :loading="importing"
          @click="submitImport"
        >
          导入
        </ElButton>
      </template>
    </ElDialog>
  </div>
</template>

<script setup lang="ts">
  import { ElMessage, ElMessageBox, type TableColumnCtx } from 'element-plus'
  import type { SearchFormItem } from '@/components/core/forms/art-search-bar/index.vue'
  import { useTableColumns } from '@/hooks/core/useTableColumns'
  import type { ColumnOption } from '@/types/component'
  import {
    fetchGetCodeGenTables,
    fetchGetDatabaseTables,
    fetchImportCodeGenTables,
    fetchSyncCodeGenFields
  } from '@/api/infra'

  defineOptions({ name: 'InfraCodeGen' })

  const router = useRouter()
  const showSearchBar = ref(true)
  const loading = ref(false)
  const tableData = ref<Api.Infra.CodeGenTableItem[]>([])
  const total = ref(0)
  const query = reactive<Api.Infra.CodeGenTableSearchParams>({
    page: 1,
    pageSize: 10,
    tableName: '',
    tableComment: '',
    moduleName: '',
    generateType: ''
  })

  const importVisible = ref(false)
  const importing = ref(false)
  const databaseLoading = ref(false)
  const databaseTableRef = ref()
  const databaseTables = ref<Api.Infra.DatabaseTable[]>([])
  const selectedTableNames = ref<string[]>([])
  const databaseQuery = reactive<Api.Infra.DatabaseTableSearchParams>({
    tableName: '',
    excludeImported: true
  })

  const searchItems: SearchFormItem[] = [
    { label: '表名', key: 'tableName', type: 'input', props: { clearable: true } },
    { label: '表描述', key: 'tableComment', type: 'input', props: { clearable: true } },
    { label: '模块名', key: 'moduleName', type: 'input', props: { clearable: true } },
    {
      label: '生成类型',
      key: 'generateType',
      type: 'select',
      props: {
        clearable: true,
        options: [
          { label: '单表', value: 'single' },
          { label: '树结构', value: 'tree' },
          { label: '多对多', value: 'manyToMany' }
        ]
      }
    }
  ]

  const tableColumnFactory = (): ColumnOption<Api.Infra.CodeGenTableItem>[] => [
    { type: 'globalIndex', label: '序号', width: 70 },
    { prop: 'tableName', label: '表名', minWidth: 170 },
    { prop: 'tableComment', label: '表描述', minWidth: 180 },
    { prop: 'businessName', label: '业务名称', minWidth: 160 },
    { prop: 'moduleName', label: '模块名', width: 110 },
    { prop: 'className', label: '实体类', minWidth: 150 },
    { prop: 'generateType', label: '生成类型', width: 110 },
    { prop: 'syncTime', label: '同步时间', minWidth: 170 },
    {
      type: 'operation',
      prop: 'operation',
      label: '操作',
      width: 250,
      fixed: 'right',
      actions: [
        {
          key: 'sync',
          label: '同步表结构',
          permission: 'infra:codegen:update',
          onClick: (row) => syncStructure(row)
        },
        {
          key: 'edit',
          label: '编辑',
          permission: 'infra:codegen:update',
          onClick: (row) => openDetail(row)
        },
        {
          key: 'generate',
          label: '生成代码',
          type: 'success',
          permission: 'infra:codegen:preview',
          onClick: (row) => openGenerate(row)
        }
      ]
    }
  ]

  const { columns, columnChecks } = useTableColumns<Api.Infra.CodeGenTableItem>(tableColumnFactory)

  const pagination = computed(() => ({
    current: query.page || 1,
    size: query.pageSize || 10,
    total: total.value
  }))

  onMounted(loadTables)

  async function loadTables() {
    loading.value = true
    try {
      const page = await fetchGetCodeGenTables(query)
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
      tableName: '',
      tableComment: '',
      moduleName: '',
      generateType: ''
    })
    loadTables()
  }

  function handleSizeChange(size: number) {
    query.pageSize = size
    query.page = 1
    loadTables()
  }

  function handleCurrentChange(page: number) {
    query.page = page
    loadTables()
  }

  async function openImport() {
    resetImportDialog()
    importVisible.value = true
    await loadImportableTables()
  }

  async function loadImportableTables() {
    databaseLoading.value = true
    try {
      databaseTables.value = await fetchGetDatabaseTables({
        tableName: databaseQuery.tableName?.trim() || undefined,
        excludeImported: true
      })
      selectedTableNames.value = []
    } finally {
      databaseLoading.value = false
    }
  }

  async function resetDatabaseQuery() {
    databaseQuery.tableName = ''
    await loadImportableTables()
  }

  function resetImportDialog() {
    databaseQuery.tableName = ''
    databaseTables.value = []
    selectedTableNames.value = []
  }

  function handleImportRowClick(
    row: Api.Infra.DatabaseTable,
    column: TableColumnCtx<Api.Infra.DatabaseTable>
  ) {
    if (column.type !== 'selection') {
      databaseTableRef.value?.toggleRowSelection(row)
    }
  }

  function handleImportSelectionChange(rows: Api.Infra.DatabaseTable[]) {
    selectedTableNames.value = rows.map((item) => item.tableName)
  }

  async function submitImport() {
    if (!selectedTableNames.value.length) {
      ElMessage.warning('请至少选择一张数据表')
      return
    }
    importing.value = true
    try {
      const tableNames = [...selectedTableNames.value]
      await fetchImportCodeGenTables({ tableNames })
      ElMessage.success(`已导入 ${tableNames.length} 张数据表`)
      importVisible.value = false
      await loadTables()
    } finally {
      importing.value = false
    }
  }

  function openDetail(row: Api.Infra.CodeGenTableItem) {
    router.push(`/infra/codegen/detail/${row.id}`)
  }

  function openGenerate(row: Api.Infra.CodeGenTableItem) {
    router.push({
      path: `/infra/codegen/detail/${row.id}`,
      query: { step: 'preview' }
    })
  }

  function syncStructure(row: Api.Infra.CodeGenTableItem) {
    ElMessageBox.confirm(`确定同步「${row.tableName}」的表结构吗？`, '同步确认', {
      type: 'warning'
    })
      .then(async () => {
        await fetchSyncCodeGenFields(row.id)
        ElMessage.success('表结构已同步')
        loadTables()
      })
      .catch(() => undefined)
  }
</script>

<style scoped lang="scss">
  .import-table-search {
    display: grid;
    grid-template-columns: minmax(220px, 1fr) auto auto;
    gap: 8px;
    margin-bottom: 12px;
  }

  .import-selection-summary {
    margin-top: 10px;
    color: var(--el-text-color-secondary);
    font-size: 13px;
  }

  @media (max-width: 640px) {
    .import-table-search {
      grid-template-columns: 1fr auto auto;
    }
  }
</style>
