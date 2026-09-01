/**
 * 快速入口配置
 * 包含：应用列表、快速链接等配置
 */
import type { FastEnterConfig } from '@/types/config'

const fastEnterConfig: FastEnterConfig = {
  // 显示条件（屏幕宽度）
  minWidth: 1200,
  // 应用列表
  applications: [
    {
      name: '首页',
      description: '系统首页',
      icon: 'ri:home-smile-line',
      iconColor: '#377dff',
      enabled: true,
      order: 1,
      routeName: 'home'
    },
    {
      name: '用户管理',
      description: '维护后台用户与角色绑定',
      icon: 'ri:user-line',
      iconColor: '#ff3b30',
      enabled: true,
      order: 2,
      routeName: 'system_user'
    },
    {
      name: '角色管理',
      description: '维护角色与菜单按钮权限',
      icon: 'ri:user-settings-line',
      iconColor: '#7A7FFF',
      enabled: true,
      order: 3,
      routeName: 'system_role'
    },
    {
      name: '菜单管理',
      description: '维护页面菜单与按钮权限',
      icon: 'ri:menu-line',
      iconColor: '#13DEB9',
      enabled: true,
      order: 4,
      routeName: 'system_menu'
    }
  ],
  // 快速链接
  quickLinks: [
    {
      name: '登录',
      enabled: true,
      order: 1,
      routeName: 'Login'
    }
  ]
}

export default Object.freeze(fastEnterConfig)
