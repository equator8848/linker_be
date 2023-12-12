package xyz.equator8848.linker.service.template;

import xyz.equator8848.linker.model.po.TbInstance;
import xyz.equator8848.linker.model.po.TbProject;
import xyz.equator8848.linker.service.template.model.JenkinsFileTemplateBuildData;

public interface TemplateBuilderService {
    String templateId();

    String getNginxProxyPassConfig(TbProject tbProject, TbInstance tbInstance);

    String getNginxConf(TbProject tbProject, TbInstance tbInstance);

    String getDockerfile(TbProject tbProject, TbInstance tbInstance);

    String getJenkinsFileTemplate(TbProject tbProject, TbInstance tbInstance, JenkinsFileTemplateBuildData buildData);

    String getImageArchiveUrl(TbInstance tbInstance);

    String getImageArchiveFileName(TbInstance tbInstance);
}
