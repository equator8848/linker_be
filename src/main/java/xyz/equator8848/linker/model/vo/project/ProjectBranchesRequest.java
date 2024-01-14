package xyz.equator8848.linker.model.vo.project;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectBranchesRequest {
    @NotNull
    private String scmType;

    @NotNull
    private String repositoryUrl;

    @NotNull
    private String accessToken;
}
