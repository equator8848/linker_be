package com.equator.linker.model.vo.instance;


import com.equator.linker.model.vo.project.ProxyConfig;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author equator
 * @since 2023-11-05
 */
@Data
public class InstanceDetailsInfo {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

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


    private Boolean isOwner;

}
