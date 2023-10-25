package com.equator.linker.service.user;


import com.equator.linker.dao.service.UserDaoService;
import com.equator.linker.model.constant.ModelStatus;
import com.equator.linker.model.po.CommonUser;
import com.equator.linker.model.po.TbUser;
import com.equator.linker.model.vo.LoginUser;
import com.equator.linker.model.vo.user.UserLoginDataVO;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DayuLoginHandler implements LoginHandler {
    @Autowired
    private UserDaoService userDaoService;

    @Override
    public LoginHandlerType loginHandlerType() {
        return LoginHandlerType.DAYU;
    }

    @Override
    public Triple<LoginStatus, LoginUser, CommonUser> login(UserLoginDataVO userLoginVO) {
        TbUser tbUser = userDaoService.getUserByIdentification(userLoginVO, ModelStatus.UserSystemType.DAYU);
        if (tbUser == null) {
            return Triple.of(LoginStatus.USER_NOT_FOUND, null, null);
        }
        if (!verifyPassword(userLoginVO.getUserPassword(), tbUser.getUserPassword())) {
            return Triple.of(LoginStatus.PASSWORD_NOT_MATCH, null, null);
        }
        if (ModelStatus.UserStatus.DISABLE.equals(tbUser.getStatus())) {
            return Triple.of(LoginStatus.FORBIDDEN, null, null);
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setUid(tbUser.getId());
        loginUser.setNickName(tbUser.getUserName());
        loginUser.setUserName(tbUser.getUserName());
        loginUser.setUserType(ModelStatus.UserType.USER);
        return Triple.of(LoginStatus.SUCCESS, loginUser, tbUser);
    }
}
