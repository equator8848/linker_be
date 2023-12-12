package xyz.equator8848.linker.service;

import xyz.equator8848.inf.core.model.exception.PreCondition;
import xyz.equator8848.linker.SpringBaseTest;
import xyz.equator8848.linker.dao.service.InstanceDaoService;
import xyz.equator8848.linker.dao.service.ProjectDaoService;
import xyz.equator8848.linker.model.po.TbInstance;
import xyz.equator8848.linker.model.po.TbProject;
import xyz.equator8848.linker.model.vo.instance.GetNginxConfRequest;
import xyz.equator8848.linker.service.template.TemplateUtil;
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
