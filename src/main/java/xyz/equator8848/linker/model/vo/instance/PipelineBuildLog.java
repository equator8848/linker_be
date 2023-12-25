package xyz.equator8848.linker.model.vo.instance;

import lombok.Data;

@Data
public class PipelineBuildLog {
    private Boolean hasMoreData;

    private String text;

    private String imageArchiveUrl;
}
