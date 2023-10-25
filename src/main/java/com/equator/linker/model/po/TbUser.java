package com.equator.linker.model.po;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author equator
 * @since 2022-09-18
 */
@TableName("tb_user")
@Data
public class TbUser implements CommonUser {
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
    private Short status;

    @TableField("user_type")
    private Short userType;

    /**
     *
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     *
     */
    @TableField(value = "user_password")
    private String userPassword;

    /**
     *
     */
    private String email;

    /**
     *
     */
    @TableField(value = "phone_number")
    private String phoneNumber;

    /**
     *
     */
    @TableField(value = "phone_number_prefix")
    private String phoneNumberPrefix;

    @TableField("relation_id")
    private Integer relationId;

    @TableField("wx_open_id")
    private String wxOpenId;

    @TableField("email_notice_switch")
    private Boolean emailNoticeSwitch;

    @TableField("wx_notice_switch")
    private Boolean wxNoticeSwitch;

}
