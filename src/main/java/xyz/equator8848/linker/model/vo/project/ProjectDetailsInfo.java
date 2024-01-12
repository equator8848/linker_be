package xyz.equator8848.linker.model.vo.project;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author equator
 * @since 2023-11-05
 */
@Data
public class ProjectDetailsInfo {
    private Long id;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String createUserName;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private String updateUserName;

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
     * 二级部署路径
     */
    private String deployFolder;

    /**
     * 路由模式
     */
    private Integer routeMode;

    /**
     * 入口相对路径
     */
    private String accessEntrance;

    /**
     * 权限等级，1私有、2指定人可访问、4公开
     */
    private String accessLevel;

    private String accessLevelCn;

    private String pipelineTemplateId;

    private Boolean isOwner;
}
