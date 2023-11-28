package com.equator.linker.service.template.impl;

import com.equator.linker.configuration.AppConfig;
import com.equator.linker.model.dto.DynamicAppConfiguration;
import com.equator.linker.model.po.TbInstance;
import com.equator.linker.model.po.TbProject;
import com.equator.linker.service.template.TemplateBuilderService;
import com.equator.linker.service.template.TemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        String nginxRootConf = TemplateUtil.getNginxRootConf(tbProject);
        return nginxConfTemplate
                .replaceAll("\\$ROOT_CONF", nginxRootConf)
                .replaceAll("\\$PROXY_PASS_CONF", nginxProxyPassConfig);
    }

    @Override
    public String getDockerfile(TbProject tbProject, TbInstance tbInstance) {
        String dockerfileTemplate = TemplateUtil.getDockerfileTemplate(tbInstance.getPipelineTemplateId());
        return dockerfileTemplate
                .replaceAll("\\$PACKAGE_OUTPUT_DIR", tbProject.getPackageOutputDir())
                .replaceAll("\\$DEPLOY_FOLDER", TemplateUtil.removeLeadingSlash(tbProject.getDeployFolder()));
    }

    @Override
    public String getImageArchiveUrl(TbInstance tbInstance) {
        // - http://172.16.8.2:28080/job/Pipeline_1727915537415495682/lastSuccessfulBuild/artifact/docker-container-img-1727915537415495682.tar
        // $DOCKER_CONTAINER_REPOSITORY_IMAGE_NAME-$DOCKER_CONTAINER_IMAGE_VERSION.tar.gz
        DynamicAppConfiguration dynamicAppConfiguration = appConfig.getConfig();
        return String.format("%s/job/%s/lastSuccessfulBuild/artifact/%s.tar.gz",
                TemplateUtil.removeEndSlash(dynamicAppConfiguration.getJenkinsEndpoint()),
                tbInstance.getPipelineName(),
                String.format("%s-%s", tbInstance.getImageName(), tbInstance.getImageVersion()));
    }
}
