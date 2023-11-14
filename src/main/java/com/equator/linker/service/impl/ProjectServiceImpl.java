package com.equator.linker.service.impl;

import cn.hutool.core.util.EnumUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.equator.core.model.exception.PreCondition;
import com.equator.core.util.json.JsonUtil;
import com.equator.linker.common.util.UserAuthUtil;
import com.equator.linker.common.util.UserContextUtil;
import com.equator.linker.dao.service.ProjectDaoService;
import com.equator.linker.dao.service.ProjectUserRefDaoService;
import com.equator.linker.model.constant.BaseConstant;
import com.equator.linker.model.po.TbProject;
import com.equator.linker.model.po.TbProjectUserRef;
import com.equator.linker.model.vo.project.*;
import com.equator.linker.service.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectDaoService projectDaoService;

    @Autowired
    private ProjectUserRefDaoService projectUserRefDaoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
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

        TbProjectUserRef projectUserRef = new TbProjectUserRef();
        projectUserRef.setProjectId(tbProject.getId());
        projectUserRef.setUserId(UserContextUtil.getUserId());
        projectUserRef.setRefType(BaseConstant.ProjectInstanceRefType.OWNER.ordinal());
        projectUserRefDaoService.save(projectUserRef);
        return tbProject.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ProjectUpdateRequest projectUpdateRequest) {
        Long projectId = projectUpdateRequest.getId();
        TbProject tbProject = projectDaoService.getById(projectId);
        PreCondition.isNotNull(tbProject, "项目不存在");
        UserAuthUtil.checkPermission(tbProject.getCreateUserId());

        tbProject.setName(projectUpdateRequest.getName());
        tbProject.setIntro(projectUpdateRequest.getIntro());
        tbProject.setScmConfig(JsonUtil.toJson(projectUpdateRequest.getScmConfig()));
        tbProject.setProxyConfig(JsonUtil.toJson(projectUpdateRequest.getProxyConfig()));
        tbProject.setPackageImage(projectUpdateRequest.getPackageImage());
        tbProject.setPackageScript(projectUpdateRequest.getPackageScript());
        tbProject.setPackageOutputDir(projectUpdateRequest.getPackageOutputDir());
        tbProject.setAccessEntrance(projectUpdateRequest.getAccessEntrance());
        tbProject.setAccessLevel(BaseConstant.AccessLevel.valueOf(projectUpdateRequest.getAccessLevel()).getCode());
        projectDaoService.updateById(tbProject);

        if (BaseConstant.AccessLevel.PRIVATE.ordinal() == tbProject.getAccessLevel()) {
            // 设置为私密项目，删除其它关联
            projectUserRefDaoService.remove(Wrappers.<TbProjectUserRef>lambdaQuery()
                    .eq(TbProjectUserRef::getProjectId, projectId)
                    .eq(TbProjectUserRef::getRefType, BaseConstant.ProjectInstanceRefType.JOIN.ordinal()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long projectId) {
        projectDaoService.removeById(projectId);
        TbProject tbProject = projectDaoService.getById(projectId);
        PreCondition.isNotNull(tbProject, "项目不存在");
        UserAuthUtil.checkPermission(tbProject.getCreateUserId());
        projectUserRefDaoService.remove(Wrappers.<TbProjectUserRef>lambdaQuery()
                .eq(TbProjectUserRef::getProjectId, projectId));
    }

    @Override
    public List<ProjectSimpleInfo> all() {
        // 获取自己创建或加入的
        Set<Long> targetProjectIds = projectUserRefDaoService.getProjectIdByUserId(UserContextUtil.getUserId());
        // 公开的
        Set<Long> publicProjectIds = projectDaoService
                .getProjectIdsByAccessLevel(BaseConstant.AccessLevel.PUBLIC.getCode(), targetProjectIds);
        targetProjectIds.addAll(publicProjectIds);

        return projectDaoService.list(Wrappers.<TbProject>lambdaQuery()
                        .select(TbProject::getId, TbProject::getName, TbProject::getIntro)
                        .in(TbProject::getId, targetProjectIds)).stream()
                .map(tbProject -> {
                    ProjectSimpleInfo projectSimpleInfo = new ProjectSimpleInfo();
                    projectSimpleInfo.setId(tbProject.getId());
                    projectSimpleInfo.setName(tbProject.getName());
                    projectSimpleInfo.setIntro(tbProject.getIntro());
                    return projectSimpleInfo;
                }).collect(Collectors.toList());
    }

    @Override
    public ProjectDetailsInfo details(Long projectId) {
        TbProject tbProject = projectDaoService.getById(projectId);
        ProjectDetailsInfo projectDetailsInfo = new ProjectDetailsInfo();
        BeanUtils.copyProperties(tbProject, projectDetailsInfo);
        projectDetailsInfo.setAccessLevel(EnumUtil.getFieldBy(BaseConstant.AccessLevel::name, BaseConstant.AccessLevel::getCode, tbProject.getAccessLevel()));
        projectDetailsInfo.setScmConfig(JsonUtil.fromJson(tbProject.getScmConfig(), ScmConfig.class));
        projectDetailsInfo.setProxyConfig(JsonUtil.fromJson(tbProject.getProxyConfig(), ProxyConfig.class));

        boolean isOwner = tbProject.getCreateUserId().equals(UserContextUtil.getUserId());
        projectDetailsInfo.setIsOwner(isOwner);
        if (!isOwner) {
            projectDetailsInfo.getScmConfig().setAccessToken("保密");
        }
        return projectDetailsInfo;
    }
}
