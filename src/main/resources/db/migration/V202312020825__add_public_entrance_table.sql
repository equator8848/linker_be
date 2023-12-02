/*
 Navicat Premium Data Transfer

 Source Server         : Local
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : localhost:3306
 Source Schema         : db_linker_swap

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 02/12/2023 08:26:06
*/

SET NAMES utf8mb4;
-- ----------------------------
-- Table structure for tb_public_entrance
-- ----------------------------
CREATE TABLE `tb_public_entrance`  (
                                       `id` bigint UNSIGNED NOT NULL,
                                       `create_time` datetime NOT NULL,
                                       `create_user_id` bigint NOT NULL,
                                       `update_time` datetime NOT NULL,
                                       `update_user_id` bigint NOT NULL,
                                       `del_flag` tinyint NOT NULL DEFAULT 0,
                                       `project_id` bigint NOT NULL COMMENT '项目ID',
                                       `instance_id` bigint NOT NULL COMMENT '实例',
                                       `enabled_switch` tinyint NOT NULL COMMENT '是否启用',
                                       `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '名称',
                                       `intro` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '介绍',
                                       PRIMARY KEY (`id`) USING BTREE,
                                       INDEX `idx_instance_id`(`instance_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '公共入口' ROW_FORMAT = DYNAMIC;