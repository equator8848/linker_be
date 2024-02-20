package xyz.equator8848.linker.model.dto;


import xyz.equator8848.inf.core.dynamic.config.ModelTransformerField;
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
    @ModelTransformerField(defaultValue = "true")
    private Boolean allowRegister;

    /**
     * 部署访问主机
     */
    @ModelTransformerField(defaultValue = "http://localhost")
    private String deployAccessHost;

    /**
     * Linker系统服务器访问地址
     */
    @ModelTransformerField(defaultValue = "http://localhost:8888/linker-server")
    private String linkerServerHostBaseUrl;

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

    /**
     * Jenkins访问地址
     */
    @ModelTransformerField(defaultValue = "#")
    private String jenkinsEndpoint;

    /**
     * Jenkins凭证
     */
    @ModelTransformerField(defaultValue = "#")
    private String jenkinsCredentials;

    /**
     * Jenkins流水线构建超时时间
     */
    @ModelTransformerField(defaultValue = "600000")
    private Long jenkinsPipelineTimeoutMs;

    /**
     * DES密钥
     */
    @ModelTransformerField(defaultValue = "9ac.*VZk")
    private String desSecretKey;

    /**
     * SM4
     */
    @ModelTransformerField(defaultValue = "6a0150d920232858462c94bdd0a967a6")
    private String sm4SecretKey;

    /**
     * 是否允许系统管理员管理全部数据
     */
    @ModelTransformerField(defaultValue = "false")
    private Boolean systemAdminManageAllData;

    /**
     * 是否开启实例自动构建
     */
    @ModelTransformerField(defaultValue = "true")
    private Boolean instanceAutoBuildCheckSwitch;
}
