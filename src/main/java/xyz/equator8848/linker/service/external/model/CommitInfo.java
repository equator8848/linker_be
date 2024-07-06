package xyz.equator8848.linker.service.external.model;

import lombok.Data;

import java.util.List;

@Data
public class CommitInfo {

    private String id;

    private String shortId;

    private String createdAt;

    private List<String> parentIds;

    private String title;

    private String message;

    private String authorName;

    private String authorEmail;

    private String authoredDate;

    private String committerName;

    private String committerEmail;

    private String committedDate;

    private String webUrl;
}
