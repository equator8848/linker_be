package com.equator.linker.service.user;


import com.equator.core.util.security.PasswordUtil;
import com.equator.linker.model.po.TbUser;
import com.equator.linker.model.vo.LoginUser;
import com.equator.linker.model.vo.user.UserLoginDataVO;
import org.apache.commons.lang3.tuple.Triple;

public interface LoginHandler {
    LoginHandlerType loginHandlerType();

    Triple<LoginStatus, LoginUser, TbUser> login(UserLoginDataVO userLoginVO);

    default boolean verifyPassword(String userInput, String passwordInDatabase) {
        String salt = PasswordUtil.parseSaltFromEncryptedPassword(passwordInDatabase);
        String encryptedPassword = PasswordUtil.generateSha512CryptPassword(userInput, salt);
        return encryptedPassword.equals(passwordInDatabase);
    }
}
