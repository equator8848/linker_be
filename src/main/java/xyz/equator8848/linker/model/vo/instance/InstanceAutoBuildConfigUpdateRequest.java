package xyz.equator8848.linker.model.vo.instance;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InstanceAutoBuildConfigUpdateRequest {

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
     * 检测间隔分钟数
     */
    @NotNull
    private Integer checkInterval;
}
