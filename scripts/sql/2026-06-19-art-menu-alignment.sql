-- Align backend-driven menus with the latest Art Design Pro sidebar structure.
-- After running this SQL against a live database, clear the role permission cache or re-login users.

BEGIN;

UPDATE `system_menu`
SET `deleted` = 1,
    `update_time` = NOW(),
    `remark` = '二期菜单结构调整：旧首页菜单停用，入口改为仪表盘'
WHERE `id` = '1737364028824821762';

UPDATE `system_menu`
SET `deleted` = 1,
    `update_time` = NOW(),
    `remark` = '二期菜单结构调整：临时 Art 示例根菜单停用'
WHERE `id` = '920000000000000000';

UPDATE `system_menu`
SET `deleted` = 1,
    `update_time` = NOW(),
    `remark` = '二期菜单结构调整：临时 Art 示例菜单停用'
WHERE `root_id` = '920000000000000000';

REPLACE INTO `system_menu`
(`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`)
VALUES
('810000000000000000', '0', '仪表盘', 'dashboard', 1, 0, '/dashboard', 'ri:pie-chart-line', NULL, 1, '', 'TABLE', 'Art Design Pro 仪表盘目录', 0, NOW(), NOW()),
('810000000000000101', '810000000000000000', '控制台', 'dashboard_console', 1, 0, '/dashboard/console', 'ri:home-smile-2-line', NULL, 1, 'art:dashboard:console:view', 'MENU', 'Art Design Pro 控制台', 0, NOW(), NOW()),
('810000000000000102', '810000000000000000', '分析页', 'dashboard_analysis', 2, 0, '/dashboard/analysis', 'ri:align-item-bottom-line', NULL, 1, 'art:dashboard:analysis:view', 'MENU', 'Art Design Pro 分析页', 0, NOW(), NOW()),
('810000000000000103', '810000000000000000', '电商页', 'dashboard_ecommerce', 3, 0, '/dashboard/ecommerce', 'ri:bar-chart-box-line', NULL, 1, 'art:dashboard:ecommerce:view', 'MENU', 'Art Design Pro 电商页', 0, NOW(), NOW()),

('820000000000000000', '0', '模板中心', 'template', 2, 0, '/template', 'ri:apps-2-line', NULL, 1, '', 'TABLE', 'Art Design Pro 模板中心', 0, NOW(), NOW()),
('820000000000000101', '820000000000000000', '卡片模板', 'template_cards', 1, 0, '/template/cards', 'ri:wallet-line', NULL, 1, 'art:template:cards:view', 'MENU', 'Art Design Pro 卡片模板', 0, NOW(), NOW()),
('820000000000000102', '820000000000000000', '横幅模板', 'template_banners', 2, 0, '/template/banners', 'ri:rectangle-line', NULL, 1, 'art:template:banners:view', 'MENU', 'Art Design Pro 横幅模板', 0, NOW(), NOW()),
('820000000000000103', '820000000000000000', '图表模板', 'template_charts', 3, 0, '/template/charts', 'ri:bar-chart-box-line', NULL, 1, 'art:template:charts:view', 'MENU', 'Art Design Pro 图表模板', 0, NOW(), NOW()),
('820000000000000104', '820000000000000000', '地图模板', 'template_map', 4, 0, '/template/map', 'ri:map-pin-line', NULL, 1, 'art:template:map:view', 'MENU', 'Art Design Pro 地图模板', 0, NOW(), NOW()),
('820000000000000105', '820000000000000000', '聊天模板', 'template_chat', 5, 0, '/template/chat', 'ri:message-3-line', NULL, 1, 'art:template:chat:view', 'MENU', 'Art Design Pro 聊天模板', 0, NOW(), NOW()),
('820000000000000106', '820000000000000000', '日历模板', 'template_calendar', 6, 0, '/template/calendar', 'ri:calendar-2-line', NULL, 1, 'art:template:calendar:view', 'MENU', 'Art Design Pro 日历模板', 0, NOW(), NOW()),
('820000000000000107', '820000000000000000', '定价模板', 'template_pricing', 7, 0, '/template/pricing', 'ri:money-cny-box-line', NULL, 1, 'art:template:pricing:view', 'MENU', 'Art Design Pro 定价模板', 0, NOW(), NOW()),

