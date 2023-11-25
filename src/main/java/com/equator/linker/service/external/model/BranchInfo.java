package com.equator.linker.service.external.model;

import lombok.Data;

@Data
public class BranchInfo {
    private String name;

    private String latestCommitId;

    private String latestCommitTitle;

    private String latestCommitUser;

    private String latestCommitDate;
}
