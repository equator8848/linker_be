package com.equator.linker.model.vo.user;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * @Author: Equator
 * @Date: 2022/9/18 11:50
 **/
@Data
public class UserLoginDataVO {
    /**
     * phone、email、username
     */
    @NotNull
    private String userIdentificationType;
    /**
     * 用户手机或者邮箱
     */
    @NotNull
    private String userIdentification;

    @NotNull
    private String userPassword;

    @NotNull
    private String captchaId;

    @NotNull
    private String captchaValue;

    @NotNull
    private String captchaSecret;


    private String remoteAddress;
}
