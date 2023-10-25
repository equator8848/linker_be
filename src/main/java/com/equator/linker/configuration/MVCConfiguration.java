package com.equator.linker.configuration;


import com.equator.linker.configuration.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Equator
 * @Date: 2020/2/4 22:48
 **/

@Configuration
public class MVCConfiguration implements WebMvcConfigurer {
    /**
     * 添加拦截器
     */
    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> interceptorList = new ArrayList<>();
        //强制拦截器 拦截所有请求
        interceptorList.add("/api/**");
        List<String> passList = new ArrayList<>();
        passList.add("/api/v1/user/login");
        passList.add("/api/v1/user/captcha");
        passList.add("/api/v1/user/secondary-verify");
        passList.add("/api/v1/admin/ping*");
        passList.add("/api/v1/admin/test*");
        passList.add("/api/v1/anonymous/**");
        registry.addInterceptor(loginInterceptor()).addPathPatterns(interceptorList).excludePathPatterns(passList);
    }
}
