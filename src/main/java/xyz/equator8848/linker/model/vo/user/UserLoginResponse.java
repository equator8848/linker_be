package xyz.equator8848.linker.model.vo.user;

import lombok.Data;
import xyz.equator8848.inf.auth.model.bo.LoginUser;

/**
 * @Author: Equator
 * @Date: 2022/9/18 13:30
 **/
@Data
public class UserLoginResponse {
    private LoginUser loginUser;

    private String token;

    private Long tokenExpiredAt;
}
