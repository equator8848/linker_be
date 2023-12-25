ALTER TABLE `tb_instance`
ADD COLUMN `package_output_dir_override_flag` tinyint(1) NULL COMMENT '是否自定义打包输出目录' AFTER `package_script`,
ADD COLUMN `package_output_dir` varchar(255) NULL COMMENT '打包输出目录' AFTER `package_output_dir_override_flag`,
ADD COLUMN `deploy_folder_override_flag` tinyint(1) NULL COMMENT '是否自定义项目部署路径' AFTER `package_output_dir`,
ADD COLUMN `deploy_folder` varchar(255) NULL COMMENT '项目部署路径' AFTER `deploy_folder_override_flag`,
ADD COLUMN `access_entrance_override_flag` tinyint(1) NULL COMMENT '是否自定义入口相对路径' AFTER `deploy_folder`,
ADD COLUMN `access_entrance` varchar(512) NULL COMMENT '入口相对路径' AFTER `access_entrance_override_flag`;