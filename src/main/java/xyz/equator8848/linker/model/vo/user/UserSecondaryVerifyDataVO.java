package xyz.equator8848.linker.model.vo.user;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * @Author: Equator
 * @Date: 2022/9/18 11:50
 **/
@Data
public class UserSecondaryVerifyDataVO {
    @NotNull
    private String preLoginSecret;

    @NotNull
    private String authCodeId;

    @NotNull
    private String authCode;
}
