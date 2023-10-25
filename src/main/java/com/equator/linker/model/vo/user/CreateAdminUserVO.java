package com.equator.linker.model.vo.user;


import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class CreateAdminUserVO {
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

    @NotNull
    private String userPassword;

    /**
     *
     */
    @NotNull
    private String email;


    /**
     * 关联的外部ID
     */
    @NotNull
    @NotEmpty
    private List<Integer> relationIds;

    /**
     * 角色类型
     */
    @NotNull
    private Integer roleType;
}
