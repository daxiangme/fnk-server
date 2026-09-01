<template>
  <div class="art-full-height codegen-detail-page">
    <ElCard class="art-table-card codegen-detail-shell">
      <template #header>
        <div class="codegen-detail-header">
          <div class="codegen-detail-title">
            <strong>{{ tableInfo?.businessName || '代码生成配置' }}</strong>
            <span v-if="tableInfo">{{ tableInfo.tableName }}</span>
          </div>
          <div class="codegen-detail-actions">
            <ElButton :disabled="activeStep === 0" @click="prevStep">上一步</ElButton>
            <ElButton :loading="submittingStep" @click="submitCurrentStep">提交</ElButton>
            <ElButton
              type="primary"
              :disabled="activeStep >= steps.length - 1"
              :loading="advancingStep"
              @click="nextStep"
            >
              下一步
            </ElButton>
          </div>
        </div>
      </template>

      <div class="codegen-step-bar">
        <ArtSteps :steps="steps" :current="activeStep" class="codegen-steps" />
      </div>

      <div v-loading="loading" class="codegen-step-body">
        <section v-show="currentStepKey === 'module'" class="codegen-step-section">
          <ElForm
            ref="moduleFormRef"
            :model="moduleForm"
            :rules="moduleRules"
            label-width="110px"
            class="codegen-module-form"
          >
            <ElRow :gutter="16">
              <ElCol :xs="24" :md="12">
                <ElFormItem label="业务名称" prop="businessName">
                  <ElInput v-model="moduleForm.businessName" />
                </ElFormItem>
              </ElCol>
              <ElCol :xs="24" :md="12">
                <ElFormItem label="模块名" prop="moduleName">
                  <ElInput v-model="moduleForm.moduleName" />
                </ElFormItem>
              </ElCol>
              <ElCol :xs="24" :md="12">
                <ElFormItem label="实体类名" prop="className">
                  <ElInput v-model="moduleForm.className" />
                </ElFormItem>
              </ElCol>
              <ElCol :xs="24">
                <ElFormItem label="包路径">
                  <ElInput v-model="moduleForm.packageName" />
                </ElFormItem>
              </ElCol>
              <ElCol :xs="24" :md="12">
                <ElFormItem label="接口路径">
                  <ElInput v-model="moduleForm.apiBasePath" />
                </ElFormItem>
              </ElCol>
              <ElCol :xs="24" :md="12">
                <ElFormItem label="路由路径">
                  <ElInput v-model="moduleForm.routePath" />
                </ElFormItem>
              </ElCol>
              <ElCol :xs="24">
                <ElFormItem label="前端路径">
                  <ElInput v-model="moduleForm.frontendPath" />
                </ElFormItem>
              </ElCol>
              <ElCol :xs="24" :md="12">
                <ElFormItem label="权限前缀">
                  <ElInput v-model="moduleForm.permissionPrefix" />
                </ElFormItem>
              </ElCol>
              <ElCol :xs="24" :md="12">
                <ElFormItem label="菜单父级">
                  <SystemMenuTreeSelect v-model="moduleForm.menuParentId" placeholder="请选择菜单父级" />
                </ElFormItem>
              </ElCol>
              <ElCol :xs="24" :md="12">
                <ElFormItem label="作者">
                  <ElInput v-model="moduleForm.author" />
                </ElFormItem>
              </ElCol>
            </ElRow>
          </ElForm>
        </section>

        <section v-show="currentStepKey === 'fields'" class="codegen-step-section">
          <div class="codegen-toolbar">
            <ElButton v-auth="'infra:codegen:update'" @click="syncFields">同步表结构</ElButton>
          </div>
          <FnkTable
            row-key="id"
            :loading="fieldLoading"
            :data="fieldData"
            :columns="fieldColumns"
            :pagination="undefined"
            :show-table-header="false"
            height="auto"
            empty-text="暂无字段配置"
          >
            <template #propertyName="{ row }">
              <ElInput v-model="row.propertyName" />
            </template>
            <template #columnComment="{ row }">
              <ElInput v-model="row.columnComment" />
            </template>
            <template #javaType="{ row }">
              <ElInput v-model="row.javaType" />
            </template>
            <template #tsType="{ row }">
              <ElInput v-model="row.tsType" />
            </template>
            <template #formType="{ row }">
              <ElSelect v-model="row.formType">
                <ElOption v-for="item in formTypeOptions" :key="item" :label="item" :value="item" />
              </ElSelect>
            </template>
            <template #queryType="{ row }">
              <ElSelect v-model="row.queryType">
                <ElOption v-for="item in queryTypeOptions" :key="item" :label="item" :value="item" />
              </ElSelect>
            </template>
            <template #required="{ row }">
              <ElSwitch v-model="row.required" />
            </template>
            <template #listVisible="{ row }">
              <ElSwitch v-model="row.listVisible" />
            </template>
            <template #searchVisible="{ row }">
              <ElSwitch v-model="row.searchVisible" />
            </template>
            <template #formVisible="{ row }">
              <ElSwitch v-model="row.formVisible" />
            </template>
          </FnkTable>
        </section>

        <section v-show="currentStepKey === 'relations'" class="codegen-step-section">
          <div class="codegen-toolbar codegen-relation-toolbar">
            <div class="codegen-relation-type">
              <span>生成类型</span>
              <ElSelect v-model="moduleForm.generateType" class="codegen-generate-type-select">
                <ElOption label="单表" value="single" />
                <ElOption label="树结构" value="tree" />
                <ElOption label="多对多" value="manyToMany" />
              </ElSelect>
            </div>
            <div class="codegen-relation-actions">
              <ElButton v-auth="'infra:codegen:update'" @click="analyzeRelations">自动分析关系</ElButton>
              <ElButton v-auth="'infra:codegen:update'" @click="addRelation">新增关系</ElButton>
            </div>
          </div>
          <FnkTable
            :data="relationData"
            :columns="relationColumns"
            :pagination="undefined"
            :show-table-header="false"
            height="auto"
            empty-text="暂无关系配置"
          >
            <template #relationName="{ row }">
              <ElInput v-model="row.relationName" />
            </template>
            <template #relationType="{ row }">
              <ElSelect v-model="row.relationType">
                <ElOption v-for="item in relationTypeOptions" :key="item" :label="item" :value="item" />
              </ElSelect>
            </template>
            <template #sourceColumn="{ row }">
              <ElSelect v-model="row.sourceColumn" clearable>
                <ElOption v-for="item in fieldOptions" :key="item.value" :label="item.label" :value="item.value" />
              </ElSelect>
            </template>
            <template #targetTable="{ row }">
              <ElSelect v-model="row.targetTable" filterable clearable>
                <ElOption
                  v-for="item in databaseTables"
                  :key="item.tableName"
                  :label="item.tableName"
                  :value="item.tableName"
                />
              </ElSelect>
            </template>
            <template #targetColumn="{ row }">
              <ElInput v-model="row.targetColumn" placeholder="例如 id" />
            </template>
            <template #displayColumn="{ row }">
              <ElInput v-model="row.displayColumn" placeholder="例如 name" />
            </template>
          </FnkTable>
        </section>

        <section v-show="currentStepKey === 'components'" class="codegen-step-section">
          <FnkTable
            :data="relationData"
            :columns="componentColumns"
            :pagination="undefined"
            :show-table-header="false"
            height="auto"
            empty-text="暂无可配置的组件关联"
          >
            <template #sourceColumn="{ row }">
              <ElSelect v-model="row.sourceColumn" clearable>
                <ElOption v-for="item in fieldOptions" :key="item.value" :label="item.label" :value="item.value" />
              </ElSelect>
            </template>
            <template #targetTable="{ row }">
              <ElSelect v-model="row.targetTable" filterable clearable>
                <ElOption
                  v-for="item in databaseTables"
                  :key="item.tableName"
                  :label="item.tableName"
                  :value="item.tableName"
                />
              </ElSelect>
            </template>
            <template #targetColumn="{ row }">
              <ElInput v-model="row.targetColumn" />
            </template>
            <template #displayColumn="{ row }">
              <ElInput v-model="row.displayColumn" />
            </template>
            <template #generateQuery="{ row }">
              <ElSwitch v-model="row.generateQuery" />
            </template>
            <template #generateForm="{ row }">
              <ElSwitch v-model="row.generateForm" />
            </template>
            <template #generateDetail="{ row }">
              <ElSwitch v-model="row.generateDetail" />
            </template>
          </FnkTable>
        </section>

        <section v-show="currentStepKey === 'preview'" class="codegen-step-section codegen-preview-section">
          <div class="codegen-toolbar">
            <ElButton type="primary" v-auth="'infra:codegen:preview'" @click="previewCode">
              生成预览
            </ElButton>
            <ElButton
              v-auth="'infra:codegen:download'"
              :disabled="!previewFiles.length"
              :loading="downloading"
              @click="downloadCode"
            >
              下载代码
            </ElButton>
          </div>
          <div class="codegen-preview-layout">
            <div class="codegen-file-list">
              <button
                v-for="file in previewFiles"
                :key="file.filePath"
                type="button"
                class="codegen-file-item"
                :class="{ active: file.filePath === activePreviewPath }"
                @click="activePreviewPath = file.filePath"
              >
                <span>{{ file.fileType }}</span>
                <strong>{{ file.filePath }}</strong>
              </button>
              <ElEmpty v-if="!previewFiles.length" description="暂无预览文件" :image-size="80" />
            </div>
            <pre class="codegen-preview">{{ activePreviewFile?.content || '暂无预览内容' }}</pre>
          </div>
        </section>
      </div>

    </ElCard>
  </div>
