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

 Date: 25/11/2023 11:25:48
*/

SET NAMES utf8mb4;

CREATE TABLE `tb_instance_star`
(
    `id`             bigint UNSIGNED NOT NULL AUTO_INCREMENT,
    `create_time`    datetime NOT NULL,
    `create_user_id` bigint   NOT NULL,
    `update_time`    datetime NOT NULL,
    `update_user_id` bigint   NOT NULL,
    `del_flag`       tinyint  NOT NULL DEFAULT 0,
    `project_id`     bigint   NOT NULL,
    `star_user_id`     bigint   NOT NULL,
    `instance_id`    bigint   NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX            `idx_project__user_id`(`project_id` ASC, `star_user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '实例收藏' ROW_FORMAT = DYNAMIC;

