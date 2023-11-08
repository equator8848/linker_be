package com.equator.linker.service.impl;

import com.equator.linker.model.vo.project.ProjectCreateRequest;
import com.equator.linker.model.vo.project.ProjectDetailsInfo;
import com.equator.linker.model.vo.project.ProjectSimpleInfo;
import com.equator.linker.model.vo.project.ProjectUpdateRequest;
import com.equator.linker.service.ProjectService;
import lombok.Data;

import java.util.List;

@Data
public class ProjectServiceImpl implements ProjectService {
    @Override
    public Long create(ProjectCreateRequest projectCreateRequest) {
        return null;
    }

    @Override
    public void update(ProjectUpdateRequest projectUpdateRequest) {

    }

    @Override
    public void delete(Long projectId) {

    }

    @Override
    public List<ProjectSimpleInfo> list(Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public ProjectDetailsInfo details(Long projectId) {
        return null;
    }
}