</template>

<script setup lang="ts">
  import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
  import SystemMenuTreeSelect from '@/components/business/system-menu-tree-select/index.vue'
  import ArtSteps from '@/components/core/navigation/art-steps/index.vue'
  import type { ColumnOption } from '@/types/component'
  import {
    fetchAnalyzeCodeGenRelations,
    fetchGetCodeGenFields,
    fetchGetCodeGenRelations,
    fetchGetCodeGenTable,
    fetchGetDatabaseTables,
    fetchDownloadCodeGen,
    fetchPreviewCodeGen,
    fetchSyncCodeGenFields,
    fetchUpdateCodeGenFields,
    fetchUpdateCodeGenRelations,
    fetchUpdateCodeGenTable
  } from '@/api/infra'

  defineOptions({ name: 'InfraCodeGenDetail' })

  const route = useRoute()
  const tableId = computed(() => String(route.params.id || ''))
  const loading = ref(false)
  const fieldLoading = ref(false)
  const submittingStep = ref(false)
  const advancingStep = ref(false)
  const downloading = ref(false)
  const tableInfo = ref<Api.Infra.CodeGenTableItem>()
  const fieldData = ref<Api.Infra.CodeGenFieldItem[]>([])
  const relationData = ref<Api.Infra.CodeGenRelationItem[]>([])
  const databaseTables = ref<Api.Infra.DatabaseTable[]>([])
  const previewFiles = ref<Api.Infra.CodeGenFileItem[]>([])
  const activePreviewPath = ref('')
  const moduleFormRef = ref<FormInstance>()

  const steps = [
    { key: 'module', title: '生成配置' },
    { key: 'fields', title: '字段映射' },
    { key: 'relations', title: '数据库关系' },
    { key: 'components', title: '组件关联' },
    { key: 'preview', title: '代码预览' }
  ]
  const activeStep = ref(stepIndexFromQuery())
  const currentStepKey = computed(() => steps[activeStep.value]?.key || 'module')

  const moduleForm = reactive<Api.Infra.CodeGenTableUpdateParams>({
    businessName: '',
    moduleName: '',
    className: '',
    packageName: '',
    apiBasePath: '',
    frontendPath: '',
    routePath: '',
    permissionPrefix: '',
    menuParentId: '',
    generateType: 'single',
    author: ''
  })

  const moduleRules: FormRules = {
    businessName: [{ required: true, message: '请输入业务名称', trigger: 'blur' }],
    moduleName: [{ required: true, message: '请输入模块名', trigger: 'blur' }],
    className: [{ required: true, message: '请输入实体类名', trigger: 'blur' }]
  }

  const fieldOptions = computed(() =>
    fieldData.value.map((item) => ({
      label: `${item.columnName}${item.columnComment ? ` - ${item.columnComment}` : ''}`,
      value: item.columnName || ''
    }))
  )
  const activePreviewFile = computed(() =>
    previewFiles.value.find((file) => file.filePath === activePreviewPath.value)
  )

  const formTypeOptions = [
    'input',
    'textarea',
    'number',
    'select',
    'dict-select',
    'switch',
    'date',
    'datetime',
    'daterange',
    'upload',
    'richtext'
  ]
  const queryTypeOptions = ['eq', 'like', 'between', 'in']
  const relationTypeOptions = [
    'ONE_TO_ONE',
    'ONE_TO_MANY',
    'MANY_TO_ONE',
    'MANY_TO_MANY',
    'LOOKUP',
    'TREE'
  ]

  const fieldColumns: ColumnOption<Api.Infra.CodeGenFieldItem>[] = [
    { prop: 'columnName', label: '数据库字段', minWidth: 150 },
    { prop: 'propertyName', label: '属性名', minWidth: 150, useSlot: true },
    { prop: 'columnComment', label: '字段说明', minWidth: 160, useSlot: true },
    { prop: 'dbType', label: '数据库类型', minWidth: 130 },
    { prop: 'javaType', label: 'Java 类型', minWidth: 120, useSlot: true },
    { prop: 'tsType', label: 'TS 类型', minWidth: 110, useSlot: true },
    { prop: 'formType', label: '控件', minWidth: 130, useSlot: true },
    { prop: 'queryType', label: '查询', minWidth: 120, useSlot: true },
    { prop: 'required', label: '必填', width: 80, useSlot: true },
    { prop: 'listVisible', label: '列表', width: 80, useSlot: true },
    { prop: 'searchVisible', label: '搜索', width: 80, useSlot: true },
    { prop: 'formVisible', label: '表单', width: 80, useSlot: true }
  ]

  const relationColumns: ColumnOption<Api.Infra.CodeGenRelationItem>[] = [
    { prop: 'relationName', label: '关系名称', minWidth: 180, useSlot: true },
    { prop: 'relationType', label: '关系类型', minWidth: 150, useSlot: true },
    { prop: 'sourceColumn', label: '当前字段', minWidth: 170, useSlot: true },
    { prop: 'targetTable', label: '关联表', minWidth: 180, useSlot: true },
    { prop: 'targetColumn', label: '关联字段', minWidth: 130, useSlot: true },
    { prop: 'displayColumn', label: '展示字段', minWidth: 130, useSlot: true },
    {
      type: 'operation',
      label: '操作',
      width: 90,
      actions: [
        {
          key: 'delete',
          label: '删除',
          type: 'error',
          permission: 'infra:codegen:update',
          onClick: (row) => removeRelation(row)
        }
      ]
    }
  ]

  const componentColumns: ColumnOption<Api.Infra.CodeGenRelationItem>[] = [
    { prop: 'sourceColumn', label: '当前字段', minWidth: 170, useSlot: true },
    { prop: 'targetTable', label: '关联表', minWidth: 180, useSlot: true },
    { prop: 'targetColumn', label: '关联字段', minWidth: 130, useSlot: true },
    { prop: 'displayColumn', label: '展示字段', minWidth: 130, useSlot: true },
    { prop: 'generateQuery', label: '查询联动', width: 100, useSlot: true },
    { prop: 'generateForm', label: '表单控件', width: 100, useSlot: true },
    { prop: 'generateDetail', label: '详情展示', width: 100, useSlot: true }
  ]

  onMounted(async () => {
    await loadDetail()
    if (currentStepKey.value === 'preview') {
      await previewCode()
    }
  })

  watch(
    () => route.query.step,
    async () => {
      const nextIndex = stepIndexFromQuery()
      if (nextIndex !== activeStep.value) {
        activeStep.value = nextIndex
      }
      if (currentStepKey.value === 'preview' && !previewFiles.value.length) {
        await previewCode()
      }
    }
  )

  async function loadDetail() {
    if (!tableId.value) return
    loading.value = true
    try {
      const [table, fields, relations, dbTables] = await Promise.all([
        fetchGetCodeGenTable(tableId.value),
        fetchGetCodeGenFields(tableId.value),
        fetchGetCodeGenRelations(tableId.value),
        fetchGetDatabaseTables()
      ])
      tableInfo.value = table
      fieldData.value = fields
      relationData.value = relations
      databaseTables.value = dbTables
      Object.assign(moduleForm, table)
    } finally {
      loading.value = false
    }
  }

  async function saveModule(showMessage = true) {
    await moduleFormRef.value?.validate()
    const table = await fetchUpdateCodeGenTable(tableId.value, moduleForm)
    tableInfo.value = table
    Object.assign(moduleForm, table)
    if (showMessage) {
      ElMessage.success('模块配置已保存')
    }
  }

  async function syncFields() {
    fieldLoading.value = true
    try {
      fieldData.value = await fetchSyncCodeGenFields(tableId.value)
      ElMessage.success('表结构已同步')
    } finally {
      fieldLoading.value = false
    }
  }

  async function saveFields() {
    fieldData.value = await fetchUpdateCodeGenFields(tableId.value, fieldData.value)
    ElMessage.success('字段映射已保存')
  }

  async function analyzeRelations() {
    relationData.value = await fetchAnalyzeCodeGenRelations(tableId.value)
    ElMessage.success('关系分析完成')
  }

  function addRelation() {
    relationData.value.push({
      relationName: `relation_${relationData.value.length + 1}`,
      relationType: 'MANY_TO_ONE',
      sourceTable: tableInfo.value?.tableName || '',
      sourceColumn: '',
      targetTable: '',
      targetColumn: 'id',
      displayColumn: 'name',
      generateQuery: true,
      generateForm: true,
      generateDetail: true,
      deleteStrategy: 'manual',
      sourceType: 'manual'
    })
  }

  function removeRelation(row: Api.Infra.CodeGenRelationItem) {
    relationData.value = relationData.value.filter((item) => item !== row)
  }

  async function saveRelations(showMessage = true) {
    relationData.value = await fetchUpdateCodeGenRelations(tableId.value, relationData.value)
    if (showMessage) {
      ElMessage.success('关系配置已保存')
    }
  }

  async function previewCode() {
    const preview = await fetchPreviewCodeGen(tableId.value)
    tableInfo.value = preview.table
    fieldData.value = preview.fields || []
    relationData.value = preview.relations || []
    previewFiles.value = preview.files || []
    activePreviewPath.value = previewFiles.value[0]?.filePath || ''
  }

  async function downloadCode() {
    if (!previewFiles.value.length) {
      await previewCode()
    }
    downloading.value = true
    try {
      const blob = await fetchDownloadCodeGen(tableId.value)
      const url = URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = `${tableInfo.value?.tableName || tableId.value}-codegen.zip`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      URL.revokeObjectURL(url)
    } finally {
      downloading.value = false
    }
  }

  async function submitCurrentStep() {
    submittingStep.value = true
    try {
      await saveCurrentStep()
    } finally {
      submittingStep.value = false
    }
  }

  async function nextStep() {
    if (activeStep.value >= steps.length - 1) return

    activeStep.value += 1
    if (currentStepKey.value === 'preview' && !previewFiles.value.length) {
      advancingStep.value = true
      try {
        await previewCode()
      } finally {
        advancingStep.value = false
      }
    }
  }

  function prevStep() {
    if (activeStep.value > 0) {
      activeStep.value -= 1
    }
  }

  async function saveCurrentStep() {
    if (currentStepKey.value === 'module') {
      await saveModule()
    }
    if (currentStepKey.value === 'fields') {
      await saveFields()
    }
    if (currentStepKey.value === 'relations') {
      await saveModule(false)
      await saveRelations(false)
      ElMessage.success('数据库关系已保存')
    }
    if (currentStepKey.value === 'components') {
      await saveRelations()
    }
  }

  function stepIndexFromQuery() {
    const step = String(route.query.step || '')
    const index = steps.findIndex((item) => item.key === step)
    return index >= 0 ? index : 0
  }
