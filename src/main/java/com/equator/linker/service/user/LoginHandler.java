package com.equator.linker.service.user;


import com.equator.linker.model.po.TbUser;
import com.equator.linker.model.vo.LoginUser;
import com.equator.linker.model.vo.user.UserLoginDataVO;
import com.equator.linker.service.util.PasswordValidator;
import org.apache.commons.lang3.tuple.Triple;

public interface LoginHandler {
    LoginHandlerType loginHandlerType();

    Triple<LoginStatus, LoginUser, TbUser> login(UserLoginDataVO userLoginVO);

    default boolean verifyPassword(String userInput, String passwordInDatabase) {
        return PasswordValidator.verifyPassword(userInput, passwordInDatabase);
    }
}
