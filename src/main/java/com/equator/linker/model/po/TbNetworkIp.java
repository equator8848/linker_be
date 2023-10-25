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
 * @since 2022-09-28
 */
@Data
@TableName("tb_network_ip")
public class TbNetworkIp implements Serializable {

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
    private Short status;

    /**
     *
     */
    @TableField(value = "ipv4_address")
    private String ipv4Address;

    /**
     *
     */
    @TableField(value = "ipv4_gateway")
    private String ipv4Gateway;

    /**
     *
     */
    @TableField(value = "instance_id")
    private Integer instanceId;


    @TableField("access_domain")
    private String accessDomain;

}
