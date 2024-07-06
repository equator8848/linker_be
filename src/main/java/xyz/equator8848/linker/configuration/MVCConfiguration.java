package xyz.equator8848.linker.configuration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.equator8848.inf.auth.interceptor.ApiPermissionInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Equator
 * @Date: 2020/2/4 22:48
 **/

@Order(0)
@Configuration
public class MVCConfiguration implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = jsonConverter.getObjectMapper();
        // 序列换成json时,将所有的Long变成string
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        jsonConverter.setObjectMapper(objectMapper);
        converters.add(jsonConverter);
    }


    /**
     * 添加拦截器
     */
    @Bean
    public ApiPermissionInterceptor loginInterceptor() {
        return new ApiPermissionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> interceptorList = new ArrayList<>();
        //强制拦截器 拦截所有请求
        interceptorList.add("/api/**");
        List<String> passList = new ArrayList<>();
        passList.add("/api/v1/auth/captcha");
        passList.add("/api/v1/auth/login");
        passList.add("/api/v1/anonymous/**");
        passList.add("/api/v1/open-api/**");
        registry.addInterceptor(loginInterceptor()).addPathPatterns(interceptorList).excludePathPatterns(passList);
    }
}
