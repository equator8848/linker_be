package com.equator.linker.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Equator
 * @Date: 2021/11/21 23:36
 **/
@Data
@Configuration
public class BaseConfiguration {

    /**
     * 环境
     */
    @Value("${linker.base.env}")
    private String env;

    public boolean isLocal() {
        return "local".equals(env);
    }

    public boolean isProd() {
        return "prod".equals(env);
    }

}
