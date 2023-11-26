package com.equator.linker.service;

import com.equator.linker.model.vo.PageData;
import com.equator.linker.model.vo.project.*;

import java.util.List;

public interface ProjectService {
    Long create(ProjectCreateRequest projectCreateRequest);

    void update(ProjectUpdateRequest projectUpdateRequest);

    void delete(Long projectId);

    List<ProjectSimpleInfo> list();

    PageData<ProjectSimpleInfo> all(ProjectListRequest projectListRequest);

    ProjectDetailsInfo details(Long projectId);

    List<ProjectBranchInfo> branches(Long projectId);
}
