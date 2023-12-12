package xyz.equator8848.linker.service.user;


import xyz.equator8848.linker.dao.service.UserDaoService;
import xyz.equator8848.linker.model.constant.ModelStatus;
import xyz.equator8848.linker.model.po.TbUser;
import xyz.equator8848.linker.model.vo.user.UserLoginDataVO;
import xyz.equator8848.linker.model.vo.LoginUser;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PasswordLoginHandler implements LoginHandler {
    @Autowired
    private UserDaoService userDaoService;

    @Override
    public LoginHandlerType loginHandlerType() {
        return LoginHandlerType.PASSWORD;
    }

    @Override
    public Triple<LoginStatus, LoginUser, TbUser> login(UserLoginDataVO userLoginVO) {
        TbUser tbUser = userDaoService.getUserByIdentification(userLoginVO);
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
        loginUser.setEmail(tbUser.getEmail());
        loginUser.setRoleType(tbUser.getRoleType());
        return Triple.of(LoginStatus.SUCCESS, loginUser, tbUser);
    }
}
