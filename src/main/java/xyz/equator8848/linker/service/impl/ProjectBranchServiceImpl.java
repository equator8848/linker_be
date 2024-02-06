package xyz.equator8848.linker.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.equator8848.inf.core.model.exception.PreCondition;
import xyz.equator8848.inf.core.util.json.JsonUtil;
import xyz.equator8848.linker.dao.service.ProjectDaoService;
import xyz.equator8848.linker.model.constant.ScmType;
import xyz.equator8848.linker.model.po.TbInstance;
import xyz.equator8848.linker.model.po.TbProject;
import xyz.equator8848.linker.model.vo.project.ProjectBranchInfo;
import xyz.equator8848.linker.model.vo.project.ProjectBranchResult;
import xyz.equator8848.linker.model.vo.project.ProjectBranchesRequest;
import xyz.equator8848.linker.model.vo.project.ScmConfig;
import xyz.equator8848.linker.service.ProjectBranchService;
import xyz.equator8848.linker.service.external.ScmService;
import xyz.equator8848.linker.service.external.model.BranchInfo;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectBranchServiceImpl implements ProjectBranchService {
    @Autowired
    private ProjectDaoService projectDaoService;

    private Map<ScmType, ScmService> scmTypeScmServiceMap;

    @Autowired
    public void setScmService(List<ScmService> scmServiceList) {
        scmTypeScmServiceMap = scmServiceList.stream().collect(Collectors.toMap(ScmService::getScmType, Function.identity()));
    }

    private static final List<ProjectBranchInfo> defaultProjectBranchInfoList = ImmutableList
            .of(new ProjectBranchInfo("master", "fakeId1", "默认分支", "now"),
                    new ProjectBranchInfo("main", "fakeId2", "默认分支", "now"),
                    new ProjectBranchInfo("dev", "fakeId3", "默认分支", "now"));

    @Override
    public List<ProjectBranchInfo> branches(Long projectId) {
        return branchesWithTips(projectId, null).getProjectBranchInfos();
    }

    @Override
    public ProjectBranchResult branchesWithTips(ProjectBranchesRequest projectBranchesRequest) {
        ScmService scmService = scmTypeScmServiceMap.get(ScmType.valueOf(projectBranchesRequest.getScmType()));
        List<BranchInfo> branchInfoList = scmService.getBranchInfo(projectBranchesRequest.getRepositoryUrl(),
                projectBranchesRequest.getAccessToken(), projectBranchesRequest.getSearchKeyword());
        ProjectBranchResult projectBranchResult = new ProjectBranchResult();
        if (CollectionUtils.isEmpty(branchInfoList)) {
            if (StringUtils.isNotBlank(projectBranchesRequest.getSearchKeyword())) {
                projectBranchResult.setIsDefaultData(false);
                projectBranchResult.setProjectBranchInfos(Collections.emptyList());
                return projectBranchResult;
            }
            projectBranchResult.setIsDefaultData(true);
            projectBranchResult.setProjectBranchInfos(defaultProjectBranchInfoList);
            return projectBranchResult;
        }
        projectBranchResult.setIsDefaultData(false);
        projectBranchResult.setProjectBranchInfos(buildProjectBranchInfo(branchInfoList));
        return projectBranchResult;
    }

    private List<ProjectBranchInfo> buildProjectBranchInfo(List<BranchInfo> branchInfoList) {
        return branchInfoList.stream().map(branch -> {
            ProjectBranchInfo projectBranchInfo = new ProjectBranchInfo();
            projectBranchInfo.setName(branch.getName());
            projectBranchInfo.setLatestCommitId(branch.getLatestCommitId());
            projectBranchInfo.setLatestCommitTitle(branch.getLatestCommitTitle());
            projectBranchInfo.setLatestCommitTime(branch.getLatestCommitDate());
            return projectBranchInfo;
        }).collect(Collectors.toList());
    }

    @Override
    public ProjectBranchResult branchesWithTips(Long projectId, String searchKeyword) {
        TbProject tbProject = projectDaoService.getById(projectId);
        PreCondition.isNotNull(tbProject, "项目不存在");

        ProjectBranchResult projectBranchResult = new ProjectBranchResult();
        projectBranchResult.setIsDefaultData(true);
        ScmConfig scmConfig = JsonUtil.fromJson(tbProject.getScmConfig(), ScmConfig.class);
        ScmService scmService = scmTypeScmServiceMap.get(ScmType.valueOf(scmConfig.getScmType()));
        if (scmService == null) {
            projectBranchResult.setProjectBranchInfos(defaultProjectBranchInfoList);
            return projectBranchResult;
        }
        List<BranchInfo> branchInfoList = scmService.getBranchInfo(scmConfig.getRepositoryUrl(),
                scmConfig.getAccessToken(), searchKeyword);
        if (CollectionUtils.isEmpty(branchInfoList)) {
            if (StringUtils.isNotBlank(searchKeyword)) {
                projectBranchResult.setIsDefaultData(false);
                projectBranchResult.setProjectBranchInfos(Collections.emptyList());
            }
            return projectBranchResult;
        }
        projectBranchResult.setIsDefaultData(false);
        projectBranchResult.setProjectBranchInfos(buildProjectBranchInfo(branchInfoList));
        return projectBranchResult;
    }

    private final Cache<String, String> scmCommitIdCache = CacheBuilder.newBuilder()
            .maximumSize(256)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    @Override
    public String getLatestCommitId(TbProject tbProject, TbInstance tbInstance) {
        String commitCacheKey = getCommitCacheKey(tbProject, tbInstance);
        String cachedCommitId = scmCommitIdCache.getIfPresent(commitCacheKey);
        if (cachedCommitId != null) {
            return cachedCommitId;
        }
        String commitIdFromDb = getBuildCommitIdFromDb(tbProject, tbInstance);
        if (commitIdFromDb != null) {
            scmCommitIdCache.put(commitCacheKey, commitIdFromDb);
        }
        return commitIdFromDb;
    }

    private String getBuildCommitIdFromDb(TbProject tbProject, TbInstance tbInstance) {
        ScmConfig scmConfig = JsonUtil.fromJson(tbProject.getScmConfig(), ScmConfig.class);
        ScmService scmService = scmTypeScmServiceMap.get(ScmType.valueOf(scmConfig.getScmType()));
        if (scmService == null) {
            return null;
        }

        String scmBranch = Optional.ofNullable(tbInstance.getScmBranch()).orElse(scmConfig.getDefaultBranch());

        List<BranchInfo> branchInfoList = scmService.getBranchInfo(scmConfig.getRepositoryUrl(),
                scmConfig.getAccessToken(), scmBranch);


        Optional<BranchInfo> branchInfoOptional = branchInfoList.stream()
                .filter(branchInfo -> scmBranch.equals(branchInfo.getName()))
                .findFirst();

        return branchInfoOptional.map(BranchInfo::getLatestCommitId).orElse(null);
    }

    private String getCommitCacheKey(TbProject tbProject, TbInstance tbInstance) {
        ScmConfig scmConfig = JsonUtil.fromJson(tbProject.getScmConfig(), ScmConfig.class);
        String scmBranch = Optional.ofNullable(tbInstance.getScmBranch()).orElse(scmConfig.getDefaultBranch());
        return scmConfig.getRepositoryUrl() + scmBranch;
    }
}
