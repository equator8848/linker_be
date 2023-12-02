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
@TableName("tb_public_entrance")
public class TbPublicEntrance extends BaseEntityField {

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
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 介绍
     */
    @TableField(value = "intro")
    private String intro;
}
