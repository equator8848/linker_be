package xyz.equator8848.linker.model.po;


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
    @TableField(value = "name")
    private String name;

    /**
     * 实例介绍
     */
    @TableField(value = "intro")
    private String intro;

    /**
     * SCM分支
     */
    @TableField(value = "scm_branch")
    private String scmBranch;

    /**
     * SCM Commit
     */
    @TableField(value = "scm_commit")
    private String scmCommit;

    /**
     * 打包脚本
     */
    @TableField(value = "package_script_override_flag")
    private Boolean packageScriptOverrideFlag;

    @TableField(value = "package_script")
    private String packageScript;

    /**
     * 打包输出目录
     */
    @TableField(value = "package_output_dir_override_flag")
    private Boolean packageOutputDirOverrideFlag;

    @TableField(value = "package_output_dir")
    private String packageOutputDir;

    /**
     * 项目部署二级路径
     */
    @TableField(value = "deploy_folder_override_flag")
    private Boolean deployFolderOverrideFlag;

    @TableField(value = "deploy_folder")
    private String deployFolder;

    /**
     * 路由模式
     */
    @TableField(value = "route_mode_override_flag")
    private Boolean routeModeOverrideFlag;

    @TableField(value = "route_mode")
    private Integer routeMode;

    /**
     * 入口相对路径
     */
    @TableField(value = "access_entrance_override_flag")
    private Boolean accessEntranceOverrideFlag;

    @TableField(value = "access_entrance")
    private String accessEntrance;

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
     * 镜像仓库前缀
     */
    @TableField(value = "image_repository_prefix")
    private String imageRepositoryPrefix;

    /**
     * 自定义镜像名称
     */
    @TableField(value = "image_name")
    private String imageName;


    /**
     * 镜像版本生成类型
     */
    @TableField(value = "image_version_type")
    private Integer imageVersionType;

    /**
     * 镜像版本生成类型
     */
    @TableField(value = "image_version_prefix")
    private String imageVersionPrefix;

    /**
     * 自定义镜像版本
     */
    @TableField(value = "image_version")
    private String imageVersion;

    /**
     * 是否归档镜像
     */
    @TableField("image_archive_flag")
    private Boolean imageArchiveFlag;


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


    /**
     * 最近一次构建的commit
     */
    @TableField(value = "last_build_commit")
    private String lastBuildCommit;
}
