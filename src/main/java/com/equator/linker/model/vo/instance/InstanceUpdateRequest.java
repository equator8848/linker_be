package com.equator.linker.model.vo.instance;


import com.equator.linker.model.vo.project.ProxyConfig;
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
public class InstanceUpdateRequest {
    private Long instanceId;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 实例名称
     */
    private String name;

    /**
     * 实例介绍
     */
    private String intro;

    /**
     * SCM分支
     */
    private String scmBranch;

    /**
     * Nginx代理配置
     */
    private ProxyConfig proxyConfig;


    private String accessLevel;

}
