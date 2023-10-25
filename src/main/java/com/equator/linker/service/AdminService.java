package com.equator.linker.service;



import com.equator.linker.configuration.AppConfig;
import com.equator.linker.configuration.BaseConfiguration;
import com.equator.linker.model.dto.DynamicAppConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;


/**
 * @Author: Equator
 * @Date: 2021/2/10 10:32
 **/
@Slf4j
@Service
public class AdminService {
    @Autowired
    private AppConfig appConfig;

    @Autowired
    private BaseConfiguration baseConfiguration;

    public Date ping(HttpServletRequest request) {
        Date date = new Date();
        //获取请求头信息
        StringBuilder headers = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        //使用循环遍历请求头，并通过getHeader()方法获取一个指定名称的头字段
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.append(String.format("%s : %s\n", headerName, request.getHeader(headerName)));
        }
        log.info("pong {}, headers {}", date, headers);
        return date;
    }
}
