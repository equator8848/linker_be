package xyz.equator8848.linker.service.template.impl;

import org.springframework.stereotype.Service;
import xyz.equator8848.linker.model.constant.RouteMode;
import xyz.equator8848.linker.model.po.TbInstance;
import xyz.equator8848.linker.model.po.TbProject;
import xyz.equator8848.linker.service.template.TemplateUtil;

import java.util.Optional;

@Service
public class TemplateBuilderService20240111 extends AbstractTemplateBuilderService {

    @Override
    public String templateId() {
        return "20240111";
    }

    @Override
    public String getIntro() {
        return "【beta】【定制化模板】生产环境打包模板，仅打包，不启动容器，配置一个静态资源访问路径，将其余请求都转发到网关（把 / 转发到 http://gateway_server）；需要配置容器环境变量gatewayServer1、gatewayServer2；";
    }


    private String getNginxStaticConf(TbProject tbProject, TbInstance tbInstance) {
        Integer routeMode = Optional.ofNullable(tbProject.getRouteMode()).orElse(RouteMode.HASH.ordinal());
        if (routeMode.equals(RouteMode.HASH.ordinal())) {
            return """
                    location ^~ /%s {
                        proxy_set_header Host \\$host;
                        proxy_set_header X-Real-IP \\$remote_addr;
                        proxy_set_header REMOTE-HOST \\$remote_addr;
                        proxy_set_header X-Forwarded-For \\$proxy_add_x_forwarded_for;
                        proxy_set_header Upgrade \\$http_upgrade;
                        proxy_set_header Connection 'upgrade';
                        
                        root   /usr/share/nginx/html;
                        index  index.html index.htm;
                    }
                    """.formatted(TemplateUtil.getDeployFolderWithoutSlashOrBlank(tbProject, tbInstance));
        } else {
            return """
                    location ^~ /%s {
                        proxy_set_header Host \\$host;
                        proxy_set_header X-Real-IP \\$remote_addr;
                        proxy_set_header REMOTE-HOST \\$remote_addr;
                        proxy_set_header X-Forwarded-For \\$proxy_add_x_forwarded_for;
                        proxy_set_header Upgrade \\$http_upgrade;
                        proxy_set_header Connection 'upgrade';
                           
                        root   /usr/share/nginx/html;
                        try_files \\$uri \\$uri/ %s/index.html;
                        index  index.html index.htm;
                    }
                    """.formatted(TemplateUtil.getDeployFolderWithoutSlashOrBlank(tbProject, tbInstance),
                    TemplateUtil.getDeployFolderStartWithSlashOrBlank(tbProject, tbInstance));
        }
    }

    @Override
    public String getNginxConf(TbProject tbProject, TbInstance tbInstance) {
        String nginxConfTemplate = TemplateUtil.getNginxConfTemplate(tbInstance.getPipelineTemplateId());
        String nginxStaticConf = getNginxStaticConf(tbProject, tbInstance);

        // replaceAll("\\$PROXY_PASS_CONF", nginxProxyPassConfig)
        // String nginxProxyPassConfig = getNginxProxyPassConfig(tbProject, tbInstance);
        return nginxConfTemplate
                .replaceAll("\\$STATIC_CONF", nginxStaticConf);
    }

}
