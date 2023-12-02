package com.equator.linker.model.vo.instance;

import lombok.Data;

@Data
public class PublicEntranceListInfo {

    /**
     * 实例ID
     */
    private Long instanceId;

    /**
     * 名称
     */
    private String name;

    /**
     * 介绍
     */
    private String intro;

    /**
     * 访问入口
     */
    private String accessUrl;
}
