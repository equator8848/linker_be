package xyz.equator8848.linker.service.template.impl;

import xyz.equator8848.linker.model.po.TbInstance;
import xyz.equator8848.linker.model.po.TbProject;
import xyz.equator8848.linker.model.vo.project.ScmConfig;
import xyz.equator8848.linker.service.template.TemplateUtil;
import xyz.equator8848.linker.service.template.model.JenkinsFileTemplateBuildData;
import xyz.equator8848.inf.core.util.json.JsonUtil;
import org.springframework.stereotype.Service;

@Service
public class TemplateBuilderService20231117 extends AbstractTemplateBuilderService {
    @Override
    public String templateId() {
        return "20231117";
    }

    @Override
    public String getJenkinsFileTemplate(TbProject tbProject, TbInstance tbInstance, JenkinsFileTemplateBuildData buildData) {
        Long instanceId = tbInstance.getId();

        String pipelineScriptsTemplate = TemplateUtil.getPipelineScriptsTemplate(tbInstance.getPipelineTemplateId());
        ScmConfig scmConfig = JsonUtil.fromJson(tbProject.getScmConfig(), ScmConfig.class);

        String scmProjectNameFromUrl = TemplateUtil.getScmProjectNameFromUrl(scmConfig.getRepositoryUrl());

        String scmUrlWithAccessToken = TemplateUtil.getScmUrlWithAccessToken(scmConfig);

        String packageScripts = TemplateUtil.getPackageScripts(tbProject, tbInstance);

        pipelineScriptsTemplate = pipelineScriptsTemplate
                .replaceAll("\\$PACKAGE_IMAGE", tbProject.getPackageImage())
                .replaceAll("\\$SCM_PROJECT_NAME", scmProjectNameFromUrl)
                .replaceAll("\\$SCM_BRANCH", tbInstance.getScmBranch())
                .replaceAll("\\$SCM_REPOSITORY_URL", scmUrlWithAccessToken)
                .replaceAll("\\$PACKAGE_SCRIPTS", packageScripts)
                .replaceAll("\\$PACKAGE_OUTPUT_DIR", TemplateUtil.getPackageOutputDir(tbProject, tbInstance))
                .replaceAll("\\$GET_NGINX_CONF_URL", buildData.getNginxConfUrl())
                .replaceAll("\\$GET_DOCKER_FILE_URL", buildData.getDockerFileUrl())
                .replaceAll("\\$DOCKER_CONTAINER_IMAGE_NAME", TemplateUtil.getDockerContainerImageName(instanceId))
                .replaceAll("\\$DOCKER_CONTAINER_NAME", TemplateUtil.getDockerContainerName(instanceId))
                .replaceAll("\\$INSTANCE_ACCESS_PORT", String.valueOf(tbInstance.getAccessPort()));

        String jenkinsFileTemplate = TemplateUtil.getJenkinsFileTemplate(tbInstance.getPipelineTemplateId());
        jenkinsFileTemplate = jenkinsFileTemplate
                .replaceAll("\\$JOB_DESCRIPTION", "由Linker系统自动化创建，请勿手动修改")
                .replaceAll("\\$PIPELINE_SCRIPTS", pipelineScriptsTemplate);
        return jenkinsFileTemplate;
    }
}
