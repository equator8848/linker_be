package xyz.equator8848.linker.model.vo.instance;

import lombok.Data;

@Data
public class InstancePipelineBuildResult {
    private String id;

    private Long duration;

    private String durationStr;

    private String submitTimeStr;

    private Boolean canReBuildFlag;

    private String pipelineResultStr;

    private String pipelineUrl;
}
