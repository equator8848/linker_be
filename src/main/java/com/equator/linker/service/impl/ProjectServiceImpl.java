package com.equator.linker.service.impl;

import cn.hutool.core.util.EnumUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.equator.core.model.exception.PreCondition;
import com.equator.core.util.json.JsonUtil;
import com.equator.linker.common.util.UserAuthUtil;
import com.equator.linker.common.util.UserContextUtil;
import com.equator.linker.dao.service.ProjectDaoService;
import com.equator.linker.dao.service.ProjectUserRefDaoService;
import com.equator.linker.dao.service.UserDaoService;
import com.equator.linker.model.constant.BaseConstant;
import com.equator.linker.model.constant.ScmType;
import com.equator.linker.model.po.TbProject;
import com.equator.linker.model.po.TbProjectUserRef;
import com.equator.linker.model.vo.PageData;
import com.equator.linker.model.vo.project.*;
import com.equator.linker.service.ProjectService;
import com.equator.linker.service.external.ScmService;
import com.equator.linker.service.external.model.BranchInfo;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectDaoService projectDaoService;

    @Autowired
    private ProjectUserRefDaoService projectUserRefDaoService;

    private Map<ScmType, ScmService> scmTypeScmServiceMap;

    @Autowired
    private UserDaoService userDaoService;

    @Autowired
    public void setScmService(List<ScmService> scmServiceList) {
        scmTypeScmServiceMap = scmServiceList.stream().collect(Collectors.toMap(ScmService::getScmType, Function.identity()));
    }

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
        tbProject.setDeployFolder(projectCreateRequest.getDeployFolder());
        tbProject.setRouteMode(projectCreateRequest.getRouteMode());
        tbProject.setAccessEntrance(projectCreateRequest.getAccessEntrance());
        tbProject.setAccessLevel(BaseConstant.AccessLevel.valueOf(projectCreateRequest.getAccessLevel()).getCode());
        tbProject.setPipelineTemplateId(projectCreateRequest.getPipelineTemplateId());
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
        tbProject.setDeployFolder(projectUpdateRequest.getDeployFolder());
        tbProject.setRouteMode(projectUpdateRequest.getRouteMode());
        tbProject.setAccessEntrance(projectUpdateRequest.getAccessEntrance());
        tbProject.setAccessLevel(BaseConstant.AccessLevel.valueOf(projectUpdateRequest.getAccessLevel()).getCode());
        tbProject.setPipelineTemplateId(projectUpdateRequest.getPipelineTemplateId());
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
        TbProject tbProject = projectDaoService.getById(projectId);
        PreCondition.isNotNull(tbProject, "项目不存在");
        projectDaoService.removeById(projectId);
        UserAuthUtil.checkPermission(tbProject.getCreateUserId());
        projectUserRefDaoService.remove(Wrappers.<TbProjectUserRef>lambdaQuery()
                .eq(TbProjectUserRef::getProjectId, projectId));
    }

    @Override
    public List<ProjectSimpleInfo> list() {
        // 获取自己创建或加入的
        Set<Long> targetProjectIds = projectUserRefDaoService.getProjectIdByUserId(UserContextUtil.getUserId());
        // 公开的
        Set<Long> publicProjectIds = projectDaoService
                .getProjectIdsByAccessLevel(BaseConstant.AccessLevel.PUBLIC.getCode(), targetProjectIds);
        targetProjectIds.addAll(publicProjectIds);

        return projectDaoService.list(Wrappers.<TbProject>lambdaQuery()
                        .select(TbProject::getId, TbProject::getName, TbProject::getIntro)
                        .in(!CollectionUtils.isEmpty(targetProjectIds), TbProject::getId, targetProjectIds)).stream()
                .map(tbProject -> {
                    ProjectSimpleInfo projectSimpleInfo = new ProjectSimpleInfo();
                    projectSimpleInfo.setId(tbProject.getId());
                    projectSimpleInfo.setName(tbProject.getName());
                    projectSimpleInfo.setIntro(tbProject.getIntro());
                    return projectSimpleInfo;
                }).collect(Collectors.toList());
    }

    @Override
    public PageData<ProjectSimpleInfo> all(ProjectListRequest projectListRequest) {
        String searchKeyword = projectListRequest.getSearchKeyword();
        LambdaQueryWrapper<TbProject> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper
                .like(StringUtils.isNotBlank(searchKeyword), TbProject::getName, searchKeyword)
                .or()
                .like(StringUtils.isNotBlank(searchKeyword), TbProject::getIntro, searchKeyword);
        Page<TbProject> tbProjectPageData = projectDaoService.page(new Page<>(projectListRequest.getPageNum(), projectListRequest.getPageSize()), queryWrapper);
        return PageData.wrap(tbProjectPageData, tbProjectPageData.getRecords().stream().map(tbProject -> {
            ProjectSimpleInfo projectSimpleInfo = new ProjectSimpleInfo();
            BeanUtils.copyProperties(tbProject, projectSimpleInfo);
            projectSimpleInfo.setId(tbProject.getId());
            projectSimpleInfo.setName(tbProject.getName());
            projectSimpleInfo.setIntro(tbProject.getIntro());
            projectSimpleInfo.setCreateUserName(userDaoService.getUsernameFromCache(tbProject.getCreateUserId()));
            projectSimpleInfo.setUpdateUserName(userDaoService.getUsernameFromCache(tbProject.getUpdateUserId()));
            return projectSimpleInfo;
        }).collect(Collectors.toList()));
    }

    @Override
    public ProjectDetailsInfo details(Long projectId) {
        TbProject tbProject = projectDaoService.getById(projectId);
        PreCondition.isNotNull(tbProject, "项目不存在");
        ProjectDetailsInfo projectDetailsInfo = new ProjectDetailsInfo();
        BeanUtils.copyProperties(tbProject, projectDetailsInfo);
        projectDetailsInfo.setAccessLevel(EnumUtil.getFieldBy(BaseConstant.AccessLevel::name, BaseConstant.AccessLevel::getCode, tbProject.getAccessLevel()));
        projectDetailsInfo.setScmConfig(JsonUtil.fromJson(tbProject.getScmConfig(), ScmConfig.class));
        projectDetailsInfo.setProxyConfig(JsonUtil.fromJson(tbProject.getProxyConfig(), ProxyConfig.class));

        projectDetailsInfo.setCreateUserName(userDaoService.getUsernameFromCache(tbProject.getCreateUserId()));
        projectDetailsInfo.setUpdateUserName(userDaoService.getUsernameFromCache(tbProject.getUpdateUserId()));

        boolean isOwner = tbProject.getCreateUserId().equals(UserContextUtil.getUserId());
        projectDetailsInfo.setIsOwner(isOwner);
        if (!isOwner) {
            projectDetailsInfo.getScmConfig().setAccessToken("保密");
        }
        return projectDetailsInfo;
    }

    private static final List<ProjectBranchInfo> defaultProjectBranchInfoList = ImmutableList
            .of(new ProjectBranchInfo("master", "fakeId1", "默认分支", "now"),
                    new ProjectBranchInfo("main", "fakeId2", "默认分支", "now"),
                    new ProjectBranchInfo("dev", "fakeId3", "默认分支", "now"));

    @Override
    public List<ProjectBranchInfo> branches(Long projectId) {
        TbProject tbProject = projectDaoService.getById(projectId);
        PreCondition.isNotNull(tbProject, "项目不存在");
        ScmConfig scmConfig = JsonUtil.fromJson(tbProject.getScmConfig(), ScmConfig.class);
        ScmService scmService = scmTypeScmServiceMap.get(ScmType.valueOf(scmConfig.getScmType()));
        if (scmService == null) {
            return defaultProjectBranchInfoList;
        }
        List<BranchInfo> branchInfoList = scmService.getBranchInfo(scmConfig.getRepositoryUrl(), scmConfig.getAccessToken());
        if (CollectionUtils.isEmpty(branchInfoList)) {
            return defaultProjectBranchInfoList;
        }
        return branchInfoList.stream().map(branch -> {
            ProjectBranchInfo projectBranchInfo = new ProjectBranchInfo();
            projectBranchInfo.setName(branch.getName());
            projectBranchInfo.setLatestCommitId(branch.getLatestCommitId());
            projectBranchInfo.setLatestCommitTitle(branch.getLatestCommitTitle());
            projectBranchInfo.setLatestCommitTime(branch.getLatestCommitDate());
            return projectBranchInfo;
        }).collect(Collectors.toList());
    }


}
