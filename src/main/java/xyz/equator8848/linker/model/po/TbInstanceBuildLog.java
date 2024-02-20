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
@TableName("tb_instance_build_log")
public class TbInstanceBuildLog extends BaseEntityField {
    @TableField(value = "project_id")
    private Long projectId;

    @TableField(value = "instance_id")
    private Long instanceId;

    @TableField(value = "build_user_id")
    private Long buildUserId;

    @TableField(value = "remark")
    private String remark;
}
