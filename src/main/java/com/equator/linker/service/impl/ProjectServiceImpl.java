package com.equator.linker.service.impl;

import com.equator.core.util.json.JsonUtil;
import com.equator.linker.dao.service.ProjectDaoService;
import com.equator.linker.model.constant.BaseConstant;
import com.equator.linker.model.po.TbProject;
import com.equator.linker.model.vo.project.ProjectCreateRequest;
import com.equator.linker.model.vo.project.ProjectDetailsInfo;
import com.equator.linker.model.vo.project.ProjectSimpleInfo;
import com.equator.linker.model.vo.project.ProjectUpdateRequest;
import com.equator.linker.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectDaoService projectDaoService;

    @Override
    public Long create(ProjectCreateRequest projectCreateRequest) {
        TbProject tbProject = new TbProject();
        tbProject.setName(projectCreateRequest.getName());
        tbProject.setIntro(projectCreateRequest.getIntro());
        tbProject.setScmConfig(JsonUtil.toJson(projectCreateRequest.getScmConfig()));
        tbProject.setProxyConfig(JsonUtil.toJson(projectCreateRequest.getProxyConfig()));
        tbProject.setPackageImage(projectCreateRequest.getPackageImage());
        tbProject.setPackageScript(projectCreateRequest.getPackageScript());
        tbProject.setPackageOutputDir(projectCreateRequest.getPackageOutputDir());
        tbProject.setAccessEntrance(projectCreateRequest.getAccessEntrance());
        tbProject.setAccessLevel(BaseConstant.AccessLevel.valueOf(projectCreateRequest.getAccessLevel()).getCode());
        projectDaoService.save(tbProject);
        return tbProject.getId();
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
