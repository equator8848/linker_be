package com.equator.linker.service.user;


import com.equator.core.util.security.MD5Util;
import com.equator.linker.configuration.SecurityConfiguration;
import com.equator.linker.dao.service.UserDaoService;
import com.equator.linker.model.constant.ModelStatus;
import com.equator.linker.model.po.CommonUser;
import com.equator.linker.model.po.TbUser;
import com.equator.linker.model.vo.LoginUser;
import com.equator.linker.model.vo.user.UserLoginDataVO;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MofangLoginHandler implements LoginHandler {
    @Autowired
    private UserDaoService userDaoService;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Override
    public LoginHandlerType loginHandlerType() {
        return LoginHandlerType.MOFANG;
    }

    @Override
    public Triple<LoginStatus, LoginUser, CommonUser> login(UserLoginDataVO userLoginVO) {
        TbUser tbUser = userDaoService.getUserByIdentification(userLoginVO, ModelStatus.UserSystemType.MOFANG);
        if (tbUser == null) {
            return Triple.of(LoginStatus.USER_NOT_FOUND, null, null);
        }
        if (StringUtils.isEmpty(tbUser.getUserPassword())) {
            return Triple.of(LoginStatus.NEED_SET_PASSWORD, null, null);
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

    @Override
    public boolean verifyPassword(String userInput, String passwordInDatabase) {
        String salt = securityConfiguration.getMofangMd5Salt();
        String encryptedPassword = MD5Util.md5(MD5Util.md5(salt + userInput));
        return ("###" + encryptedPassword).equals(passwordInDatabase);
    }
}
