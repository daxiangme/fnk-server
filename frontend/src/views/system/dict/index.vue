<template>
  <div class="art-full-height dict-layout">
    <div class="dict-shell">
      <aside class="dict-type-panel">
        <DictTypeSearch
          v-show="showTypeSearch"
          v-model="typeQuery"
          @search="loadTypes"
          @reset="resetTypeQuery"
        />

        <ElCard class="art-table-card" :style="{ marginTop: showTypeSearch ? '12px' : '0' }">
          <ArtTableHeader
            v-model:columns="typeColumnChecks"
            v-model:showSearchBar="showTypeSearch"
            :loading="typeLoading"
            @refresh="loadTypes"
          >
            <template #left>
              <ElButton type="primary" v-auth="'system:dict:create'" @click="openCreateType">
                新增类型
              </ElButton>
            </template>
          </ArtTableHeader>

          <FnkTable
            action-mode="menu"
            row-key="id"
            highlight-current-row
            :loading="typeLoading"
            :data="typeData"
            :columns="typeColumns"
            :pagination="typePagination"
            @row-click="handleSelectType"
            @pagination:size-change="handleTypeSizeChange"
            @pagination:current-change="handleTypeCurrentChange"
          />
        </ElCard>
      </aside>

      <section class="dict-item-panel">
        <ElCard class="art-table-card !mt-0">
          <ArtTableHeader
            v-model:columns="itemColumnChecks"
            :loading="itemLoading"
            layout="refresh,size,fullscreen,columns,settings"
            @refresh="loadItems"
          >
            <template #left>
              <div class="dict-current">
                <strong>{{ selectedDictType?.dictName || '请选择字典类型' }}</strong>
                <span v-if="selectedDictType">{{ selectedDictType.dictCode }}</span>
              </div>
              <ElButton
                type="primary"
                :disabled="!selectedDictType"
                v-auth="'system:dict:create'"
                @click="openCreateItem"
              >
                新增字典项
              </ElButton>
            </template>
          </ArtTableHeader>

          <FnkTable
            action-mode="menu"
            row-key="id"
            :loading="itemLoading"
            :data="itemRecords"
            :columns="itemColumns"
            :pagination="undefined"
            empty-text="当前字典类型暂无数据"
          />
        </ElCard>
      </section>
    </div>

    <ElDialog v-model="typeDialogVisible" :title="typeForm.id ? '编辑字典类型' : '新增字典类型'" width="520px">
      <ElForm ref="typeFormRef" :model="typeForm" :rules="typeRules" label-width="90px">
        <ElFormItem label="字典编码" prop="dictCode">
          <ElInput v-model="typeForm.dictCode" placeholder="请输入字典编码" />
        </ElFormItem>
        <ElFormItem label="字典名称" prop="dictName">
          <ElInput v-model="typeForm.dictName" placeholder="请输入字典名称" />
        </ElFormItem>
        <ElFormItem label="状态">
          <ElSwitch v-model="typeForm.status" />
        </ElFormItem>
        <ElFormItem label="备注">
          <ElInput v-model="typeForm.remark" type="textarea" />
        </ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="typeDialogVisible = false">取消</ElButton>
        <ElButton type="primary" :loading="typeSaving" @click="submitType">保存</ElButton>
      </template>
    </ElDialog>

    <ElDialog v-model="itemDialogVisible" :title="itemForm.id ? '编辑字典项' : '新增字典项'" width="560px">
      <ElForm ref="itemFormRef" :model="itemForm" :rules="itemRules" label-width="90px">
        <ElFormItem label="字典编码" prop="dictCode">
          <ElInput v-model="itemForm.dictCode" disabled />
        </ElFormItem>
        <ElFormItem label="标签" prop="label">
          <ElInput v-model="itemForm.label" placeholder="请输入标签" />
        </ElFormItem>
        <ElFormItem label="值" prop="value">
          <ElInput v-model="itemForm.value" placeholder="请输入值" />
        </ElFormItem>
        <ElFormItem label="标签样式">
          <ElInput v-model="itemForm.tagType" placeholder="例如 success / warning" />
        </ElFormItem>
        <ElFormItem label="排序">
          <ElInputNumber v-model="itemForm.orderSort" :min="0" />
        </ElFormItem>
        <ElFormItem label="状态">
          <ElSwitch v-model="itemForm.status" />
        </ElFormItem>
        <ElFormItem label="备注">
          <ElInput v-model="itemForm.remark" type="textarea" />
        </ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="itemDialogVisible = false">取消</ElButton>
        <ElButton type="primary" :loading="itemSaving" @click="submitItem">保存</ElButton>
      </template>
    </ElDialog>
  </div>
</template>

