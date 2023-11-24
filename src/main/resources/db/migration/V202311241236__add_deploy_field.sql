ALTER TABLE `tb_project`
    ADD COLUMN `deploy_folder` varchar(255) NULL COMMENT '项目部署路径' AFTER `package_output_dir`;