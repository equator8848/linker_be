package com.equator.linker.service;

import com.equator.linker.model.vo.instance.InstanceCreateRequest;
import com.equator.linker.model.vo.instance.InstanceDetailsInfo;
import com.equator.linker.model.vo.instance.InstanceListRequest;
import com.equator.linker.model.vo.instance.InstanceUpdateRequest;

import java.util.List;

public interface InstanceService {
    Long create(InstanceCreateRequest instanceCreateRequest);

    void update(InstanceUpdateRequest instanceUpdateRequest);

    void delete(Long instanceId);

    List<InstanceDetailsInfo> list(InstanceListRequest instanceListRequest);
}
