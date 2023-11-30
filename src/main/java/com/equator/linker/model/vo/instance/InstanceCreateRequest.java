package com.equator.linker.model.vo.instance;


import com.equator.linker.model.vo.project.ProxyConfig;
import jakarta.validation.constraints.NotNull;
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
public class InstanceCreateRequest {

    /**
     * 项目ID
     */
    @NotNull
    private Long projectId;

    /**
     * 实例名称
     */
    @NotNull
    private String name;

    /**
     * 实例介绍
     */
    @NotNull
    private String intro;

    /**
     * SCM分支
     */
    private String scmBranch;

    /**
     * 是否自定义打包脚本
     */
    private Boolean packageScriptOverrideFlag;

    /**
     * 打包脚本
     */
    private String packageScript;

    /**
     * Nginx代理配置
     */
    private ProxyConfig proxyConfig;


    @NotNull
    private String accessLevel;

    /**
     * 镜像仓库前缀
     */
    private String imageRepositoryPrefix;

    /**
     * 自定义镜像名称
     */
    private String imageName;

    private Integer imageVersionType;


    /**
     * 自定义镜像版本
     */
    private String imageVersion;

    /**
     * 是否归档镜像
     */
    @NotNull
    private Boolean imageArchiveFlag;

}
