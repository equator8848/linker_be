package xyz.equator8848.linker.service;

import xyz.equator8848.inf.core.model.page.PageData;
import xyz.equator8848.linker.model.vo.project.*;

import java.util.List;

public interface ProjectService {
    Long create(ProjectCreateRequest projectCreateRequest);

    void update(ProjectUpdateRequest projectUpdateRequest);

    void delete(Long projectId);

    List<ProjectSimpleInfo> list();

    PageData<ProjectSimpleInfo> all(ProjectListRequest projectListRequest);

    ProjectDetailsInfo details(Long projectId);

    List<ProjectBranchInfo> branches(Long projectId);

    ProjectBranchResult branchesWithTips(ProjectBranchesRequest projectBranchesRequest);

    ProjectBranchResult branchesWithTips(Long projectId, String searchKeyword);
}
