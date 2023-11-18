package com.equator.linker.service;

import com.equator.linker.model.vo.instance.*;

import java.util.List;

public interface InstanceService {
    Long create(InstanceCreateRequest instanceCreateRequest);

    void update(InstanceUpdateRequest instanceUpdateRequest);

    void delete(Long instanceId);

    List<InstanceDetailsInfo> list(InstanceListRequest instanceListRequest);

    void buildPipeline(Long instanceId);

    InstancePipelineBuildResult getPipelineBuildResult(Long instanceId);
}
