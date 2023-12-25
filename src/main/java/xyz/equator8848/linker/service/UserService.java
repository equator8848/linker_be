package xyz.equator8848.linker.service;


import org.springframework.stereotype.Service;
import xyz.equator8848.inf.core.model.page.PageData;
import xyz.equator8848.linker.model.vo.user.UserCreateVO;
import xyz.equator8848.linker.model.vo.user.UserInfoVO;
import xyz.equator8848.linker.model.vo.user.UserPasswordUpdateVO;
import xyz.equator8848.linker.model.vo.user.UserUpdateVO;


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
