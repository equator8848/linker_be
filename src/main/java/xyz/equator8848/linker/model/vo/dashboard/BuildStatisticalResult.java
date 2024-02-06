package xyz.equator8848.linker.model.vo.dashboard;

import lombok.Data;

@Data
public class BuildStatisticalResult {
    /**
     * 全局实例构建次数
     */
    private Long instanceBuildTimes;

    /**
     * 项目实例构建次数
     */
    private Long projectInstanceBuildTimes;
}
