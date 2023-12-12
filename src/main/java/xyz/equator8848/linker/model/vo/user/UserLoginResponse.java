package xyz.equator8848.linker.model.vo.user;

import xyz.equator8848.linker.model.vo.LoginUser;
import lombok.Data;

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
