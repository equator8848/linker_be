package xyz.equator8848.linker.service.user;


import xyz.equator8848.linker.dao.service.LoginLogDaoService;
import xyz.equator8848.linker.dao.service.UserDaoService;
import xyz.equator8848.linker.model.constant.BaseConstant;
import xyz.equator8848.linker.model.constant.ModelStatus;
import xyz.equator8848.linker.model.constant.RoleType;
import xyz.equator8848.linker.model.dto.DynamicAppConfiguration;
import xyz.equator8848.linker.model.po.TbUser;
import xyz.equator8848.linker.model.vo.user.UserLoginDataVO;
import xyz.equator8848.inf.core.model.exception.InnerException;
import xyz.equator8848.inf.core.model.exception.PreCondition;
import xyz.equator8848.inf.core.model.exception.VerifyException;
import xyz.equator8848.inf.core.util.json.JsonUtil;
import xyz.equator8848.inf.core.util.security.DESUtil;
import xyz.equator8848.inf.core.util.security.IDUtil;
import xyz.equator8848.inf.core.util.security.PasswordUtil;
import xyz.equator8848.linker.configuration.AppConfig;
import xyz.equator8848.linker.configuration.SecurityConfiguration;
import xyz.equator8848.linker.model.vo.LoginUser;
import xyz.equator8848.linker.service.captcha.CaptchaSecret;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Component
public class LoginService implements InitializingBean {
    private final List<LoginHandler> loginHandlers = new LinkedList<>();

    @Autowired
    private PasswordLoginHandler passwordLoginHandler;

    @Autowired
    private LoginLogDaoService loginLogDaoService;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private UserDaoService userDaoService;

    public Pair<LoginUser, TbUser> login(UserLoginDataVO userLoginVO) {
        captchaVerify(userLoginVO);
        Set<Integer> loginStatusSet = new HashSet<>();
        for (LoginHandler loginHandler : loginHandlers) {
            Triple<LoginStatus, LoginUser, TbUser> loginTriple = loginHandler.login(userLoginVO);
            LoginUser loginUser = loginTriple.getMiddle();
            if (loginUser != null) {
                loginLogDaoService.appendLoginLog(String.format("%s##%s", loginHandler.loginHandlerType(), userLoginVO.getUserIdentification()),
                        LoginStatus.SUCCESS, userLoginVO.getRemoteAddress());
                return Pair.of(loginUser, loginTriple.getRight());
            }
            loginStatusSet.add(loginTriple.getLeft().ordinal());
        }
        if (loginStatusSet.contains(LoginStatus.NEED_SET_PASSWORD.ordinal())) {
            loginLogDaoService.appendLoginLog(userLoginVO.getUserIdentification(), LoginStatus.NEED_SET_PASSWORD, userLoginVO.getRemoteAddress());
            throw new VerifyException(LoginStatus.NEED_SET_PASSWORD.getMessage());
        }
        if (loginStatusSet.contains(LoginStatus.PASSWORD_NOT_MATCH.ordinal())) {
            loginLogDaoService.appendLoginLog(userLoginVO.getUserIdentification(), LoginStatus.PASSWORD_NOT_MATCH, userLoginVO.getRemoteAddress());
            throw new VerifyException(LoginStatus.PASSWORD_NOT_MATCH.getMessage());
        }
        if (loginStatusSet.contains(LoginStatus.FORBIDDEN.ordinal())) {
            loginLogDaoService.appendLoginLog(userLoginVO.getUserIdentification(), LoginStatus.FORBIDDEN, userLoginVO.getRemoteAddress());
            throw new VerifyException(LoginStatus.FORBIDDEN.getMessage());
        }
        if (loginStatusSet.size() == 1 && loginStatusSet.contains(LoginStatus.USER_NOT_FOUND.ordinal())) {
            DynamicAppConfiguration config = appConfig.getConfig();
            if (config.getAllowRegister()) {
                TbUser tbUser = registerUser(userLoginVO);
                LoginUser loginUser = new LoginUser();
                loginUser.setUid(tbUser.getId());
                loginUser.setNickName(tbUser.getUserName());
                loginUser.setUserName(tbUser.getUserName());
                loginUser.setEmail(tbUser.getEmail());
                loginUser.setRoleType(tbUser.getRoleType());
                return Pair.of(loginUser, tbUser);
            }
            loginLogDaoService.appendLoginLog(userLoginVO.getUserIdentification(), LoginStatus.USER_NOT_FOUND, userLoginVO.getRemoteAddress());
            throw new VerifyException(LoginStatus.USER_NOT_FOUND.getMessage());
        }
        throw new InnerException("找不到合适的登录处理器");
    }

    private TbUser registerUser(UserLoginDataVO userLoginVO) {
        TbUser tbUser = new TbUser();
        tbUser.setUserName("用户" + IDUtil.getRandomChar(4));
        tbUser.setUserPassword(PasswordUtil.generateSha512CryptPassword(userLoginVO.getUserPassword()));
        if (BaseConstant.UserIdentificationType.PHONE.equals(userLoginVO.getUserIdentificationType())) {
            tbUser.setPhoneNumber(userLoginVO.getUserIdentification());
        } else if (BaseConstant.UserIdentificationType.EMAIL.equals(userLoginVO.getUserIdentificationType())) {
            tbUser.setEmail(userLoginVO.getUserIdentification());
        } else {
            throw new VerifyException("不合法的登录类型");
        }
        long userCount = userDaoService.count();
        if (userCount == 0) {
            // 第一个注册的用户，视为超级管理员
            tbUser.setRoleType(RoleType.SUPER_ADMIN);
        } else {
            tbUser.setRoleType(RoleType.USER);
        }
        tbUser.setStatus(ModelStatus.UserStatus.NORMAL);
        userDaoService.save(tbUser);
        return tbUser;
    }

    /**
     * 验证码验证
     *
     * @param userLoginVO
     */
    public void captchaVerify(UserLoginDataVO userLoginVO) {
        captchaVerify(userLoginVO.getCaptchaId(), userLoginVO.getCaptchaValue(), userLoginVO.getCaptchaSecret());
    }

    /**
     * @param captchaId
     * @param captchaValue
     * @param captchaSecretInput
     */
    public void captchaVerify(String captchaId, String captchaValue, String captchaSecretInput) {
        String decryptCaptchaSecret = DESUtil.decrypt(securityConfiguration.getDesKey(), captchaSecretInput);
        CaptchaSecret captchaSecret = JsonUtil.fromJson(decryptCaptchaSecret, CaptchaSecret.class);
        if (Instant.now().isAfter(Instant.ofEpochMilli(captchaSecret.getExpiredAt()))) {
            throw new VerifyException("验证码已失效");
        }
        PreCondition.isEquals(captchaId, captchaSecret.getCaptchaId(), "验证码ID不匹配");
        PreCondition.isEquals(captchaValue.toUpperCase(), captchaSecret.getCaptchaValue().toUpperCase(), "验证码错误");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        loginHandlers.add(passwordLoginHandler);
    }
}
