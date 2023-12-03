package com.equator.linker.service.impl;

import cn.hutool.core.util.EnumUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdancy.jenkins.rest.JenkinsClient;
import com.cdancy.jenkins.rest.domain.common.IntegerResponse;
import com.cdancy.jenkins.rest.domain.common.RequestStatus;
import com.cdancy.jenkins.rest.domain.job.BuildInfo;
import com.cdancy.jenkins.rest.domain.job.JobInfo;
import com.cdancy.jenkins.rest.domain.job.ProgressiveText;
import com.cdancy.jenkins.rest.features.JobsApi;
import com.equator.core.model.exception.InnerException;
import com.equator.core.model.exception.PreCondition;
import com.equator.core.util.json.JsonUtil;
import com.equator.linker.common.util.FormatUtil;
import com.equator.linker.common.util.UserAuthUtil;
import com.equator.linker.common.util.UserContextUtil;
import com.equator.linker.configuration.AppConfig;
import com.equator.linker.dao.service.*;
import com.equator.linker.model.constant.BaseConstant;
import com.equator.linker.model.constant.JenkinsPipelineBuildResult;
import com.equator.linker.model.constant.SeparatorEnum;
import com.equator.linker.model.dto.DynamicAppConfiguration;
import com.equator.linker.model.po.TbInstance;
import com.equator.linker.model.po.TbInstanceStar;
import com.equator.linker.model.po.TbInstanceUserRef;
import com.equator.linker.model.po.TbProject;
import com.equator.linker.model.vo.PageData;
import com.equator.linker.model.vo.instance.*;
import com.equator.linker.model.vo.project.ProxyConfig;
import com.equator.linker.model.vo.project.ScmConfig;
import com.equator.linker.service.InstanceService;
import com.equator.linker.service.jenkins.JenkinsClientFactory;
import com.equator.linker.service.template.TemplateBuilderServiceHolder;
import com.equator.linker.service.template.TemplateUtil;
import com.equator.linker.service.template.model.JenkinsFileTemplateBuildData;
import com.equator.linker.service.util.sm4.SM4Util;
import com.equator.linker.service.version.ImageVersionGenerator;
import com.equator.linker.service.version.ImageVersionGeneratorHolder;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InstanceServiceImpl implements InstanceService {
    @Autowired
    private ProjectDaoService projectDaoService;

    @Autowired
    private InstanceDaoService instanceDaoService;

    @Autowired
    private InstanceStarDaoService instanceStarDaoService;

    @Autowired
    private InstanceUserRefDaoService instanceUserRefDaoService;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private JenkinsClientFactory jenkinsClientFactory;

    @Autowired
    private UserDaoService userDaoService;

    @Autowired
    private TemplateBuilderServiceHolder templateBuilderServiceHolder;

    @Autowired
    private ProjectTemplateDaoService projectTemplateDaoService;

    @Autowired
    private ImageVersionGeneratorHolder imageVersionGeneratorHolder;

    @Autowired
    private PublicEntranceDaoService publicEntranceDaoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(InstanceCreateRequest instanceCreateRequest) {
        TbProject tbProject = projectDaoService.getById(instanceCreateRequest.getProjectId());
        PreCondition.isNotNull(tbProject, "项目不存在");

        DynamicAppConfiguration dynamicAppConfiguration = appConfig.getConfig();
        Integer nextAccessPort = instanceDaoService.getBaseMapper().getNextAccessPort(dynamicAppConfiguration.getMinAccessPort());

        TbInstance tbInstance = new TbInstance();
        tbInstance.setProjectId(instanceCreateRequest.getProjectId());
        tbInstance.setName(instanceCreateRequest.getName());
        tbInstance.setIntro(instanceCreateRequest.getIntro());

        if (StringUtils.isEmpty(instanceCreateRequest.getScmBranch())) {
            ScmConfig scmConfig = JsonUtil.fromJson(tbProject.getScmConfig(), ScmConfig.class);
            tbInstance.setScmBranch(scmConfig.getDefaultBranch());
        } else {
            tbInstance.setScmBranch(instanceCreateRequest.getScmBranch());
        }

        tbInstance.setPackageScriptOverrideFlag(instanceCreateRequest.getPackageScriptOverrideFlag());
        tbInstance.setPackageScript(instanceCreateRequest.getPackageScript());

        ProxyConfig requestProxyConfig = instanceCreateRequest.getProxyConfig();
        if (requestProxyConfig == null || CollectionUtils.isEmpty(requestProxyConfig.getProxyPassConfigs())) {
            ProxyConfig proxyConfig = JsonUtil.fromJson(tbProject.getProxyConfig(), ProxyConfig.class);
            tbInstance.setProxyConfig(JsonUtil.toJson(proxyConfig));
        } else {
            tbInstance.setProxyConfig(JsonUtil.toJson(requestProxyConfig));
        }

        tbInstance.setAccessPort(nextAccessPort);

        String deployFolder = Optional.ofNullable(tbProject.getDeployFolder()).orElse("");
        String accessEntrance = Optional.ofNullable(tbProject.getAccessEntrance()).orElse("");
        if (StringUtils.isNotBlank(deployFolder)) {
            if (!accessEntrance.startsWith(SeparatorEnum.SLASH.getSeparator())) {
                accessEntrance = SeparatorEnum.SLASH.getSeparator() + accessEntrance;
            }
        }
        tbInstance.setAccessLink(String.format("%s:%s/%s%s",
                dynamicAppConfiguration.getDeployAccessHost(), nextAccessPort,
                deployFolder,
                accessEntrance));
        tbInstance.setAccessLevel(BaseConstant.AccessLevel.valueOf(instanceCreateRequest.getAccessLevel()).getCode());

        tbInstance.setImageArchiveFlag(instanceCreateRequest.getImageArchiveFlag());
        if (instanceCreateRequest.getImageArchiveFlag()) {
            PreCondition.isTrue(StringUtils.isNotBlank(instanceCreateRequest.getImageRepositoryPrefix()), "生产环境打包时，镜像仓库前缀不能为空");
        }
        // 镜像版本处理
        tbInstance.setImageVersionType(instanceCreateRequest.getImageVersionType());
        ImageVersionGenerator imageVersionGenerator = imageVersionGeneratorHolder.getImageVersionGenerator(instanceCreateRequest.getImageVersionType());
        imageVersionGenerator.validate(instanceCreateRequest.getImageVersion());
        tbInstance.setImageVersion(instanceCreateRequest.getImageVersion());
        tbInstance.setImageRepositoryPrefix(instanceCreateRequest.getImageRepositoryPrefix());
        tbInstance.setImageName(TemplateUtil.getStringOrDefault(instanceCreateRequest.getImageName(), String.format("docker-container-img-%s", tbInstance.getId())));


        tbInstance.setPipelineTemplateId(tbProject.getPipelineTemplateId());
        tbInstance.setBuildingFlag(false);
        instanceDaoService.save(tbInstance);

        TbInstanceUserRef tbInstanceUserRef = new TbInstanceUserRef();
        tbInstanceUserRef.setInstanceId(tbInstance.getId());
        tbInstanceUserRef.setUserId(UserContextUtil.getUserId());
        tbInstanceUserRef.setRefType(BaseConstant.ProjectInstanceRefType.OWNER.ordinal());
        instanceUserRefDaoService.save(tbInstanceUserRef);
        return tbInstance.getId();
    }

    @Override
    public void update(InstanceUpdateRequest instanceUpdateRequest) {

        Long instanceId = instanceUpdateRequest.getId();
        TbInstance tbInstance = instanceDaoService.getById(instanceId);
        PreCondition.isNotNull(tbInstance, "实例不存在");
        UserAuthUtil.checkPermission(tbInstance.getCreateUserId());

        TbProject tbProject = projectDaoService.getById(tbInstance.getProjectId());
        PreCondition.isNotNull(tbProject, "项目不存在");

        tbInstance.setName(instanceUpdateRequest.getName());
        tbInstance.setIntro(instanceUpdateRequest.getIntro());

        String deployFolder = Optional.ofNullable(tbProject.getDeployFolder()).orElse("");
        String accessEntrance = Optional.ofNullable(tbProject.getAccessEntrance()).orElse("");
        if (StringUtils.isNotBlank(deployFolder)) {
            if (!accessEntrance.startsWith(SeparatorEnum.SLASH.getSeparator())) {
                accessEntrance = SeparatorEnum.SLASH.getSeparator() + accessEntrance;
            }
        } else {
            if (StringUtils.isNotBlank(accessEntrance) && accessEntrance.startsWith(SeparatorEnum.SLASH.getSeparator())) {
                accessEntrance = TemplateUtil.removeLeadingSlash(accessEntrance);
            }
        }
        DynamicAppConfiguration dynamicAppConfiguration = appConfig.getConfig();
        tbInstance.setAccessLink(String.format("%s:%s/%s%s",
                dynamicAppConfiguration.getDeployAccessHost(), tbInstance.getAccessPort(),
                deployFolder, accessEntrance));

        if (StringUtils.isNotEmpty(instanceUpdateRequest.getScmBranch())) {
            tbInstance.setScmBranch(instanceUpdateRequest.getScmBranch());
        }

        tbInstance.setPackageScriptOverrideFlag(instanceUpdateRequest.getPackageScriptOverrideFlag());
        tbInstance.setPackageScript(instanceUpdateRequest.getPackageScript());

        ProxyConfig requestProxyConfig = instanceUpdateRequest.getProxyConfig();
        if (requestProxyConfig != null && !CollectionUtils.isEmpty(requestProxyConfig.getProxyPassConfigs())) {
            tbInstance.setProxyConfig(JsonUtil.toJson(requestProxyConfig));
        }

        tbInstance.setAccessLevel(BaseConstant.AccessLevel.valueOf(instanceUpdateRequest.getAccessLevel()).getCode());
        tbInstance.setPipelineTemplateId(tbProject.getPipelineTemplateId());

        tbInstance.setImageArchiveFlag(instanceUpdateRequest.getImageArchiveFlag());
        if (instanceUpdateRequest.getImageArchiveFlag()) {
            PreCondition.isTrue(StringUtils.isNotBlank(instanceUpdateRequest.getImageRepositoryPrefix()), "生产环境打包时，镜像仓库前缀不能为空");
        }

        tbInstance.setImageVersionType(instanceUpdateRequest.getImageVersionType());
        ImageVersionGenerator imageVersionGenerator = imageVersionGeneratorHolder.getImageVersionGenerator(instanceUpdateRequest.getImageVersionType());
        imageVersionGenerator.validate(instanceUpdateRequest.getImageVersion());
        tbInstance.setImageVersion(instanceUpdateRequest.getImageVersion());
        tbInstance.setImageRepositoryPrefix(instanceUpdateRequest.getImageRepositoryPrefix());
        tbInstance.setImageName(TemplateUtil.getStringOrDefault(instanceUpdateRequest.getImageName(), String.format("docker-container-img-%s", tbInstance.getId())));

        instanceDaoService.updateById(tbInstance);

        if (BaseConstant.AccessLevel.PRIVATE.ordinal() == tbInstance.getAccessLevel()) {
            // 设置为私密实例，删除其它关联
            instanceUserRefDaoService.remove(Wrappers.<TbInstanceUserRef>lambdaQuery()
                    .eq(TbInstanceUserRef::getInstanceId, instanceId)
                    .eq(TbInstanceUserRef::getRefType, BaseConstant.ProjectInstanceRefType.JOIN.ordinal()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long instanceId) {
        TbInstance tbInstance = instanceDaoService.getById(instanceId);
        PreCondition.isNotNull(tbInstance, "实例不存在");
        instanceDaoService.removeById(instanceId);
        UserAuthUtil.checkPermission(tbInstance.getCreateUserId());
        instanceUserRefDaoService.remove(Wrappers.<TbInstanceUserRef>lambdaQuery()
                .eq(TbInstanceUserRef::getInstanceId, instanceId));
        publicEntranceDaoService.deleteByInstanceId(instanceId);

        try (JenkinsClient jenkinsClient = jenkinsClientFactory.buildJenkinsClient()) {
            JobsApi jobsApi = jenkinsClient.api().jobsApi();
            jobsApi.delete(null, tbInstance.getPipelineName());
        } catch (Exception e) {
            log.error("delete instance pipeline failed {}", instanceId, e);
        }
    }

    @Override
    public List<InstanceDetailsInfo> list(InstanceListRequest instanceListRequest) {
        // 获取自己创建或加入的
        Long userId = UserContextUtil.getUserId();
        Set<Long> targetInstanceIds = instanceUserRefDaoService.getInstanceIdByUserId(userId);
        // 公开的
        Set<Long> publicInstanceIds = instanceDaoService
                .getInstanceIdsByAccessLevel(BaseConstant.AccessLevel.PUBLIC.getCode(), targetInstanceIds);
        targetInstanceIds.addAll(publicInstanceIds);

        Long projectId = instanceListRequest.getProjectId();
        Set<Long> starInstanceId = instanceStarDaoService.getStarInstanceIds(projectId, userId);
        if (Boolean.TRUE.equals(instanceListRequest.getOnlyStar())) {
            targetInstanceIds = Sets.intersection(targetInstanceIds, starInstanceId);
        }

        if (CollectionUtils.isEmpty(targetInstanceIds)) {
            return Collections.emptyList();
        }


        return instanceDaoService.list(Wrappers.<TbInstance>lambdaQuery()
                        .like(StringUtils.isNotEmpty(instanceListRequest.getSearchKeyword()),
                                TbInstance::getName, instanceListRequest.getSearchKeyword())
                        .eq(TbInstance::getProjectId, projectId)
                        .in(TbInstance::getId, targetInstanceIds).orderByDesc(TbInstance::getId)).stream()
                .map(tbInstance -> {
                    InstanceDetailsInfo instanceDetailsInfo = new InstanceDetailsInfo();
                    BeanUtils.copyProperties(tbInstance, instanceDetailsInfo);
                    instanceDetailsInfo.setProxyConfig(JsonUtil.fromJson(tbInstance.getProxyConfig(), ProxyConfig.class));
                    instanceDetailsInfo.setAccessLevel(EnumUtil.getFieldBy(BaseConstant.AccessLevel::name,
                            BaseConstant.AccessLevel::getCode, tbInstance.getAccessLevel()));

                    boolean isOwner = tbInstance.getCreateUserId().equals(userId);
                    instanceDetailsInfo.setIsOwner(isOwner);

                    instanceDetailsInfo.setAccessUrl(tbInstance.getAccessLink());

                    instanceDetailsInfo.setCreateUserName(userDaoService.getUsernameFromCache(tbInstance.getCreateUserId()));
                    instanceDetailsInfo.setUpdateUserName(userDaoService.getUsernameFromCache(tbInstance.getUpdateUserId()));

                    instanceDetailsInfo.setStared(starInstanceId.contains(tbInstance.getId()));

                    if (Boolean.TRUE.equals(tbInstance.getImageArchiveFlag()) && Boolean.FALSE.equals(tbInstance.getBuildingFlag())) {
                        String imageArchiveFileName = templateBuilderServiceHolder
                                .getTemplateBuilderServiceById(tbInstance.getPipelineTemplateId())
                                .getImageArchiveFileName(tbInstance);
                        instanceDetailsInfo.setImageArchiveUrl(TemplateUtil.buildDownloadInstanceArtifactUrl(appConfig.getConfig(), tbInstance.getId(), imageArchiveFileName));
                    }

                    instanceDetailsInfo.setPipelineTemplateId(tbInstance.getPipelineTemplateId());
                    instanceDetailsInfo.setPipelineTemplateIntro(projectTemplateDaoService.getIntroFromCache(tbInstance.getPipelineTemplateId()));

                    instanceDetailsInfo.setInstancePipelineBuildResult(getInstancePipelineBuildResult(tbInstance));
                    return instanceDetailsInfo;
                }).collect(Collectors.toList());
    }

    @Override
    public PageData<InstanceSimpleInfo> all(InstanceListRequest instanceListRequest) {
        String searchKeyword = instanceListRequest.getSearchKeyword();
        LambdaQueryWrapper<TbInstance> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(TbInstance::getProjectId, instanceListRequest.getProjectId());
        queryWrapper
                .like(StringUtils.isNotBlank(searchKeyword), TbInstance::getName, searchKeyword)
                .or()
                .like(StringUtils.isNotBlank(searchKeyword), TbInstance::getIntro, searchKeyword);
        Page<TbInstance> tbInstancePageData = instanceDaoService.page(new Page<>(instanceListRequest.getPageNum(), instanceListRequest.getPageSize()), queryWrapper);
        return PageData.wrap(tbInstancePageData, tbInstancePageData.getRecords().stream().map(tbInstance -> {
            InstanceSimpleInfo instanceSimpleInfo = new InstanceSimpleInfo();
            BeanUtils.copyProperties(tbInstance, instanceSimpleInfo);
            instanceSimpleInfo.setId(tbInstance.getId());
            instanceSimpleInfo.setName(tbInstance.getName());
            instanceSimpleInfo.setIntro(tbInstance.getIntro());
            instanceSimpleInfo.setCreateUserName(userDaoService.getUsernameFromCache(tbInstance.getCreateUserId()));
            instanceSimpleInfo.setUpdateUserName(userDaoService.getUsernameFromCache(tbInstance.getUpdateUserId()));
            return instanceSimpleInfo;
        }).collect(Collectors.toList()));
    }

    private InstancePipelineBuildResult getInstancePipelineBuildResult(TbInstance tbInstance) {
        Long instanceId = tbInstance.getId();
        if (tbInstance.getBuildingFlag()) {
            return getPipelineBuildResult(instanceId);
        }
        InstancePipelineBuildResult instancePipelineBuildResult = new InstancePipelineBuildResult();
        instancePipelineBuildResult.setId(Optional.ofNullable(tbInstance.getLatestBuildNumber()).orElse(0).toString());
        instancePipelineBuildResult.setDuration(tbInstance.getLatestBuildDuration());
        instancePipelineBuildResult.setDurationStr(FormatUtil.msTimePretty(tbInstance.getLatestBuildDuration()));
        instancePipelineBuildResult.setSubmitTimeStr(buildSubmitTimeStr(tbInstance.getLatestSubmitTimestamp()));
        DynamicAppConfiguration dynamicAppConfiguration = appConfig.getConfig();
        if (tbInstance.getLatestSubmitTimestamp() != null) {
            instancePipelineBuildResult.setCanReBuildFlag((System.currentTimeMillis() - tbInstance.getLatestSubmitTimestamp()) > dynamicAppConfiguration.getJenkinsPipelineTimeoutMs());

        }
        instancePipelineBuildResult.setPipelineResultStr(buildPipelineResultStr(tbInstance.getLatestBuildResult()));
        instancePipelineBuildResult.setPipelineUrl(tbInstance.getLatestBuildPipelineUrl());
        return instancePipelineBuildResult;
    }

    private String buildSubmitTimeStr(Long latestSubmitTimestamp) {
        if (latestSubmitTimestamp == null) {
            return "未知";
        }
        return FormatUtil.msTimePretty(System.currentTimeMillis() - latestSubmitTimestamp);
    }

    private String buildPipelineResultStr(Integer latestBuildResult) {
        if (latestBuildResult == null) {
            return JenkinsPipelineBuildResult.UNKNOWN.getNameCn();
        }
        return EnumUtil.getFieldBy(JenkinsPipelineBuildResult::getNameCn,
                JenkinsPipelineBuildResult::ordinal, latestBuildResult);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void buildPipeline(Long instanceId) {
        TbInstance tbInstance = instanceDaoService.getById(instanceId);
        PreCondition.isNotNull(tbInstance, "找不到实例信息");

        TbProject tbProject = projectDaoService.getById(tbInstance.getProjectId());
        PreCondition.isNotNull(tbProject, "找不到项目信息");


        try (JenkinsClient jenkinsClient = jenkinsClientFactory.buildJenkinsClient()) {
            // 先设置镜像版本
            tbInstance.setImageVersion(imageVersionGeneratorHolder.getImageVersionGenerator(tbInstance.getImageVersionType()).genNextVersion(tbInstance));

            String pipelineName = TemplateUtil.getPipelineName(instanceId);
            JobsApi jobsApi = jenkinsClient.api().jobsApi();
            if (tbInstance.getLatestBuildNumber() == null) {
                // 未构建过
                createJob(jobsApi, tbProject, tbInstance, pipelineName);
                tbInstance.setPipelineName(pipelineName);
            } else {
                // 更新流水线配置
                String jobConfigXml = getJobConfigXml(tbProject, tbInstance);
                jobsApi.config(null, pipelineName, jobConfigXml);
            }
            JobInfo jobInfo = jobsApi.jobInfo(null, pipelineName);
            int nextBuildNumber = jobInfo.nextBuildNumber();
            tbInstance.setLatestBuildNumber(nextBuildNumber);
            tbInstance.setBuildingFlag(true);
            instanceDaoService.updateById(tbInstance);
            IntegerResponse buildPipelineResult = jobsApi.build(null, pipelineName);
            log.info("buildPipeline, instanceId {}, buildPipelineResult {}", instanceId, buildPipelineResult);
        } catch (IOException e) {
            log.error("buildPipeline error {}", instanceId, e);
            throw new InnerException("实例构建触发失败，请联系管理员");
        }
    }

    @Override
    public PipelineBuildLog getPipelineLog(Long instanceId) {
        TbInstance tbInstance = instanceDaoService.getById(instanceId);
        PreCondition.isNotNull(tbInstance, "找不到实例信息");
        try (JenkinsClient jenkinsClient = jenkinsClientFactory.buildJenkinsClient()) {
            JobsApi jobsApi = jenkinsClient.api().jobsApi();
            ProgressiveText progressiveText = jobsApi.progressiveText(null, tbInstance.getPipelineName(), tbInstance.getLatestBuildNumber());
            PipelineBuildLog pipelineBuildLog = new PipelineBuildLog();
            pipelineBuildLog.setHasMoreData(progressiveText.hasMoreData());
            pipelineBuildLog.setText(progressiveText.text());

            if (!pipelineBuildLog.getHasMoreData()) {
                pipelineBuildLog.setImageArchiveUrl(templateBuilderServiceHolder
                        .getTemplateBuilderServiceById(tbInstance.getPipelineTemplateId())
                        .getImageArchiveUrl(tbInstance));
            }
            return pipelineBuildLog;
        } catch (Exception e) {
            log.error("getPipelineLog error {}", instanceId, e);
            return null;
        }
    }

    private void createJob(JobsApi jobsApi, TbProject tbProject, TbInstance tbInstance, String pipelineName) {
        String jenkinsFileTemplate = getJobConfigXml(tbProject, tbInstance);
        RequestStatus requestStatus = jobsApi.create(null, pipelineName, jenkinsFileTemplate);
        PreCondition.isTrue(requestStatus.value(), "Jenkins流水线创建失败");
    }

    private String getJobConfigXml(TbProject tbProject, TbInstance tbInstance) {
        Long instanceId = tbInstance.getId();
        DynamicAppConfiguration dynamicAppConfiguration = appConfig.getConfig();

        String linkerServerHostBaseUrl = dynamicAppConfiguration.getLinkerServerHostBaseUrl();

        GetNginxConfRequest getNginxConfRequest = new GetNginxConfRequest();
        getNginxConfRequest.setInstanceId(instanceId);

        String getNginxConfSecret = SM4Util.encryptBySM4ECB(JsonUtil.toJson(getNginxConfRequest), dynamicAppConfiguration.getSm4SecretKey());
        String GET_NGINX_CONF_URL = linkerServerHostBaseUrl + "/api/v1/open-api/nginx-conf?getNginxConfSecret=%s".formatted(getNginxConfSecret);

        GetDockerfileRequest getDockerfileRequest = new GetDockerfileRequest();
        getDockerfileRequest.setInstanceId(instanceId);
        String getDockerfileSecret = SM4Util.encryptBySM4ECB(JsonUtil.toJson(getDockerfileRequest), dynamicAppConfiguration.getSm4SecretKey());
        String GET_DOCKER_FILE_URL = linkerServerHostBaseUrl + "/api/v1/open-api/dockerfile?getDockerfileSecret=%s".formatted(getDockerfileSecret);

        JenkinsFileTemplateBuildData buildData = new JenkinsFileTemplateBuildData();
        buildData.setDockerFileUrl(GET_DOCKER_FILE_URL);
        buildData.setNginxConfUrl(GET_NGINX_CONF_URL);

        return templateBuilderServiceHolder.getTemplateBuilderServiceById(tbInstance.getPipelineTemplateId())
                .getJenkinsFileTemplate(tbProject, tbInstance, buildData);
    }

    @Override
    public InstancePipelineBuildResult getPipelineBuildResult(Long instanceId) {
        TbInstance tbInstance = instanceDaoService.getById(instanceId);
        PreCondition.isNotNull(tbInstance, "找不到实例信息");

        try (JenkinsClient jenkinsClient = jenkinsClientFactory.buildJenkinsClient()) {
            JobsApi jobsApi = jenkinsClient.api().jobsApi();

            BuildInfo buildInfo = jobsApi.buildInfo(null, tbInstance.getPipelineName(), tbInstance.getLatestBuildNumber());
            InstancePipelineBuildResult instancePipelineBuildResult = new InstancePipelineBuildResult();
            if (buildInfo == null) {
                instancePipelineBuildResult.setCanReBuildFlag(true);
                return instancePipelineBuildResult;
            }
            instancePipelineBuildResult.setId(buildInfo.id());
            instancePipelineBuildResult.setDuration(buildInfo.duration());
            instancePipelineBuildResult.setPipelineResultStr(FormatUtil.msTimePretty(buildInfo.duration()));

            instancePipelineBuildResult.setSubmitTimeStr(buildSubmitTimeStr(buildInfo.timestamp()));
            DynamicAppConfiguration dynamicAppConfiguration = appConfig.getConfig();
            instancePipelineBuildResult.setCanReBuildFlag((System.currentTimeMillis() - buildInfo.timestamp()) > dynamicAppConfiguration.getJenkinsPipelineTimeoutMs());
            String result = buildInfo.result();

            boolean building = buildInfo.building();
            tbInstance.setBuildingFlag(building);
            tbInstance.setLatestBuildDuration(buildInfo.duration());
            if (building) {
                tbInstance.setLatestBuildResult(JenkinsPipelineBuildResult.BUILDING.ordinal());
                instancePipelineBuildResult.setPipelineResultStr(JenkinsPipelineBuildResult.BUILDING.getNameCn());
            } else {
                if (JenkinsPipelineBuildResult.SUCCESS.name().equals(result)) {
                    tbInstance.setLatestBuildResult(JenkinsPipelineBuildResult.SUCCESS.ordinal());
                    instancePipelineBuildResult.setPipelineResultStr(JenkinsPipelineBuildResult.SUCCESS.getNameCn());
                } else {
                    tbInstance.setLatestBuildResult(JenkinsPipelineBuildResult.FAILURE.ordinal());
                    instancePipelineBuildResult.setPipelineResultStr(JenkinsPipelineBuildResult.FAILURE.getNameCn());
                }
            }
            String pipelineUrl = buildInfo.url();
            tbInstance.setLatestBuildPipelineUrl(pipelineUrl);
            tbInstance.setLatestSubmitTimestamp(buildInfo.timestamp());
            instancePipelineBuildResult.setPipelineUrl(pipelineUrl);

            instanceDaoService.updateById(tbInstance);
            return instancePipelineBuildResult;
        } catch (Exception e) {
            log.error("getPipelineBuildResult error {}", instanceId, e);
            throw new InnerException("获取流水线构建状态失败，请联系管理员");
        }
    }

    @Override
    public void instanceStarAction(InstanceStarRequest instanceStarRequest) {
        if (instanceStarRequest.getStarAction()) {
            TbInstanceStar tbInstanceStar = new TbInstanceStar();
            tbInstanceStar.setProjectId(instanceStarRequest.getProjectId());
            tbInstanceStar.setInstanceId(instanceStarRequest.getInstanceId());
            tbInstanceStar.setStarUserId(UserContextUtil.getUserId());
            instanceStarDaoService.save(tbInstanceStar);
        } else {
            instanceStarDaoService.getBaseMapper()
                    .unStarInstance(instanceStarRequest.getProjectId(),
                            UserContextUtil.getUserId(),
                            instanceStarRequest.getInstanceId());
        }
    }
}
