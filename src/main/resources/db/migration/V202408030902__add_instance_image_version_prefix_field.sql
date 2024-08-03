ALTER TABLE `tb_instance`
    ADD COLUMN `image_version_prefix` varchar(255) NULL COMMENT '镜像版本前缀' AFTER `image_version_type`;
