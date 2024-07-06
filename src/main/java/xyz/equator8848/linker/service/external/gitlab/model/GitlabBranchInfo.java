package xyz.equator8848.linker.service.external.gitlab.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GitlabBranchInfo {
    private String name;

    private GitlabCommitInfo commit;

    private boolean merged;

    @JsonProperty("can_push")
    private boolean canPush;

    @JsonProperty("web_url")
    private String webUrl;
}
