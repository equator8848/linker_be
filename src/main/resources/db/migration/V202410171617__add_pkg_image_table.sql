-- db_linker.tb_package_image definition

CREATE TABLE `tb_package_image` (
                                    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
                                    `create_time` datetime NOT NULL,
                                    `create_user_id` bigint NOT NULL,
                                    `update_time` datetime NOT NULL,
                                    `update_user_id` bigint NOT NULL,
                                    `del_flag` tinyint NOT NULL DEFAULT '0',
                                    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '镜像名称',
                                    `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '镜像介绍',
                                    `image_path` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '镜像路径',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    KEY `idx_name` (`name`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='打包镜像';

INSERT INTO tb_package_image
(id, create_time, create_user_id, update_time, update_user_id, del_flag, name, intro, image_path)
VALUES(1, '2024-10-17 17:31:30', 0, '2024-10-17 17:31:30', 0, 0, 'node 16.13.1 + pnpm 7.5.1', '【系统预置】node 16 内置 pnpm7', 'lsage/pnpm-circleci-node:16.13.1-pnpm7.5.1');