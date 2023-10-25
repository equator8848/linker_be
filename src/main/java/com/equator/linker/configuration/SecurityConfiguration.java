package com.equator.linker.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Equator
 * @Date: 2021/11/21 23:36
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "linker.security")
public class SecurityConfiguration {

    /**
     * MD5 盐
     */
    @Deprecated
    private String md5Salt;

    /**
     * DES 对称加密密钥
     */
    private String desKey;

    /***
     * 魔方系统密码盐
     */
    private String mofangMd5Salt;

    /***
     * 魔方系统密码盐
     */
    private String mofangApiToken;

}
