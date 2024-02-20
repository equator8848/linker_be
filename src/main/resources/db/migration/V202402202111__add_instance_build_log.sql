CREATE TABLE `tb_instance_build_log` (
                                         `id` bigint unsigned NOT NULL,
                                         `create_time` datetime NOT NULL,
                                         `create_user_id` bigint NOT NULL,
                                         `update_time` datetime NOT NULL,
                                         `update_user_id` bigint NOT NULL,
                                         `del_flag` tinyint NOT NULL DEFAULT '0',
                                         `project_id` bigint NOT NULL,
                                         `instance_id` bigint NOT NULL,
                                         `build_user_id` bigint NOT NULL,
                                         `remark` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
                                         PRIMARY KEY (`id`),
                                         KEY `idx_instance_id` (`instance_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='实例构建历史';