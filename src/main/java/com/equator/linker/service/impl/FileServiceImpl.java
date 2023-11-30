package com.equator.linker.service.impl;

import com.equator.core.http.HttpUtil;
import com.equator.linker.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    /**
     * 文件访问中转
     *
     * @param url  地址
     * @param resp 响应
     */
    public void fileForward(String url, HttpServletResponse resp) {
        if (StringUtils.isBlank(url)) {
            return;
        }
        try (InputStream is = HttpUtil.getFile(url);
             OutputStream os = resp.getOutputStream()) {

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

}
