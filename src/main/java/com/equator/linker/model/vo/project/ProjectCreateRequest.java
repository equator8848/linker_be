package com.equator.linker.model.vo.project;


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
public class ProjectCreateRequest {

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目介绍
     */
    private String intro;

    /**
     * SCM配置
     */
    private ScmConfig scmConfig;

    /**
     * Nginx代理配置
     */
    private ProxyConfig proxyConfig;

    /**
     * 打包镜像
     */
    private String packageImage;

    /**
     * 打包脚本
     */
    private String packageScript;

    /**
     * 打包输出目录
     */
    private String packageOutputDir;

    /**
     * 入口相对路径
     */
    private String accessEntrance;

    /**
     * 权限等级，1私有、2指定人可访问、4公开
     */
    private String accessLevel;

}
