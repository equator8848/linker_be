package xyz.equator8848.linker.service;

import xyz.equator8848.linker.model.vo.instance.PublicEntranceDetailsInfo;
import xyz.equator8848.linker.model.vo.instance.PublicEntranceGroupByProject;
import xyz.equator8848.linker.model.vo.instance.PublicEntranceUpdateRequest;

import java.util.List;

public interface PublicEntranceService {
    PublicEntranceDetailsInfo getDetails(Long instanceId);

    void update(PublicEntranceUpdateRequest publicEntranceUpdateRequest);

    List<PublicEntranceGroupByProject> getPublicEntrance();
}
