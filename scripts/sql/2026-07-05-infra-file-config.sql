SET FOREIGN_KEY_CHECKS = 0;

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

SET @schema_name = DATABASE();
SET @column_exists = (
    SELECT COUNT(1) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'infra_file' AND COLUMN_NAME = 'config_id'
);
SET @ddl = IF(@column_exists = 0,
              'ALTER TABLE `infra_file` ADD COLUMN `config_id` varchar(32) DEFAULT NULL COMMENT ''文件配置ID'' AFTER `storage_type`',
              'SELECT 1');
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @column_exists = (
    SELECT COUNT(1) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'infra_file' AND COLUMN_NAME = 'config_name'
);
SET @ddl = IF(@column_exists = 0,
              'ALTER TABLE `infra_file` ADD COLUMN `config_name` varchar(128) DEFAULT NULL COMMENT ''文件配置名称'' AFTER `config_id`',
              'SELECT 1');
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @index_exists = (
    SELECT COUNT(1) FROM information_schema.STATISTICS
    WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'infra_file' AND INDEX_NAME = 'idx_infra_file_config'
);
SET @ddl = IF(@index_exists = 0,
              'ALTER TABLE `infra_file` ADD KEY `idx_infra_file_config` (`config_id`)',
              'SELECT 1');
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

REPLACE INTO `infra_file_config`
(`id`, `name`, `storage_type`, `master`, `base_path`, `domain`, `max_size_mb`, `endpoint`, `bucket`, `access_key`, `access_secret`, `enable_path_style_access`, `remark`, `create_time`, `update_time`, `deleted`)
VALUES
('960000000000020000', '默认本地存储', 'local', 1, 'dev/uploads', NULL, 50, NULL, NULL, NULL, NULL, 0, '系统默认文件存储配置', NOW(), NOW(), 0);

REPLACE INTO `system_menu`
(`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`)
VALUES
('960000000000000250', '960000000000000000', '文件配置', 'infra_file_config', 3, 0, '/infra/file-config', 'ri:folder-settings-line', NULL, 1, 'infra:file-config:view', 'MENU', '文件存储配置', 0, NOW(), NOW()),
('960000000000000251', '960000000000000250', '新增文件配置', 'infra_file_config_create', 1, 0, '', NULL, NULL, 0, 'infra:file-config:create', 'BUTTON', NULL, 0, NOW(), NOW()),
('960000000000000252', '960000000000000250', '编辑文件配置', 'infra_file_config_update', 2, 0, '', NULL, NULL, 0, 'infra:file-config:update', 'BUTTON', NULL, 0, NOW(), NOW()),
('960000000000000253', '960000000000000250', '删除文件配置', 'infra_file_config_delete', 3, 0, '', NULL, NULL, 0, 'infra:file-config:delete', 'BUTTON', NULL, 0, NOW(), NOW()),
('960000000000000254', '960000000000000250', '设置主配置', 'infra_file_config_master', 4, 0, '', NULL, NULL, 0, 'infra:file-config:master', 'BUTTON', NULL, 0, NOW(), NOW()),
('960000000000000255', '960000000000000250', '测试文件配置', 'infra_file_config_test', 5, 0, '', NULL, NULL, 0, 'infra:file-config:test', 'BUTTON', NULL, 0, NOW(), NOW()),
('960000000000000300', '960000000000000000', '文件管理', 'infra_file', 4, 0, '/infra/file', 'ri:folder-upload-line', NULL, 1, 'infra:file:view', 'MENU', '文件资源管理', 0, NOW(), NOW());

DELETE FROM `infra_config`
WHERE `config_key` IN ('infra.file.local-path', 'infra.file.max-size-mb');

SET FOREIGN_KEY_CHECKS = 1;
