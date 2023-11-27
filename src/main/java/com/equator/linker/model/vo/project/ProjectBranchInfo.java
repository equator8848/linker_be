package com.equator.linker.model.vo.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectBranchInfo {
    private String name;

    private String latestCommitId;

    private String latestCommitTitle;

    private String latestCommitTime;
}
