package com.equator.linker.service;

import com.equator.linker.model.vo.project.ProjectCreateRequest;
import com.equator.linker.model.vo.project.ProjectDetailsInfo;
import com.equator.linker.model.vo.project.ProjectSimpleInfo;
import com.equator.linker.model.vo.project.ProjectUpdateRequest;

import java.util.List;

public interface ProjectService {
    Long create(ProjectCreateRequest projectCreateRequest);

    void update(ProjectUpdateRequest projectUpdateRequest);

    void delete(Long projectId);

    List<ProjectSimpleInfo> list(Integer pageNum, Integer pageSize);

    ProjectDetailsInfo details(Long projectId);
}
