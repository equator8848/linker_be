package com.equator.linker.service.impl;


import com.equator.linker.common.util.IpNetUtil;
import com.equator.linker.common.util.UserAuthUtil;
import com.equator.linker.configuration.SecurityConfiguration;
import com.equator.linker.dao.service.UserDaoService;
import com.equator.linker.model.po.TbUser;
import com.equator.linker.model.vo.LoginUser;
import com.equator.linker.model.vo.user.UserLoginDataVO;
import com.equator.linker.model.vo.user.UserLoginResponse;
import com.equator.linker.service.AuthService;
import com.equator.linker.service.user.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * @Author: Equator
 * @Date: 2021/2/10 10:32
 **/
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserDaoService userDaoService;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private LoginService loginService;

    @Override
    public UserLoginResponse login(UserLoginDataVO userLoginVO, HttpServletRequest request) {
        userLoginVO.setRemoteAddress(IpNetUtil.getRealIp(request));
        log.info("user {} try login at IP {}", userLoginVO.getUserIdentification(), userLoginVO.getRemoteAddress());
        Pair<LoginUser, TbUser> userPair = loginService.login(userLoginVO);
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        LoginUser loginUser = userPair.getKey();
        userLoginResponse.setLoginUser(loginUser);
        Pair<String, Date> tokenWithExpiredTime = UserAuthUtil.buildTokenWithExpiredTime(loginUser);
        userLoginResponse.setToken(tokenWithExpiredTime.getLeft());
        userLoginResponse.setTokenExpiredAt(tokenWithExpiredTime.getRight().getTime());
        return userLoginResponse;
    }

    @Override
    public UserLoginResponse getUserInfo(String token) {
        Pair<LoginUser, Date> loginUserDatePair = UserAuthUtil.parseLoginUserFromJWT(token);
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setLoginUser(loginUserDatePair.getKey());
        userLoginResponse.setToken(token);
        userLoginResponse.setTokenExpiredAt(loginUserDatePair.getRight().getTime());
        return userLoginResponse;
    }
}
