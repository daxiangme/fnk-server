import { useUserStore } from '@/store/modules/user'

function normalizeValues(value: string | string[]) {
  return Array.isArray(value) ? value : [value]
}

export function hasPermission(permission: string | string[]) {
  const userStore = useUserStore()
  const permissions = userStore.getUserInfo.permissions || []
  if (permissions.includes('*')) {
    return true
  }
  return normalizeValues(permission).some((item) => permissions.includes(item))
}

export function hasRole(role: string | string[]) {
  const userStore = useUserStore()
  const roles = userStore.getUserInfo.roles || []
  if (roles.includes('*')) {
    return true
  }
  return normalizeValues(role).some((item) => roles.includes(item))
}