<script setup lang="ts">
  import { h } from 'vue'
  import {
    ElMessage,
    ElMessageBox,
    ElTag,
    type FormInstance,
    type FormRules
  } from 'element-plus'
  import {
    fetchDeleteDictItem,
    fetchDeleteDictType,
    fetchGetAllDictTypes,
    fetchGetDictItemsByCode,
    fetchGetDictTypeList,
    fetchSaveDictItem,
    fetchSaveDictType
  } from '@/api/foundation'
  import { useCrudTable } from '@/hooks/core/useCrudTable'
  import { useTableColumns } from '@/hooks/core/useTableColumns'
  import DictTypeSearch from './modules/dict-type-search.vue'

  defineOptions({ name: 'SystemDict' })

  type DictTypeForm = Pick<Api.Foundation.DictTypeItem, 'dictCode' | 'dictName' | 'status'> &
    Partial<Pick<Api.Foundation.DictTypeItem, 'id' | 'remark'>>

  type DictItemForm = Pick<
    Api.Foundation.DictItemItem,
    'dictCode' | 'label' | 'value' | 'orderSort' | 'status'
  > &
    Partial<Pick<Api.Foundation.DictItemItem, 'id' | 'tagType' | 'remark'>>

  const showTypeSearch = ref(false)
  const selectedDictType = ref<Api.Foundation.DictTypeItem>()
  const dictTypeOptions = ref<Api.Foundation.DictTypeItem[]>([])
  const itemRecords = ref<Api.Foundation.DictItemItem[]>([])
  const itemLoading = ref(false)
  const itemSaving = ref(false)
  const itemDialogVisible = ref(false)
  const itemFormRef = ref<FormInstance>()
  const itemForm = reactive<DictItemForm>({
    id: '',
    dictCode: '',
    label: '',
    value: '',
    orderSort: 0,
    status: true,
    tagType: '',
    remark: ''
  })

  const {
    loading: typeLoading,
    saving: typeSaving,
    dialogVisible: typeDialogVisible,
    formRef: typeFormRef,
    query: typeQuery,
    form: typeForm,
    data: typeData,
    pagination: typePagination,
    columns: typeColumns,
    columnChecks: typeColumnChecks,
    loadData: loadTypes,
    resetQuery: resetTypeQuery,
    openCreate: openCreateType,
    openEdit: openEditType,
    submit: submitType,
    remove: removeType,
    handleSizeChange: handleTypeSizeChange,
    handleCurrentChange: handleTypeCurrentChange
  } = useCrudTable<Api.Foundation.DictTypeItem, Api.Foundation.DictTypeSearchParams, DictTypeForm>({
    immediate: false,
    defaultQuery: () => ({ page: 1, pageSize: 10, dictCode: undefined, dictName: undefined }),
    defaultForm: () => ({ id: '', dictCode: '', dictName: '', status: true, remark: '' }),
    listApi: fetchGetDictTypeList,
    saveApi: fetchSaveDictType,
    removeApi: (row) => fetchDeleteDictType(row.id),
    getEditForm: (row) => ({ ...row }),
    afterLoad: (records) => syncSelectedType(records),
    afterSave: loadDictTypeOptions,
    afterRemove: loadDictTypeOptions,
    removeOptions: {
      message: (row) => `确定删除字典类型「${row.dictName}」吗？关联字典项会同步删除。`
    },
    columnsFactory: () => [
      {
        prop: 'dictName',
        label: '字典类型',
        minWidth: 190,
        formatter: (row) =>
          h('div', { class: 'dict-type-cell' }, [
            h('strong', row.dictName),
            h('span', row.dictCode)
          ])
      },
      {
        prop: 'status',
        label: '状态',
        width: 72,
        formatter: (row) =>
          h(ElTag, { type: row.status ? 'success' : 'info' }, () => (row.status ? '启用' : '禁用'))
      },
      {
        type: 'operation',
        prop: 'operation',
        label: '操作',
        width: 104,
        fixed: 'right',
        actions: [
          {
            key: 'edit',
            label: '编辑',
            permission: 'system:dict:update',
            onClick: (row) => openEditType(row)
          },
          {
            key: 'delete',
            label: '删除',
            type: 'error',
            permission: 'system:dict:delete',
            onClick: (row) => removeType(row)
          }
        ]
      }
    ]
  })

  const { columns: itemColumns, columnChecks: itemColumnChecks } =
    useTableColumns<Api.Foundation.DictItemItem>(() => [
      { type: 'globalIndex', label: '序号', width: 70 },
      { prop: 'label', label: '标签', minWidth: 120, showOverflowTooltip: true },
      { prop: 'value', label: '值', minWidth: 160, showOverflowTooltip: true },
      { prop: 'tagType', label: '标签样式', width: 110, showOverflowTooltip: true },
      { prop: 'orderSort', label: '排序', width: 80 },
      {
        prop: 'status',
        label: '状态',
        width: 90,
        formatter: (row) =>
          h(ElTag, { type: row.status ? 'success' : 'info' }, () => (row.status ? '启用' : '禁用'))
      },
      {
        type: 'operation',
        prop: 'operation',
        label: '操作',
        width: 150,
        fixed: 'right',
        actions: [
          {
            key: 'edit',
            label: '编辑',
            permission: 'system:dict:update',
            onClick: (row) => openEditItem(row)
          },
          {
            key: 'delete',
            label: '删除',
            type: 'error',
            permission: 'system:dict:delete',
            onClick: (row) => removeItem(row)
          }
        ]
      }
    ])

  const typeRules: FormRules = {
    dictCode: [{ required: true, message: '请输入字典编码', trigger: 'blur' }],
    dictName: [{ required: true, message: '请输入字典名称', trigger: 'blur' }]
  }

  const itemRules: FormRules = {
    dictCode: [{ required: true, message: '请选择字典', trigger: 'change' }],
    label: [{ required: true, message: '请输入标签', trigger: 'blur' }],
    value: [{ required: true, message: '请输入值', trigger: 'blur' }]
  }

  onMounted(() => {
    loadDictTypeOptions()
    loadTypes()
  })

  async function loadDictTypeOptions() {
    dictTypeOptions.value = await fetchGetAllDictTypes()
  }

  function syncSelectedType(records: Api.Foundation.DictTypeItem[]) {
    const nextSelected =
      records.find((item) => item.id === selectedDictType.value?.id) || records[0]
    selectedDictType.value = nextSelected
    if (nextSelected) {
      loadItems()
    } else {
      itemRecords.value = []
    }
  }

  function handleSelectType(row: Api.Foundation.DictTypeItem) {
    selectedDictType.value = row
    loadItems()
  }

  async function loadItems() {
    if (!selectedDictType.value) {
      itemRecords.value = []
      return
    }
    itemLoading.value = true
    try {
      itemRecords.value = await fetchGetDictItemsByCode(selectedDictType.value.dictCode)
    } finally {
      itemLoading.value = false
    }
  }

  function resetItemForm() {
    Object.assign(itemForm, {
      id: '',
      dictCode: selectedDictType.value?.dictCode || '',
      label: '',
      value: '',
      orderSort: 0,
      status: true,
      tagType: '',
      remark: ''
    })
    itemFormRef.value?.clearValidate()
  }

  function openCreateItem() {
    if (!selectedDictType.value) {
      ElMessage.warning('请先选择字典类型')
      return
    }
    resetItemForm()
    itemDialogVisible.value = true
  }

  function openEditItem(row: Api.Foundation.DictItemItem) {
    resetItemForm()
    Object.assign(itemForm, row)
    itemDialogVisible.value = true
  }

  async function submitItem() {
    if (!itemFormRef.value) return
    await itemFormRef.value.validate()
    itemSaving.value = true
    try {
      await fetchSaveDictItem({ ...itemForm })
      ElMessage.success('保存成功')
      itemDialogVisible.value = false
      await loadItems()
    } finally {
      itemSaving.value = false
    }
  }

  function removeItem(row: Api.Foundation.DictItemItem) {
    ElMessageBox.confirm(`确定删除字典项「${row.label}」吗？`, '删除确认', { type: 'warning' })
      .then(async () => {
        await fetchDeleteDictItem(row.id)
        ElMessage.success('删除成功')
        loadItems()
      })
      .catch(() => undefined)
  }

