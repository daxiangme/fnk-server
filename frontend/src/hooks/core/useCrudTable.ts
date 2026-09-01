import { computed } from 'vue'
import { useTableColumns } from './useTableColumns'
import { useCrudPage, type CrudPageConfig } from './useCrud'
import type { ColumnOption } from '@/types/component'

type PageQuery = {
  page?: number
  pageSize?: number
}

export type CrudTableConfig<
  TRecord,
  TQuery extends PageQuery,
  TForm extends object
> = CrudPageConfig<TRecord, TQuery, TForm> & {
  columnsFactory?: () => ColumnOption<TRecord>[]
}

export function useCrudTable<TRecord, TQuery extends PageQuery, TForm extends object>(
  config: CrudTableConfig<TRecord, TQuery, TForm>
) {
  const crud = useCrudPage<TRecord, TQuery, TForm>(config)
  const columnConfig = config.columnsFactory ? useTableColumns<TRecord>(config.columnsFactory) : null

  const data = computed(() => crud.records.value)
  const pagination = computed(() => ({
    current: crud.query.page || 1,
    size: crud.query.pageSize || 10,
    total: crud.total.value
  }))

  function handleSizeChange(size: number) {
    crud.query.pageSize = size as TQuery['pageSize']
    crud.query.page = 1 as TQuery['page']
    crud.loadData()
  }

  function handleCurrentChange(current: number) {
    crud.query.page = current as TQuery['page']
    crud.loadData()
  }

  function refreshData() {
    return crud.loadData()
  }

  return {
    ...crud,
    data,
    pagination,
    columns: columnConfig?.columns,
    columnChecks: columnConfig?.columnChecks,
    handleSizeChange,
    handleCurrentChange,
    refreshData
  }
}
