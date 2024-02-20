package xyz.equator8848.linker.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xyz.equator8848.linker.configuration.AppConfig;
import xyz.equator8848.linker.dao.service.InstanceAutoBuildConfigDaoService;
import xyz.equator8848.linker.dao.service.InstanceDaoService;
import xyz.equator8848.linker.dao.service.ProjectDaoService;
import xyz.equator8848.linker.model.constant.ModelStatus;
import xyz.equator8848.linker.model.dto.DynamicAppConfiguration;
import xyz.equator8848.linker.model.po.TbInstance;
import xyz.equator8848.linker.model.po.TbInstanceAutoBuildConfig;
import xyz.equator8848.linker.model.po.TbProject;
import xyz.equator8848.linker.service.InstanceService;
import xyz.equator8848.linker.service.ProjectBranchService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
public class InstanceAutoBuildScheduleService {
    @Autowired
    private InstanceAutoBuildConfigDaoService instanceAutoBuildConfigDaoService;

    @Autowired
    private ProjectDaoService projectDaoService;

    @Autowired
    private InstanceDaoService instanceDaoService;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private ProjectBranchService projectBranchService;

    @Autowired
    private InstanceService instanceService;

    @Scheduled(cron = "0 */1 * * * ?")
    public void instanceAutoBuildCheck() {
        DynamicAppConfiguration dynamicAppConfiguration = appConfig.getConfig();
        if (Boolean.FALSE.equals(dynamicAppConfiguration.getInstanceAutoBuildCheckSwitch())) {
            return;
        }
        Instant now = Instant.now();
        Long nowTimestamp = now.toEpochMilli();
        List<TbInstanceAutoBuildConfig> activateAutoBuildInstanceList = instanceAutoBuildConfigDaoService.getActivateAutoBuildInstanceList(nowTimestamp);
        for (TbInstanceAutoBuildConfig instanceAutoBuildConfig : activateAutoBuildInstanceList) {
            instanceAutoBuildConfig.setLastCheckTimestamp(nowTimestamp);
            instanceAutoBuildConfig.setNextCheckTimestamp(now.plus(instanceAutoBuildConfig.getCheckInterval(), ChronoUnit.MINUTES).toEpochMilli());
            instanceAutoBuildConfig.setLastCheckResult(ModelStatus.InstanceAutoBuildCheckResult.DO_NOT_THING);
            // 判断代码是否有变化
            Long instanceId = instanceAutoBuildConfig.getInstanceId();
            TbInstance instanceByIdFromCache = instanceDaoService.getInstanceByIdFromCache(instanceId);
            if (instanceByIdFromCache == null) {
                instanceAutoBuildConfigDaoService.updateById(instanceAutoBuildConfig);
                continue;
            }

            TbProject projectByIdFromCache = projectDaoService.getProjectByIdFromCache(instanceByIdFromCache.getProjectId());
            if (projectByIdFromCache == null) {
                instanceAutoBuildConfigDaoService.updateById(instanceAutoBuildConfig);
                continue;
            }

            String latestCommitId = projectBranchService.getLatestCommitId(projectByIdFromCache, instanceByIdFromCache);
            if (latestCommitId == null) {
                instanceAutoBuildConfigDaoService.updateById(instanceAutoBuildConfig);
                continue;
            }

            boolean isCodeChange = !latestCommitId.equals(instanceByIdFromCache.getLastBuildCommit());

            if (isCodeChange) {
                TbInstance instanceByIdFromDb = instanceDaoService.getById(instanceId);
                if (instanceByIdFromDb.getBuildingFlag()) {
                    log.info("instanceAutoBuildCheck skip instance because it is building, id:{},latestCommitId:{}", instanceId, latestCommitId);
                    instanceAutoBuildConfigDaoService.updateById(instanceAutoBuildConfig);
                    continue;
                }
                instanceService.buildPipeline(instanceId);
                log.info("instanceAutoBuildCheck auto build instance, id:{},latestCommitId:{}", instanceId, latestCommitId);
                instanceAutoBuildConfig.setLastCheckResult(ModelStatus.InstanceAutoBuildCheckResult.AUTO_BUILD);
            }

            instanceAutoBuildConfigDaoService.updateById(instanceAutoBuildConfig);
        }
    }
}
