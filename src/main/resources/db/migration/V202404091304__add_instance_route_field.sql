ALTER TABLE `tb_instance`
    ADD COLUMN `route_mode_override_flag` tinyint(1) NULL COMMENT '是否覆盖项目路由模式配置' AFTER `deploy_folder_override_flag`;
ALTER TABLE `tb_instance`
    ADD COLUMN `route_mode` tinyint(1) NULL COMMENT '前端路由模式，0 hash、2 history' AFTER `route_mode_override_flag`;