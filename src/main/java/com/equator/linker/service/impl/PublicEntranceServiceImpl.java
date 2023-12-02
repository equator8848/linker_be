package com.equator.linker.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.equator.core.model.exception.PreCondition;
import com.equator.linker.dao.service.InstanceDaoService;
import com.equator.linker.dao.service.ProjectDaoService;
import com.equator.linker.dao.service.PublicEntranceDaoService;
import com.equator.linker.model.po.TbInstance;
import com.equator.linker.model.po.TbProject;
import com.equator.linker.model.po.TbPublicEntrance;
import com.equator.linker.model.vo.instance.PublicEntranceDetailsInfo;
import com.equator.linker.model.vo.instance.PublicEntranceGroupByProject;
import com.equator.linker.model.vo.instance.PublicEntranceListInfo;
import com.equator.linker.model.vo.instance.PublicEntranceUpdateRequest;
import com.equator.linker.service.PublicEntranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PublicEntranceServiceImpl implements PublicEntranceService {
    @Autowired
    private ProjectDaoService projectDaoService;

    @Autowired
    private InstanceDaoService instanceDaoService;

    @Autowired
    private PublicEntranceDaoService publicEntranceDaoService;

    @Override
    public PublicEntranceDetailsInfo getDetails(Long instanceId) {
        TbPublicEntrance tbPublicEntrance = publicEntranceDaoService.getTbPublicEntranceByInstanceId(instanceId);
        PublicEntranceDetailsInfo publicEntranceDetailsInfo = new PublicEntranceDetailsInfo();
        if (tbPublicEntrance != null) {
            publicEntranceDetailsInfo.setProjectId(tbPublicEntrance.getProjectId());
            publicEntranceDetailsInfo.setInstanceId(tbPublicEntrance.getInstanceId());
            publicEntranceDetailsInfo.setName(tbPublicEntrance.getName());
            publicEntranceDetailsInfo.setIntro(tbPublicEntrance.getIntro());
            publicEntranceDetailsInfo.setEnabledFlag(tbPublicEntrance.getEnabledSwitch());
        } else {
            TbInstance tbInstance = instanceDaoService.getById(instanceId);
            PreCondition.isNotNull(tbInstance, "实例不存在");
            publicEntranceDetailsInfo.setProjectId(tbInstance.getProjectId());
            publicEntranceDetailsInfo.setInstanceId(tbInstance.getId());
            publicEntranceDetailsInfo.setName(tbInstance.getName());
            publicEntranceDetailsInfo.setIntro(tbInstance.getIntro());
            publicEntranceDetailsInfo.setEnabledFlag(false);
        }
        return publicEntranceDetailsInfo;
    }

    @Override
    public void update(PublicEntranceUpdateRequest publicEntranceUpdateRequest) {
        Long instanceId = publicEntranceUpdateRequest.getInstanceId();
        TbPublicEntrance tbPublicEntrance = publicEntranceDaoService.getTbPublicEntranceByInstanceId(instanceId);
        if (tbPublicEntrance == null) {
            tbPublicEntrance = new TbPublicEntrance();
        }
        TbInstance tbInstance = instanceDaoService.getById(instanceId);
        PreCondition.isNotNull(tbInstance, "实例不存在");

        tbPublicEntrance.setProjectId(tbInstance.getProjectId());
        tbPublicEntrance.setInstanceId(instanceId);
        tbPublicEntrance.setEnabledSwitch(publicEntranceUpdateRequest.getEnabledFlag());
        tbPublicEntrance.setName(publicEntranceUpdateRequest.getName());
        tbPublicEntrance.setIntro(publicEntranceUpdateRequest.getIntro());
        publicEntranceDaoService.saveOrUpdate(tbPublicEntrance);
    }

    @Override
    public List<PublicEntranceGroupByProject> getPublicEntrance() {
        List<TbPublicEntrance> tbPublicEntrances = publicEntranceDaoService.list(Wrappers.<TbPublicEntrance>lambdaQuery().eq(TbPublicEntrance::getEnabledSwitch, true));

        Map<Long, List<TbPublicEntrance>> tbPublicEntrancesGroupByProjectMap = tbPublicEntrances.stream()
                .collect(Collectors.groupingBy(TbPublicEntrance::getProjectId));

        return tbPublicEntrancesGroupByProjectMap.entrySet().stream().map(entry -> {
            Long projectId = entry.getKey();
            PublicEntranceGroupByProject publicEntranceGroupByProject = new PublicEntranceGroupByProject();
            publicEntranceGroupByProject.setProjectId(projectId);
            TbProject projectByIdFromCache = projectDaoService.getProjectByIdFromCache(projectId);
            PreCondition.isNotNull(publicEntranceGroupByProject);

            publicEntranceGroupByProject.setProjectName(projectByIdFromCache.getName());
            publicEntranceGroupByProject.setPublicEntranceListInfos(entry.getValue()
                    .stream()
                    .map(tbPublicEntrance -> {
                        PublicEntranceListInfo publicEntranceListInfo = new PublicEntranceListInfo();
                        Long instanceId = tbPublicEntrance.getInstanceId();
                        publicEntranceListInfo.setInstanceId(instanceId);

                        TbInstance instanceByIdFromCache = instanceDaoService.getInstanceByIdFromCache(instanceId);
                        PreCondition.isNotNull(instanceByIdFromCache);

                        publicEntranceListInfo.setName(tbPublicEntrance.getName());
                        publicEntranceListInfo.setIntro(tbPublicEntrance.getIntro());
                        publicEntranceListInfo.setAccessUrl(instanceByIdFromCache.getAccessLink());

                        return publicEntranceListInfo;
                    }).collect(Collectors.toList()));

            return publicEntranceGroupByProject;
        }).collect(Collectors.toList());
    }
}
