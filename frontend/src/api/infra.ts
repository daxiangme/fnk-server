import request from '@/utils/http'
import axios from 'axios'
import { useUserStore } from '@/store/modules/user'

const API_BASE_URL = (import.meta.env.VITE_API_URL || '').replace(/\/$/, '')

export function fetchGetDatabaseTables(params?: Api.Infra.DatabaseTableSearchParams) {
  return request.get<Api.Infra.DatabaseTable[]>({
    url: '/infra/codegen/database/tables',
    params
  })
}

export function fetchGetCodeGenTables(params: Api.Infra.CodeGenTableSearchParams) {
  return request.get<Api.Infra.CodeGenTableList>({
    url: '/infra/codegen/tables',
    params
  })
}

export function fetchImportCodeGenTables(data: Api.Infra.CodeGenTableImportParams) {
  return request.post<Api.Infra.CodeGenTableItem[]>({
    url: '/infra/codegen/tables/import',
    data
  })
}

export function fetchGetCodeGenTable(id: string) {
  return request.get<Api.Infra.CodeGenTableItem>({
    url: `/infra/codegen/tables/${id}`
  })
}

export function fetchUpdateCodeGenTable(id: string, data: Api.Infra.CodeGenTableUpdateParams) {
  return request.put<Api.Infra.CodeGenTableItem>({
    url: `/infra/codegen/tables/${id}`,
    data
  })
}

export function fetchSyncCodeGenFields(id: string) {
  return request.post<Api.Infra.CodeGenFieldItem[]>({
    url: `/infra/codegen/tables/${id}/sync-fields`
  })
}

export function fetchGetCodeGenFields(id: string) {
  return request.get<Api.Infra.CodeGenFieldItem[]>({
    url: `/infra/codegen/tables/${id}/fields`
  })
}

export function fetchUpdateCodeGenFields(id: string, fields: Api.Infra.CodeGenFieldItem[]) {
  return request.put<Api.Infra.CodeGenFieldItem[]>({
    url: `/infra/codegen/tables/${id}/fields`,
    data: { fields }
  })
}

export function fetchGetCodeGenRelations(id: string) {
  return request.get<Api.Infra.CodeGenRelationItem[]>({
    url: `/infra/codegen/tables/${id}/relations`
  })
}

export function fetchAnalyzeCodeGenRelations(id: string) {
  return request.post<Api.Infra.CodeGenRelationItem[]>({
    url: `/infra/codegen/tables/${id}/relations/analyze`
  })
}

export function fetchUpdateCodeGenRelations(
  id: string,
  relations: Api.Infra.CodeGenRelationItem[]
) {
  return request.put<Api.Infra.CodeGenRelationItem[]>({
    url: `/infra/codegen/tables/${id}/relations`,
    data: { relations }
  })
}

export function fetchPreviewCodeGen(id: string) {
  return request.post<Api.Infra.CodeGenPreview>({
    url: `/infra/codegen/tables/${id}/preview`
  })
}

export async function fetchDownloadCodeGen(id: string) {
  const { accessToken } = useUserStore()
  const response = await axios.post<Blob>(
    `${API_BASE_URL}/infra/codegen/tables/${id}/download`,
    undefined,
    {
      responseType: 'blob',
      headers: accessToken ? { Authorization: accessToken } : undefined
    }
  )
  return response.data
}

export type InfraConfigPayload = Partial<Api.Infra.ConfigItem>
export type InfraFileConfigPayload = Partial<Api.Infra.FileConfigItem>

export function fetchGetInfraConfigList(params: Api.Infra.ConfigSearchParams) {
  return request.get<Api.Infra.ConfigList>({
    url: '/infra/config',
    params
  })
}

export function fetchSaveInfraConfig(data: InfraConfigPayload) {
  if (data.id) {
    return request.put<Api.Infra.ConfigItem>({
      url: `/infra/config/${data.id}`,
      data
    })
  }
  return request.post<Api.Infra.ConfigItem>({
    url: '/infra/config',
    data
  })
}

export function fetchDeleteInfraConfig(id: string) {
  return request.del<void>({
    url: `/infra/config/${id}`
  })
}

export function fetchGetInfraFileConfigList(params: Api.Infra.FileConfigSearchParams) {
  return request.get<Api.Infra.FileConfigList>({
    url: '/infra/file-configs',
    params
  })
}

export function fetchSaveInfraFileConfig(data: InfraFileConfigPayload) {
  if (data.id) {
    return request.put<Api.Infra.FileConfigItem>({
      url: `/infra/file-configs/${data.id}`,
      data
    })
  }
  return request.post<Api.Infra.FileConfigItem>({
    url: '/infra/file-configs',
    data
  })
}

export function fetchSetInfraFileConfigMaster(id: string) {
  return request.put<Api.Infra.FileConfigItem>({
    url: `/infra/file-configs/${id}/master`
  })
}

export function fetchTestInfraFileConfig(id: string) {
  return request.post<string>({
    url: `/infra/file-configs/${id}/test`
  })
}

export function fetchDeleteInfraFileConfig(id: string) {
  return request.del<void>({
    url: `/infra/file-configs/${id}`
  })
}

export function fetchGetInfraFileList(params: Api.Infra.FileSearchParams) {
  return request.get<Api.Infra.FileList>({
    url: '/infra/files',
    params
  })
}

export function fetchUploadInfraFile(file: File) {
  const data = new FormData()
  data.append('file', file)
  return request.post<Api.Infra.FileItem>({
    url: '/infra/files/upload',
    data
  })
}

export function fetchDeleteInfraFile(id: string) {
  return request.del<void>({
    url: `/infra/files/${id}`
  })
}

export async function fetchDownloadInfraFile(id: string) {
  const { accessToken } = useUserStore()
  const response = await axios.get<Blob>(`${API_BASE_URL}/infra/files/${id}/content`, {
    responseType: 'blob',
    headers: accessToken ? { Authorization: accessToken } : undefined
  })
  return response.data
}
