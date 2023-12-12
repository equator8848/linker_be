package xyz.equator8848.linker.model.vo.user;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * @Author: Equator
 * @Date: 2022/9/18 11:50
 **/
@Data
public class UserUpdateVO {
    @NotNull
    private Long id;

    @NotNull
    private String userName;

    private String phoneNumber;

    @Email(message = "邮箱格式不对")
    private String email;

    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[.#?!@$%^&*-]).{10,20}$", message = "密码强度不符合要求，必须包含至少1位大写字母，1位小写字母，1位数字，1位特殊字符(.#?!@$%^&*-)，长度在10-20之间")
    private String userPassword;

    @NotNull
    private Short status;

    @NotNull
    private Short roleType;
}
