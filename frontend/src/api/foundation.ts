import request from '@/utils/http'

export type DictTypePayload = Partial<Api.Foundation.DictTypeItem>
export type DictItemPayload = Partial<Api.Foundation.DictItemItem>
export type NoticePayload = Partial<Api.Foundation.NoticeItem>

export function fetchGetDictTypeList(params: Api.Foundation.DictTypeSearchParams) {
  return request.get<Api.Foundation.DictTypeList>({
    url: '/system/dict/type',
    params
  })
}

export function fetchGetAllDictTypes() {
  return request.get<Api.Foundation.DictTypeItem[]>({
    url: '/system/dict/type/all'
  })
}

export function fetchSaveDictType(data: DictTypePayload) {
  if (data.id) {
    return request.put<Api.Foundation.DictTypeItem>({
      url: `/system/dict/type/${data.id}`,
      data
    })
  }
  return request.post<Api.Foundation.DictTypeItem>({
    url: '/system/dict/type',
    data
  })
}

export function fetchDeleteDictType(id: string) {
  return request.del<void>({
    url: `/system/dict/type/${id}`
  })
}

export function fetchGetDictItemList(params: Api.Foundation.DictItemSearchParams) {
  return request.get<Api.Foundation.DictItemList>({
    url: '/system/dict/item',
    params
  })
}

export async function fetchGetDictItemsByCode(dictCode: string) {
  const page = await fetchGetDictItemList({
    dictCode,
    page: 1,
    pageSize: 1000
  })
  return page.records || []
}

export function fetchSaveDictItem(data: DictItemPayload) {
  if (data.id) {
    return request.put<Api.Foundation.DictItemItem>({
      url: `/system/dict/item/${data.id}`,
      data
    })
  }
  return request.post<Api.Foundation.DictItemItem>({
    url: '/system/dict/item',
    data
  })
}

export function fetchDeleteDictItem(id: string) {
  return request.del<void>({
    url: `/system/dict/item/${id}`
  })
}

export function fetchGetDictOptions(dictCode: string) {
  return request.get<Api.Foundation.DictItemItem[]>({
    url: `/system/dict/options/${dictCode}`
  })
}

export function fetchGetNoticeList(params: Api.Foundation.NoticeSearchParams) {
  return request.get<Api.Foundation.NoticeList>({
    url: '/system/notice',
    params
  })
}

export function fetchSaveNotice(data: NoticePayload) {
  if (data.id) {
    return request.put<Api.Foundation.NoticeItem>({
      url: `/system/notice/${data.id}`,
      data
    })
  }
  return request.post<Api.Foundation.NoticeItem>({
    url: '/system/notice',
    data
  })
}

export function fetchDeleteNotice(id: string) {
  return request.del<void>({
    url: `/system/notice/${id}`
  })
}

export function fetchPublishNotice(id: string) {
  return request.post<Api.Foundation.NoticeItem>({
    url: `/system/notice/${id}/publish`
  })
}

export function fetchRevokeNotice(id: string) {
  return request.post<Api.Foundation.NoticeItem>({
    url: `/system/notice/${id}/revoke`
  })
}

export function fetchGetPublishedNotices() {
  return request.get<Api.Foundation.NoticeItem[]>({
    url: '/system/notice/published'
  })
}

export function fetchGetMyNoticeList(params: Api.Foundation.UserNoticeSearchParams) {
  return request.get<Api.Foundation.UserNoticeList>({
    url: '/system/notice/my',
    params
  })
}

export function fetchGetMyUnreadNoticeCount() {
  return request.get<number>({
    url: '/system/notice/my/unread-count'
  })
}

export function fetchReadMyNotice(id: string) {
  return request.post<void>({
    url: `/system/notice/my/${id}/read`
  })
}

export function fetchReadAllMyNotices() {
  return request.post<void>({
    url: '/system/notice/my/read-all'
  })
}
