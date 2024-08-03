package xyz.equator8848.linker.model.vo.instance;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import xyz.equator8848.linker.model.vo.project.ProxyConfig;

import javax.annotation.Nullable;

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
     * SCM Commit
     */
    private String scmCommit;

    /**
     * 打包脚本
     */
    private Boolean packageScriptOverrideFlag;

    private String packageScript;

    /**
     * 打包输出目录
     */
    private Boolean packageOutputDirOverrideFlag;

    private String packageOutputDir;

    /**
     * 项目部署二级路径
     */
    private Boolean deployFolderOverrideFlag;

    private String deployFolder;

    /**
     * 路由模式
     */
    private Boolean routeModeOverrideFlag;

    private Integer routeMode;

    /**
     * 入口相对路径
     */
    private Boolean accessEntranceOverrideFlag;

    private String accessEntrance;


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

    private String imageVersionPrefix;


    /**
     * 自定义镜像版本
     */
    private String imageVersion;

    /**
     * 是否归档镜像
     */
    @NotNull
    private Boolean imageArchiveFlag;

    @Nullable
    private String pipelineTemplateId;

}
