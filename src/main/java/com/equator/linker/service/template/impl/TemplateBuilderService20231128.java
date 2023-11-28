package com.equator.linker.service.template.impl;

import com.equator.core.util.json.JsonUtil;
import com.equator.linker.model.constant.SeparatorEnum;
import com.equator.linker.model.po.TbInstance;
import com.equator.linker.model.po.TbProject;
import com.equator.linker.model.vo.project.ScmConfig;
import com.equator.linker.service.template.TemplateUtil;
import com.equator.linker.service.template.model.JenkinsFileTemplateBuildData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TemplateBuilderService20231128 extends AbstractTemplateBuilderService {
    @Override
    public String templateId() {
        return "20231128";
    }

    @Override
    public String getJenkinsFileTemplate(TbProject tbProject, TbInstance tbInstance, JenkinsFileTemplateBuildData buildData) {
        Long instanceId = tbInstance.getId();

        String pipelineScriptsTemplate = TemplateUtil.getPipelineScriptsTemplate(tbInstance.getPipelineTemplateId());
        ScmConfig scmConfig = JsonUtil.fromJson(tbProject.getScmConfig(), ScmConfig.class);

        String scmProjectNameFromUrl = TemplateUtil.getScmProjectNameFromUrl(scmConfig.getRepositoryUrl());

        String scmUrlWithAccessToken = TemplateUtil.getScmUrlWithAccessToken(scmConfig);

        String packageScripts = TemplateUtil.getPackageScripts(tbProject);

        String imageRepositoryPrefix = tbInstance.getImageRepositoryPrefix();
        if (StringUtils.isNotBlank(imageRepositoryPrefix)) {
            if (!imageRepositoryPrefix.endsWith(SeparatorEnum.SLASH.getSeparator())) {
                imageRepositoryPrefix = imageRepositoryPrefix + SeparatorEnum.SLASH.getSeparator();
            }
        } else {
            imageRepositoryPrefix = "";
        }

        String dockerContainerRepositoryImageName = "%s%s".formatted(imageRepositoryPrefix, tbInstance.getImageName());

        pipelineScriptsTemplate = pipelineScriptsTemplate
                .replaceAll("\\$PACKAGE_IMAGE", tbProject.getPackageImage())
                .replaceAll("\\$SCM_PROJECT_NAME", scmProjectNameFromUrl)
                .replaceAll("\\$SCM_BRANCH", tbInstance.getScmBranch())
                .replaceAll("\\$SCM_REPOSITORY_URL", scmUrlWithAccessToken)
                .replaceAll("\\$PACKAGE_SCRIPTS", packageScripts)
                .replaceAll("\\$PACKAGE_OUTPUT_DIR", tbProject.getPackageOutputDir())
                .replaceAll("\\$GET_NGINX_CONF_URL", buildData.getNginxConfUrl())
                .replaceAll("\\$GET_DOCKER_FILE_URL", buildData.getDockerFileUrl())
                .replaceAll("\\$DOCKER_CONTAINER_IMAGE_VERSION", tbInstance.getImageVersion())
                .replaceAll("\\$IMAGE_ARCHIVE_FLAG", tbInstance.getImageArchiveFlag().toString())
                .replaceAll("\\$DOCKER_CONTAINER_REPOSITORY_IMAGE_NAME", dockerContainerRepositoryImageName)
                .replaceAll("\\$DOCKER_CONTAINER_IMAGE_NAME", Optional.ofNullable(tbInstance.getImageName()).orElse(TemplateUtil.getDockerContainerImageName(instanceId)))
                .replaceAll("\\$DOCKER_CONTAINER_NAME", TemplateUtil.getDockerContainerName(instanceId))
                .replaceAll("\\$INSTANCE_ACCESS_PORT", String.valueOf(tbInstance.getAccessPort()));

        String jenkinsFileTemplate = TemplateUtil.getJenkinsFileTemplate(tbInstance.getPipelineTemplateId());
        jenkinsFileTemplate = jenkinsFileTemplate
                .replaceAll("\\$JOB_DESCRIPTION", "由Linker系统自动化创建，请勿手动修改")
                .replaceAll("\\$PIPELINE_SCRIPTS", pipelineScriptsTemplate);
        return jenkinsFileTemplate;
    }
}