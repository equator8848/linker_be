package xyz.equator8848.linker.service.captcha;


import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.equator8848.inf.core.util.json.JsonUtil;
import xyz.equator8848.inf.security.des.DESUtil;
import xyz.equator8848.inf.security.random.IDUtil;
import xyz.equator8848.linker.configuration.SecurityConfiguration;

import java.awt.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Slf4j
@Component
public class CaptchaGenerator {
    private LoginCodeProperties loginCode;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    /**
     * 验证码生成
     *
     * @return
     */
    public CaptchaVO codeGen() {
        Captcha captcha = getCaptcha();
        String captchaValue = captcha.text();
        // 当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        if (captcha.getCharType() == LoginCodeEnum.ARITHMETIC.ordinal() && captchaValue.contains(".")) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setId(loginCode.getCodeKeyPrefix() + IDUtil.getUUID());
        captchaVO.setImage(captcha.toBase64());

        CaptchaSecret captchaSecret = new CaptchaSecret();
        captchaSecret.setCaptchaId(captchaVO.getId());
        captchaSecret.setCaptchaValue(captchaValue);
        captchaSecret.setExpiredAt(Instant.now().plus(loginCode.getExpiration(), ChronoUnit.MINUTES).toEpochMilli());
        String secret = DESUtil.encrypt(securityConfiguration.getDesKey(), JsonUtil.toJson(captchaSecret));
        log.debug("authCodeGen {}", captchaSecret);
        captchaVO.setSecret(secret);
        return captchaVO;
    }

    /**
     * 获取验证码生产类
     *
     * @return
     */
    private Captcha getCaptcha() {
        if (Objects.isNull(loginCode)) {
            loginCode = new LoginCodeProperties();
            if (Objects.isNull(loginCode.getCodeType())) {
                loginCode.setCodeType(LoginCodeEnum.CHINESE_GIF);
            }

        }
        return switchCaptcha(loginCode);
    }

    /**
     * 依据配置信息生产验证码
     *
     * @param loginCode
     * @return
     */
    private Captcha switchCaptcha(LoginCodeProperties loginCode) {
        Captcha captcha = null;
        synchronized (this) {
            switch (loginCode.getCodeType()) {
                case ARITHMETIC:
                    captcha = new FixedArithmeticCaptcha(loginCode.getWidth(), loginCode.getHeight());
                    captcha.setLen(loginCode.getLength());
                    break;
                case CHINESE:
                    captcha = new ChineseCaptcha(loginCode.getWidth(), loginCode.getHeight());
                    captcha.setLen(loginCode.getLength());
                    break;
                case CHINESE_GIF:
                    captcha = new ChineseGifCaptcha(loginCode.getWidth(), loginCode.getHeight());
                    captcha.setLen(loginCode.getLength());
                    break;
                case GIF:
                    captcha = new GifCaptcha(loginCode.getWidth(), loginCode.getHeight());
                    captcha.setLen(loginCode.getLength());
                    break;
                case SPEC:
                    captcha = new SpecCaptcha(loginCode.getWidth(), loginCode.getHeight());
                    captcha.setLen(loginCode.getLength());
                default:
                    System.out.println("验证码配置信息错误！正确配置查看 LoginCodeEnum ");

            }
        }
        if (StringUtils.isNotBlank(loginCode.getFontName())) {
            captcha.setFont(new Font(loginCode.getFontName(), Font.PLAIN, loginCode.getFontSize()));
        }
        return captcha;
    }

    static class FixedArithmeticCaptcha extends ArithmeticCaptcha {
        public FixedArithmeticCaptcha(int width, int height) {
            super(width, height);
        }

        @Override
        protected char[] alphas() {
            // 生成随机数字和运算符
            int n1 = num(1, 10), n2 = num(1, 10);
            int opt = num(3);

            // 计算结果
            int res = new int[]{n1 + n2, n1 - n2, n1 * n2}[opt];
            // 转换为字符运算符
            char optChar = "+-x".charAt(opt);

            this.setArithmeticString(String.format("%s%c%s=?", n1, optChar, n2));
            this.chars = String.valueOf(res);

            return chars.toCharArray();
        }
    }
}
