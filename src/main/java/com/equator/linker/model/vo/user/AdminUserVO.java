package com.equator.linker.model.vo.user;


import com.equator.linker.model.po.CommonUser;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author equator
 * @since 2022-10-23
 */
@Data
public class AdminUserVO implements CommonUser {
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
    private String userName;

    /**
     *
     */
    private String email;


    /**
     * 关联的外部ID
     */
    private List<Integer> relationIds;

    /**
     * 角色类型
     */
    private Integer roleType;
}
