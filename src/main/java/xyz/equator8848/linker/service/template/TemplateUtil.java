package xyz.equator8848.linker.service.template;


import cn.hutool.core.io.resource.ClassPathResource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import xyz.equator8848.inf.core.model.exception.PreCondition;
import xyz.equator8848.inf.core.model.exception.VerifyException;
import xyz.equator8848.inf.core.util.json.JsonUtil;
import xyz.equator8848.linker.model.constant.RouteMode;
import xyz.equator8848.linker.model.constant.SeparatorEnum;
import xyz.equator8848.linker.model.dto.DynamicAppConfiguration;
import xyz.equator8848.linker.model.po.TbInstance;
import xyz.equator8848.linker.model.po.TbProject;
import xyz.equator8848.linker.model.vo.project.ProxyConfig;
import xyz.equator8848.linker.model.vo.project.ProxyPassConfig;
import xyz.equator8848.linker.model.vo.project.ScmConfig;

import javax.annotation.Nullable;
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

    public static String getPackageScript(TbProject tbProject, TbInstance tbInstance) {
        if (Boolean.TRUE.equals(tbInstance.getPackageScriptOverrideFlag()) && StringUtils.isNotBlank(tbInstance.getPackageScript())) {
            return tbInstance.getPackageScript();
        }
        return tbProject.getPackageScript();
    }

    public static String getPackageOutputDir(TbProject tbProject, TbInstance tbInstance) {
        if (Boolean.TRUE.equals(tbInstance.getPackageOutputDirOverrideFlag())) {
            return tbInstance.getPackageOutputDir();
        }
        return tbProject.getPackageOutputDir();
    }

    public static String getDeployFolder(TbProject tbProject, TbInstance tbInstance) {
        if (Boolean.TRUE.equals(tbInstance.getDeployFolderOverrideFlag())) {
            return tbInstance.getDeployFolder();
        }
        return tbProject.getDeployFolder();
    }

    public static String getDeployFolderWithoutSlashOrBlank(TbProject tbProject, TbInstance tbInstance) {
        String deployFolder = getDeployFolder(tbProject, tbInstance);
        return StringUtils.isBlank(deployFolder) ? "" : removeAroundSlash(deployFolder);
    }

    public static String getDeployFolderStartWithSlashOrBlank(TbProject tbProject, TbInstance tbInstance) {
        String deployFolder = getDeployFolder(tbProject, tbInstance);
        return StringUtils.isBlank(deployFolder) ? "" : SeparatorEnum.SLASH.getSeparator() + deployFolder;
    }

    public static String getAccessEntrance(TbProject tbProject, TbInstance tbInstance) {
        if (Boolean.TRUE.equals(tbInstance.getAccessEntranceOverrideFlag())) {
            return tbInstance.getAccessEntrance();
        }
        return tbProject.getAccessEntrance();
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

    public static String getJenkinsFileTemplateByName(String templateName) {
        return getTemplateAsString("template/jenkins/", () -> "%s.xml".formatted(templateName));
    }

    private static String getNginxConfFileName(String templateId) {
        return String.format("NginxConf%s.conf".formatted(templateId));
    }

    public static String getNginxConfTemplate(String templateId) {
        return getTemplateAsString("template/nginx/", () -> getNginxConfFileName(templateId));
    }

    public static Integer getRouteMode(TbProject tbProject, TbInstance tbInstance) {
        if (Boolean.TRUE.equals(tbInstance.getRouteModeOverrideFlag())) {
            return Optional.ofNullable(tbInstance.getRouteMode()).orElse(RouteMode.HASH.ordinal());
        }
        return Optional.ofNullable(tbProject.getRouteMode()).orElse(RouteMode.HASH.ordinal());
    }

    /**
     * 需要对$进行转义
     * @param tbProject
     * @param tbInstance
     * @return
     */
    public static String getNginxRootConf(TbProject tbProject, TbInstance tbInstance) {
        Integer routeMode = getRouteMode(tbProject, tbInstance);
        if (routeMode.equals(RouteMode.HASH.ordinal())) {
            return """
                    location / {
                        root   /usr/share/nginx/html;
                        index  index.html index.htm;
                        if (\\$request_filename ~* .*.(?:htm|html)\\$) {
                            add_header Cache-Control "no-store, no-cache, must-revalidate, proxy-revalidate";
                        }
                    }
                    """;
        } else {
            return """
                    location / {
                        root   /usr/share/nginx/html;
                        try_files \\$uri \\$uri/ %s/index.html;
                        index  index.html index.htm;
                        if (\\$request_filename ~* .*.(?:htm|html)\\$) {
                            add_header Cache-Control "no-store, no-cache, must-revalidate, proxy-revalidate";
                        }
                    }
                    """.formatted(getDeployFolderStartWithSlashOrBlank(tbProject, tbInstance));
        }
    }

    public static String getNginxProxyPassConfig(TbInstance tbInstance) {
        String instanceProxyConfig = tbInstance.getProxyConfig();
        if (StringUtils.isBlank(instanceProxyConfig)) {
            return "";
        }
        ProxyConfig proxyConfig = JsonUtil.fromJson(instanceProxyConfig, ProxyConfig.class);
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
        String packageScript = getPackageScript(tbProject, tbInstance);
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

    public static String removeAroundSlash(String input) {
        if (StringUtils.isBlank(input)) {
            return "";
        }
        if (input.startsWith(SeparatorEnum.SLASH.getSeparator())) {
            input = input.substring(1);
        }
        if (input.endsWith(SeparatorEnum.SLASH.getSeparator())) {
            input = input.substring(0, input.length() - 1);
        }
        return input;
    }

    public static String buildDownloadInstanceArtifactUrl(DynamicAppConfiguration dynamicAppConfiguration, Long instanceId, @Nullable String fileName) {
        return "%s/api/v1/anonymous/download-instance-latest-artifact/%s?fileName=%s"
                .formatted(dynamicAppConfiguration.getLinkerServerHostBaseUrl(), instanceId, Optional.ofNullable(fileName).orElse("unknown"));
    }
}
