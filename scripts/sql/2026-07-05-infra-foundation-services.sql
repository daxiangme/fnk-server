SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE IF NOT EXISTS `system_user_notice` (
                                      `id` varchar(32) NOT NULL COMMENT 'id',
                                      `user_id` varchar(32) NOT NULL COMMENT '用户ID',
                                      `notice_id` varchar(32) NOT NULL COMMENT '通知公告ID',
                                      `title` varchar(255) NOT NULL COMMENT '标题',
                                      `notice_type` varchar(90) NOT NULL COMMENT '通知类型',
                                      `content` text COMMENT '内容',
                                      `read_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '已读状态',
                                      `read_time` datetime DEFAULT NULL COMMENT '阅读时间',
                                      `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                      `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                      `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                                      PRIMARY KEY (`id`) USING BTREE,
                                      KEY `idx_system_user_notice_user` (`user_id`,`read_status`),
                                      KEY `idx_system_user_notice_notice` (`notice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户站内通知';

CREATE TABLE IF NOT EXISTS `infra_config` (
                                `id` varchar(32) NOT NULL COMMENT 'id',
                                `config_name` varchar(128) NOT NULL COMMENT '参数名称',
                                `config_key` varchar(180) NOT NULL COMMENT '参数键',
                                `config_value` text COMMENT '参数值',
                                `group_code` varchar(90) NOT NULL COMMENT '参数分组',
                                `value_type` varchar(60) NOT NULL DEFAULT 'string' COMMENT '值类型',
                                `visible` tinyint(1) NOT NULL DEFAULT '1' COMMENT '前端可见',
                                `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态',
                                `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                                `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                                PRIMARY KEY (`id`) USING BTREE,
                                UNIQUE KEY `uk_infra_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统参数配置';

CREATE TABLE IF NOT EXISTS `infra_file_config` (
                                     `id` varchar(32) NOT NULL COMMENT 'id',
                                     `name` varchar(128) NOT NULL COMMENT '配置名称',
                                     `storage_type` varchar(60) NOT NULL DEFAULT 'local' COMMENT '存储类型',
                                     `master` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否主配置',
                                     `base_path` varchar(900) DEFAULT NULL COMMENT '本地基础路径',
                                     `domain` varchar(500) DEFAULT NULL COMMENT '访问域名',
                                     `max_size_mb` bigint NOT NULL DEFAULT '50' COMMENT '最大上传大小MB',
                                     `endpoint` varchar(500) DEFAULT NULL COMMENT 'S3 Endpoint',
                                     `bucket` varchar(180) DEFAULT NULL COMMENT 'S3 Bucket',
                                     `access_key` varchar(255) DEFAULT NULL COMMENT 'S3 Access Key',
                                     `access_secret` varchar(500) DEFAULT NULL COMMENT 'S3 Access Secret',
                                     `enable_path_style_access` tinyint(1) NOT NULL DEFAULT '0' COMMENT '启用Path Style',
                                     `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                                     `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                     `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                     `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     UNIQUE KEY `uk_infra_file_config_name` (`name`),
                                     KEY `idx_infra_file_config_master` (`master`),
                                     KEY `idx_infra_file_config_storage` (`storage_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='文件存储配置';

CREATE TABLE IF NOT EXISTS `infra_file` (
                              `id` varchar(32) NOT NULL COMMENT 'id',
                              `original_name` varchar(255) NOT NULL COMMENT '原始文件名',
                              `file_name` varchar(255) NOT NULL COMMENT '存储文件名',
                              `content_type` varchar(180) NOT NULL COMMENT '内容类型',
                              `file_size` bigint NOT NULL DEFAULT '0' COMMENT '文件大小',
                              `storage_type` varchar(60) NOT NULL DEFAULT 'local' COMMENT '存储类型',
                              `config_id` varchar(32) DEFAULT NULL COMMENT '文件配置ID',
                              `config_name` varchar(128) DEFAULT NULL COMMENT '文件配置名称',
                              `storage_path` varchar(900) NOT NULL COMMENT '存储路径',
                              `url` varchar(900) DEFAULT NULL COMMENT '访问地址',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                              `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                              PRIMARY KEY (`id`) USING BTREE,
                              KEY `idx_infra_file_original_name` (`original_name`),
                              KEY `idx_infra_file_storage_type` (`storage_type`),
                              KEY `idx_infra_file_config` (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='文件资源';

REPLACE INTO `infra_file_config`
(`id`, `name`, `storage_type`, `master`, `base_path`, `domain`, `max_size_mb`, `endpoint`, `bucket`, `access_key`, `access_secret`, `enable_path_style_access`, `remark`, `create_time`, `update_time`, `deleted`)
VALUES
('960000000000020000', '默认本地存储', 'local', 1, 'dev/uploads', NULL, 50, NULL, NULL, NULL, NULL, 0, '系统默认文件存储配置', NOW(), NOW(), 0);

REPLACE INTO `system_menu`
(`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`)
VALUES
('960000000000000200', '960000000000000000', '参数配置', 'infra_config', 2, 0, '/infra/config', 'ri:settings-4-line', NULL, 1, 'infra:config:view', 'MENU', '系统参数配置', 0, NOW(), NOW()),
('960000000000000201', '960000000000000200', '新增参数', 'infra_config_create', 1, 0, '', NULL, NULL, 0, 'infra:config:create', 'BUTTON', NULL, 0, NOW(), NOW()),
('960000000000000202', '960000000000000200', '编辑参数', 'infra_config_update', 2, 0, '', NULL, NULL, 0, 'infra:config:update', 'BUTTON', NULL, 0, NOW(), NOW()),
('960000000000000203', '960000000000000200', '删除参数', 'infra_config_delete', 3, 0, '', NULL, NULL, 0, 'infra:config:delete', 'BUTTON', NULL, 0, NOW(), NOW()),
('960000000000000250', '960000000000000000', '文件配置', 'infra_file_config', 3, 0, '/infra/file-config', 'ri:folder-settings-line', NULL, 1, 'infra:file-config:view', 'MENU', '文件存储配置', 0, NOW(), NOW()),
('960000000000000251', '960000000000000250', '新增文件配置', 'infra_file_config_create', 1, 0, '', NULL, NULL, 0, 'infra:file-config:create', 'BUTTON', NULL, 0, NOW(), NOW()),
('960000000000000252', '960000000000000250', '编辑文件配置', 'infra_file_config_update', 2, 0, '', NULL, NULL, 0, 'infra:file-config:update', 'BUTTON', NULL, 0, NOW(), NOW()),
('960000000000000253', '960000000000000250', '删除文件配置', 'infra_file_config_delete', 3, 0, '', NULL, NULL, 0, 'infra:file-config:delete', 'BUTTON', NULL, 0, NOW(), NOW()),
('960000000000000254', '960000000000000250', '设置主配置', 'infra_file_config_master', 4, 0, '', NULL, NULL, 0, 'infra:file-config:master', 'BUTTON', NULL, 0, NOW(), NOW()),
('960000000000000255', '960000000000000250', '测试文件配置', 'infra_file_config_test', 5, 0, '', NULL, NULL, 0, 'infra:file-config:test', 'BUTTON', NULL, 0, NOW(), NOW()),
('960000000000000300', '960000000000000000', '文件管理', 'infra_file', 4, 0, '/infra/file', 'ri:folder-upload-line', NULL, 1, 'infra:file:view', 'MENU', '文件资源管理', 0, NOW(), NOW()),
('960000000000000301', '960000000000000300', '上传文件', 'infra_file_upload', 1, 0, '', NULL, NULL, 0, 'infra:file:upload', 'BUTTON', NULL, 0, NOW(), NOW()),
('960000000000000302', '960000000000000300', '删除文件', 'infra_file_delete', 2, 0, '', NULL, NULL, 0, 'infra:file:delete', 'BUTTON', NULL, 0, NOW(), NOW()),
('940000000000000100', '173736381381221581', '消息中心', 'system_notice_center', 6, 0, '/system/notice-center', 'ri:message-3-line', NULL, 1, 'system:notice:mine', 'MENU', '当前用户站内通知', 0, NOW(), NOW());

SET FOREIGN_KEY_CHECKS = 1;
