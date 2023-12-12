package xyz.equator8848.linker.model.vo.login;

import lombok.Data;

/**
 * 预登录
 *
 * @Author: Equator
 * @Date: 2022/9/18 13:30
 **/
@Data
public class PreUserLoginResponse {
    private String authCodeId;

    private String secret;
}
