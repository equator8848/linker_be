package com.equator.linker.service;

import com.equator.core.model.exception.PreCondition;
import com.equator.linker.SpringBaseTest;
import com.equator.linker.dao.service.InstanceDaoService;
import com.equator.linker.dao.service.ProjectDaoService;
import com.equator.linker.model.po.TbInstance;
import com.equator.linker.model.po.TbProject;
import com.equator.linker.model.vo.instance.GetNginxConfRequest;
import com.equator.linker.service.util.TemplateUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OpenApiTest extends SpringBaseTest {
    @Autowired
    private InstanceDaoService instanceDaoService;

    @Autowired
    private ProjectDaoService projectDaoService;

    @Test
    public void test() {
        GetNginxConfRequest getNginxConfRequest = new GetNginxConfRequest();
        getNginxConfRequest.setInstanceId(1727915537415495682L);


        TbInstance tbInstance = instanceDaoService.getById(getNginxConfRequest.getInstanceId());
        PreCondition.isNotNull(tbInstance, "无法获取实例信息");

        TbProject tbProject = projectDaoService.getById(tbInstance.getProjectId());
        PreCondition.isNotNull(tbProject, "无法获取项目信息");


        String nginxConfTemplate = TemplateUtil.getNginxConfTemplate(tbInstance.getPipelineTemplateId());
        String nginxProxyPassConfig = TemplateUtil.getNginxProxyPassConfig(tbInstance);
        String nginxRootConf = TemplateUtil.getNginxRootConf(tbProject);
        String template = nginxConfTemplate
                .replaceAll("\\$ROOT_CONF", nginxRootConf)
                .replaceAll("\\$PROXY_PASS_CONF", nginxProxyPassConfig);
        System.out.println(template);
    }
}
