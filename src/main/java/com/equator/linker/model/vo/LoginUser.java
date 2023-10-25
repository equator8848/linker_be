package com.equator.linker.model.vo;


import com.equator.linker.model.constant.ModelStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author equator
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {
    private Integer uid;
    private String avatarUrl;
    private String userName;
    private String nickName;
    private Integer gender;
    private Short userType;
    private Short systemType;
    private Integer roleType;

    private Boolean loginByToken;

    public static LoginUser buildSystemUser() {
        LoginUser loginUser = new LoginUser();
        loginUser.setUid(0);
        loginUser.setUserName("dayuSystem");
        loginUser.setNickName("dayuSystem");
        loginUser.setUserType(ModelStatus.UserType.ADMIN);
        return loginUser;
    }
}
