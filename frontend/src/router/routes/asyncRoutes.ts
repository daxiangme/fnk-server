// 权限文档：https://www.artd.pro/docs/zh/guide/in-depth/permission.html
import { AppRouteRecord } from '@/types/router'

/**
 * 动态路由（需要权限才能访问的路由）
 * 一期只允许后端菜单驱动业务路由；前端权限模式不再注册示例路由。
 */
export const asyncRoutes: AppRouteRecord[] = []