('830000000000000000', '0', '组件中心', 'widgets', 3, 0, '/widgets', 'ri:apps-2-add-line', NULL, 1, '', 'TABLE', 'Art Design Pro 组件中心', 0, NOW(), NOW()),
('830000000000000101', '830000000000000000', '图标组件', 'widgets_icon', 1, 0, '/widgets/icon', 'ri:palette-line', NULL, 1, 'art:widgets:icon:view', 'MENU', 'Art Design Pro 图标组件', 0, NOW(), NOW()),
('830000000000000102', '830000000000000000', '图片裁剪', 'widgets_image_crop', 2, 0, '/widgets/image-crop', 'ri:screenshot-line', NULL, 1, 'art:widgets:image-crop:view', 'MENU', 'Art Design Pro 图片裁剪', 0, NOW(), NOW()),
('830000000000000103', '830000000000000000', 'Excel', 'widgets_excel', 3, 0, '/widgets/excel', 'ri:download-2-line', NULL, 1, 'art:widgets:excel:view', 'MENU', 'Art Design Pro Excel', 0, NOW(), NOW()),
('830000000000000104', '830000000000000000', '视频组件', 'widgets_video', 4, 0, '/widgets/video', 'ri:vidicon-line', NULL, 1, 'art:widgets:video:view', 'MENU', 'Art Design Pro 视频组件', 0, NOW(), NOW()),
('830000000000000105', '830000000000000000', '数字动画', 'widgets_count_to', 5, 0, '/widgets/count-to', 'ri:anthropic-line', NULL, 1, 'art:widgets:count-to:view', 'MENU', 'Art Design Pro 数字动画', 0, NOW(), NOW()),
('830000000000000106', '830000000000000000', '富文本', 'widgets_wang_editor', 6, 0, '/widgets/wang-editor', 'ri:t-box-line', NULL, 1, 'art:widgets:wang-editor:view', 'MENU', 'Art Design Pro 富文本', 0, NOW(), NOW()),
('830000000000000107', '830000000000000000', '水印', 'widgets_watermark', 7, 0, '/widgets/watermark', 'ri:water-flash-line', NULL, 1, 'art:widgets:watermark:view', 'MENU', 'Art Design Pro 水印', 0, NOW(), NOW()),
('830000000000000108', '830000000000000000', '右键菜单', 'widgets_context_menu', 8, 0, '/widgets/context-menu', 'ri:menu-2-line', NULL, 1, 'art:widgets:context-menu:view', 'MENU', 'Art Design Pro 右键菜单', 0, NOW(), NOW()),
('830000000000000109', '830000000000000000', '二维码', 'widgets_qrcode', 9, 0, '/widgets/qrcode', 'ri:qr-code-line', NULL, 1, 'art:widgets:qrcode:view', 'MENU', 'Art Design Pro 二维码', 0, NOW(), NOW()),
('830000000000000110', '830000000000000000', '拖拽', 'widgets_drag', 10, 0, '/widgets/drag', 'ri:drag-move-fill', NULL, 1, 'art:widgets:drag:view', 'MENU', 'Art Design Pro 拖拽', 0, NOW(), NOW()),
('830000000000000111', '830000000000000000', '文字滚动', 'widgets_text_scroll', 11, 0, '/widgets/text-scroll', 'ri:input-method-line', NULL, 1, 'art:widgets:text-scroll:view', 'MENU', 'Art Design Pro 文字滚动', 0, NOW(), NOW()),
('830000000000000112', '830000000000000000', '烟花效果', 'widgets_fireworks', 12, 0, '/widgets/fireworks', 'ri:magic-line', NULL, 1, 'art:widgets:fireworks:view', 'MENU', 'Art Design Pro 烟花效果', 0, NOW(), NOW()),

