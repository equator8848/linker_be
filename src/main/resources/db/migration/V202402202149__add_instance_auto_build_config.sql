CREATE TABLE tb_instance_auto_build_config
(
    id                   bigint unsigned NOT NULL,
    create_time          datetime          NOT NULL,
    create_user_id       bigint            NOT NULL,
    update_time          datetime          NOT NULL,
    update_user_id       bigint            NOT NULL,
    del_flag             tinyint DEFAULT 0 NOT NULL,
    project_id           bigint            NOT NULL COMMENT '项目ID',
    instance_id          bigint            NOT NULL COMMENT '实例',
    enabled_switch       tinyint           NOT NULL COMMENT '是否启用',
    check_interval       SMALLINT          NOT NULL COMMENT '检测间隔分钟数',
    last_check_timestamp BIGINT            NOT NULL COMMENT '上次检测时间',
    last_check_result    SMALLINT          NOT NULL COMMENT '0，不构建；1，构建',
    next_check_timestamp BIGINT            NOT NULL COMMENT '下次检测时间',
    CONSTRAINT `PRIMARY` PRIMARY KEY (id),
    UNIQUE KEY `idx_instance_id` (`instance_id`) USING BTREE,
    INDEX                `idx_activate` (`enabled_switch`, `next_check_timestamp`) USING BTREE
) ENGINE=InnoDB
CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci
COMMENT='实例自动构建配置';
