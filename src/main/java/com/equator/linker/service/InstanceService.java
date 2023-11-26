package com.equator.linker.service;

import com.equator.linker.model.vo.PageData;
import com.equator.linker.model.vo.instance.*;

import java.util.List;

public interface InstanceService {
    Long create(InstanceCreateRequest instanceCreateRequest);

    void update(InstanceUpdateRequest instanceUpdateRequest);

    void delete(Long instanceId);

    List<InstanceDetailsInfo> list(InstanceListRequest instanceListRequest);

    PageData<InstanceSimpleInfo> all(InstanceListRequest instanceListRequest);

    void buildPipeline(Long instanceId);

    PipelineBuildLog getPipelineLog(Long instanceId);

    InstancePipelineBuildResult getPipelineBuildResult(Long instanceId);

    void instanceStarAction(InstanceStarRequest instanceStarRequest);
}
