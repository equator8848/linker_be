package com.equator.linker.service.impl;

import com.equator.core.http.HttpUtil;
import com.equator.core.model.exception.PreCondition;
import com.equator.linker.configuration.AppConfig;
import com.equator.linker.dao.service.InstanceDaoService;
import com.equator.linker.model.constant.SeparatorEnum;
import com.equator.linker.model.po.TbInstance;
import com.equator.linker.service.FileService;
import com.equator.linker.service.template.TemplateBuilderServiceHolder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;
import okhttp3.Headers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Optional;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Autowired
    private InstanceDaoService instanceDaoService;

    @Autowired
    private TemplateBuilderServiceHolder templateBuilderServiceHolder;

    @Autowired
    private AppConfig appConfig;

    /**
     * 文件访问中转
     *
     * @param url      地址
     * @param fileName
     * @param resp     响应
     */
    public void fileForward(String url, String fileName, Headers headers, HttpServletResponse resp) {
        if (StringUtils.isBlank(url)) {
            return;
        }

        try (InputStream is = HttpUtil.getFile(url, headers);
             OutputStream os = resp.getOutputStream()) {
            resp.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            byte[] buffer = new byte[1024];
            int ch;
            while ((ch = is.read(buffer)) != -1) {
                os.write(buffer, 0, ch);
            }
            os.flush();
        } catch (IOException e) {
            log.error("fileForward error {}", url, e);
        }
    }

    @Override
    public void downloadInstanceArtifact(Long instanceId, @Nullable String fileName, HttpServletResponse resp) {
        log.info("try downloadInstanceArtifact instanceId {}, fileName {}", instanceId, fileName);
        Headers.Builder builder = new Headers.Builder();
        String jenkinsCredentials = appConfig.getConfig().getJenkinsCredentials();
        PreCondition.isFalse(StringUtils.isBlank(jenkinsCredentials), "无法获取Jenkins账密信息");

        String[] usernamePassword = jenkinsCredentials.split(SeparatorEnum.COLON.getSeparator());
        builder.add("Authorization", Credentials.basic(usernamePassword[0], usernamePassword[1]));

        TbInstance tbInstance = instanceDaoService.getById(instanceId);
        String imageArchiveUrl = templateBuilderServiceHolder.getTemplateBuilderServiceById(tbInstance.getPipelineTemplateId()).getImageArchiveUrl(tbInstance);
        fileForward(imageArchiveUrl, Optional.ofNullable(fileName).orElse("unknown"), builder.build(), resp);
    }

}