</script>

<style scoped lang="scss">
  .dict-layout {
    min-height: 0;
  }

  .dict-shell {
    display: grid;
    grid-template-columns: minmax(0, 2fr) minmax(0, 3fr);
    gap: 16px;
    height: 100%;
    min-height: 0;
  }

  .dict-type-panel,
  .dict-item-panel {
    display: flex;
    flex-direction: column;
    overflow: hidden;
    min-height: 0;
    min-width: 0;
  }

  .dict-type-panel :deep(.el-card__body),
  .dict-item-panel :deep(.el-card__body) {
    min-height: 0;
  }

  .dict-type-panel :deep(.art-table-card),
  .dict-item-panel :deep(.art-table-card),
  .dict-type-panel :deep(.art-table),
  .dict-item-panel :deep(.art-table) {
    width: 100%;
  }

  .dict-current {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    margin-right: 12px;

    span {
      color: var(--el-text-color-secondary);
      font-size: 13px;
    }
  }

  :deep(.dict-type-cell) {
    display: flex;
    min-width: 0;
    flex-direction: column;
    gap: 4px;

    strong,
    span {
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    span {
      color: var(--el-text-color-secondary);
      font-size: 12px;
    }
  }

  .dict-item-panel :deep(.el-table .cell) {
    word-break: normal;
  }

  @media (max-width: 1024px) {
    .dict-shell {
      grid-template-columns: 1fr;
      height: auto;
    }
  }
</style>
