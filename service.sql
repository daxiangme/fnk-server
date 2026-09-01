/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50744 (5.7.44)
 Source Host           : localhost:3306
 Source Schema         : fnk-service

 Target Server Type    : MySQL
 Target Server Version : 50744 (5.7.44)
 File Encoding         : 65001

 Date: 25/12/2023 18:14:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_user
-- ----------------------------
DROP TABLE IF EXISTS `admin_user`;
CREATE TABLE `admin_user` (
                              `id` varchar(32) NOT NULL COMMENT 'id',
                              `phone` varchar(32) NOT NULL COMMENT '手机号',
                              `password` varchar(255) NOT NULL COMMENT '密码',
                              `salt` varchar(32) NOT NULL COMMENT '盐',
                              `username` varchar(255) DEFAULT NULL COMMENT '用户名称',
                              `avatar` varchar(255) DEFAULT NULL COMMENT '用户头像',
                              `sex` varchar(2) DEFAULT NULL COMMENT '用户性别',
                              `login_ip` varchar(255) DEFAULT NULL COMMENT '登录ip',
                              `dept_id` varchar(32) DEFAULT NULL COMMENT '部门ID',
                              `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '用户状态;0正常 1不可用',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                              `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统用户';

-- ----------------------------
-- Records of admin_user
-- ----------------------------
BEGIN;
INSERT INTO `admin_user` (`id`, `phone`, `password`, `salt`, `username`, `avatar`, `sex`, `login_ip`, `dept_id`, `status`, `create_time`, `update_time`, `deleted`) VALUES ('1736671483388059649', '18888888888', '4066604c3f5a33d64aada1466abc55cf', 'Q48G', 'admin', NULL, '0', '', NULL, 1, '2023-12-18 16:55:22', '2023-12-18 16:55:22', 0);
INSERT INTO `admin_user` (`id`, `phone`, `password`, `salt`, `username`, `avatar`, `sex`, `login_ip`, `dept_id`, `status`, `create_time`, `update_time`, `deleted`) VALUES ('1737764212557844482', '18100000000', '1498b77199645ec7cb68c9a40e013eeb', 'A1KM', '游客', NULL, '0', NULL, NULL, 1, '2023-12-21 17:17:29', '2023-12-21 17:20:05', 1);
INSERT INTO `admin_user` (`id`, `phone`, `password`, `salt`, `username`, `avatar`, `sex`, `login_ip`, `dept_id`, `status`, `create_time`, `update_time`, `deleted`) VALUES ('1739226400963280898', '18800000000', 'e500074630aba91e8b346c09c14387d4', '5LW5', 'guest', NULL, '0', NULL, NULL, 1, '2023-12-25 18:07:42', '2023-12-25 18:07:42', 0);
COMMIT;

-- ----------------------------
-- Table structure for dept_info
-- ----------------------------
DROP TABLE IF EXISTS `dept_info`;
CREATE TABLE `dept_info` (
                             `id` varchar(32) NOT NULL COMMENT 'id',
                             `root_id` varchar(255) DEFAULT NULL COMMENT '上级ID',
                             `all_root_id` json DEFAULT NULL COMMENT '所有的上级ID集合列表',
                             `name` varchar(255) DEFAULT NULL COMMENT '部门名称',
                             `order_sort` int(2) DEFAULT NULL COMMENT '排序',
                             `leader` varchar(255) DEFAULT NULL COMMENT '管理用户名称',
                             `phone` varchar(255) DEFAULT NULL COMMENT '手机号码',
                             `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
                             `status` tinyint(1) DEFAULT '0' COMMENT '状态',
                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                             `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='部门信息';

-- ----------------------------
-- Records of dept_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for post_info
-- ----------------------------
DROP TABLE IF EXISTS `post_info`;
CREATE TABLE `post_info` (
                             `id` varchar(32) NOT NULL COMMENT 'id',
                             `name` varchar(255) DEFAULT NULL COMMENT '名称',
                             `key_word` varchar(90) DEFAULT NULL COMMENT '岗位key',
                             `order_sort` int(2) DEFAULT NULL COMMENT '排序',
                             `status` tinyint(1) DEFAULT '0' COMMENT '状态',
                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                             `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='岗位信息';

-- ----------------------------
-- Records of post_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for role_dept
-- ----------------------------
DROP TABLE IF EXISTS `role_dept`;
CREATE TABLE `role_dept` (
                             `id` varchar(32) NOT NULL COMMENT 'id',
                             `role_id` varchar(255) NOT NULL COMMENT '角色ID',
                             `dept_id` varchar(255) NOT NULL COMMENT '部门ID',
                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                             `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE KEY `role_dept_index` (`role_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色关联部门';

-- ----------------------------
-- Records of role_dept
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for role_info
-- ----------------------------
DROP TABLE IF EXISTS `role_info`;
CREATE TABLE `role_info` (
                             `id` varchar(32) NOT NULL COMMENT 'id',
                             `role_name` varchar(255) DEFAULT NULL COMMENT '名称',
                             `role_key` varchar(90) DEFAULT NULL COMMENT '角色key',
                             `order_sort` int(4) DEFAULT NULL COMMENT '显示排序',
                             `role_scope` json DEFAULT NULL COMMENT '角色数据范围',
                             `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态;0',
                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                             `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE KEY `role_key` (`role_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色信息';

-- ----------------------------
-- Records of role_info
-- ----------------------------
BEGIN;
INSERT INTO `role_info` (`id`, `role_name`, `role_key`, `order_sort`, `role_scope`, `status`, `create_time`, `update_time`, `deleted`) VALUES ('1737408771218792450', '超级管理员', 'SuperAdmin', 1, '[\"*\"]', 1, '2023-12-20 17:45:05', '2023-12-20 17:45:05', 0);
INSERT INTO `role_info` (`id`, `role_name`, `role_key`, `order_sort`, `role_scope`, `status`, `create_time`, `update_time`, `deleted`) VALUES ('1737760149430677506', 'test', 'test', 3, '[\"173736381381221581\", \"173736381381221583\", \"1737363813812215810\", \"1737369714325528577\"]', 1, '2023-12-21 17:01:20', '2023-12-21 17:01:20', 1);
INSERT INTO `role_info` (`id`, `role_name`, `role_key`, `order_sort`, `role_scope`, `status`, `create_time`, `update_time`, `deleted`) VALUES ('1737763732809142273', '游客', 'guest', 2, '[\"1737364028824821762\", \"1739224770612150273\", \"1739225110254305282\", \"1739225241577963521\", \"1739225337644302337\", \"1739225546147348481\", \"1738099497552429057\", \"1739115016615215105\", \"1739223870027972609\", \"1739224144796827649\", \"1739224376339185665\", \"1739224547890413570\"]', 1, '2023-12-21 17:15:35', '2023-12-21 17:15:35', 0);
COMMIT;

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu` (
                             `id` varchar(32) NOT NULL COMMENT 'id',
                             `role_id` varchar(255) NOT NULL COMMENT '角色id',
                             `menu_id` varchar(255) NOT NULL COMMENT '菜单ID',
                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                             `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE KEY `role_menu_index` (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色和菜单关联';

-- ----------------------------
-- Records of role_menu
-- ----------------------------
BEGIN;
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('1739226237507059713', '1737763732809142273', '1737364028824821762', '2023-12-25 18:07:03', '2023-12-25 18:07:03', 0);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('1739226237511254018', '1737763732809142273', '1739224770612150273', '2023-12-25 18:07:03', '2023-12-25 18:07:03', 0);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('1739226237511254019', '1737763732809142273', '1739225110254305282', '2023-12-25 18:07:03', '2023-12-25 18:07:03', 0);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('1739226237511254020', '1737763732809142273', '1739225241577963521', '2023-12-25 18:07:03', '2023-12-25 18:07:03', 0);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('1739226237515448321', '1737763732809142273', '1739225337644302337', '2023-12-25 18:07:03', '2023-12-25 18:07:03', 0);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('1739226237515448322', '1737763732809142273', '1739225546147348481', '2023-12-25 18:07:03', '2023-12-25 18:07:03', 0);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('1739226237523836930', '1737763732809142273', '1738099497552429057', '2023-12-25 18:07:03', '2023-12-25 18:07:03', 0);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('1739226237523836931', '1737763732809142273', '1739115016615215105', '2023-12-25 18:07:03', '2023-12-25 18:07:03', 0);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('1739226237523836932', '1737763732809142273', '1739223870027972609', '2023-12-25 18:07:03', '2023-12-25 18:07:03', 0);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('1739226237532225538', '1737763732809142273', '1739224144796827649', '2023-12-25 18:07:03', '2023-12-25 18:07:03', 0);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('1739226237532225539', '1737763732809142273', '1739224376339185665', '2023-12-25 18:07:03', '2023-12-25 18:07:03', 0);
INSERT INTO `role_menu` (`id`, `role_id`, `menu_id`, `create_time`, `update_time`, `deleted`) VALUES ('1739226237532225540', '1737763732809142273', '1739224547890413570', '2023-12-25 18:07:03', '2023-12-25 18:07:03', 0);
COMMIT;

-- ----------------------------
-- Table structure for system_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `system_dict_type`;
CREATE TABLE `system_dict_type` (
                                    `id` varchar(32) NOT NULL COMMENT 'id',
                                    `dict_code` varchar(90) NOT NULL COMMENT '字典编码',
                                    `dict_name` varchar(255) NOT NULL COMMENT '字典名称',
                                    `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态',
                                    `remark` varchar(900) DEFAULT NULL COMMENT '备注',
                                    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                    `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    UNIQUE KEY `dict_code` (`dict_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统字典类型';

-- ----------------------------
-- Records of system_dict_type
-- ----------------------------
BEGIN;
INSERT INTO `system_dict_type` (`id`, `dict_code`, `dict_name`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES ('950000000000000000', 'system_notice_type', '通知类型', 1, '通知公告类型', '2026-06-19 00:00:00', '2026-06-19 00:00:00', 0);
INSERT INTO `system_dict_type` (`id`, `dict_code`, `dict_name`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES ('950000000000000200', 'system_sms_channel_code', '短信渠道编码', 1, '消息中心短信渠道编码', '2026-07-05 00:00:00', '2026-07-05 00:00:00', 0);
INSERT INTO `system_dict_type` (`id`, `dict_code`, `dict_name`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES ('950000000000000210', 'system_sms_template_type', '短信模板类型', 1, '消息中心短信模板类型', '2026-07-05 00:00:00', '2026-07-05 00:00:00', 0);
INSERT INTO `system_dict_type` (`id`, `dict_code`, `dict_name`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES ('950000000000000220', 'system_message_send_status', '消息发送状态', 1, '短信、邮件发送状态', '2026-07-05 00:00:00', '2026-07-05 00:00:00', 0);
INSERT INTO `system_dict_type` (`id`, `dict_code`, `dict_name`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES ('950000000000000230', 'system_notify_template_type', '站内信模板类型', 1, '消息中心站内信模板类型', '2026-07-05 00:00:00', '2026-07-05 00:00:00', 0);
COMMIT;

-- ----------------------------
-- Table structure for system_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `system_dict_item`;
CREATE TABLE `system_dict_item` (
                                    `id` varchar(32) NOT NULL COMMENT 'id',
                                    `dict_code` varchar(90) NOT NULL COMMENT '字典编码',
                                    `label` varchar(255) NOT NULL COMMENT '字典标签',
                                    `value` varchar(255) NOT NULL COMMENT '字典值',
                                    `order_sort` int(4) NOT NULL DEFAULT '0' COMMENT '排序',
                                    `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态',
                                    `tag_type` varchar(90) DEFAULT NULL COMMENT '标签样式',
                                    `remark` varchar(900) DEFAULT NULL COMMENT '备注',
                                    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                    `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    UNIQUE KEY `dict_item_value` (`dict_code`,`value`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统字典项';

-- ----------------------------
-- Records of system_dict_item
-- ----------------------------
BEGIN;
INSERT INTO `system_dict_item` (`id`, `dict_code`, `label`, `value`, `order_sort`, `status`, `tag_type`, `remark`, `create_time`, `update_time`, `deleted`) VALUES ('950000000000000101', 'system_notice_type', '通知', 'NOTICE', 1, 1, 'primary', NULL, '2026-06-19 00:00:00', '2026-06-19 00:00:00', 0);
INSERT INTO `system_dict_item` (`id`, `dict_code`, `label`, `value`, `order_sort`, `status`, `tag_type`, `remark`, `create_time`, `update_time`, `deleted`) VALUES ('950000000000000102', 'system_notice_type', '公告', 'ANNOUNCEMENT', 2, 1, 'success', NULL, '2026-06-19 00:00:00', '2026-06-19 00:00:00', 0);
INSERT INTO `system_dict_item` (`id`, `dict_code`, `label`, `value`, `order_sort`, `status`, `tag_type`, `remark`, `create_time`, `update_time`, `deleted`) VALUES ('950000000000000201', 'system_sms_channel_code', 'DEBUG', 'DEBUG', 1, 1, 'info', '本地调试渠道', '2026-07-05 00:00:00', '2026-07-05 00:00:00', 0);
INSERT INTO `system_dict_item` (`id`, `dict_code`, `label`, `value`, `order_sort`, `status`, `tag_type`, `remark`, `create_time`, `update_time`, `deleted`) VALUES ('950000000000000202', 'system_sms_channel_code', '阿里云', 'ALIYUN', 2, 1, 'primary', NULL, '2026-07-05 00:00:00', '2026-07-05 00:00:00', 0);
INSERT INTO `system_dict_item` (`id`, `dict_code`, `label`, `value`, `order_sort`, `status`, `tag_type`, `remark`, `create_time`, `update_time`, `deleted`) VALUES ('950000000000000203', 'system_sms_channel_code', '腾讯云', 'TENCENT', 3, 1, 'success', NULL, '2026-07-05 00:00:00', '2026-07-05 00:00:00', 0);
INSERT INTO `system_dict_item` (`id`, `dict_code`, `label`, `value`, `order_sort`, `status`, `tag_type`, `remark`, `create_time`, `update_time`, `deleted`) VALUES ('950000000000000211', 'system_sms_template_type', '验证码', 'CAPTCHA', 1, 1, 'primary', NULL, '2026-07-05 00:00:00', '2026-07-05 00:00:00', 0);
INSERT INTO `system_dict_item` (`id`, `dict_code`, `label`, `value`, `order_sort`, `status`, `tag_type`, `remark`, `create_time`, `update_time`, `deleted`) VALUES ('950000000000000212', 'system_sms_template_type', '通知', 'NOTICE', 2, 1, 'success', NULL, '2026-07-05 00:00:00', '2026-07-05 00:00:00', 0);
INSERT INTO `system_dict_item` (`id`, `dict_code`, `label`, `value`, `order_sort`, `status`, `tag_type`, `remark`, `create_time`, `update_time`, `deleted`) VALUES ('950000000000000213', 'system_sms_template_type', '营销', 'MARKETING', 3, 1, 'warning', NULL, '2026-07-05 00:00:00', '2026-07-05 00:00:00', 0);
INSERT INTO `system_dict_item` (`id`, `dict_code`, `label`, `value`, `order_sort`, `status`, `tag_type`, `remark`, `create_time`, `update_time`, `deleted`) VALUES ('950000000000000221', 'system_message_send_status', '待发送', 'INIT', 1, 1, 'info', NULL, '2026-07-05 00:00:00', '2026-07-05 00:00:00', 0);
INSERT INTO `system_dict_item` (`id`, `dict_code`, `label`, `value`, `order_sort`, `status`, `tag_type`, `remark`, `create_time`, `update_time`, `deleted`) VALUES ('950000000000000222', 'system_message_send_status', '成功', 'SUCCESS', 2, 1, 'success', NULL, '2026-07-05 00:00:00', '2026-07-05 00:00:00', 0);
INSERT INTO `system_dict_item` (`id`, `dict_code`, `label`, `value`, `order_sort`, `status`, `tag_type`, `remark`, `create_time`, `update_time`, `deleted`) VALUES ('950000000000000223', 'system_message_send_status', '失败', 'FAILED', 3, 1, 'danger', NULL, '2026-07-05 00:00:00', '2026-07-05 00:00:00', 0);
INSERT INTO `system_dict_item` (`id`, `dict_code`, `label`, `value`, `order_sort`, `status`, `tag_type`, `remark`, `create_time`, `update_time`, `deleted`) VALUES ('950000000000000231', 'system_notify_template_type', '系统消息', 'SYSTEM', 1, 1, 'primary', NULL, '2026-07-05 00:00:00', '2026-07-05 00:00:00', 0);
INSERT INTO `system_dict_item` (`id`, `dict_code`, `label`, `value`, `order_sort`, `status`, `tag_type`, `remark`, `create_time`, `update_time`, `deleted`) VALUES ('950000000000000232', 'system_notify_template_type', '业务通知', 'NOTICE', 2, 1, 'success', NULL, '2026-07-05 00:00:00', '2026-07-05 00:00:00', 0);
COMMIT;

-- ----------------------------
-- Table structure for system_notice
-- ----------------------------
DROP TABLE IF EXISTS `system_notice`;
CREATE TABLE `system_notice` (
                                 `id` varchar(32) NOT NULL COMMENT 'id',
                                 `title` varchar(255) NOT NULL COMMENT '标题',
                                 `notice_type` varchar(90) NOT NULL COMMENT '通知类型',
                                 `content` text COMMENT '内容',
                                 `publish_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '发布状态',
                                 `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
                                 `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                 `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                 `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='通知公告';

-- ----------------------------
-- Records of system_notice
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for system_user_notice
-- ----------------------------
DROP TABLE IF EXISTS `system_user_notice`;
CREATE TABLE `system_user_notice` (
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

-- ----------------------------
-- Records of system_user_notice
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for system_sms_channel
-- ----------------------------
DROP TABLE IF EXISTS `system_sms_channel`;
CREATE TABLE `system_sms_channel` (
                                      `id` varchar(32) NOT NULL COMMENT 'id',
                                      `channel_name` varchar(255) NOT NULL COMMENT '渠道名称',
                                      `channel_code` varchar(90) NOT NULL COMMENT '渠道编码',
                                      `access_key` varchar(255) DEFAULT NULL COMMENT '访问密钥',
                                      `access_secret` varchar(900) DEFAULT NULL COMMENT '访问密钥Secret',
                                      `signature` varchar(255) DEFAULT NULL COMMENT '短信签名',
                                      `endpoint` varchar(255) DEFAULT NULL COMMENT '服务端点',
                                      `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态',
                                      `remark` varchar(900) DEFAULT NULL COMMENT '备注',
                                      `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                      `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                      `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                                      PRIMARY KEY (`id`) USING BTREE,
                                      UNIQUE KEY `uk_system_sms_channel_code` (`channel_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='短信渠道';

-- ----------------------------
-- Records of system_sms_channel
-- ----------------------------
BEGIN;
INSERT INTO `system_sms_channel` (`id`, `channel_name`, `channel_code`, `access_key`, `access_secret`, `signature`, `endpoint`, `status`, `remark`, `create_time`, `update_time`, `deleted`) VALUES ('970000000000010000', 'DEBUG 调试渠道', 'DEBUG', NULL, NULL, 'DEBUG', NULL, 1, '本地调试渠道，仅记录发送日志', '2026-07-05 00:00:00', '2026-07-05 00:00:00', 0);
COMMIT;

-- ----------------------------
-- Table structure for system_sms_template
-- ----------------------------
DROP TABLE IF EXISTS `system_sms_template`;
CREATE TABLE `system_sms_template` (
                                       `id` varchar(32) NOT NULL COMMENT 'id',
                                       `channel_id` varchar(32) NOT NULL COMMENT '短信渠道ID',
                                       `template_name` varchar(255) NOT NULL COMMENT '模板名称',
                                       `template_code` varchar(120) NOT NULL COMMENT '模板编码',
                                       `provider_template_code` varchar(255) DEFAULT NULL COMMENT '供应商模板编码',
                                       `template_type` varchar(90) DEFAULT NULL COMMENT '模板类型',
                                       `content` text NOT NULL COMMENT '模板内容',
                                       `params` text COMMENT '模板参数JSON',
                                       `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态',
                                       `remark` varchar(900) DEFAULT NULL COMMENT '备注',
                                       `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                       `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                       `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                                       PRIMARY KEY (`id`) USING BTREE,
                                       UNIQUE KEY `uk_system_sms_template_code` (`template_code`) USING BTREE,
                                       KEY `idx_system_sms_template_channel` (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='短信模板';

-- ----------------------------
-- Records of system_sms_template
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for system_sms_log
-- ----------------------------
DROP TABLE IF EXISTS `system_sms_log`;
CREATE TABLE `system_sms_log` (
                                  `id` varchar(32) NOT NULL COMMENT 'id',
                                  `channel_id` varchar(32) NOT NULL COMMENT '短信渠道ID',
                                  `template_id` varchar(32) NOT NULL COMMENT '短信模板ID',
                                  `template_code` varchar(120) NOT NULL COMMENT '模板编码',
                                  `mobile` varchar(32) NOT NULL COMMENT '手机号',
                                  `content` text COMMENT '发送内容',
                                  `template_params` text COMMENT '模板参数JSON',
                                  `send_status` varchar(60) NOT NULL COMMENT '发送状态',
                                  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
                                  `error_msg` varchar(900) DEFAULT NULL COMMENT '失败原因',
                                  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  KEY `idx_system_sms_log_template` (`template_id`),
                                  KEY `idx_system_sms_log_mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='短信发送日志';

-- ----------------------------
-- Records of system_sms_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for system_mail_account
-- ----------------------------
DROP TABLE IF EXISTS `system_mail_account`;
CREATE TABLE `system_mail_account` (
                                       `id` varchar(32) NOT NULL COMMENT 'id',
                                       `mail` varchar(255) NOT NULL COMMENT '邮箱地址',
                                       `username` varchar(255) NOT NULL COMMENT 'SMTP用户名',
                                       `password` varchar(900) DEFAULT NULL COMMENT 'SMTP密码',
                                       `host` varchar(255) NOT NULL COMMENT 'SMTP主机',
                                       `port` int NOT NULL COMMENT 'SMTP端口',
                                       `ssl_enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '启用SSL',
                                       `starttls_enable` tinyint(1) NOT NULL DEFAULT '0' COMMENT '启用STARTTLS',
                                       `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态',
                                       `remark` varchar(900) DEFAULT NULL COMMENT '备注',
                                       `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                       `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                       `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                                       PRIMARY KEY (`id`) USING BTREE,
                                       UNIQUE KEY `uk_system_mail_account_mail` (`mail`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='邮箱账号';

-- ----------------------------
-- Records of system_mail_account
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for system_mail_template
-- ----------------------------
DROP TABLE IF EXISTS `system_mail_template`;
CREATE TABLE `system_mail_template` (
                                        `id` varchar(32) NOT NULL COMMENT 'id',
                                        `account_id` varchar(32) NOT NULL COMMENT '邮箱账号ID',
                                        `name` varchar(255) NOT NULL COMMENT '模板名称',
                                        `code` varchar(120) NOT NULL COMMENT '模板编码',
                                        `from_name` varchar(255) DEFAULT NULL COMMENT '发件人昵称',
                                        `title` varchar(255) NOT NULL COMMENT '邮件标题',
                                        `content` text NOT NULL COMMENT '邮件内容',
                                        `params` text COMMENT '模板参数JSON',
                                        `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态',
                                        `remark` varchar(900) DEFAULT NULL COMMENT '备注',
                                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                        `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                        `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                                        PRIMARY KEY (`id`) USING BTREE,
                                        UNIQUE KEY `uk_system_mail_template_code` (`code`) USING BTREE,
                                        KEY `idx_system_mail_template_account` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='邮件模板';

-- ----------------------------
-- Records of system_mail_template
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for system_mail_log
-- ----------------------------
DROP TABLE IF EXISTS `system_mail_log`;
CREATE TABLE `system_mail_log` (
                                   `id` varchar(32) NOT NULL COMMENT 'id',
                                   `account_id` varchar(32) NOT NULL COMMENT '邮箱账号ID',
                                   `template_id` varchar(32) NOT NULL COMMENT '邮件模板ID',
                                   `code` varchar(120) NOT NULL COMMENT '模板编码',
                                   `from_mail` varchar(255) NOT NULL COMMENT '发件邮箱',
                                   `to_mail` varchar(255) NOT NULL COMMENT '收件邮箱',
                                   `title` varchar(255) NOT NULL COMMENT '邮件标题',
                                   `content` text COMMENT '邮件内容',
                                   `template_params` text COMMENT '模板参数JSON',
                                   `send_status` varchar(60) NOT NULL COMMENT '发送状态',
                                   `send_time` datetime DEFAULT NULL COMMENT '发送时间',
                                   `error_msg` varchar(900) DEFAULT NULL COMMENT '失败原因',
                                   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                   `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   KEY `idx_system_mail_log_template` (`template_id`),
                                   KEY `idx_system_mail_log_to` (`to_mail`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='邮件发送日志';

-- ----------------------------
-- Records of system_mail_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for system_notify_template
-- ----------------------------
DROP TABLE IF EXISTS `system_notify_template`;
CREATE TABLE `system_notify_template` (
                                          `id` varchar(32) NOT NULL COMMENT 'id',
                                          `name` varchar(255) NOT NULL COMMENT '模板名称',
                                          `code` varchar(120) NOT NULL COMMENT '模板编码',
                                          `nickname` varchar(255) NOT NULL COMMENT '发送人昵称',
                                          `template_type` varchar(90) DEFAULT NULL COMMENT '模板类型',
                                          `content` text NOT NULL COMMENT '模板内容',
                                          `params` text COMMENT '模板参数JSON',
                                          `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态',
                                          `remark` varchar(900) DEFAULT NULL COMMENT '备注',
                                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                          `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                          `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                                          PRIMARY KEY (`id`) USING BTREE,
                                          UNIQUE KEY `uk_system_notify_template_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='站内信模板';

-- ----------------------------
-- Records of system_notify_template
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for system_notify_message
-- ----------------------------
DROP TABLE IF EXISTS `system_notify_message`;
CREATE TABLE `system_notify_message` (
                                         `id` varchar(32) NOT NULL COMMENT 'id',
                                         `user_id` varchar(32) NOT NULL COMMENT '用户ID',
                                         `template_id` varchar(32) NOT NULL COMMENT '模板ID',
                                         `template_code` varchar(120) NOT NULL COMMENT '模板编码',
                                         `template_nickname` varchar(255) NOT NULL COMMENT '发送人昵称',
                                         `template_content` text NOT NULL COMMENT '消息内容',
                                         `template_type` varchar(90) DEFAULT NULL COMMENT '模板类型',
                                         `template_params` text COMMENT '模板参数JSON',
                                         `read_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '已读状态',
                                         `read_time` datetime DEFAULT NULL COMMENT '阅读时间',
                                         `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                         `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                         `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                                         PRIMARY KEY (`id`) USING BTREE,
                                         KEY `idx_system_notify_message_user` (`user_id`,`read_status`),
                                         KEY `idx_system_notify_message_template` (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='站内信消息';

-- ----------------------------
-- Records of system_notify_message
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for system_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_menu`;
CREATE TABLE `system_menu` (
                               `id` varchar(32) NOT NULL COMMENT 'id',
                               `root_id` varchar(32) NOT NULL COMMENT '上级ID',
                               `name` varchar(255) NOT NULL COMMENT '菜单名称',
                               `route_key` varchar(255) DEFAULT NULL COMMENT '路由key全局唯一',
                               `order_sort` int(3) NOT NULL COMMENT '显示顺序',
                               `is_iframe` tinyint(1) DEFAULT NULL COMMENT '是否为网页',
                               `path` varchar(255) DEFAULT NULL COMMENT '请求路径',
                               `icon` varchar(90) DEFAULT NULL COMMENT 'icones ',
                               `local_icon` varchar(255) DEFAULT NULL COMMENT '本地icon',
                               `visible` tinyint(1) DEFAULT NULL COMMENT '是否显示',
                               `permission` varchar(90) DEFAULT NULL COMMENT '权限标识',
                               `type` varchar(90) NOT NULL COMMENT '菜单类型（table目录 menu菜单 button按钮）',
                               `remark` varchar(900) DEFAULT NULL COMMENT '备注',
                               `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                               `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                               `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统菜单';

-- ----------------------------
-- Records of system_menu
-- ----------------------------
BEGIN;
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('173736381381221581', '0', '系统管理', 'system', 2, 0, '', 'tdesign:system-setting', NULL, 1, '', 'TABLE', NULL, 0, '2023-12-19 14:38:13', '2023-12-19 14:38:19');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('1737363813812215810', '173736381381221581', '角色管理', 'system_role', 2, 0, '/system/role', 'mingcute:safety-certificate-fill', NULL, 1, 'system:role:view', 'MENU', NULL, 0, '2023-12-20 14:46:27', '2023-12-20 14:46:27');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('173736381381221583', '173736381381221581', '菜单管理', 'system_menu', 2, 0, '/system/menu', 'line-md:list-3-filled', NULL, 1, 'system:menu:view', 'MENU', NULL, 0, NULL, '2023-12-20 14:57:03');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('1737364028824821762', '0', '首页', 'home', 1, 0, '/home', 'line-md:emoji-smile-wink', NULL, 1, 'home:view', 'MENU', NULL, 0, '2023-12-20 14:47:18', '2023-12-20 14:47:18');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('1737369714325528577', '173736381381221581', '用户管理', 'system_user', 2, 0, '/system/user', 'line-md:person-search-twotone', NULL, 1, 'system:user:view', 'MENU', NULL, 0, '2023-12-20 15:09:53', '2023-12-20 15:09:53');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('900000000000000001', '1737369714325528577', '新增用户', 'system_user_create', 1, 0, '', NULL, NULL, 0, 'system:user:create', 'BUTTON', NULL, 0, '2026-05-12 00:00:00', '2026-05-12 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('900000000000000002', '1737369714325528577', '编辑用户', 'system_user_update', 2, 0, '', NULL, NULL, 0, 'system:user:update', 'BUTTON', NULL, 0, '2026-05-12 00:00:00', '2026-05-12 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('900000000000000003', '1737369714325528577', '删除用户', 'system_user_delete', 3, 0, '', NULL, NULL, 0, 'system:user:delete', 'BUTTON', NULL, 0, '2026-05-12 00:00:00', '2026-05-12 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('900000000000000011', '1737363813812215810', '新增角色', 'system_role_create', 1, 0, '', NULL, NULL, 0, 'system:role:create', 'BUTTON', NULL, 0, '2026-05-12 00:00:00', '2026-05-12 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('900000000000000012', '1737363813812215810', '编辑角色', 'system_role_update', 2, 0, '', NULL, NULL, 0, 'system:role:update', 'BUTTON', NULL, 0, '2026-05-12 00:00:00', '2026-05-12 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('900000000000000013', '1737363813812215810', '删除角色', 'system_role_delete', 3, 0, '', NULL, NULL, 0, 'system:role:delete', 'BUTTON', NULL, 0, '2026-05-12 00:00:00', '2026-05-12 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('900000000000000014', '1737363813812215810', '分配权限', 'system_role_permission', 4, 0, '', NULL, NULL, 0, 'system:role:permission', 'BUTTON', NULL, 0, '2026-05-12 00:00:00', '2026-05-12 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('900000000000000021', '173736381381221583', '新增菜单', 'system_menu_create', 1, 0, '', NULL, NULL, 0, 'system:menu:create', 'BUTTON', NULL, 0, '2026-05-12 00:00:00', '2026-05-12 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('900000000000000022', '173736381381221583', '编辑菜单', 'system_menu_update', 2, 0, '', NULL, NULL, 0, 'system:menu:update', 'BUTTON', NULL, 0, '2026-05-12 00:00:00', '2026-05-12 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('900000000000000023', '173736381381221583', '删除菜单', 'system_menu_delete', 3, 0, '', NULL, NULL, 0, 'system:menu:delete', 'BUTTON', NULL, 0, '2026-05-12 00:00:00', '2026-05-12 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('930000000000000000', '173736381381221581', '系统字典', 'system_dict', 3, 0, '/system/dict', 'ri:book-2-line', NULL, 1, 'system:dict:view', 'MENU', '基础服务：字典管理', 0, '2026-06-19 00:00:00', '2026-06-19 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('930000000000000001', '930000000000000000', '新增字典', 'system_dict_create', 1, 0, '', NULL, NULL, 0, 'system:dict:create', 'BUTTON', NULL, 0, '2026-06-19 00:00:00', '2026-06-19 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('930000000000000002', '930000000000000000', '编辑字典', 'system_dict_update', 2, 0, '', NULL, NULL, 0, 'system:dict:update', 'BUTTON', NULL, 0, '2026-06-19 00:00:00', '2026-06-19 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('930000000000000003', '930000000000000000', '删除字典', 'system_dict_delete', 3, 0, '', NULL, NULL, 0, 'system:dict:delete', 'BUTTON', NULL, 0, '2026-06-19 00:00:00', '2026-06-19 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('940000000000000000', '173736381381221581', '通知公告', 'system_notice', 4, 0, '/system/notice', 'ri:notification-3-line', NULL, 1, 'system:notice:view', 'MENU', '基础服务：通知公告', 0, '2026-06-19 00:00:00', '2026-06-19 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('940000000000000001', '940000000000000000', '新增通知', 'system_notice_create', 1, 0, '', NULL, NULL, 0, 'system:notice:create', 'BUTTON', NULL, 0, '2026-06-19 00:00:00', '2026-06-19 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('940000000000000002', '940000000000000000', '编辑通知', 'system_notice_update', 2, 0, '', NULL, NULL, 0, 'system:notice:update', 'BUTTON', NULL, 0, '2026-06-19 00:00:00', '2026-06-19 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('940000000000000003', '940000000000000000', '删除通知', 'system_notice_delete', 3, 0, '', NULL, NULL, 0, 'system:notice:delete', 'BUTTON', NULL, 0, '2026-06-19 00:00:00', '2026-06-19 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('940000000000000004', '940000000000000000', '发布通知', 'system_notice_publish', 4, 0, '', NULL, NULL, 0, 'system:notice:publish', 'BUTTON', NULL, 0, '2026-06-19 00:00:00', '2026-06-19 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('940000000000000005', '940000000000000000', '撤回通知', 'system_notice_revoke', 5, 0, '', NULL, NULL, 0, 'system:notice:revoke', 'BUTTON', NULL, 0, '2026-06-19 00:00:00', '2026-06-19 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('920000000000000000', '0', 'Art 示例', 'art_demo', 90, 0, '', 'ri:sparkling-line', NULL, 1, '', 'TABLE', '二期 Art Design Pro 示例菜单，仅授权角色可见', 0, '2026-06-19 00:00:00', '2026-06-19 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('920000000000000101', '920000000000000000', '控制台', 'art_dashboard_console', 1, 0, '/dashboard/console', 'ri:dashboard-line', NULL, 1, 'art:dashboard:console:view', 'MENU', 'Art Design Pro 示例页', 0, '2026-06-19 00:00:00', '2026-06-19 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('920000000000000102', '920000000000000000', '分析页', 'art_dashboard_analysis', 2, 0, '/dashboard/analysis', 'ri:pie-chart-line', NULL, 1, 'art:dashboard:analysis:view', 'MENU', 'Art Design Pro 示例页', 0, '2026-06-19 00:00:00', '2026-06-19 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('920000000000000201', '920000000000000000', '基础表格', 'art_examples_tables_basic', 3, 0, '/examples/tables/basic', 'ri:table-line', NULL, 1, 'art:examples:tables:basic:view', 'MENU', 'Art Design Pro 示例页', 0, '2026-06-19 00:00:00', '2026-06-19 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('920000000000000202', '920000000000000000', '表单示例', 'art_examples_forms', 4, 0, '/examples/forms', 'ri:file-edit-line', NULL, 1, 'art:examples:forms:view', 'MENU', 'Art Design Pro 示例页', 0, '2026-06-19 00:00:00', '2026-06-19 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('920000000000000301', '920000000000000000', '图标组件', 'art_widgets_icon', 5, 0, '/widgets/icon', 'ri:remixicon-line', NULL, 1, 'art:widgets:icon:view', 'MENU', 'Art Design Pro 示例页', 0, '2026-06-19 00:00:00', '2026-06-19 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('920000000000000401', '920000000000000000', '卡片模板', 'art_template_cards', 6, 0, '/template/cards', 'ri:layout-grid-line', NULL, 1, 'art:template:cards:view', 'MENU', 'Art Design Pro 示例页', 0, '2026-06-19 00:00:00', '2026-06-19 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('920000000000000501', '920000000000000000', '成功结果', 'art_result_success', 7, 0, '/result/success', 'ri:checkbox-circle-line', NULL, 1, 'art:result:success:view', 'MENU', 'Art Design Pro 示例页', 0, '2026-06-19 00:00:00', '2026-06-19 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('920000000000000601', '920000000000000000', '服务保障', 'art_safeguard_server', 8, 0, '/safeguard/server', 'ri:server-line', NULL, 1, 'art:safeguard:server:view', 'MENU', 'Art Design Pro 示例页', 0, '2026-06-19 00:00:00', '2026-06-19 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('920000000000000701', '920000000000000000', '文章列表', 'art_article_list', 9, 0, '/article/list', 'ri:article-line', NULL, 1, 'art:article:list:view', 'MENU', 'Art Design Pro 示例页', 0, '2026-06-19 00:00:00', '2026-06-19 00:00:00');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('1738099497552429057', '0', '文档管理', 'doc', 5, 0, '', 'line-md:text-box', NULL, 1, '', 'TABLE', '旧前端演示菜单，Art Design Pro 迁移后默认停用', 1, '2023-12-22 15:29:47', '2023-12-22 15:29:47');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('1739115016615215105', '1738099497552429057', 'Naive UI', 'doc_naive', 5, 1, 'https://www.naiveui.com/zh-CN/os-theme', 'line-md:menu', NULL, 1, 'doc:show', 'MENU', '旧前端演示菜单，Art Design Pro 迁移后默认停用', 1, '2023-12-25 10:45:06', '2023-12-25 10:45:06');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('1739223870027972609', '0', '组件', 'component', 3, 0, '', 'line-md:coffee-twotone', NULL, 1, '', 'TABLE', '旧前端演示菜单，Art Design Pro 迁移后默认停用', 1, '2023-12-25 17:57:38', '2023-12-25 17:57:38');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('1739224144796827649', '1739223870027972609', '图标', 'component_icon', 3, 0, '/component/icon', 'line-md:emoji-smile-wink', NULL, 1, 'component:icon:view', 'MENU', '旧前端演示菜单，Art Design Pro 迁移后默认停用', 1, '2023-12-25 17:58:44', '2023-12-25 17:58:44');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('1739224376339185665', '1739223870027972609', '表单', 'component_form', 3, 0, '/component/form', 'line-md:emoji-smile-wink', NULL, 1, 'component:form:view', 'MENU', '旧前端演示菜单，Art Design Pro 迁移后默认停用', 1, '2023-12-25 17:59:39', '2023-12-25 17:59:39');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('1739224547890413570', '1739223870027972609', '表单', 'component_table', 3, 0, '/component/table', 'line-md:emoji-smile-wink', NULL, 1, 'component:table:view', 'MENU', '旧前端演示菜单，Art Design Pro 迁移后默认停用', 1, '2023-12-25 18:00:20', '2023-12-25 18:00:20');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('1739224770612150273', '0', '异常页', 'exception', 4, 0, '', 'line-md:alert-circle', NULL, 1, '', 'TABLE', '旧前端演示菜单，Art Design Pro 迁移后默认停用', 1, '2023-12-25 18:01:13', '2023-12-25 18:01:13');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('1739225110254305282', '1739224770612150273', '404', 'exception_404', 4, 0, '/exception/404', 'line-md:alert-circle', NULL, 1, '', 'MENU', '旧前端演示菜单，Art Design Pro 迁移后默认停用', 1, '2023-12-25 18:02:34', '2023-12-25 18:02:34');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('1739225241577963521', '1739224770612150273', '403', 'exception_403', 4, 0, '/exception/403', 'line-md:alert-circle', NULL, 1, '', 'MENU', '旧前端演示菜单，Art Design Pro 迁移后默认停用', 1, '2023-12-25 18:03:05', '2023-12-25 18:03:05');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('1739225337644302337', '1739224770612150273', '500', 'exception_500', 4, 0, '/exception/500', 'line-md:alert-circle', NULL, 1, '', 'MENU', '旧前端演示菜单，Art Design Pro 迁移后默认停用', 1, '2023-12-25 18:03:28', '2023-12-25 18:03:28');
INSERT INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES ('1739225546147348481', '0', '关于', 'about', 6, 0, '/about', 'line-md:lightbulb-twotone', NULL, 1, 'about:view', 'MENU', '旧前端演示菜单，Art Design Pro 迁移后默认停用', 1, '2023-12-25 18:04:18', '2023-12-25 18:04:18');
COMMIT;

-- ----------------------------
-- Art Design Pro 二期菜单结构对齐
-- ----------------------------
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

REPLACE INTO `system_menu`
(`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`)
VALUES
('890000000000000000', '173736381381221581', '嵌套菜单', 'system_nested', 6, 0, '/system/nested', 'ri:menu-fold-line', NULL, 1, '', 'TABLE', 'Art Design Pro 嵌套菜单示例', 0, NOW(), NOW()),
('890000000000000101', '890000000000000000', '菜单1', 'system_nested_menu1', 1, 0, '/system/nested/menu1', 'ri:menu-2-line', NULL, 1, 'art:system:nested:menu1:view', 'MENU', 'Art Design Pro 嵌套菜单1', 0, NOW(), NOW()),
('890000000000000201', '890000000000000000', '菜单2', 'system_nested_menu2', 2, 0, '/system/nested/menu2', 'ri:menu-2-line', NULL, 1, 'art:system:nested:menu2:view', 'MENU', 'Art Design Pro 嵌套菜单2', 0, NOW(), NOW()),
('890000000000000301', '890000000000000000', '菜单3', 'system_nested_menu3', 3, 0, '/system/nested/menu3', 'ri:menu-2-line', NULL, 1, 'art:system:nested:menu3:view', 'MENU', 'Art Design Pro 嵌套菜单3', 0, NOW(), NOW()),
('890000000000000321', '890000000000000301', '菜单3-2', 'system_nested_menu3_2', 2, 0, '/system/nested/menu3/menu3-2', 'ri:menu-3-line', NULL, 1, 'art:system:nested:menu3-2:view', 'MENU', 'Art Design Pro 嵌套菜单3-2', 0, NOW(), NOW());

COMMIT;

-- ----------------------------
-- Table structure for user_post
-- ----------------------------
DROP TABLE IF EXISTS `user_post`;
CREATE TABLE `user_post` (
                             `id` varchar(32) NOT NULL COMMENT 'id',
                             `role_id` varchar(255) NOT NULL COMMENT '角色ID',
                             `post_id` varchar(255) NOT NULL COMMENT '部门ID',
                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                             `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE KEY `user_post_index` (`role_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户关联岗位';

-- ----------------------------
-- Records of user_post
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
                             `id` varchar(32) NOT NULL COMMENT 'id',
                             `user_id` varchar(255) NOT NULL COMMENT '系统用户ID',
                             `role_id` varchar(255) NOT NULL COMMENT '角色ID',
                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                             `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE KEY `user_role_index` (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户和角色关联';

-- ----------------------------
-- Records of user_role
-- ----------------------------
BEGIN;
INSERT INTO `user_role` (`id`, `user_id`, `role_id`, `create_time`, `update_time`, `deleted`) VALUES ('1737762638733332482', '1736671483388059649', '1737408771218792450', '2023-12-21 17:11:14', '2023-12-21 17:11:14', 0);
INSERT INTO `user_role` (`id`, `user_id`, `role_id`, `create_time`, `update_time`, `deleted`) VALUES ('1737764212616564737', '1737764212557844482', '1737763732809142273', '2023-12-21 17:17:29', '2023-12-21 17:17:29', 0);
INSERT INTO `user_role` (`id`, `user_id`, `role_id`, `create_time`, `update_time`, `deleted`) VALUES ('1739226401051361281', '1739226400963280898', '1737763732809142273', '2023-12-25 18:07:42', '2023-12-25 18:07:42', 0);
COMMIT;

-- ----------------------------
-- Table structure for infra_config
-- ----------------------------
DROP TABLE IF EXISTS `infra_config`;
CREATE TABLE `infra_config` (
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

-- ----------------------------
-- Records of infra_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for infra_file_config
-- ----------------------------
DROP TABLE IF EXISTS `infra_file_config`;
CREATE TABLE `infra_file_config` (
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

-- ----------------------------
-- Records of infra_file_config
-- ----------------------------
BEGIN;
INSERT INTO `infra_file_config`
(`id`, `name`, `storage_type`, `master`, `base_path`, `domain`, `max_size_mb`, `endpoint`, `bucket`, `access_key`, `access_secret`, `enable_path_style_access`, `remark`, `create_time`, `update_time`, `deleted`)
VALUES
('960000000000020000', '默认本地存储', 'local', 1, 'dev/uploads', NULL, 50, NULL, NULL, NULL, NULL, 0, '系统默认文件存储配置', NOW(), NOW(), 0);
COMMIT;

-- ----------------------------
-- Table structure for infra_file
-- ----------------------------
DROP TABLE IF EXISTS `infra_file`;
CREATE TABLE `infra_file` (
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

-- ----------------------------
-- Records of infra_file
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for infra_codegen_table
-- ----------------------------
DROP TABLE IF EXISTS `infra_codegen_table`;
CREATE TABLE `infra_codegen_table` (
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

-- ----------------------------
-- Table structure for infra_codegen_field
-- ----------------------------
DROP TABLE IF EXISTS `infra_codegen_field`;
CREATE TABLE `infra_codegen_field` (
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

-- ----------------------------
-- Table structure for infra_codegen_relation
-- ----------------------------
DROP TABLE IF EXISTS `infra_codegen_relation`;
CREATE TABLE `infra_codegen_relation` (
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

-- ----------------------------
-- Records of infra codegen menu
-- ----------------------------
BEGIN;
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
('960000000000000106', '960000000000000100', '删除配置', 'infra_codegen_delete', 6, 0, '', NULL, NULL, 0, 'infra:codegen:delete', 'BUTTON', NULL, 0, NOW(), NOW()),
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
('940000000000000100', '173736381381221581', '我的消息', 'system_notice_center', 99, 0, '/system/notice-center', 'ri:message-3-line', NULL, 0, 'system:notice:mine', 'MENU', '当前用户站内通知', 0, NOW(), NOW()),
('970000000000000000', '173736381381221581', '消息中心', 'system_messages', 6, 0, '/system/messages', 'ri:message-3-line', NULL, 1, '', 'TABLE', '系统消息中心', 0, NOW(), NOW()),
('970000000000000100', '970000000000000000', '短信管理', 'system_messages_sms', 1, 0, '/system/messages/sms', 'ri:message-2-line', NULL, 1, '', 'TABLE', '短信渠道、模板、日志', 0, NOW(), NOW()),
('970000000000000101', '970000000000000100', '短信渠道', 'system_messages_sms_channel', 1, 0, '/system/messages/sms/channel', 'ri:base-station-line', NULL, 1, 'system:sms-channel:view', 'MENU', '短信渠道管理', 0, NOW(), NOW()),
('970000000000000111', '970000000000000101', '新增短信渠道', 'system_messages_sms_channel_create', 1, 0, '', NULL, NULL, 0, 'system:sms-channel:create', 'BUTTON', NULL, 0, NOW(), NOW()),
('970000000000000112', '970000000000000101', '编辑短信渠道', 'system_messages_sms_channel_update', 2, 0, '', NULL, NULL, 0, 'system:sms-channel:update', 'BUTTON', NULL, 0, NOW(), NOW()),
('970000000000000113', '970000000000000101', '删除短信渠道', 'system_messages_sms_channel_delete', 3, 0, '', NULL, NULL, 0, 'system:sms-channel:delete', 'BUTTON', NULL, 0, NOW(), NOW()),
('970000000000000102', '970000000000000100', '短信模板', 'system_messages_sms_template', 2, 0, '/system/messages/sms/template', 'ri:file-list-3-line', NULL, 1, 'system:sms-template:view', 'MENU', '短信模板管理', 0, NOW(), NOW()),
('970000000000000121', '970000000000000102', '新增短信模板', 'system_messages_sms_template_create', 1, 0, '', NULL, NULL, 0, 'system:sms-template:create', 'BUTTON', NULL, 0, NOW(), NOW()),
('970000000000000122', '970000000000000102', '编辑短信模板', 'system_messages_sms_template_update', 2, 0, '', NULL, NULL, 0, 'system:sms-template:update', 'BUTTON', NULL, 0, NOW(), NOW()),
('970000000000000123', '970000000000000102', '删除短信模板', 'system_messages_sms_template_delete', 3, 0, '', NULL, NULL, 0, 'system:sms-template:delete', 'BUTTON', NULL, 0, NOW(), NOW()),
('970000000000000124', '970000000000000102', '测试短信模板', 'system_messages_sms_template_send', 4, 0, '', NULL, NULL, 0, 'system:sms-template:send', 'BUTTON', NULL, 0, NOW(), NOW()),
('970000000000000103', '970000000000000100', '短信日志', 'system_messages_sms_log', 3, 0, '/system/messages/sms/log', 'ri:file-search-line', NULL, 1, 'system:sms-log:view', 'MENU', '短信发送日志', 0, NOW(), NOW()),
('970000000000000200', '970000000000000000', '邮箱管理', 'system_messages_mail', 2, 0, '/system/messages/mail', 'ri:mail-line', NULL, 1, '', 'TABLE', '邮箱账号、模板、记录', 0, NOW(), NOW()),
('970000000000000201', '970000000000000200', '邮箱账号', 'system_messages_mail_account', 1, 0, '/system/messages/mail/account', 'ri:account-circle-line', NULL, 1, 'system:mail-account:view', 'MENU', '邮箱账号管理', 0, NOW(), NOW()),
('970000000000000211', '970000000000000201', '新增邮箱账号', 'system_messages_mail_account_create', 1, 0, '', NULL, NULL, 0, 'system:mail-account:create', 'BUTTON', NULL, 0, NOW(), NOW()),
('970000000000000212', '970000000000000201', '编辑邮箱账号', 'system_messages_mail_account_update', 2, 0, '', NULL, NULL, 0, 'system:mail-account:update', 'BUTTON', NULL, 0, NOW(), NOW()),
('970000000000000213', '970000000000000201', '删除邮箱账号', 'system_messages_mail_account_delete', 3, 0, '', NULL, NULL, 0, 'system:mail-account:delete', 'BUTTON', NULL, 0, NOW(), NOW()),
('970000000000000202', '970000000000000200', '邮件模板', 'system_messages_mail_template', 2, 0, '/system/messages/mail/template', 'ri:price-tag-3-line', NULL, 1, 'system:mail-template:view', 'MENU', '邮件模板管理', 0, NOW(), NOW()),
('970000000000000221', '970000000000000202', '新增邮件模板', 'system_messages_mail_template_create', 1, 0, '', NULL, NULL, 0, 'system:mail-template:create', 'BUTTON', NULL, 0, NOW(), NOW()),
('970000000000000222', '970000000000000202', '编辑邮件模板', 'system_messages_mail_template_update', 2, 0, '', NULL, NULL, 0, 'system:mail-template:update', 'BUTTON', NULL, 0, NOW(), NOW()),
('970000000000000223', '970000000000000202', '删除邮件模板', 'system_messages_mail_template_delete', 3, 0, '', NULL, NULL, 0, 'system:mail-template:delete', 'BUTTON', NULL, 0, NOW(), NOW()),
('970000000000000224', '970000000000000202', '测试邮件模板', 'system_messages_mail_template_send', 4, 0, '', NULL, NULL, 0, 'system:mail-template:send', 'BUTTON', NULL, 0, NOW(), NOW()),
('970000000000000203', '970000000000000200', '邮件记录', 'system_messages_mail_log', 3, 0, '/system/messages/mail/log', 'ri:file-edit-line', NULL, 1, 'system:mail-log:view', 'MENU', '邮件发送记录', 0, NOW(), NOW()),
('970000000000000300', '970000000000000000', '站内信管理', 'system_messages_notify', 3, 0, '/system/messages/notify', 'ri:inbox-line', NULL, 1, '', 'TABLE', '站内信模板和记录', 0, NOW(), NOW()),
('970000000000000301', '970000000000000300', '模板管理', 'system_messages_notify_template', 1, 0, '/system/messages/notify/template', 'ri:file-list-line', NULL, 1, 'system:notify-template:view', 'MENU', '站内信模板管理', 0, NOW(), NOW()),
('970000000000000311', '970000000000000301', '新增站内信模板', 'system_messages_notify_template_create', 1, 0, '', NULL, NULL, 0, 'system:notify-template:create', 'BUTTON', NULL, 0, NOW(), NOW()),
('970000000000000312', '970000000000000301', '编辑站内信模板', 'system_messages_notify_template_update', 2, 0, '', NULL, NULL, 0, 'system:notify-template:update', 'BUTTON', NULL, 0, NOW(), NOW()),
('970000000000000313', '970000000000000301', '删除站内信模板', 'system_messages_notify_template_delete', 3, 0, '', NULL, NULL, 0, 'system:notify-template:delete', 'BUTTON', NULL, 0, NOW(), NOW()),
('970000000000000314', '970000000000000301', '测试站内信模板', 'system_messages_notify_template_send', 4, 0, '', NULL, NULL, 0, 'system:notify-template:send', 'BUTTON', NULL, 0, NOW(), NOW()),
('970000000000000302', '970000000000000300', '消息记录', 'system_messages_notify_message', 2, 0, '/system/messages/notify/message', 'ri:chat-history-line', NULL, 1, 'system:notify-message:view', 'MENU', '站内信消息记录', 0, NOW(), NOW()),
('970000000000000400', '970000000000000000', '通知公告', 'system_messages_notice', 4, 0, '/system/messages/notice', 'ri:notification-3-line', NULL, 1, 'system:notice:view', 'MENU', '通知公告', 0, NOW(), NOW()),
('970000000000000401', '970000000000000400', '新增通知', 'system_messages_notice_create', 1, 0, '', NULL, NULL, 0, 'system:notice:create', 'BUTTON', NULL, 0, NOW(), NOW()),
('970000000000000402', '970000000000000400', '编辑通知', 'system_messages_notice_update', 2, 0, '', NULL, NULL, 0, 'system:notice:update', 'BUTTON', NULL, 0, NOW(), NOW()),
('970000000000000403', '970000000000000400', '删除通知', 'system_messages_notice_delete', 3, 0, '', NULL, NULL, 0, 'system:notice:delete', 'BUTTON', NULL, 0, NOW(), NOW()),
('970000000000000404', '970000000000000400', '发布通知', 'system_messages_notice_publish', 4, 0, '', NULL, NULL, 0, 'system:notice:publish', 'BUTTON', NULL, 0, NOW(), NOW()),
('970000000000000405', '970000000000000400', '撤回通知', 'system_messages_notice_revoke', 5, 0, '', NULL, NULL, 0, 'system:notice:revoke', 'BUTTON', NULL, 0, NOW(), NOW());

UPDATE `system_menu`
SET `visible` = 0,
    `deleted` = 1,
    `update_time` = NOW(),
    `remark` = '通知公告已迁移到系统管理-消息中心'
WHERE `id` = '940000000000000000';
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
