/**
 * 后端菜单 routeKey 到前端页面组件的稳定映射。
 *
 * path 只表达浏览器 URL；组件加载优先使用 routeKey，避免动态参数路由依赖
 * /foo/:id 这类 path 推导文件路径。
 */
export const backendComponentPathMap: Record<string, string> = {
  home: '/home',
  system_user: '/system/user',
  system_role: '/system/role',
  system_menu: '/system/menu',
  system_dict: '/system/dict',
  system_notice: '/system/notice',
  system_notice_center: '/system/notice-center',
  system_messages_sms_channel: '/system/messages/sms/channel',
  system_messages_sms_template: '/system/messages/sms/template',
  system_messages_sms_log: '/system/messages/sms/log',
  system_messages_mail_account: '/system/messages/mail/account',
  system_messages_mail_template: '/system/messages/mail/template',
  system_messages_mail_log: '/system/messages/mail/log',
  system_messages_notify_template: '/system/messages/notify/template',
  system_messages_notify_message: '/system/messages/notify/message',
  system_messages_notice: '/system/notice',
  infra_codegen: '/infra/codegen',
  infra_codegen_detail: '/infra/codegen/detail',
  infra_config: '/infra/config',
  infra_file: '/infra/file'
}

export function resolveBackendComponentPath(routeKey?: string) {
  if (!routeKey) return ''
  return backendComponentPathMap[routeKey] || ''
}
