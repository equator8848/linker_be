package com.equator.linker.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author equator
 * @since 2022-11-08
 */
@Data
@TableName("tb_inf_login_log")
public class TbLoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private Short status;

    /**
     * 用户ID
     */
    @TableField(value = "user_key")
    private String userKey;

    /**
     * 登录状态
     */
    @TableField(value = "login_status")
    private Integer loginStatus;

    /**
     * IP地址
     */
    @TableField(value = "remote_address")
    private String remoteAddress;

}
