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

ALTER TABLE `tb_instance`
    ADD COLUMN `image_repository_prefix` varchar(255) NULL COMMENT '镜像仓库前缀' AFTER `access_level`,
ADD COLUMN `image_name` varchar(255) NULL COMMENT '自定义镜像名称' AFTER `image_repository_prefix`,
ADD COLUMN `image_version` varchar(255) NULL COMMENT '自定义镜像版本' AFTER `image_name`,
ADD COLUMN `image_archive_flag` tinyint NOT NULL DEFAULT 0 COMMENT '是否归档镜像'  AFTER `image_version`;

