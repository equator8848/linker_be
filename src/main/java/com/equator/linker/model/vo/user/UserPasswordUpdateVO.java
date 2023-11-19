package com.equator.linker.model.vo.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author: Equator
 * @Date: 2022/9/18 11:50
 **/
@Data
public class UserPasswordUpdateVO {
    @NotNull
    private String currentPassword;

    @NotNull
    private String password;

    @NotNull
    private String passwordConfirm;
}
