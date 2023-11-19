package com.equator.linker.service;


import com.equator.linker.model.vo.PageData;
import com.equator.linker.model.vo.user.UserCreateVO;
import com.equator.linker.model.vo.user.UserInfoVO;
import com.equator.linker.model.vo.user.UserPasswordUpdateVO;
import com.equator.linker.model.vo.user.UserUpdateVO;
import org.springframework.stereotype.Service;


/**
 * @Author: Equator
 * @Date: 2021/2/10 10:32
 **/
@Service
public interface UserService {
    void addUser(UserCreateVO userCreateVO);

    void updateUser(UserUpdateVO userUpdateVO);

    void updateUserStatus(Long userId, Short userStatus);

    PageData<UserInfoVO> getUserList(String search, Integer pageNum, Integer pageSize);

    UserInfoVO getUserInfo(Long uid);

    UserInfoVO changeUsername(String newUsername);

    void updateUserPassword(UserPasswordUpdateVO userPasswordUpdateVO);
}
