package com.equator.linker.service.user;


import com.equator.linker.dao.service.AdminDaoService;
import com.equator.linker.model.constant.ModelStatus;
import com.equator.linker.model.po.CommonUser;
import com.equator.linker.model.po.TbAdmin;
import com.equator.linker.model.vo.LoginUser;
import com.equator.linker.model.vo.user.UserLoginDataVO;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminLoginHandler implements LoginHandler {
    @Autowired
    private AdminDaoService adminDaoService;

    @Override
    public LoginHandlerType loginHandlerType() {
        return LoginHandlerType.ADMIN;
    }

    @Override
    public Triple<LoginStatus, LoginUser, CommonUser> login(UserLoginDataVO userLoginVO) {
        TbAdmin tbAdmin = adminDaoService.getUserByName(userLoginVO.getUserIdentification());
        if (tbAdmin == null) {
            return Triple.of(LoginStatus.USER_NOT_FOUND, null, null);
        }
        if (!verifyPassword(userLoginVO.getUserPassword(), tbAdmin.getUserPassword())) {
            return Triple.of(LoginStatus.PASSWORD_NOT_MATCH, null, null);
        }
        if (ModelStatus.UserStatus.DISABLE.equals(tbAdmin.getStatus())) {
            return Triple.of(LoginStatus.FORBIDDEN, null, null);
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setUid(tbAdmin.getId());
        loginUser.setUserName(tbAdmin.getUserName());
        loginUser.setNickName(tbAdmin.getNickname());
        loginUser.setUserType(ModelStatus.UserType.ADMIN);
        loginUser.setRoleType(tbAdmin.getRoleType());
        return Triple.of(LoginStatus.SUCCESS, loginUser, tbAdmin);
    }
}
