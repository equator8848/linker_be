package xyz.equator8848.linker.service;

import xyz.equator8848.linker.model.po.TbInstance;
import xyz.equator8848.linker.model.po.TbProject;
import xyz.equator8848.linker.model.vo.project.ProjectBranchInfo;
import xyz.equator8848.linker.model.vo.project.ProjectBranchResult;
import xyz.equator8848.linker.model.vo.project.ProjectBranchesRequest;

import java.util.List;

public interface ProjectBranchService {
    List<ProjectBranchInfo> branches(Long projectId);

    ProjectBranchResult branchesWithTips(ProjectBranchesRequest projectBranchesRequest);

    ProjectBranchResult branchesWithTips(Long projectId, String searchKeyword);

    String getLatestCommitId(TbProject tbProject, TbInstance tbInstance);
}
