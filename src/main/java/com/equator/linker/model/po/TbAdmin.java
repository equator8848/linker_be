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
 * @since 2022-10-23
 */
@Data
@TableName("tb_admin")
public class TbAdmin implements CommonUser {

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
     *
     */
    private String nickname;

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
     * 关联的外部ID
     */
    @TableField("relation_ids")
    private String relationIds;

    /**
     * 角色类型
     */
    @TableField("role_type")
    private Integer roleType;
}
