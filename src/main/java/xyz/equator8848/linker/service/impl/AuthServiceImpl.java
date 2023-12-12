package xyz.equator8848.linker.service.impl;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.equator8848.inf.auth.model.bo.LoginUser;
import xyz.equator8848.inf.auth.util.UserAuthUtil;
import xyz.equator8848.linker.model.po.TbUser;
import xyz.equator8848.linker.model.vo.user.UserLoginDataVO;
import xyz.equator8848.linker.model.vo.user.UserLoginResponse;
import xyz.equator8848.linker.service.AuthService;
import xyz.equator8848.linker.service.user.LoginService;
import xyz.equator8848.linker.util.IpNetUtil;

import java.util.Date;


/**
 * @Author: Equator
 * @Date: 2021/2/10 10:32
 **/
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private LoginService loginService;

    @Autowired
    private UserAuthUtil userAuthUtil;

    @Override
    public UserLoginResponse login(UserLoginDataVO userLoginVO, HttpServletRequest request) {
        userLoginVO.setRemoteAddress(IpNetUtil.getRealIp(request));
        log.info("user {} try login at IP {}", userLoginVO.getUserIdentification(), userLoginVO.getRemoteAddress());
        Pair<LoginUser, TbUser> userPair = loginService.login(userLoginVO);
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        LoginUser loginUser = userPair.getKey();
        userLoginResponse.setLoginUser(loginUser);
        Pair<String, Date> tokenWithExpiredTime = userAuthUtil.buildTokenWithExpiredTime(loginUser);
        userLoginResponse.setToken(tokenWithExpiredTime.getLeft());
        userLoginResponse.setTokenExpiredAt(tokenWithExpiredTime.getRight().getTime());
        return userLoginResponse;
    }

    @Override
    public UserLoginResponse getUserInfo(String token) {
        Pair<LoginUser, Date> loginUserDatePair = userAuthUtil.parseLoginUserFromJWT(token);
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setLoginUser(loginUserDatePair.getKey());
        userLoginResponse.setToken(token);
        userLoginResponse.setTokenExpiredAt(loginUserDatePair.getRight().getTime());
        return userLoginResponse;
    }
}
