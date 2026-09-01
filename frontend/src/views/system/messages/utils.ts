import type { TagProps } from 'element-plus'

export function splitText(value?: string) {
  return (value || '')
    .split(/[\n,，]/)
    .map((item) => item.trim())
    .filter(Boolean)
}

export function joinText(value?: string[]) {
  return (value || []).join('\n')
}

export function paramsFromKeys(keys?: string[], source?: Record<string, string>) {
  return (keys || []).reduce<Record<string, string>>((result, key) => {
    result[key] = source?.[key] || ''
    return result
  }, {})
}

export function statusText(status?: string) {
  if (status === 'SUCCESS') return '成功'
  if (status === 'FAILED') return '失败'
  return '待发送'
}

export function statusTagType(status?: string): TagProps['type'] {
  if (status === 'SUCCESS') return 'success'
  if (status === 'FAILED') return 'danger'
  return 'info'
}

export function stringifyParams(params?: Record<string, string>) {
  if (!params || Object.keys(params).length === 0) return '-'
  return Object.entries(params)
    .map(([key, value]) => `${key}: ${value}`)
    .join('\n')
}
