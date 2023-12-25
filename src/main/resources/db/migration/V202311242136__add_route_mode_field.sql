ALTER TABLE `tb_project`
    ADD COLUMN `route_mode` tinyint(1) NULL COMMENT '前端路由模式，0 hash、2 history' AFTER `deploy_folder`;