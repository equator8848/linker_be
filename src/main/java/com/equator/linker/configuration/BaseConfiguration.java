package com.equator.linker.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Equator
 * @Date: 2021/11/21 23:36
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "linker.base")
public class BaseConfiguration {

    /**
     * 环境
     */
    private String env;

    public boolean isLocal() {
        return "local".equals(env);
    }

    public boolean isProd() {
        return "prod".equals(env);
    }

}
