package com.equator.linker.model.vo.instance;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author equator
 * @since 2023-11-05
 */
@Data
public class InstanceStarRequest {

    /**
     * 项目ID
     */
    @NotNull
    private Long projectId;

    /**
     * 实例ID
     */
    @NotNull
    private Long instanceId;

    /**
     * 是否是收藏
     */
    @NotNull
    private Boolean starAction;
}
