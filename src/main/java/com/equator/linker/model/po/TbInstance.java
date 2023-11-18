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
@TableName("tb_instance")
public class TbInstance extends BaseEntityField {

    /**
     * 项目ID
     */
    @TableField(value = "project_id")
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
    @TableField(value = "scm_branch")
    private String scmBranch;

    /**
     * Nginx代理配置
     */
    @TableField(value = "proxy_config")
    private String proxyConfig;

    /**
     * 访问端口
     */
    @TableField(value = "access_port")
    private Integer accessPort;

    /**
     * 完整访问链接
     */
    @TableField(value = "access_link")
    private String accessLink;

    /**
     * 权限等级，1私有、2指定人可访问、4公开
     */
    @TableField(value = "access_level")
    private Integer accessLevel;


    /**
     * 流水线模板ID
     */
    @TableField(value = "pipeline_template_id")
    private String pipelineTemplateId;

    /**
     * 流水线唯一名称
     */
    @TableField(value = "pipeline_name")
    private String pipelineName;

    /**
     * 最近一次构建的ID
     */
    @TableField(value = "latest_build_number")
    private Integer latestBuildNumber;

    /**
     * 最近一次构建提交时间
     */
    @TableField(value = "latest_submit_timestamp")
    private Long latestSubmitTimestamp;

    /**
     * 最近一次构建的URL
     */
    @TableField(value = "latest_build_pipeline_url")
    private String latestBuildPipelineUrl;

    /**
     * 是否正在构建中
     */
    @TableField(value = "building_flag")
    private Boolean buildingFlag;


    /**
     * 最近一次构建的状态
     */
    @TableField(value = "latest_build_result")
    private Integer latestBuildResult;

    /**
     * 最近一次构建耗时
     */
    @TableField(value = "latest_build_duration")
    private Long latestBuildDuration;
}
