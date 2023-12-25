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

 Date: 28/11/2023 21:03:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_project_template
-- ----------------------------
CREATE TABLE `tb_project_template`  (
  `id` bigint UNSIGNED NOT NULL,
  `create_time` datetime NOT NULL,
  `create_user_id` bigint NOT NULL,
  `update_time` datetime NOT NULL,
  `update_user_id` bigint NOT NULL,
  `del_flag` tinyint NOT NULL DEFAULT 0,
  `template_version_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '版本ID',
  `intro` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '模板说明',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '项目流水线模板' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
