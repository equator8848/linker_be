package xyz.equator8848.linker.service;

import xyz.equator8848.linker.model.vo.instance.InstanceAutoBuildConfigInfo;
import xyz.equator8848.linker.model.vo.instance.InstanceAutoBuildConfigUpdateRequest;

public interface InstanceAutoBuildConfigService {
    InstanceAutoBuildConfigInfo getDetails(Long instanceId);

    void update(InstanceAutoBuildConfigUpdateRequest instanceAutoBuildConfigUpdateRequest);
}
