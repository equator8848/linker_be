package com.equator.linker.service.impl;

import cn.hutool.core.util.EnumUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cdancy.jenkins.rest.JenkinsClient;
import com.cdancy.jenkins.rest.domain.common.IntegerResponse;
import com.cdancy.jenkins.rest.domain.common.RequestStatus;
import com.cdancy.jenkins.rest.domain.job.BuildInfo;
import com.cdancy.jenkins.rest.domain.job.JobInfo;
import com.cdancy.jenkins.rest.features.JobsApi;
import com.equator.core.model.exception.InnerException;
import com.equator.core.model.exception.PreCondition;
import com.equator.core.util.json.JsonUtil;
import com.equator.linker.common.util.FormatUtil;
import com.equator.linker.common.util.UserAuthUtil;
import com.equator.linker.common.util.UserContextUtil;
import com.equator.linker.configuration.AppConfig;
import com.equator.linker.dao.service.InstanceDaoService;
import com.equator.linker.dao.service.InstanceUserRefDaoService;
import com.equator.linker.dao.service.ProjectDaoService;
import com.equator.linker.model.constant.BaseConstant;
import com.equator.linker.model.constant.JenkinsPipelineBuildResult;
import com.equator.linker.model.dto.DynamicAppConfiguration;
import com.equator.linker.model.po.TbInstance;
import com.equator.linker.model.po.TbInstanceUserRef;
import com.equator.linker.model.po.TbProject;
import com.equator.linker.model.vo.instance.*;
import com.equator.linker.model.vo.project.ProxyConfig;
import com.equator.linker.model.vo.project.ScmConfig;
import com.equator.linker.service.InstanceService;
import com.equator.linker.service.jenkins.JenkinsClientFactory;
import com.equator.linker.service.util.TemplateUtil;
import com.equator.linker.service.util.sm4.SM4Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
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
    private InstanceUserRefDaoService instanceUserRefDaoService;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private JenkinsClientFactory jenkinsClientFactory;

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

        ProxyConfig requestProxyConfig = instanceCreateRequest.getProxyConfig();
        if (requestProxyConfig == null || CollectionUtils.isEmpty(requestProxyConfig.getProxyPassConfigs())) {
            ProxyConfig proxyConfig = JsonUtil.fromJson(tbProject.getProxyConfig(), ProxyConfig.class);
            tbInstance.setProxyConfig(JsonUtil.toJson(proxyConfig));
        } else {
            tbInstance.setProxyConfig(JsonUtil.toJson(requestProxyConfig));
        }

        tbInstance.setAccessPort(nextAccessPort);
        tbInstance.setAccessLink(String.format("%s:%s%s",
                dynamicAppConfiguration.getDeployAccessHost(), nextAccessPort,
                Optional.ofNullable(tbProject.getAccessEntrance()).orElse("/")));
        tbInstance.setAccessLevel(BaseConstant.AccessLevel.valueOf(instanceCreateRequest.getAccessLevel()).getCode());

        tbInstance.setPipelineTemplateId(dynamicAppConfiguration.getJenkinsPipelineTemplateId());
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

        tbInstance.setName(instanceUpdateRequest.getName());
        tbInstance.setIntro(instanceUpdateRequest.getIntro());

        if (StringUtils.isNotEmpty(instanceUpdateRequest.getScmBranch())) {
            tbInstance.setScmBranch(instanceUpdateRequest.getScmBranch());
        }

        ProxyConfig requestProxyConfig = instanceUpdateRequest.getProxyConfig();
        if (requestProxyConfig != null && !CollectionUtils.isEmpty(requestProxyConfig.getProxyPassConfigs())) {
            tbInstance.setProxyConfig(JsonUtil.toJson(requestProxyConfig));
        }

        tbInstance.setAccessLevel(BaseConstant.AccessLevel.valueOf(instanceUpdateRequest.getAccessLevel()).getCode());
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
        Set<Long> targetInstanceIds = instanceUserRefDaoService.getInstanceIdByUserId(UserContextUtil.getUserId());
        // 公开的
        Set<Long> publicInstanceIds = instanceDaoService
                .getInstanceIdsByAccessLevel(BaseConstant.AccessLevel.PUBLIC.getCode(), targetInstanceIds);
        targetInstanceIds.addAll(publicInstanceIds);

        return instanceDaoService.list(Wrappers.<TbInstance>lambdaQuery()
                        .like(StringUtils.isNotEmpty(instanceListRequest.getSearchKeyword()),
                                TbInstance::getName, instanceListRequest.getSearchKeyword())
                        .eq(TbInstance::getProjectId, instanceListRequest.getProjectId())
                        .in(!CollectionUtils.isEmpty(targetInstanceIds),
                                TbInstance::getId, targetInstanceIds).orderByDesc(TbInstance::getId)).stream()
                .map(tbInstance -> {
                    InstanceDetailsInfo instanceDetailsInfo = new InstanceDetailsInfo();
                    BeanUtils.copyProperties(tbInstance, instanceDetailsInfo);
                    instanceDetailsInfo.setProxyConfig(JsonUtil.fromJson(tbInstance.getProxyConfig(), ProxyConfig.class));
                    instanceDetailsInfo.setAccessLevel(EnumUtil.getFieldBy(BaseConstant.AccessLevel::name,
                            BaseConstant.AccessLevel::getCode, tbInstance.getAccessLevel()));

                    boolean isOwner = tbInstance.getCreateUserId().equals(UserContextUtil.getUserId());
                    instanceDetailsInfo.setIsOwner(isOwner);

                    instanceDetailsInfo.setAccessUrl(tbInstance.getAccessLink());

                    instanceDetailsInfo.setInstancePipelineBuildResult(getInstancePipelineBuildResult(tbInstance));
                    return instanceDetailsInfo;
                }).collect(Collectors.toList());
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
            String pipelineName = TemplateUtil.getPipelineName(instanceId);
            JobsApi jobsApi = jenkinsClient.api().jobsApi();
            if (tbInstance.getLatestBuildNumber() == null) {
                // 未构建过
                createJob(jobsApi, tbProject, tbInstance, pipelineName);
                tbInstance.setPipelineName(pipelineName);
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

    private void createJob(JobsApi jobsApi, TbProject tbProject, TbInstance tbInstance, String pipelineName) {
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


        String pipelineScriptsTemplate = TemplateUtil.getPipelineScriptsTemplate(tbInstance.getPipelineTemplateId());
        ScmConfig scmConfig = JsonUtil.fromJson(tbProject.getScmConfig(), ScmConfig.class);

        String scmProjectNameFromUrl = TemplateUtil.getScmProjectNameFromUrl(scmConfig.getRepositoryUrl());

        String scmUrlWithAccessToken = TemplateUtil.getScmUrlWithAccessToken(scmConfig);

        String packageScripts = TemplateUtil.getPackageScripts(tbProject);

        pipelineScriptsTemplate = pipelineScriptsTemplate
                .replaceAll("\\$PACKAGE_IMAGE", tbProject.getPackageImage())
                .replaceAll("\\$SCM_PROJECT_NAME", scmProjectNameFromUrl)
                .replaceAll("\\$SCM_BRANCH", tbInstance.getScmBranch())
                .replaceAll("\\$SCM_REPOSITORY_URL", scmUrlWithAccessToken)
                .replaceAll("\\$PACKAGE_SCRIPTS", packageScripts)
                .replaceAll("\\$PACKAGE_OUTPUT_DIR", tbProject.getPackageOutputDir())
                .replaceAll("\\$GET_NGINX_CONF_URL", GET_NGINX_CONF_URL)
                .replaceAll("\\$GET_DOCKER_FILE_URL", GET_DOCKER_FILE_URL)
                .replaceAll("\\$DOCKER_CONTAINER_IMAGE_NAME", TemplateUtil.getDockerContainerImageName(instanceId))
                .replaceAll("\\$DOCKER_CONTAINER_NAME", TemplateUtil.getDockerContainerName(instanceId))
                .replaceAll("\\$INSTANCE_ACCESS_PORT", String.valueOf(tbInstance.getAccessPort()));

        String jenkinsFileTemplate = TemplateUtil.getJenkinsFileTemplate(tbInstance.getPipelineTemplateId());
        jenkinsFileTemplate = jenkinsFileTemplate
                .replaceAll("\\$JOB_DESCRIPTION", "由Linker系统自动化创建，请勿手动修改")
                .replaceAll("\\$PIPELINE_SCRIPTS", pipelineScriptsTemplate);
        RequestStatus requestStatus = jobsApi.create(null, pipelineName, jenkinsFileTemplate);
        PreCondition.isTrue(requestStatus.value(), "Jenkins流水线创建失败");
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
                return instancePipelineBuildResult;
            }
            instancePipelineBuildResult.setId(buildInfo.id());
            instancePipelineBuildResult.setDuration(buildInfo.duration());
            instancePipelineBuildResult.setPipelineResultStr(FormatUtil.msTimePretty(buildInfo.duration()));

            instancePipelineBuildResult.setSubmitTimeStr(buildSubmitTimeStr(buildInfo.timestamp()));
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
}
