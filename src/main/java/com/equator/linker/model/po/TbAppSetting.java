package com.equator.linker.model.po;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 服务器配置
 * </p>
 *
 * @author equator
 * @since 2022-09-18
 */
@Data
@TableName("tb_inf_app_setting")
public class TbAppSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     *
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     *
     */
    @TableField(value = "setting_key")
    private String settingKey;

    /**
     *
     */
    @TableField(value = "setting_value")
    private String settingValue;

    /**
     * 备注
     */
    private String remark;

}
