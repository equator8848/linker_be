package com.equator.linker.model.vo.user;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author equator
 * @since 2022-10-23
 */
@Data
public class UpdateAdminUserVO {
    @NotNull
    private Integer id;

    @NotNull
    private Short status;

    /**
     *
     */
    @NotNull
    private String nickname;

    /**
     *
     */
    @NotNull
    private String userName;

    /**
     *
     */
    @NotNull
    private String email;


    private String userPassword;

    /**
     * 关联的外部ID
     */
    @NotEmpty
    private List<Integer> relationIds;

    /**
     * 角色类型
     */
    @NotNull
    private Integer roleType;
}
