package com.equator.linker.model.vo.login;

import com.equator.linker.model.vo.user.UserLoginResponse;
import lombok.Data;

/**
 * 预登录
 *
 * @Author: Equator
 * @Date: 2022/9/18 13:30
 **/
@Data
public class PreUserLoginSecret {
    private String authCodeId;

    private String authCode;

    /**
     * 过期时间（MS）
     */
    private Long expiredAt;

    private UserLoginResponse userLoginResponse;
}
