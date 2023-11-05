package com.equator.linker.model.vo.user;

import lombok.Data;

/**
 * @Author: Equator
 * @Date: 2022/9/18 11:50
 **/
@Data
public class UserInfoVO {
    private Long id;

    private String userName;

    private String phoneNumber;

    private String email;

    private Short status;
}
