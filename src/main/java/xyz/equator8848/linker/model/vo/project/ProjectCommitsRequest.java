package xyz.equator8848.linker.model.vo.project;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectCommitsRequest {
    @NotNull
    private Long instanceId;

    private String refName;
}
