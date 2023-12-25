ALTER TABLE `tb_instance`
    ADD COLUMN `image_version_type` int NOT NULL DEFAULT 0 COMMENT '镜像版本生成类型' AFTER `image_name`;