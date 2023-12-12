package xyz.equator8848.linker.model.vo.instance;

import lombok.Data;

@Data
public class PublicEntranceDetailsInfo {
    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 实例ID
     */
    private Long instanceId;

    /**
     * 是否启用
     */
    private Boolean enabledFlag;

    /**
     * 名称
     */
    private String name;

    /**
     * 介绍
     */
    private String intro;
}
