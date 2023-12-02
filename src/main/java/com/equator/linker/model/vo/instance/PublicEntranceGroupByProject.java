package com.equator.linker.model.vo.instance;

import lombok.Data;

import java.util.List;

@Data
public class PublicEntranceGroupByProject {
    /**
     * 项目ID
     */
    private Long projectId;


    /**
     * 项目名称
     */
    private String projectName;


    /**
     * 公开入口列表
     */
    private List<PublicEntranceListInfo> publicEntranceListInfos;
}
