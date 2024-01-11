package xyz.equator8848.linker.service.template.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.equator8848.inf.core.util.json.JsonUtil;
import xyz.equator8848.linker.configuration.AppConfig;
import xyz.equator8848.linker.model.constant.SeparatorEnum;
import xyz.equator8848.linker.model.dto.DynamicAppConfiguration;
import xyz.equator8848.linker.model.po.TbInstance;
import xyz.equator8848.linker.model.po.TbProject;
import xyz.equator8848.linker.model.vo.project.ScmConfig;
import xyz.equator8848.linker.service.template.TemplateBuilderService;
import xyz.equator8848.linker.service.template.TemplateUtil;
import xyz.equator8848.linker.service.template.model.JenkinsFileTemplateBuildData;

import java.util.Optional;

@Service
public abstract class AbstractTemplateBuilderService implements TemplateBuilderService {
    @Autowired
    private AppConfig appConfig;

    @Override
    public String getNginxProxyPassConfig(TbProject tbProject, TbInstance tbInstance) {
        return TemplateUtil.getNginxProxyPassConfig(tbInstance);
    }

    @Override
    public String getNginxConf(TbProject tbProject, TbInstance tbInstance) {
        String nginxConfTemplate = TemplateUtil.getNginxConfTemplate(tbInstance.getPipelineTemplateId());
        String nginxProxyPassConfig = getNginxProxyPassConfig(tbProject, tbInstance);
        String nginxRootConf = TemplateUtil.getNginxRootConf(tbProject, tbInstance);
        return nginxConfTemplate
                .replaceAll("\\$ROOT_CONF", nginxRootConf)
                .replaceAll("\\$PROXY_PASS_CONF", nginxProxyPassConfig);
    }

    @Override
    public String getDockerfile(TbProject tbProject, TbInstance tbInstance) {
        String dockerfileTemplate = TemplateUtil.getDockerfileTemplate(tbInstance.getPipelineTemplateId());
        return dockerfileTemplate
                .replaceAll("\\$PACKAGE_OUTPUT_DIR", TemplateUtil.getPackageOutputDir(tbProject, tbInstance))
                .replaceAll("\\$DEPLOY_FOLDER", TemplateUtil
                        .removeAroundSlash(TemplateUtil.getDeployFolder(tbProject, tbInstance)));
    }

    @Override
    public String getJenkinsFileTemplate(TbProject tbProject, TbInstance tbInstance, JenkinsFileTemplateBuildData buildData) {
        Long instanceId = tbInstance.getId();

        String pipelineScriptsTemplate = TemplateUtil.getPipelineScriptsTemplate(tbInstance.getPipelineTemplateId());
        ScmConfig scmConfig = JsonUtil.fromJson(tbProject.getScmConfig(), ScmConfig.class);

        String scmProjectNameFromUrl = TemplateUtil.getScmProjectNameFromUrl(scmConfig.getRepositoryUrl());

        String scmUrlWithAccessToken = TemplateUtil.getScmUrlWithAccessToken(scmConfig);

        String packageScripts = TemplateUtil.getPackageScripts(tbProject, tbInstance);

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
                .replaceAll("\\$PACKAGE_OUTPUT_DIR", TemplateUtil.getPackageOutputDir(tbProject, tbInstance))
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

    @Override
    public String getImageArchiveUrl(TbInstance tbInstance) {
        // - http://172.16.8.2:28080/job/Pipeline_1727915537415495682/lastSuccessfulBuild/artifact/docker-container-img-1727915537415495682.tar
        // $DOCKER_CONTAINER_REPOSITORY_IMAGE_NAME-$DOCKER_CONTAINER_IMAGE_VERSION.tar.gz
        DynamicAppConfiguration dynamicAppConfiguration = appConfig.getConfig();
        return String.format("%s/job/%s/lastSuccessfulBuild/artifact/%s",
                TemplateUtil.removeEndSlash(dynamicAppConfiguration.getJenkinsEndpoint()),
                tbInstance.getPipelineName(),
                getImageArchiveFileName(tbInstance));
    }

    @Override
    public String getImageArchiveFileName(TbInstance tbInstance) {
        return "%s.tar.gz".formatted(String.format("%s-%s", tbInstance.getImageName(), tbInstance.getImageVersion()));
    }
}
