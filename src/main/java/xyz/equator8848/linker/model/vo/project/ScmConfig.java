package xyz.equator8848.linker.model.vo.project;

import lombok.Data;

@Data
public class ScmConfig {
    private String scmType;

    private String username;

    private String repositoryUrl;

    private String defaultBranch;

    private String accessToken;
}
