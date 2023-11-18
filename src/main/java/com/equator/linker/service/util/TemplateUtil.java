package com.equator.linker.service.util;


import cn.hutool.core.io.resource.ClassPathResource;
import com.equator.core.model.exception.VerifyException;
import com.equator.core.util.json.JsonUtil;
import com.equator.linker.model.po.TbInstance;
import com.equator.linker.model.po.TbProject;
import com.equator.linker.model.vo.project.ProxyConfig;
import com.equator.linker.model.vo.project.ProxyPassConfig;
import com.equator.linker.model.vo.project.ScmConfig;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateUtil {
    public static String getPipelineName(Long instanceId) {
        return String.format("Pipeline_%s", instanceId);
    }

    public static String getDockerContainerImageName(Long instanceId) {
        return String.format("docker-container-img-%s", instanceId);
    }

    public static String getDockerContainerName(Long instanceId) {
        return String.format("docker-container-%s", instanceId);
    }

    private static String getDockerfileFileName(String templateId) {
        return String.format("Dockerfile%s.txt".formatted(templateId));
    }

    public static String getDockerfileTemplate(String templateId) {
        return getTemplateAsString("template/docker/", () -> getDockerfileFileName(templateId));
    }

    private static String getJenkinsFileTemplateFileName(String templateId) {
        return String.format("JenkinsFile%s.xml".formatted(templateId));
    }

    public static String getJenkinsFileTemplate(String templateId) {
        return getTemplateAsString("template/jenkins/", () -> getJenkinsFileTemplateFileName(templateId));
    }

    private static String getNginxConfFileName(String templateId) {
        return String.format("NginxConf%s.conf".formatted(templateId));
    }

    public static String getNginxConfTemplate(String templateId) {
        return getTemplateAsString("template/nginx/", () -> getNginxConfFileName(templateId));
    }

    public static String getNginxProxyPassConfig(TbInstance tbInstance) {
        ProxyConfig proxyConfig = JsonUtil.fromJson(tbInstance.getProxyConfig(), ProxyConfig.class);
        StringBuilder sb = new StringBuilder();
        List<ProxyPassConfig> proxyPassConfigs = proxyConfig.getProxyPassConfigs();
        for (ProxyPassConfig proxyPassConfig : proxyPassConfigs) {
            sb.append("""
                    location %s {
                        proxy_pass %s;
                    }
                     """.formatted(proxyPassConfig.getLocation(), proxyPassConfig.getProxyPass()));
        }
        return sb.toString();
    }

    public static String getPackageScripts(TbProject tbProject) {
        String[] packageScriptsArr = tbProject.getPackageScript().split("\n");
        StringBuilder sb = new StringBuilder();
        for (String commandLine : packageScriptsArr) {
            sb.append("sh '%s'\n".formatted(commandLine));
        }
        return sb.toString();
    }

    public static String getScmProjectNameFromUrl(String url) {
        Pattern pattern = Pattern.compile(".*/(.*).git");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new VerifyException("无法从SCM仓库地址获取仓库名称，请检查仓库地址是否以.git结尾");
    }

    /**
     * http://gitlab.localhost.com/equator/linker-fe.git
     * http://equator:glpat-kFqFt1vEP_DGRHf3mxuE@gitlab.localhost.com/equator/linker-fe.git
     *
     * @param scmConfig
     * @return
     */
    public static String getScmUrlWithAccessToken(ScmConfig scmConfig) {
        String usernameAndToken = "%s:%s@".formatted(scmConfig.getUsername(), scmConfig.getAccessToken());
        return scmConfig.getRepositoryUrl().replaceFirst("://(.*?)/", "://%s$1/".formatted(usernameAndToken));
    }

    private static String getPipelineScriptsFileName(String templateId) {
        return String.format("PipelineScripts%s.txt".formatted(templateId));
    }

    public static String getPipelineScriptsTemplate(String templateId) {
        return getTemplateAsString("template/pipelineScripts/", () -> getPipelineScriptsFileName(templateId));
    }


    private static String getTemplateAsString(String path, Supplier<String> fileNameSupplier) {
        ClassPathResource classPathResource = new ClassPathResource(path + fileNameSupplier.get());
        InputStream inputStream = classPathResource.getStream();
        try {
            return IOUtils.toString(inputStream, Charset.defaultCharset());
        } catch (IOException e) {
            return null;
        }
    }
}
