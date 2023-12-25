package xyz.equator8848.linker.service.captcha;

import lombok.Data;

import static xyz.equator8848.linker.service.captcha.LoginCodeEnum.GIF;


@Data
public class LoginCodeProperties {
    /**
     * 验证码配置
     */
    private LoginCodeEnum codeType = GIF;
    /**
     * 验证码有效期 分钟
     */
    private Long expiration = 5L;
    /**
     * 验证码内容长度
     */
    private int length = 4;
    /**
     * 验证码宽度
     */
    private int width = 111;
    /**
     * 验证码高度
     */
    private int height = 36;
    /**
     * 验证码字体
     */
    private String fontName;
    /**
     * 字体大小
     */
    private int fontSize = 25;

    /**
     * 验证码前缀
     *
     * @return
     */
    private String codeKeyPrefix = "AkPaUof313IW";

}
