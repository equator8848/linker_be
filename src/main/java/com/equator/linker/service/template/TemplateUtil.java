package com.equator.linker.service.template;


import cn.hutool.core.io.resource.ClassPathResource;
import com.equator.core.model.exception.PreCondition;
import com.equator.core.model.exception.VerifyException;
import com.equator.core.util.json.JsonUtil;
import com.equator.linker.model.constant.RouteMode;
import com.equator.linker.model.constant.SeparatorEnum;
import com.equator.linker.model.po.TbInstance;
import com.equator.linker.model.po.TbProject;
import com.equator.linker.model.vo.project.ProxyConfig;
import com.equator.linker.model.vo.project.ProxyPassConfig;
import com.equator.linker.model.vo.project.ScmConfig;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateUtil {
    public static String getStringOrDefault(String str, String defaultVal) {
        if (StringUtils.isBlank(str)) {
            return defaultVal;
        }
        return str;
    }

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

    public static String getNginxRootConf(TbProject tbProject) {
        Integer routeMode = Optional.ofNullable(tbProject.getRouteMode()).orElse(RouteMode.HASH.ordinal());
        if (routeMode.equals(RouteMode.HASH.ordinal())) {
            return """
                    location / {
                        root   /usr/share/nginx/html;
                        index  index.html index.htm;
                    }
                    """;
        } else {
            String deployFolder = tbProject.getDeployFolder();
            return """
                    location / {
                        root   /usr/share/nginx/html;
                        try_files \\$uri \\$uri/ %s/index.html;
                        index  index.html index.htm;
                    }
                    """.formatted(StringUtils.isBlank(deployFolder) ? "" : SeparatorEnum.SLASH.getSeparator() + deployFolder);
        }
    }

    public static String getNginxProxyPassConfig(TbInstance tbInstance) {
        ProxyConfig proxyConfig = JsonUtil.fromJson(tbInstance.getProxyConfig(), ProxyConfig.class);
        StringBuilder sb = new StringBuilder();
        List<ProxyPassConfig> proxyPassConfigs = proxyConfig.getProxyPassConfigs();
        for (ProxyPassConfig proxyPassConfig : proxyPassConfigs) {
            String location = proxyPassConfig.getLocation();
            String rewriteConfig = proxyPassConfig.getRewriteConfig();
            String proxyPass = proxyPassConfig.getProxyPass();
            if (StringUtils.isBlank(rewriteConfig)) {
                PreCondition.isTrue(!StringUtils.isBlank(proxyPass), "rewriteConfig与proxyPass不能同时为空");
                sb.append("""
                        location %s {
                            proxy_pass %s;
                        }
                         """.formatted(location, proxyPass));
            } else if (StringUtils.isBlank(proxyPass)) {
                PreCondition.isTrue(!StringUtils.isBlank(rewriteConfig), "rewriteConfig与proxyPass不能同时为空");
                sb.append("""
                        location %s {
                            rewrite %s;
                        }
                         """.formatted(location, rewriteConfig));
            } else {
                sb.append("""
                        location %s {
                            proxy_pass %s;
                            rewrite %s;
                        }
                         """.formatted(location, proxyPass, rewriteConfig));
            }
        }
        return sb.toString();
    }

    public static String getPackageScripts(TbProject tbProject, TbInstance tbInstance) {
        String packageScript = null;
        if (Boolean.TRUE.equals(tbInstance.getPackageScriptOverrideFlag()) && StringUtils.isNotBlank(tbInstance.getPackageScript())) {
            packageScript = tbInstance.getPackageScript();
        } else {
            packageScript = tbProject.getPackageScript();
        }
        String[] packageScriptsArr = packageScript.split("\n");
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

    public static String removeLeadingSlash(String input) {
        if (input == null) {
            return "";
        }
        if (input.startsWith(SeparatorEnum.SLASH.getSeparator())) {
            return input.substring(1);
        }
        return input;
    }

    public static String removeEndSlash(String input) {
        if (input == null) {
            return "";
        }
        if (input.endsWith(SeparatorEnum.SLASH.getSeparator())) {
            return input.substring(0, input.length() - 1);
        }
        return input;
    }
}