('840000000000000000', '0', '功能示例', 'examples', 4, 0, '/examples', 'ri:sparkling-line', NULL, 1, '', 'TABLE', 'Art Design Pro 功能示例', 0, NOW(), NOW()),
('840000000000000100', '840000000000000000', '权限示例', 'examples_permission', 1, 0, '/examples/permission', 'ri:fingerprint-line', NULL, 1, '', 'TABLE', 'Art Design Pro 权限示例', 0, NOW(), NOW()),
('840000000000000101', '840000000000000100', '切换角色', 'examples_permission_switch_role', 1, 0, '/examples/permission/switch-role', 'ri:contacts-line', NULL, 1, 'art:examples:permission:switch-role:view', 'MENU', 'Art Design Pro 切换角色', 0, NOW(), NOW()),
('840000000000000102', '840000000000000100', '按钮权限', 'examples_permission_button_auth', 2, 0, '/examples/permission/button-auth', 'ri:mouse-line', NULL, 1, 'art:examples:permission:button-auth:view', 'MENU', 'Art Design Pro 按钮权限', 0, NOW(), NOW()),
('840000000000000103', '840000000000000100', '页面权限', 'examples_permission_page_visibility', 3, 0, '/examples/permission/page-visibility', 'ri:user-3-line', NULL, 1, 'art:examples:permission:page-visibility:view', 'MENU', 'Art Design Pro 页面权限', 0, NOW(), NOW()),
('840000000000000201', '840000000000000000', '标签页', 'examples_tabs', 2, 0, '/examples/tabs', 'ri:price-tag-line', NULL, 1, 'art:examples:tabs:view', 'MENU', 'Art Design Pro 标签页', 0, NOW(), NOW()),
('840000000000000202', '840000000000000000', '基础表格', 'examples_tables_basic', 3, 0, '/examples/tables/basic', 'ri:layout-grid-line', NULL, 1, 'art:examples:tables:basic:view', 'MENU', 'Art Design Pro 基础表格', 0, NOW(), NOW()),
('840000000000000203', '840000000000000000', '复杂表格', 'examples_tables', 4, 0, '/examples/tables', 'ri:table-3', NULL, 1, 'art:examples:tables:view', 'MENU', 'Art Design Pro 复杂表格', 0, NOW(), NOW()),
('840000000000000204', '840000000000000000', '表单示例', 'examples_forms', 5, 0, '/examples/forms', 'ri:table-view', NULL, 1, 'art:examples:forms:view', 'MENU', 'Art Design Pro 表单示例', 0, NOW(), NOW()),
('840000000000000205', '840000000000000000', '树形表格', 'examples_tables_tree', 6, 0, '/examples/tables/tree', 'ri:layout-2-line', NULL, 1, 'art:examples:tables:tree:view', 'MENU', 'Art Design Pro 树形表格', 0, NOW(), NOW()),
('840000000000000206', '840000000000000000', 'Socket 聊天', 'examples_socket_chat', 7, 0, '/examples/socket-chat', 'ri:shake-hands-line', NULL, 1, 'art:examples:socket-chat:view', 'MENU', 'Art Design Pro Socket 聊天', 0, NOW(), NOW()),

('850000000000000000', '0', '文章管理', 'article', 6, 0, '/article', 'ri:book-2-line', NULL, 1, '', 'TABLE', 'Art Design Pro 文章管理', 0, NOW(), NOW()),
('850000000000000101', '850000000000000000', '文章列表', 'article_list', 1, 0, '/article/list', 'ri:article-line', NULL, 1, 'art:article:list:view', 'MENU', 'Art Design Pro 文章列表', 0, NOW(), NOW()),
('850000000000000102', '850000000000000000', '文章评论', 'article_comment', 2, 0, '/article/comment', 'ri:mail-line', NULL, 1, 'art:article:comment:view', 'MENU', 'Art Design Pro 文章评论', 0, NOW(), NOW()),
('850000000000000103', '850000000000000000', '文章发布', 'article_publish', 3, 0, '/article/publish', 'ri:telegram-2-line', NULL, 1, 'art:article:publish:view', 'MENU', 'Art Design Pro 文章发布', 0, NOW(), NOW()),

