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
@TableName("tb_instance_auto_build_config")
public class TbInstanceAutoBuildConfig extends BaseEntityField {

    /**
     * 项目ID
     */
    @TableField(value = "project_id")
    private Long projectId;

    /**
     * 实例ID
     */
    @TableField(value = "instance_id")
    private Long instanceId;

    /**
     * 是否启用
     */
    @TableField(value = "enabled_switch")
    private Boolean enabledSwitch;

    /**
     * 检测间隔分钟数
     */
    @TableField(value = "check_interval")
    private Integer checkInterval;

    /**
     * 上次检测时间
     */
    @TableField(value = "last_check_timestamp")
    private Long lastCheckTimestamp;

    /**
     * 0，不构建；1，构建
     */
    @TableField(value = "last_check_result")
    private Integer lastCheckResult;

    /**
     * '下次检测时间'
     */
    @TableField(value = "next_check_timestamp")
    private Long nextCheckTimestamp;
}
