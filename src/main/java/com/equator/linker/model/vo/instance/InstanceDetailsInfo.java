package com.equator.linker.model.vo.instance;


import com.equator.linker.model.vo.project.ProxyConfig;
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
public class InstanceDetailsInfo {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String createUserName;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private String updateUserName;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 实例名称
     */
    private String name;

    /**
     * 实例介绍
     */
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


    private String accessLevel;


    private Boolean isOwner;

    /**
     * 访问地址
     */
    private String accessUrl;

    /**
     * 是否构建中
     */
    private Boolean buildingFlag;

    /**
     * 是否已经收藏
     */
    private Boolean stared;

    /**
     * 镜像仓库前缀
     */
    private String imageRepositoryPrefix;


    /**
     * 自定义镜像名称
     */
    private String imageName;

    /**
     * 自定义镜像版本
     */
    private String imageVersion;

    private Integer imageVersionType;

    /**
     * 是否归档镜像
     */
    private Boolean imageArchiveFlag;

    private String imageArchiveUrl;

    private String pipelineTemplateId;

    private String pipelineTemplateIntro;

    private InstancePipelineBuildResult instancePipelineBuildResult;
}
