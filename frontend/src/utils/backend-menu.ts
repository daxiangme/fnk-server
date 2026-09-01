import type { AppRouteRecord } from '@/types/router'
import { RoutesAlias } from '@/router/routesAlias'
import { resolveBackendComponentPath } from '@/router/component-map'

export function normalizeMenuType(type: Api.SystemManage.MenuType): 'TABLE' | 'MENU' | 'BUTTON' {
  if (typeof type === 'string') return type as 'TABLE' | 'MENU' | 'BUTTON'
  return (type?.value || 'MENU') as 'TABLE' | 'MENU' | 'BUTTON'
}

const viewModules = import.meta.glob('../views/**/*.vue')
const missingComponentFallback = '/exception/403'

export function buildMenuTree(items: Api.SystemManage.MenuItem[]) {
  const cloned = items.map((item) => ({ ...item, children: [] as Api.SystemManage.MenuItem[] }))
  const map = new Map(cloned.map((item) => [item.id, item]))
  const roots: Api.SystemManage.MenuItem[] = []

  cloned
    .sort((a, b) => (a.orderSort || 0) - (b.orderSort || 0))
    .forEach((item) => {
      if (item.rootId && item.rootId !== '0' && map.has(item.rootId)) {
        map.get(item.rootId)?.children?.push(item)
      } else {
        roots.push(item)
      }
    })

  return roots
}

export function backendMenusToRoutes(items: Api.SystemManage.MenuItem[]): AppRouteRecord[] {
  return buildMenuTree(items)
    .flatMap((item) => menuToRoutes(item, 0))
}

function menuToRoutes(
  item: Api.SystemManage.MenuItem,
  depth: number,
  parent?: Api.SystemManage.MenuItem
): AppRouteRecord[] {
  const type = normalizeMenuType(item.type)
  if (type === 'BUTTON') return []

  const childRoutes = (item.children || []).flatMap((child) => menuToRoutes(child, depth + 1, item))

  const isDirectory = type === 'TABLE'
  const routePath = normalizeRoutePath(item.path || item.routeKey)
  const component = resolveComponent(item, type, depth)

  if (type === 'MENU' && !routePath) {
    warnInvalidMenu(item, '菜单类型必须配置访问路径')
    return childRoutes
  }

  const route: AppRouteRecord = {
    id: item.id,
    path: routePath,
    name: item.routeKey,
    component,
    redirect: isDirectory ? findFirstNavigablePath(childRoutes) : undefined,
    meta: {
      title: item.name,
      icon: item.localIcon || item.icon,
      isHide: item.visible === false,
      link: item.isIframe ? item.path : undefined,
      isIframe: item.isIframe,
      authMark: item.permission,
      parentPath: item.rootId,
      activePath: item.visible === false ? normalizeRoutePath(parent?.path) : undefined,
      menuType: type,
      isDirectory
    },
    children: isDirectory && childRoutes.length ? childRoutes : undefined
  }

  // 数据库菜单树表达权限归属，不完全等同 Vue Router 组件嵌套。
  // 页面 MENU 下的隐藏详情页不能作为 Vue 子路由，否则父页面需要 <router-view> 才能显示。
  return isDirectory ? [route] : [route, ...childRoutes]
}

function resolveComponent(
  item: Api.SystemManage.MenuItem,
  type: 'TABLE' | 'MENU' | 'BUTTON',
  depth: number
): string | undefined {
  if (item.isIframe) return undefined
  if (type === 'TABLE') return depth === 0 ? RoutesAlias.Layout : ''

  const componentPath = resolveBackendComponentPath(item.routeKey) || normalizeRoutePath(item.path)
  if (!componentPath) return missingComponentFallback

  if (!hasViewComponent(componentPath)) {
    warnInvalidMenu(item, `未找到页面组件 ${componentPath}，将展示 403 页面`)
    return missingComponentFallback
  }

  return componentPath
}

function hasViewComponent(componentPath: string) {
  const cleanPath = componentPath.replace(/^\/+/, '')
  return Boolean(
    viewModules[`../views/${cleanPath}.vue`] || viewModules[`../views/${cleanPath}/index.vue`]
  )
}

function normalizeRoutePath(path?: string) {
  if (!path) return ''
  if (path.startsWith('http://') || path.startsWith('https://')) return path
  return path.startsWith('/') ? path : `/${path}`
}

function findFirstNavigablePath(children: AppRouteRecord[]): string | undefined {
  for (const child of children) {
    if (
      child.path &&
      !child.meta?.isHide &&
      child.component &&
      child.component !== RoutesAlias.Layout &&
      !child.meta?.link &&
      child.meta?.isIframe !== true
    ) {
      return child.path
    }

    const nestedPath = findFirstNavigablePath(child.children || [])
    if (nestedPath) return nestedPath
  }

  return undefined
}

function warnInvalidMenu(item: Api.SystemManage.MenuItem, message: string) {
  if (import.meta.env.DEV) {
    console.warn(`[BackendMenu] ${message}: ${item.name}(${item.routeKey})`)
  }
}
