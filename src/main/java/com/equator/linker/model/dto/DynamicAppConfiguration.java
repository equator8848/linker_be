package com.equator.linker.model.dto;


import com.equator.core.dynamic.config.ModelTransformerField;
import lombok.Data;

/**
 * @Author: Equator
 * @Date: 2022/8/14 22:49
 **/
@Data
public class DynamicAppConfiguration {
    @ModelTransformerField(nullable = true)
    private long version;

    /**
     * 是否允许用户自行注册
     */
    @ModelTransformerField(defaultValue = "false")
    private Boolean allowRegister;

    /**
     * 访问主机
     */
    @ModelTransformerField(defaultValue = "http://localhost")
    private String accessHost;

    /**
     * 最小访问端口
     */
    @ModelTransformerField(defaultValue = "10000")
    private Integer minAccessPort;

    /***
     * 最大访问端口
     */
    @ModelTransformerField(defaultValue = "60000")
    private Integer maxAccessPort;

    private String jenkinsEndpoint;

    private String jenkinsCredentials;
}
