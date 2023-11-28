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

ALTER TABLE `tb_project`
  ADD COLUMN `pipeline_template_id` varchar(64) NOT NULL COMMENT '流水线模板ID' AFTER `access_level`;

SET FOREIGN_KEY_CHECKS = 1;
