package com.equator.linker.service;

import com.equator.linker.model.vo.instance.PublicEntranceDetailsInfo;
import com.equator.linker.model.vo.instance.PublicEntranceGroupByProject;
import com.equator.linker.model.vo.instance.PublicEntranceUpdateRequest;

import java.util.List;

public interface PublicEntranceService {
    PublicEntranceDetailsInfo getDetails(Long instanceId);

    void update(PublicEntranceUpdateRequest publicEntranceUpdateRequest);

    List<PublicEntranceGroupByProject> getPublicEntrance();
}
