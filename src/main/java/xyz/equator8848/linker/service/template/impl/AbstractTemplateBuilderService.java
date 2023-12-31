package xyz.equator8848.linker.service.template.impl;

import xyz.equator8848.linker.model.dto.DynamicAppConfiguration;
import xyz.equator8848.linker.model.po.TbInstance;
import xyz.equator8848.linker.model.po.TbProject;
import xyz.equator8848.linker.service.template.TemplateBuilderService;
import xyz.equator8848.linker.service.template.TemplateUtil;
import xyz.equator8848.linker.configuration.AppConfig;
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
                        .removeLeadingSlash(TemplateUtil.getDeployFolder(tbProject, tbInstance)));
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
