package com.equator.linker.model.vo.user;

import lombok.Data;

/**
 * @Author: Equator
 * @Date: 2022/9/18 11:50
 **/
@Data
public class UserInfoVO {
    private Integer id;

    private String userName;

    private String phoneNumberPrefix;

    private String phoneNumber;

    private String email;

    private Short status;

    private Short userSystemType;

    private Integer relationId;
}