('860000000000000000', '0', '结果页面', 'result', 7, 0, '/result', 'ri:checkbox-circle-line', NULL, 1, '', 'TABLE', 'Art Design Pro 结果页面', 0, NOW(), NOW()),
('860000000000000101', '860000000000000000', '成功结果', 'result_success', 1, 0, '/result/success', 'ri:checkbox-circle-line', NULL, 1, 'art:result:success:view', 'MENU', 'Art Design Pro 成功结果', 0, NOW(), NOW()),
('860000000000000102', '860000000000000000', '失败结果', 'result_fail', 2, 0, '/result/fail', 'ri:close-circle-line', NULL, 1, 'art:result:fail:view', 'MENU', 'Art Design Pro 失败结果', 0, NOW(), NOW()),

('870000000000000000', '0', '异常页面', 'exception', 8, 0, '/exception', 'ri:error-warning-line', NULL, 1, '', 'TABLE', 'Art Design Pro 异常页面', 0, NOW(), NOW()),
('870000000000000403', '870000000000000000', '403', 'exception_403', 1, 0, '/exception/403', 'ri:forbid-line', NULL, 1, 'art:exception:403:view', 'MENU', 'Art Design Pro 403 页面', 0, NOW(), NOW()),
('870000000000000404', '870000000000000000', '404', 'exception_404', 2, 0, '/exception/404', 'ri:file-damage-line', NULL, 1, 'art:exception:404:view', 'MENU', 'Art Design Pro 404 页面', 0, NOW(), NOW()),
('870000000000000500', '870000000000000000', '500', 'exception_500', 3, 0, '/exception/500', 'ri:server-line', NULL, 1, 'art:exception:500:view', 'MENU', 'Art Design Pro 500 页面', 0, NOW(), NOW()),

('880000000000000000', '0', '运维管理', 'safeguard', 9, 0, '/safeguard', 'ri:shield-check-line', NULL, 1, '', 'TABLE', 'Art Design Pro 运维管理', 0, NOW(), NOW()),
('880000000000000101', '880000000000000000', '服务监控', 'safeguard_server', 1, 0, '/safeguard/server', 'ri:hard-drive-3-line', NULL, 1, 'art:safeguard:server:view', 'MENU', 'Art Design Pro 服务监控', 0, NOW(), NOW());

UPDATE `system_menu`
SET `name` = '系统管理',
    `route_key` = 'system',
    `order_sort` = 5,
    `is_iframe` = 0,
    `path` = '/system',
    `icon` = 'ri:user-3-line',
    `visible` = 1,
    `permission` = '',
    `type` = 'TABLE',
    `deleted` = 0,
    `update_time` = NOW()
WHERE `id` = '173736381381221581';

UPDATE `system_menu` SET `name` = '用户管理', `order_sort` = 1, `icon` = 'ri:user-line', `permission` = 'system:user:view', `update_time` = NOW() WHERE `id` = '1737369714325528577';
UPDATE `system_menu` SET `name` = '角色管理', `order_sort` = 2, `icon` = 'ri:user-settings-line', `permission` = 'system:role:view', `update_time` = NOW() WHERE `id` = '1737363813812215810';
UPDATE `system_menu` SET `name` = '菜单管理', `order_sort` = 3, `icon` = 'ri:menu-line', `permission` = 'system:menu:view', `update_time` = NOW() WHERE `id` = '173736381381221583';
UPDATE `system_menu` SET `name` = '系统字典', `order_sort` = 4, `icon` = 'ri:book-2-line', `permission` = 'system:dict:view', `update_time` = NOW() WHERE `id` = '930000000000000000';
UPDATE `system_menu` SET `name` = '通知公告', `order_sort` = 5, `icon` = 'ri:notification-3-line', `permission` = 'system:notice:view', `update_time` = NOW() WHERE `id` = '940000000000000000';

