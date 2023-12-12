package xyz.equator8848.linker.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.equator8848.inf.auth.util.UserContextUtil;
import xyz.equator8848.inf.core.model.exception.PreCondition;
import xyz.equator8848.inf.core.model.exception.VerifyException;
import xyz.equator8848.inf.core.model.page.PageData;
import xyz.equator8848.inf.core.util.security.PasswordUtil;
import xyz.equator8848.linker.dao.service.UserDaoService;
import xyz.equator8848.linker.model.constant.ModelStatus;
import xyz.equator8848.linker.model.po.TbUser;
import xyz.equator8848.linker.model.vo.user.UserCreateVO;
import xyz.equator8848.linker.model.vo.user.UserInfoVO;
import xyz.equator8848.linker.model.vo.user.UserPasswordUpdateVO;
import xyz.equator8848.linker.model.vo.user.UserUpdateVO;
import xyz.equator8848.linker.service.UserService;
import xyz.equator8848.linker.service.util.PasswordValidator;

import java.util.stream.Collectors;


/**
 * @Author: Equator
 * @Date: 2021/2/10 10:32
 **/
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDaoService userDaoService;

    @Override
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
        tbUser.setRoleType(userCreateVO.getRoleType());
        userDaoService.save(tbUser);
    }

    @Override
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
        tbUser.setRoleType(userUpdateVO.getRoleType());
        userDaoService.updateById(tbUser);
    }

    @Override
    public void updateUserStatus(Long userId, Short userStatus) {
        TbUser tbUser = userDaoService.getBaseMapper().selectById(userId);
        PreCondition.isNotNull(tbUser, "用户不存在");
        tbUser.setStatus(userStatus);
        userDaoService.updateById(tbUser);
    }

    @Override
    public PageData<UserInfoVO> getUserList(String search, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<TbUser> query = Wrappers.lambdaQuery();
        query.select(TbUser::getId, TbUser::getUserName, TbUser::getEmail, TbUser::getStatus);
        if (!StringUtils.isEmpty(search)) {
            query.like(TbUser::getUserName, search)
                    .or().like(TbUser::getEmail, search)
                    .or().like(TbUser::getPhoneNumber, search);
            if (StringUtils.isNumeric(search)) {
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
            userInfoVO.setEmail(user.getEmail());
            return userInfoVO;
        }).collect(Collectors.toList()));
    }

    @Override
    public UserInfoVO getUserInfo(Long uid) {
        TbUser tbUser = userDaoService.getById(uid);
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setId(tbUser.getId());
        userInfoVO.setUserName(tbUser.getUserName());
        userInfoVO.setNickName(tbUser.getUserName());
        userInfoVO.setEmail(tbUser.getEmail());
        userInfoVO.setPhoneNumber(tbUser.getPhoneNumber());
        userInfoVO.setEmail(tbUser.getEmail());
        userInfoVO.setStatus(tbUser.getStatus());
        userInfoVO.setRoleType(tbUser.getRoleType());
        return userInfoVO;
    }

    @Override
    public UserInfoVO changeUsername(String newUsername) {
        TbUser tbUser = userDaoService.getById(UserContextUtil.getUserId());
        PreCondition.isNotNull(tbUser, "无法获取当前用户数据");
        tbUser.setUserName(newUsername);
        userDaoService.updateById(tbUser);
        return getUserInfo(UserContextUtil.getUserId());
    }

    @Override
    public void updateUserPassword(UserPasswordUpdateVO userPasswordUpdateVO) {
        PreCondition.isTrue(userPasswordUpdateVO.getPassword().equals(userPasswordUpdateVO.getPasswordConfirm()), "两次输入的密码不一致");
        TbUser tbUser = userDaoService.getById(UserContextUtil.getUserId());
        PreCondition.isNotNull(tbUser, "无法获取当前用户数据");

        PreCondition.isTrue(PasswordValidator.verifyPassword(userPasswordUpdateVO.getCurrentPassword(), tbUser.getUserPassword()), "当前密码不正确，忘记当前密码请联系管理员重置");

        tbUser.setUserPassword(PasswordUtil.generateSha512CryptPassword(userPasswordUpdateVO.getPassword()));

        userDaoService.updateById(tbUser);
    }
}
