ALTER TABLE `tb_instance`
    ADD COLUMN `package_script_override_flag` tinyint(1) NULL COMMENT '是否自定义打包脚本' AFTER `scm_branch`,
ADD COLUMN `package_script` varchar(1024) NULL COMMENT '自定义打包脚本' AFTER `package_script_override_flag`;