package xyz.equator8848.linker.service;

import xyz.equator8848.linker.model.vo.PageData;
import xyz.equator8848.linker.model.vo.instance.*;

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