</script>

<style scoped lang="scss">
  .codegen-detail-page {
    display: flex;
    height: var(--art-full-height);
    min-height: 0;
    font-size: 13px;
  }

  .codegen-detail-shell {
    display: flex;
    flex: 1;
    flex-direction: column;
    height: 100%;
    min-height: 0;
    margin-top: 0;

    :deep(.el-card__header) {
      padding: 10px 18px;
    }

    :deep(.el-card__body) {
      display: flex;
      flex: 1;
      flex-direction: column;
      min-height: 0;
      height: 100%;
      padding: 0;
      overflow: visible;
    }
  }

  .codegen-detail-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    min-height: 32px;
    gap: 16px;
  }

  .codegen-detail-title {
    display: flex;
    gap: 8px;
    align-items: baseline;
    min-width: 0;

    strong {
      font-size: 14px;
      font-weight: 600;
      line-height: 20px;
      color: var(--el-text-color-primary);
    }

    span {
      overflow: hidden;
      font-size: 12px;
      line-height: 18px;
      color: var(--el-text-color-secondary);
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }

  .codegen-detail-actions {
    display: flex;
    flex-shrink: 0;
    gap: 8px;
    align-items: center;
  }

  .codegen-step-bar {
    padding: 16px 22px 14px;
    overflow-x: auto;
    border-bottom: 1px solid var(--el-border-color-lighter);
  }

  .codegen-steps {
    width: min(1100px, 100%);
    margin: 0 auto;
  }

  .codegen-step-body {
    display: flex;
    flex: 1;
    flex-direction: column;
    min-height: 0;
    padding: 12px 18px 18px;
    overflow: hidden;
  }

  .codegen-step-section {
    flex: 1;
    min-height: 0;

    :deep(.art-table),
    :deep(.el-table) {
      height: auto !important;
    }

    :deep(.el-table__inner-wrapper),
    :deep(.el-table__body-wrapper),
    :deep(.el-scrollbar__wrap) {
      height: auto !important;
      max-height: none !important;
    }

    :deep(.el-table th),
    :deep(.el-table td) {
      font-size: 13px;
    }
  }

  .codegen-module-form {
    width: min(100%, 1080px);
    max-width: 1080px;
    margin: 0 auto;

    :deep(.el-form-item) {
      margin-bottom: 14px;
    }

    :deep(.el-form-item__label),
    :deep(.el-input__inner),
    :deep(.el-select__placeholder) {
      font-size: 13px;
    }
  }

  .codegen-toolbar {
    display: flex;
    gap: 8px;
    align-items: center;
    margin-bottom: 8px;
  }

  .codegen-relation-toolbar {
    justify-content: space-between;
  }

  .codegen-relation-type,
  .codegen-relation-actions {
    display: flex;
    gap: 8px;
    align-items: center;
  }

  .codegen-relation-type {
    span {
      flex-shrink: 0;
      font-size: 13px;
      color: var(--el-text-color-regular);
    }
  }

  .codegen-generate-type-select {
    width: 220px;
  }

  .codegen-preview-section {
    display: flex;
    flex-direction: column;
    height: 100%;
    overflow: hidden;
  }

  .codegen-preview-layout {
    display: grid;
    grid-template-columns: 300px minmax(0, 1fr);
    flex: 1;
    min-height: 0;
    gap: 12px;
  }

  .codegen-file-list {
    display: flex;
    flex-direction: column;
    min-height: 0;
    padding: 8px;
    overflow: auto;
    background: var(--el-fill-color-lighter);
    border: 1px solid var(--el-border-color-light);
    border-radius: 8px;
  }

  .codegen-file-item {
    display: flex;
    flex-direction: column;
    gap: 3px;
    width: 100%;
    padding: 8px 10px;
    text-align: left;
    cursor: pointer;
    background: transparent;
    border: 0;
    border-radius: 6px;

    span {
      font-size: 12px;
      line-height: 16px;
      color: var(--el-text-color-secondary);
    }

    strong {
      overflow: hidden;
      font-size: 12px;
      font-weight: 500;
      line-height: 18px;
      color: var(--el-text-color-primary);
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    &:hover,
    &.active {
      background: var(--el-color-primary-light-9);
    }

    &.active strong {
      color: var(--el-color-primary);
    }
  }

  .codegen-preview {
    height: 100%;
    margin: 0;
    min-height: 0;
    padding: 12px;
    overflow: auto;
    font-size: 13px;
    line-height: 1.6;
    color: var(--el-text-color-primary);
    white-space: pre;
    background: var(--el-fill-color-lighter);
    border: 1px solid var(--el-border-color-light);
    border-radius: 8px;
  }
</style>