UPDATE `system_menu` SET `permission` = 'system:user:create', `update_time` = NOW() WHERE `id` = '900000000000000001';
UPDATE `system_menu` SET `permission` = 'system:user:update', `update_time` = NOW() WHERE `id` = '900000000000000002';
UPDATE `system_menu` SET `permission` = 'system:user:delete', `update_time` = NOW() WHERE `id` = '900000000000000003';
UPDATE `system_menu` SET `permission` = 'system:role:create', `update_time` = NOW() WHERE `id` = '900000000000000011';
UPDATE `system_menu` SET `permission` = 'system:role:update', `update_time` = NOW() WHERE `id` = '900000000000000012';
UPDATE `system_menu` SET `permission` = 'system:role:delete', `update_time` = NOW() WHERE `id` = '900000000000000013';
UPDATE `system_menu` SET `permission` = 'system:role:permission', `update_time` = NOW() WHERE `id` = '900000000000000014';
UPDATE `system_menu` SET `permission` = 'system:menu:create', `update_time` = NOW() WHERE `id` = '900000000000000021';
UPDATE `system_menu` SET `permission` = 'system:menu:update', `update_time` = NOW() WHERE `id` = '900000000000000022';
UPDATE `system_menu` SET `permission` = 'system:menu:delete', `update_time` = NOW() WHERE `id` = '900000000000000023';
UPDATE `system_menu` SET `permission` = 'system:dict:create', `update_time` = NOW() WHERE `id` = '930000000000000001';
UPDATE `system_menu` SET `permission` = 'system:dict:update', `update_time` = NOW() WHERE `id` = '930000000000000002';
UPDATE `system_menu` SET `permission` = 'system:dict:delete', `update_time` = NOW() WHERE `id` = '930000000000000003';
UPDATE `system_menu` SET `permission` = 'system:notice:create', `update_time` = NOW() WHERE `id` = '940000000000000001';
UPDATE `system_menu` SET `permission` = 'system:notice:update', `update_time` = NOW() WHERE `id` = '940000000000000002';
UPDATE `system_menu` SET `permission` = 'system:notice:delete', `update_time` = NOW() WHERE `id` = '940000000000000003';
UPDATE `system_menu` SET `permission` = 'system:notice:publish', `update_time` = NOW() WHERE `id` = '940000000000000004';
UPDATE `system_menu` SET `permission` = 'system:notice:revoke', `update_time` = NOW() WHERE `id` = '940000000000000005';

REPLACE INTO `system_menu`
(`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`)
VALUES
('890000000000000000', '173736381381221581', '嵌套菜单', 'system_nested', 6, 0, '/system/nested', 'ri:menu-fold-line', NULL, 1, '', 'TABLE', 'Art Design Pro 嵌套菜单示例', 0, NOW(), NOW()),
('890000000000000101', '890000000000000000', '菜单1', 'system_nested_menu1', 1, 0, '/system/nested/menu1', 'ri:menu-2-line', NULL, 1, 'art:system:nested:menu1:view', 'MENU', 'Art Design Pro 嵌套菜单1', 0, NOW(), NOW()),
('890000000000000201', '890000000000000000', '菜单2', 'system_nested_menu2', 2, 0, '/system/nested/menu2', 'ri:menu-2-line', NULL, 1, 'art:system:nested:menu2:view', 'MENU', 'Art Design Pro 嵌套菜单2', 0, NOW(), NOW()),
('890000000000000301', '890000000000000000', '菜单3', 'system_nested_menu3', 3, 0, '/system/nested/menu3', 'ri:menu-2-line', NULL, 1, 'art:system:nested:menu3:view', 'MENU', 'Art Design Pro 嵌套菜单3', 0, NOW(), NOW()),
('890000000000000321', '890000000000000301', '菜单3-2', 'system_nested_menu3_2', 2, 0, '/system/nested/menu3/menu3-2', 'ri:menu-3-line', NULL, 1, 'art:system:nested:menu3-2:view', 'MENU', 'Art Design Pro 嵌套菜单3-2', 0, NOW(), NOW());

COMMIT;
