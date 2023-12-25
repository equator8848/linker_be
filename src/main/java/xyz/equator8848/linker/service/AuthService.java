package xyz.equator8848.linker.service;


import xyz.equator8848.linker.model.vo.user.UserLoginDataVO;
import xyz.equator8848.linker.model.vo.user.UserLoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;


/**
 * @Author: Equator
 * @Date: 2021/2/10 10:32
 **/
@Service
public interface AuthService {
    UserLoginResponse login(UserLoginDataVO userLoginVO, HttpServletRequest request);

    UserLoginResponse getUserInfo(String token);
}
