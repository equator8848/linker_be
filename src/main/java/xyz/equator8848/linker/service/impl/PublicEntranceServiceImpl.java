package xyz.equator8848.linker.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import xyz.equator8848.linker.dao.service.ProjectDaoService;
import xyz.equator8848.linker.dao.service.PublicEntranceDaoService;
import xyz.equator8848.linker.model.po.TbInstance;
import xyz.equator8848.linker.model.po.TbProject;
import xyz.equator8848.linker.model.po.TbPublicEntrance;
import xyz.equator8848.linker.model.vo.instance.PublicEntranceDetailsInfo;
import xyz.equator8848.linker.model.vo.instance.PublicEntranceGroupByProject;
import xyz.equator8848.linker.model.vo.instance.PublicEntranceListInfo;
import xyz.equator8848.linker.model.vo.instance.PublicEntranceUpdateRequest;
import xyz.equator8848.inf.core.model.exception.PreCondition;
import xyz.equator8848.linker.dao.service.InstanceDaoService;
import xyz.equator8848.linker.service.PublicEntranceService;
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
