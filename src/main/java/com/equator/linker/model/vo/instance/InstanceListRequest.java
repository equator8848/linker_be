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
public class InstanceListRequest {

    /**
     * 项目ID
     */
    @NotNull
    private Long projectId;

    private String searchKeyword;

    private Integer pageNum;


    private Integer pageSize;
}
