package xyz.equator8848.linker.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.equator8848.inf.core.model.exception.PreCondition;
import xyz.equator8848.linker.dao.service.InstanceAutoBuildConfigDaoService;
import xyz.equator8848.linker.dao.service.InstanceDaoService;
import xyz.equator8848.linker.model.constant.ModelStatus;
import xyz.equator8848.linker.model.po.TbInstance;
import xyz.equator8848.linker.model.po.TbInstanceAutoBuildConfig;
import xyz.equator8848.linker.model.vo.instance.InstanceAutoBuildConfigInfo;
import xyz.equator8848.linker.model.vo.instance.InstanceAutoBuildConfigUpdateRequest;
import xyz.equator8848.linker.service.InstanceAutoBuildConfigService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class InstanceAutoBuildConfigServiceImpl implements InstanceAutoBuildConfigService {

    @Autowired
    private InstanceDaoService instanceDaoService;

    @Autowired
    private InstanceAutoBuildConfigDaoService instanceAutoBuildConfigDaoService;

    @Override
    public InstanceAutoBuildConfigInfo getDetails(Long instanceId) {
        TbInstanceAutoBuildConfig instanceAutoBuildConfig = instanceAutoBuildConfigDaoService.getTbInstanceAutoBuildConfigByInstanceId(instanceId);
        InstanceAutoBuildConfigInfo instanceAutoBuildConfigInfo = new InstanceAutoBuildConfigInfo();
        if (instanceAutoBuildConfig != null) {
            instanceAutoBuildConfigInfo.setProjectId(instanceAutoBuildConfig.getProjectId());
            instanceAutoBuildConfigInfo.setInstanceId(instanceAutoBuildConfig.getInstanceId());
            instanceAutoBuildConfigInfo.setCheckInterval(instanceAutoBuildConfig.getCheckInterval());
            instanceAutoBuildConfigInfo.setEnabledFlag(instanceAutoBuildConfig.getEnabledSwitch());
        } else {
            TbInstance tbInstance = instanceDaoService.getById(instanceId);
            PreCondition.isNotNull(tbInstance, "实例不存在");
            instanceAutoBuildConfigInfo.setProjectId(tbInstance.getProjectId());
            instanceAutoBuildConfigInfo.setInstanceId(tbInstance.getId());
            instanceAutoBuildConfigInfo.setCheckInterval(10);
            instanceAutoBuildConfigInfo.setEnabledFlag(false);
        }
        return instanceAutoBuildConfigInfo;
    }

    @Override
    public void update(InstanceAutoBuildConfigUpdateRequest instanceAutoBuildConfigUpdateRequest) {
        Long instanceId = instanceAutoBuildConfigUpdateRequest.getInstanceId();
        TbInstanceAutoBuildConfig instanceAutoBuildConfig = instanceAutoBuildConfigDaoService.getTbInstanceAutoBuildConfigByInstanceId(instanceId);
        if (instanceAutoBuildConfig == null) {
            instanceAutoBuildConfig = new TbInstanceAutoBuildConfig();
            instanceAutoBuildConfig.setLastCheckTimestamp(Instant.now().toEpochMilli());
            instanceAutoBuildConfig.setLastCheckResult(ModelStatus.InstanceAutoBuildCheckResult.DO_NOT_THING);
        }
        TbInstance tbInstance = instanceDaoService.getById(instanceId);
        PreCondition.isNotNull(tbInstance, "实例不存在");

        instanceAutoBuildConfig.setProjectId(tbInstance.getProjectId());
        instanceAutoBuildConfig.setInstanceId(instanceId);
        instanceAutoBuildConfig.setEnabledSwitch(instanceAutoBuildConfigUpdateRequest.getEnabledFlag());
        instanceAutoBuildConfig.setCheckInterval(instanceAutoBuildConfigUpdateRequest.getCheckInterval());
        instanceAutoBuildConfig.setNextCheckTimestamp(Instant.now().plus(instanceAutoBuildConfigUpdateRequest.getCheckInterval(), ChronoUnit.MINUTES).toEpochMilli());
        instanceAutoBuildConfigDaoService.saveOrUpdate(instanceAutoBuildConfig);
    }
}
