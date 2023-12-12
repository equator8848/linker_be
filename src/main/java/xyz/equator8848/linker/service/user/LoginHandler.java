package xyz.equator8848.linker.service.user;


import xyz.equator8848.linker.model.po.TbUser;
import xyz.equator8848.linker.model.vo.user.UserLoginDataVO;
import xyz.equator8848.linker.service.util.PasswordValidator;
import xyz.equator8848.linker.model.vo.LoginUser;
import org.apache.commons.lang3.tuple.Triple;

public interface LoginHandler {
    LoginHandlerType loginHandlerType();

    Triple<LoginStatus, LoginUser, TbUser> login(UserLoginDataVO userLoginVO);

    default boolean verifyPassword(String userInput, String passwordInDatabase) {
        return PasswordValidator.verifyPassword(userInput, passwordInDatabase);
    }
}
