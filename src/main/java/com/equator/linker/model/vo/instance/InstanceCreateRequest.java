package com.equator.linker.model.vo.instance;


import com.equator.linker.model.vo.project.ProxyConfig;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author equator
 * @since 2023-11-05
 */
@Data
public class InstanceCreateRequest {

    /**
     * 项目ID
     */
    @NotNull
    private Long projectId;

    /**
     * 实例名称
     */
    @NotNull
    private String name;

    /**
     * 实例介绍
     */
    @NotNull
    private String intro;

    /**
     * SCM分支
     */
    private String scmBranch;

    /**
     * Nginx代理配置
     */
    private ProxyConfig proxyConfig;


    @NotNull
    private String accessLevel;

}
