ALTER TABLE `tb_user`
    ADD COLUMN `need_update_user_name` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否需要修改用户名' AFTER `role_type`;