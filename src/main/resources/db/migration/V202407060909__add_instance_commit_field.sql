ALTER TABLE `tb_instance`
    ADD COLUMN `scm_commit` varchar(255) NOT NULL DEFAULT 'HEAD' COMMENT 'SCM commit，默认为HEAD' AFTER `scm_branch`;