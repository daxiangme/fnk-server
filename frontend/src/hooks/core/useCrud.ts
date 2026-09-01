import { reactive, ref, shallowRef, onMounted, toRaw, type UnwrapNestedRefs } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance } from 'element-plus'
import type { ElMessageBoxOptions } from 'element-plus'

type Awaitable<T> = T | Promise<T>

export interface CrudPageResult<TRecord> {
  records?: TRecord[]
  total?: number
}

export interface CrudRemoveOptions<TRecord> {
  title?: string
  message: string | ((row: TRecord) => string)
  successMessage?: string
  confirmOptions?: ElMessageBoxOptions
}

export interface CrudPageConfig<TRecord, TQuery extends object, TForm extends object> {
  defaultQuery: () => TQuery
  defaultForm: () => TForm
  listApi: (query: UnwrapNestedRefs<TQuery>) => Promise<CrudPageResult<TRecord>>
  saveApi?: (form: TForm) => Promise<unknown>
  removeApi?: (row: TRecord) => Promise<unknown>
  getEditForm?: (row: TRecord) => Awaitable<Partial<TForm>>
  immediate?: boolean
  saveSuccessMessage?: string
  removeOptions?: CrudRemoveOptions<TRecord>
  afterLoad?: (records: TRecord[], total: number) => void
  afterSave?: () => void
  afterRemove?: () => void
}

export function useCrudPage<TRecord, TQuery extends object, TForm extends object>(
  config: CrudPageConfig<TRecord, TQuery, TForm>
) {
  const loading = ref(false)
  const saving = ref(false)
  const dialogVisible = ref(false)
  const formRef = ref<FormInstance>()
  const records = shallowRef<TRecord[]>([])
  const total = ref(0)
  const query = reactive(config.defaultQuery())
  const form = reactive(config.defaultForm())

  async function loadData() {
    loading.value = true
    try {
      const page = await config.listApi(query)
      records.value = page.records || []
      total.value = page.total || 0
      config.afterLoad?.(records.value, total.value)
    } finally {
      loading.value = false
    }
  }

  function resetQuery() {
    Object.assign(query, config.defaultQuery())
    loadData()
  }

  function resetForm() {
    Object.assign(form, config.defaultForm())
    formRef.value?.clearValidate()
  }

  function openCreate() {
    resetForm()
    dialogVisible.value = true
  }

  async function openEdit(row: TRecord) {
    resetForm()
    const editForm = config.getEditForm ? await config.getEditForm(row) : (row as Partial<TForm>)
    Object.assign(form, editForm)
    dialogVisible.value = true
  }

  async function submit() {
    if (!config.saveApi || !formRef.value) return
    await formRef.value.validate()
    saving.value = true
    try {
      await config.saveApi({ ...(toRaw(form) as TForm) })
      ElMessage.success(config.saveSuccessMessage || '保存成功')
      dialogVisible.value = false
      await loadData()
      config.afterSave?.()
    } finally {
      saving.value = false
    }
  }

  function remove(row: TRecord) {
    if (!config.removeApi || !config.removeOptions) return

    const message =
      typeof config.removeOptions.message === 'function'
        ? config.removeOptions.message(row)
        : config.removeOptions.message

    ElMessageBox.confirm(message, config.removeOptions.title || '删除确认', {
      type: 'warning',
      ...(config.removeOptions.confirmOptions || {})
    })
      .then(async () => {
        await config.removeApi?.(row)
        ElMessage.success(config.removeOptions?.successMessage || '删除成功')
        await loadData()
        config.afterRemove?.()
      })
      .catch(() => undefined)
  }

  if (config.immediate !== false) {
    onMounted(loadData)
  }

  return {
    loading,
    saving,
    dialogVisible,
    formRef,
    records,
    total,
    query,
    form,
    loadData,
    resetQuery,
    resetForm,
    openCreate,
    openEdit,
    submit,
    remove
  }
}
