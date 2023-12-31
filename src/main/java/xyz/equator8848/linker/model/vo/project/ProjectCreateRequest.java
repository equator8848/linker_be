package xyz.equator8848.linker.model.vo.project;


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
public class ProjectCreateRequest {

    /**
     * 项目名称
     */
    @NotNull
    private String name;

    /**
     * 项目介绍
     */
    private String intro;

    /**
     * SCM配置
     */
    @NotNull
    private ScmConfig scmConfig;

    /**
     * Nginx代理配置
     */
    @NotNull
    private ProxyConfig proxyConfig;

    /**
     * 打包镜像
     */
    @NotNull
    private String packageImage;

    /**
     * 打包脚本
     */
    @NotNull
    private String packageScript;

    /**
     * 打包输出目录
     */
    @NotNull
    private String packageOutputDir;

    /**
     * 二级部署路径
     */
    private String deployFolder;

    private Integer routeMode;

    /**
     * 入口相对路径
     */
    @NotNull
    private String accessEntrance;

    /**
     * 权限等级，1私有、2指定人可访问、4公开
     */
    @NotNull
    private String accessLevel;

    @NotNull
    private String pipelineTemplateId;

}
