package com.equator.linker.service.external.gitlab.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GitlabBranchInfo {
    private String name;

    private Commit commit;

    private boolean merged;

    @JsonProperty("can_push")
    private boolean canPush;

    @JsonProperty("web_url")
    private String webUrl;

    @Data
    public static class Commit {
        private String id;

        @JsonProperty("short_id")
        private String shortId;

        @JsonProperty("created_at")
        private String createdAt;

        @JsonProperty("parent_ids")
        private List<String> parentIds;

        private String title;

        private String message;

        @JsonProperty("author_name")
        private String authorName;

        @JsonProperty("author_email")
        private String authorEmail;

        @JsonProperty("authored_date")
        private String authoredDate;

        @JsonProperty("committer_name")
        private String committerName;

        @JsonProperty("committer_email")
        private String committerEmail;

        @JsonProperty("committed_date")
        private String committedDate;

        @JsonProperty("web_url")
        private String webUrl;
    }
}
