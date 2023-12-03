package com.equator.linker.service.template;

import com.equator.linker.model.po.TbInstance;
import com.equator.linker.model.po.TbProject;
import com.equator.linker.service.template.model.JenkinsFileTemplateBuildData;

public interface TemplateBuilderService {
    String templateId();

    String getNginxProxyPassConfig(TbProject tbProject, TbInstance tbInstance);

    String getNginxConf(TbProject tbProject, TbInstance tbInstance);

    String getDockerfile(TbProject tbProject, TbInstance tbInstance);

    String getJenkinsFileTemplate(TbProject tbProject, TbInstance tbInstance, JenkinsFileTemplateBuildData buildData);

    String getImageArchiveUrl(TbInstance tbInstance);

    String getImageArchiveFileName(TbInstance tbInstance);
}
