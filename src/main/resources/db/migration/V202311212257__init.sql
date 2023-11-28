/*
 Navicat Premium Data Transfer

 Source Server         : Local
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : localhost:3306
 Source Schema         : db_linker

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 21/11/2023 22:58:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_inf_app_setting
-- ----------------------------
CREATE TABLE `tb_inf_app_setting`  (
                                     `id` bigint UNSIGNED NOT NULL,
                                     `create_time` datetime NOT NULL,
                                     `create_user_id` bigint NOT NULL,
                                     `update_time` datetime NOT NULL,
                                     `update_user_id` bigint NOT NULL,
                                     `del_flag` tinyint NOT NULL DEFAULT 0,
                                     `setting_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                     `setting_value` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                     `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     INDEX `idx_key`(`setting_key` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_inf_login_log
-- ----------------------------
CREATE TABLE `tb_inf_login_log`  (
                                   `id` bigint UNSIGNED NOT NULL,
                                   `create_time` datetime NOT NULL,
                                   `create_user_id` bigint NOT NULL,
                                   `update_time` datetime NOT NULL,
                                   `update_user_id` bigint NOT NULL,
                                   `del_flag` tinyint NOT NULL DEFAULT 0,
                                   `login_user_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户登录账号',
                                   `login_status` smallint NOT NULL COMMENT '登录状态',
                                   `remote_address` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录IP地址',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_instance
-- ----------------------------
CREATE TABLE `tb_instance`  (
                              `id` bigint UNSIGNED NOT NULL,
                              `create_time` datetime NOT NULL,
                              `create_user_id` bigint NOT NULL,
                              `update_time` datetime NOT NULL,
                              `update_user_id` bigint NOT NULL,
                              `del_flag` tinyint NOT NULL DEFAULT 0,
                              `project_id` bigint NOT NULL COMMENT '项目ID',
                              `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '实例名称',
                              `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '实例介绍',
                              `scm_branch` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'SCM分支',
                              `proxy_config` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Nginx代理配置',
                              `access_port` int NOT NULL COMMENT '访问端口',
                              `access_link` varchar(768) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '完整访问链接',
                              `access_level` smallint NOT NULL COMMENT '权限等级，1私有、2指定人可访问、4公开',
                              `pipeline_template_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流水线模板ID',
                              `pipeline_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流水线名称',
                              `latest_build_number` int NULL DEFAULT NULL COMMENT '最近一次构建的ID',
                              `latest_submit_timestamp` bigint NULL DEFAULT NULL COMMENT '最近一次构建提交时间',
                              `latest_build_pipeline_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最近一次构建的URL',
                              `building_flag` tinyint(1) NULL DEFAULT NULL COMMENT '是否正在构建中',
                              `latest_build_result` int NULL DEFAULT NULL COMMENT '最近一次构建的状态',
                              `latest_build_duration` bigint NULL DEFAULT NULL COMMENT '最近一次构建耗时',
                              PRIMARY KEY (`id`) USING BTREE,
                              INDEX `idx_project_id`(`project_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_instance_user_ref
-- ----------------------------
CREATE TABLE `tb_instance_user_ref`  (
                                       `id` bigint UNSIGNED NOT NULL,
                                       `create_time` datetime NOT NULL,
                                       `create_user_id` bigint NOT NULL,
                                       `update_time` datetime NOT NULL,
                                       `update_user_id` bigint NOT NULL,
                                       `del_flag` tinyint NOT NULL DEFAULT 0,
                                       `instance_id` bigint NOT NULL,
                                       `user_id` bigint NOT NULL,
                                       `ref_type` smallint NOT NULL COMMENT '关联类型，1 自己创建、2 加入',
                                       PRIMARY KEY (`id`) USING BTREE,
                                       INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_project
-- ----------------------------
CREATE TABLE `tb_project`  (
                             `id` bigint UNSIGNED NOT NULL,
                             `create_time` datetime NOT NULL,
                             `create_user_id` bigint NOT NULL,
                             `update_time` datetime NOT NULL,
                             `update_user_id` bigint NOT NULL,
                             `del_flag` tinyint NOT NULL DEFAULT 0,
                             `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目名称',
                             `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目介绍',
                             `scm_config` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'SCM配置',
                             `proxy_config` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Nginx代理配置',
                             `package_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '打包镜像',
                             `package_script` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '打包脚本',
                             `package_output_dir` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '打包输出目录',
                             `access_entrance` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '入口相对路径',
                             `access_level` smallint NOT NULL COMMENT '权限等级，1私有、2指定人可访问、4公开',
                             PRIMARY KEY (`id`) USING BTREE,
                             INDEX `idx_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_project_user_ref
-- ----------------------------
CREATE TABLE `tb_project_user_ref`  (
                                      `id` bigint UNSIGNED NOT NULL,
                                      `create_time` datetime NOT NULL,
                                      `create_user_id` bigint NOT NULL,
                                      `update_time` datetime NOT NULL,
                                      `update_user_id` bigint NOT NULL,
                                      `del_flag` tinyint NOT NULL DEFAULT 0,
                                      `project_id` bigint NOT NULL,
                                      `user_id` bigint NOT NULL,
                                      `ref_type` smallint NOT NULL COMMENT '关联类型，1 自己创建、2 加入',
                                      PRIMARY KEY (`id`) USING BTREE,
                                      INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
CREATE TABLE `tb_user`  (
                          `id` bigint UNSIGNED NOT NULL,
                          `create_time` datetime NOT NULL,
                          `create_user_id` bigint NOT NULL,
                          `update_time` datetime NOT NULL,
                          `update_user_id` bigint NOT NULL,
                          `del_flag` tinyint NOT NULL DEFAULT 0,
                          `status` smallint NOT NULL,
                          `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                          `user_password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                          `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                          `phone_number` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                          `wx_open_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信ID',
                          `email_notice_switch` tinyint(1) NULL DEFAULT NULL COMMENT '邮箱通知开关',
                          `wx_notice_switch` tinyint(1) NULL DEFAULT NULL COMMENT '微信通知开关',
                          `role_type` smallint NOT NULL DEFAULT 1 COMMENT '角色类型',
                          PRIMARY KEY (`id`) USING BTREE,
                          INDEX `idx_phonenumber`(`phone_number` ASC) USING BTREE,
                          INDEX `idx_email`(`email` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;