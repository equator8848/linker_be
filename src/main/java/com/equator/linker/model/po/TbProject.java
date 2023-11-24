package com.equator.linker.model.po;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author equator
 * @since 2023-11-05
 */
@Data
@TableName("tb_project")
public class TbProject extends BaseEntityField {

    /**
     * 项目名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 项目介绍
     */
    @TableField(value = "intro")
    private String intro;

    /**
     * SCM配置
     */
    @TableField(value = "scm_config")
    private String scmConfig;

    /**
     * Nginx代理配置
     */
    @TableField(value = "proxy_config")
    private String proxyConfig;

    /**
     * 打包镜像
     */
    @TableField(value = "package_image")
    private String packageImage;

    /**
     * 打包脚本
     */
    @TableField(value = "package_script")
    private String packageScript;

    /**
     * 打包输出目录
     */
    @TableField(value = "package_output_dir")
    private String packageOutputDir;

    /**
     * 项目部署二级路径
     */
    @TableField(value = "deploy_folder")
    private String deployFolder;

    /**
     * 入口相对路径
     */
    @TableField(value = "access_entrance")
    private String accessEntrance;

    /**
     * 权限等级，1私有、2指定人可访问、4公开
     */
    @TableField(value = "access_level")
    private Integer accessLevel;

}
