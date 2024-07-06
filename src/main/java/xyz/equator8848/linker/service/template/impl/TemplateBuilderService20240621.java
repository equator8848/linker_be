package xyz.equator8848.linker.service.template.impl;

import org.springframework.stereotype.Service;
import xyz.equator8848.inf.core.util.json.JsonUtil;
import xyz.equator8848.linker.model.po.TbInstance;
import xyz.equator8848.linker.model.po.TbProject;
import xyz.equator8848.linker.model.vo.project.ScmConfig;
import xyz.equator8848.linker.service.template.TemplateUtil;
import xyz.equator8848.linker.service.template.model.JenkinsFileTemplateBuildData;

import java.util.Optional;

@Service
public class TemplateBuilderService20240621 extends AbstractTemplateBuilderService {

    @Override
    public String templateId() {
        return "20240621";
    }

    @Override
    public String getIntro() {
        return "仅打包静态资源";
    }

    @Override
    public String getNginxProxyPassConfig(TbProject tbProject, TbInstance tbInstance) {
        throw new UnsupportedOperationException("此模板仅打包静态资源");
    }

    @Override
    public String getNginxConf(TbProject tbProject, TbInstance tbInstance) {
        throw new UnsupportedOperationException("此模板仅打包静态资源");
    }

    @Override
    public String getDockerfile(TbProject tbProject, TbInstance tbInstance) {
        throw new UnsupportedOperationException("此模板仅打包静态资源");
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
                .replaceAll("\\$SCM_COMMIT", tbInstance.getScmCommit())
                .replaceAll("\\$SCM_REPOSITORY_URL", scmUrlWithAccessToken)
                .replaceAll("\\$PACKAGE_SCRIPTS", packageScripts)
                .replaceAll("\\$PACKAGE_OUTPUT_DIR", TemplateUtil.getPackageOutputDir(tbProject, tbInstance))
                .replaceAll("\\$DOCKER_CONTAINER_IMAGE_NAME", Optional.ofNullable(tbInstance.getImageName()).orElse(TemplateUtil.getDockerContainerImageName(instanceId)))
                .replaceAll("\\$DOCKER_CONTAINER_IMAGE_VERSION", tbInstance.getImageVersion());

        String jenkinsFileTemplate = TemplateUtil.getJenkinsFileTemplate(tbInstance.getPipelineTemplateId());
        jenkinsFileTemplate = jenkinsFileTemplate
                .replaceAll("\\$JOB_DESCRIPTION", "由Linker系统自动化创建，请勿手动修改")
                .replaceAll("\\$PIPELINE_SCRIPTS", pipelineScriptsTemplate);
        return jenkinsFileTemplate;
    }

    @Override
    public String getImageArchiveUrl(TbInstance tbInstance) {
        return super.getImageArchiveUrl(tbInstance);
    }

    @Override
    public String getImageArchiveFileName(TbInstance tbInstance) {
        return super.getImageArchiveFileName(tbInstance);
    }
}
