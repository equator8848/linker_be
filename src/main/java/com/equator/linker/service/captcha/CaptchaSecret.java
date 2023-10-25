package com.equator.linker.service.captcha;

import lombok.Data;

@Data
public class CaptchaSecret {
    private String captchaId;

    private String captchaValue;

    /**
     * 过期时间（MS）
     */
    private Long expiredAt;
}
