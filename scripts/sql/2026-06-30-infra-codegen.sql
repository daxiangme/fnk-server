-- 基础服务：企业级代码生成中心增量脚本
-- 已有库执行本脚本即可补齐代码生成元数据表、菜单和按钮权限。

CREATE TABLE IF NOT EXISTS `infra_codegen_table` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `table_name` varchar(128) NOT NULL COMMENT '表名',
  `table_comment` varchar(255) DEFAULT NULL COMMENT '表描述',
  `business_name` varchar(128) NOT NULL COMMENT '业务名称',
  `module_name` varchar(90) NOT NULL COMMENT '模块名',
  `class_name` varchar(128) NOT NULL COMMENT '实体类名',
  `package_name` varchar(255) DEFAULT NULL COMMENT '包路径',
  `api_base_path` varchar(255) DEFAULT NULL COMMENT '接口路径',
  `frontend_path` varchar(255) DEFAULT NULL COMMENT '前端页面路径',
  `route_path` varchar(255) DEFAULT NULL COMMENT '路由路径',
  `permission_prefix` varchar(180) DEFAULT NULL COMMENT '权限前缀',
  `menu_parent_id` varchar(32) DEFAULT NULL COMMENT '菜单父级ID',
  `generate_type` varchar(60) NOT NULL DEFAULT 'single' COMMENT '生成类型',
  `author` varchar(90) DEFAULT NULL COMMENT '作者',
  `sync_time` datetime DEFAULT NULL COMMENT '最近同步时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_infra_codegen_table_name` (`table_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='代码生成表配置';

CREATE TABLE IF NOT EXISTS `infra_codegen_field` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `table_id` varchar(32) NOT NULL COMMENT '表配置ID',
  `column_name` varchar(128) NOT NULL COMMENT '数据库字段',
  `property_name` varchar(128) NOT NULL COMMENT '属性名',
  `column_comment` varchar(255) DEFAULT NULL COMMENT '字段说明',
  `db_type` varchar(128) DEFAULT NULL COMMENT '数据库类型',
  `java_type` varchar(90) NOT NULL COMMENT 'Java类型',
  `ts_type` varchar(90) NOT NULL COMMENT 'TypeScript类型',
  `primary_key` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否主键',
  `required` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否必填',
  `list_visible` tinyint(1) NOT NULL DEFAULT '1' COMMENT '列表显示',
  `search_visible` tinyint(1) NOT NULL DEFAULT '0' COMMENT '搜索显示',
  `form_visible` tinyint(1) NOT NULL DEFAULT '1' COMMENT '表单显示',
  `detail_visible` tinyint(1) NOT NULL DEFAULT '1' COMMENT '详情显示',
  `form_type` varchar(60) NOT NULL DEFAULT 'input' COMMENT '表单控件',
  `query_type` varchar(60) NOT NULL DEFAULT 'eq' COMMENT '查询方式',
  `dict_code` varchar(90) DEFAULT NULL COMMENT '字典编码',
  `default_value` varchar(255) DEFAULT NULL COMMENT '默认值',
  `order_sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `width` int DEFAULT NULL COMMENT '列宽',
  `readonly_on_create` tinyint(1) NOT NULL DEFAULT '0' COMMENT '新增只读',
  `readonly_on_edit` tinyint(1) NOT NULL DEFAULT '0' COMMENT '编辑只读',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_infra_codegen_field` (`table_id`,`column_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='代码生成字段配置';

CREATE TABLE IF NOT EXISTS `infra_codegen_relation` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `table_id` varchar(32) NOT NULL COMMENT '表配置ID',
  `relation_name` varchar(128) NOT NULL COMMENT '关系名称',
  `relation_type` varchar(60) NOT NULL COMMENT '关系类型',
  `source_table` varchar(128) NOT NULL COMMENT '当前表',
  `source_column` varchar(128) DEFAULT NULL COMMENT '当前表字段',
  `target_table` varchar(128) DEFAULT NULL COMMENT '关联表',
  `target_column` varchar(128) DEFAULT NULL COMMENT '关联字段',
  `join_table` varchar(128) DEFAULT NULL COMMENT '中间表',
  `join_source_column` varchar(128) DEFAULT NULL COMMENT '中间表当前侧字段',
  `join_target_column` varchar(128) DEFAULT NULL COMMENT '中间表目标侧字段',
  `display_column` varchar(128) DEFAULT NULL COMMENT '展示字段',
  `generate_query` tinyint(1) NOT NULL DEFAULT '1' COMMENT '生成关联查询',
  `generate_form` tinyint(1) NOT NULL DEFAULT '1' COMMENT '生成表单控件',
  `generate_detail` tinyint(1) NOT NULL DEFAULT '1' COMMENT '生成详情',
  `delete_strategy` varchar(60) NOT NULL DEFAULT 'manual' COMMENT '删除策略',
  `confidence` int NOT NULL DEFAULT '0' COMMENT '置信度',
  `source_type` varchar(60) NOT NULL DEFAULT 'manual' COMMENT '来源类型',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_infra_codegen_relation_table` (`table_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='代码生成表关系';

REPLACE INTO `system_menu`
(`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`)
VALUES
('960000000000000000', '0', '基础服务', 'infra', 10, 0, '/infra', 'ri:settings-3-line', NULL, 1, '', 'TABLE', '基础服务能力', 0, NOW(), NOW()),
('960000000000000100', '960000000000000000', '代码生成', 'infra_codegen', 1, 0, '/infra/codegen', 'ri:code-box-line', NULL, 1, 'infra:codegen:view', 'MENU', '企业级代码生成中心', 0, NOW(), NOW()),
('960000000000000107', '960000000000000100', '代码生成详情', 'infra_codegen_detail', 0, 0, '/infra/codegen/detail/:id', NULL, NULL, 0, 'infra:codegen:view', 'MENU', '代码生成隐藏详情页', 0, NOW(), NOW()),
('960000000000000101', '960000000000000100', '导入数据表', 'infra_codegen_import', 1, 0, '', NULL, NULL, 0, 'infra:codegen:import', 'BUTTON', NULL, 0, NOW(), NOW()),
('960000000000000102', '960000000000000100', '更新配置', 'infra_codegen_update', 2, 0, '', NULL, NULL, 0, 'infra:codegen:update', 'BUTTON', NULL, 0, NOW(), NOW()),
('960000000000000103', '960000000000000100', '预览代码', 'infra_codegen_preview', 3, 0, '', NULL, NULL, 0, 'infra:codegen:preview', 'BUTTON', NULL, 0, NOW(), NOW()),
('960000000000000104', '960000000000000100', '下载代码', 'infra_codegen_download', 4, 0, '', NULL, NULL, 0, 'infra:codegen:download', 'BUTTON', NULL, 0, NOW(), NOW()),
('960000000000000106', '960000000000000100', '删除配置', 'infra_codegen_delete', 6, 0, '', NULL, NULL, 0, 'infra:codegen:delete', 'BUTTON', NULL, 0, NOW(), NOW());
