/**
 * useAuth - 权限验证管理
 *
 * 提供统一的权限验证功能，支持前端和后端两种权限模式。
 * 用于控制页面按钮、操作等功能的显示和访问权限。
 *
 * ## 主要功能
 *
 * 1. 权限检查 - 检查用户是否拥有指定的权限标识
 * 2. 双模式支持 - 自动适配前端模式和后端模式的权限验证
 * 3. 前端模式 - 从用户信息中获取按钮权限列表（如 ['add', 'edit', 'delete']）
 * 4. 后端模式 - 从路由 meta 配置中获取权限列表（如 [{ authMark: 'add' }]）
 *
 * ## 使用示例
 *
 * ```typescript
 * const { hasAuth } = useAuth()
 *
 * // 检查是否有新增权限
 * if (hasAuth('add')) {
 *   // 显示新增按钮
 * }
 *
 * // 在模板中使用
 * <el-button v-if="hasAuth('edit')">编辑</el-button>
 * <el-button v-if="hasAuth('delete')">删除</el-button>
 * ```
 *
 * @module useAuth
 * @author Art Design Pro Team
 */

import { hasPermission, hasRole } from '@/utils/permission'

export const useAuth = () => {
  return {
    hasAuth: hasPermission,
    hasPermission,
    hasRole
  }
}
