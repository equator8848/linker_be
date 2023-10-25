package com.equator.linker.service.captcha;

import lombok.Data;

@Data
public class CaptchaVO {
    private String id;

    /**
     * base64编码
     */
    private String image;

    /**
     * 对称加密后的答案信息
     */
    private String secret;
}
