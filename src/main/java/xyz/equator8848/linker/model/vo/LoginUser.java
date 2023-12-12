package xyz.equator8848.linker.model.vo;


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
    private Long uid;

    private String avatarUrl;

    private String email;

    private String userName;

    private String nickName;

    private Integer gender;

    private Short roleType;

    public static LoginUser buildSystemUser() {
        LoginUser loginUser = new LoginUser();
        loginUser.setUid(0L);
        loginUser.setUserName("LinkerSystem");
        loginUser.setNickName("LinkerSystem");
        return loginUser;
    }
}
