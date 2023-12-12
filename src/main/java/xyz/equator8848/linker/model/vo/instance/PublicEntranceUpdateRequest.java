package xyz.equator8848.linker.model.vo.instance;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PublicEntranceUpdateRequest {

    /**
     * 实例ID
     */
    @NotNull
    private Long instanceId;

    /**
     * 是否启用
     */
    @NotNull
    private Boolean enabledFlag;

    /**
     * 名称
     */
    @NotNull
    private String name;

    /**
     * 介绍
     */
    @NotNull
    private String intro;
}
