package com.equator.linker.service.impl;

import com.equator.core.model.exception.PreCondition;
import com.equator.core.util.json.JsonUtil;
import com.equator.linker.configuration.AppConfig;
import com.equator.linker.dao.service.InstanceDaoService;
import com.equator.linker.dao.service.ProjectDaoService;
import com.equator.linker.model.dto.DynamicAppConfiguration;
import com.equator.linker.model.po.TbInstance;
import com.equator.linker.model.po.TbProject;
import com.equator.linker.model.vo.instance.GetDockerfileRequest;
import com.equator.linker.model.vo.instance.GetNginxConfRequest;
import com.equator.linker.service.OpenService;
import com.equator.linker.service.util.TemplateUtil;
import com.equator.linker.service.util.sm4.SM4Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpenServiceImpl implements OpenService {
    @Autowired
    private AppConfig appConfig;

    @Autowired
    private InstanceDaoService instanceDaoService;

    @Autowired
    private ProjectDaoService projectDaoService;

    @Override
    public String getNginxConf(String getNginxConfSecret) {
        DynamicAppConfiguration appConfigConfig = appConfig.getConfig();
        String getNginxConfString = SM4Util.decryptBySM4ECB(getNginxConfSecret, appConfigConfig.getSm4SecretKey());
        GetNginxConfRequest getNginxConfRequest = JsonUtil.fromJson(getNginxConfString, GetNginxConfRequest.class);
        TbInstance tbInstance = instanceDaoService.getById(getNginxConfRequest.getInstanceId());
        PreCondition.isNotNull(tbInstance, "无法获取实例信息");
        String nginxConfTemplate = TemplateUtil.getNginxConfTemplate(tbInstance.getPipelineTemplateId());
        String nginxProxyPassConfig = TemplateUtil.getNginxProxyPassConfig(tbInstance);
        return nginxConfTemplate.replaceAll("\\$PROXY_PASS_CONF", nginxProxyPassConfig);
    }

    @Override
    public String getDockerfile(String getDockerfileSecret) {
        DynamicAppConfiguration appConfigConfig = appConfig.getConfig();
        String getDockerfileString = SM4Util.decryptBySM4ECB(getDockerfileSecret, appConfigConfig.getSm4SecretKey());
        GetDockerfileRequest getDockerfileRequest = JsonUtil.fromJson(getDockerfileString, GetDockerfileRequest.class);

        TbInstance tbInstance = instanceDaoService.getById(getDockerfileRequest.getInstanceId());
        PreCondition.isNotNull(tbInstance, "无法获取实例信息");

        TbProject tbProject = projectDaoService.getById(tbInstance.getProjectId());
        PreCondition.isNotNull(tbProject, "无法获取项目信息");

        String dockerfileTemplate = TemplateUtil.getDockerfileTemplate(tbInstance.getPipelineTemplateId());
        return dockerfileTemplate.replaceAll("\\$PACKAGE_OUTPUT_DIR", tbProject.getPackageOutputDir());
    }
}
