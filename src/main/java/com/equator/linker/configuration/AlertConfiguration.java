package com.equator.linker.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author: Equator
 * @Date: 2021/11/21 23:36
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "dayu.alert")
public class AlertConfiguration {

    private List<String> noticeTypes;

}
