package com.equator.linker.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class BaseEntityField {
    /**
     *
     */
    private Long id;

    /**
     *
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     *
     */
    @TableField(value = "create_user_id")
    private Long createUserId;

    /**
     *
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     *
     */
    @TableField(value = "update_user_id")
    private Long updateUserId;

    /**
     *
     */
    @TableField(value = "del_flag")
    private Integer delFlag;
}
