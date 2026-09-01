import request from '@/utils/http'

export type UserPayload = Partial<Api.SystemManage.UserListItem> & {
  password?: string
  roleIdList?: string[]
}

export type RolePayload = Partial<Api.SystemManage.RoleListItem> & {
  roleScope?: string[]
}

export type MenuPayload = Partial<Api.SystemManage.MenuItem>

export function fetchGetUserList(params: Api.SystemManage.UserSearchParams) {
  return request.get<Api.SystemManage.UserList>({
    url: '/system/admin/user',
    params
  })
}

export function fetchGetUserRoles(userId: string) {
  return request.get<string[]>({
    url: `/system/admin/user/${userId}/roles`
  })
}

export function fetchSaveUser(data: UserPayload) {
  if (data.id) {
    return request.put<Api.SystemManage.UserListItem>({
      url: `/system/admin/user/${data.id}`,
      data
    })
  }
  return request.post<Api.SystemManage.UserListItem>({
    url: '/system/admin/user',
    data
  })
}

export function fetchDeleteUser(id: string) {
  return request.del<void>({
    url: `/system/admin/user/${id}`
  })
}

export function fetchGetRoleList(params: Api.SystemManage.RoleSearchParams) {
  return request.get<Api.SystemManage.RoleList>({
    url: '/system/role',
    params
  })
}

export function fetchGetAllRoles() {
  return request.get<Api.SystemManage.RoleListItem[]>({
    url: '/system/role/all'
  })
}

export function fetchGetRoleMenus(roleId: string) {
  return request.get<string[]>({
    url: `/system/role/${roleId}/menus`
  })
}

export function fetchSaveRole(data: RolePayload) {
  if (data.id) {
    return request.put<Api.SystemManage.RoleListItem>({
      url: `/system/role/${data.id}`,
      data
    })
  }
  return request.post<Api.SystemManage.RoleListItem>({
    url: '/system/role',
    data
  })
}

export function fetchDeleteRole(id: string) {
  return request.del<number>({
    url: `/system/role/${id}`
  })
}

export function fetchGetMenuList(params?: { name?: string; permission?: string }) {
  return request.get<Api.SystemManage.MenuItem[]>({
    url: '/system/menu',
    params
  })
}

export function fetchSaveMenu(data: MenuPayload) {
  if (data.id) {
    return request.put<Api.SystemManage.MenuItem>({
      url: `/system/menu/${data.id}`,
      data
    })
  }
  return request.post<Api.SystemManage.MenuItem>({
    url: '/system/menu',
    data
  })
}

export function fetchDeleteMenu(id: string) {
  return request.del<void>({
    url: `/system/menu/${id}`
  })
}

export function fetchRefreshMenuPermissionCache() {
  return request.post<void>({
    url: '/system/menu/permission-cache/refresh'
  })
}
