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
@TableName("tb_instance_star")
public class TbInstanceStar extends BaseEntityField {
    @TableField(value = "project_id")
    private Long projectId;

    @TableField(value = "star_user_id")
    private Long starUserId;

    @TableField(value = "instance_id")
    private Long instanceId;
}
