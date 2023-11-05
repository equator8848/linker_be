package com.equator.linker.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.equator.core.model.exception.PreCondition;
import com.equator.core.model.exception.VerifyException;
import com.equator.core.util.security.PasswordUtil;
import com.equator.linker.common.util.IpNetUtil;
import com.equator.linker.common.util.UserAuthUtil;
import com.equator.linker.configuration.SecurityConfiguration;
import com.equator.linker.dao.service.UserDaoService;
import com.equator.linker.model.constant.ModelStatus;
import com.equator.linker.model.po.TbUser;
import com.equator.linker.model.vo.LoginUser;
import com.equator.linker.model.vo.PageData;
import com.equator.linker.model.vo.user.*;
import com.equator.linker.service.user.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;


/**
 * @Author: Equator
 * @Date: 2021/2/10 10:32
 **/
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserDaoService userDaoService;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private LoginService loginService;

    public void addUser(UserCreateVO userCreateVO) {
        if (StringUtils.isEmpty(userCreateVO.getPhoneNumber()) && StringUtils.isEmpty(userCreateVO.getEmail())) {
            throw new VerifyException("手机与邮箱不能同时为空");
        }
        String password = PasswordUtil.generateSha512CryptPassword(userCreateVO.getUserPassword());
        TbUser tbUser = userDaoService.getUser(userCreateVO.getPhoneNumber(), userCreateVO.getEmail());
        if (tbUser != null) {
            throw new VerifyException("用户已存在");
        }
        tbUser = new TbUser();
        tbUser.setUserName(userCreateVO.getUserName());
        tbUser.setUserPassword(password);
        tbUser.setPhoneNumber(userCreateVO.getPhoneNumber());
        tbUser.setEmail(userCreateVO.getEmail());
        tbUser.setStatus(ModelStatus.UserStatus.NORMAL);
        userDaoService.save(tbUser);
    }

    public void updateUser(UserUpdateVO userUpdateVO) {
        TbUser tbUser = userDaoService.getBaseMapper().selectById(userUpdateVO.getId());
        PreCondition.isNotNull(tbUser, "用户不存在");
        if (StringUtils.isEmpty(userUpdateVO.getPhoneNumber()) && StringUtils.isEmpty(userUpdateVO.getEmail())) {
            throw new VerifyException("手机与邮箱不能同时为空");
        }
        if (!StringUtils.isEmpty(userUpdateVO.getUserPassword())) {
            String password = PasswordUtil.generateSha512CryptPassword(userUpdateVO.getUserPassword());
            tbUser.setUserPassword(password);
        }
        tbUser.setUserName(userUpdateVO.getUserName());
        tbUser.setPhoneNumber(userUpdateVO.getPhoneNumber());
        tbUser.setEmail(userUpdateVO.getEmail());
        tbUser.setStatus(userUpdateVO.getStatus());
        userDaoService.updateById(tbUser);
    }

    public void updateUserStatus(Integer userId, Short userStatus) {
        TbUser tbUser = userDaoService.getBaseMapper().selectById(userId);
        PreCondition.isNotNull(tbUser, "用户不存在");
        tbUser.setStatus(userStatus);
        userDaoService.updateById(tbUser);
    }

    public PageData<UserInfoVO> getUserList(String search, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<TbUser> query = Wrappers.lambdaQuery();
        query.select(TbUser::getId, TbUser::getUserName, TbUser::getEmail, TbUser::getStatus);
        if (!StringUtils.isEmpty(search)) {
            query.like(TbUser::getUserName, search)
                    .or().like(TbUser::getEmail, search)
                    .or().like(TbUser::getPhoneNumber, search);
            if (org.apache.commons.lang3.StringUtils.isNumeric(search)) {
                long parseNumber = Long.parseLong(search);
                query.or().eq(TbUser::getId, (int) parseNumber)
                        .or().eq(TbUser::getPhoneNumber, String.valueOf(parseNumber));
            }
        }
        query.orderByDesc(TbUser::getCreateTime);
        Page<TbUser> userPage = userDaoService.page(new Page<>(pageNum, pageSize), query);
        return PageData.wrap(userPage, userPage.getRecords().stream().map(user -> {
            UserInfoVO userInfoVO = new UserInfoVO();
            userInfoVO.setId(user.getId());
            userInfoVO.setUserName(user.getUserName());
            userInfoVO.setStatus(user.getStatus());
            return userInfoVO;
        }).collect(Collectors.toList()));
    }

    public UserInfoVO getUserInfo(Integer uid) {
        TbUser tbUser = userDaoService.getById(uid);
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setId(tbUser.getId());
        userInfoVO.setUserName(tbUser.getUserName());
        userInfoVO.setEmail(tbUser.getEmail());
        userInfoVO.setPhoneNumber(tbUser.getPhoneNumber());
        userInfoVO.setEmail(tbUser.getEmail());
        userInfoVO.setStatus(tbUser.getStatus());
        return userInfoVO;
    }

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

    public UserLoginResponse getUserInfo(String token) {
        Pair<LoginUser, Date> loginUserDatePair = UserAuthUtil.parseLoginUserFromJWT(token);
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setLoginUser(loginUserDatePair.getKey());
        userLoginResponse.setToken(token);
        userLoginResponse.setTokenExpiredAt(loginUserDatePair.getRight().getTime());
        return userLoginResponse;
    }
}
