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
import xyz.equator8848.linker.service.jenkins.RemoveDockerImageService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
public class InstanceScheduleService {
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

    @Autowired
    private RemoveDockerImageService removeDockerImageService;

    @Scheduled(cron = "0 */3 * * * ?")
    public void instanceAutoBuildCheck() {
        DynamicAppConfiguration dynamicAppConfiguration = appConfig.getConfig();
        if (Boolean.FALSE.equals(dynamicAppConfiguration.getInstanceAutoBuildCheckSwitch())) {
            return;
        }
        Instant now = Instant.now();
        Long nowTimestamp = now.toEpochMilli();
        List<TbInstanceAutoBuildConfig> activateAutoBuildInstanceList = instanceAutoBuildConfigDaoService.getActivateAutoBuildInstanceList(nowTimestamp);
        log.info("nowTimestamp {}, instanceAutoBuildCheck get activateAutoBuildInstanceList {}", nowTimestamp, activateAutoBuildInstanceList);
        for (TbInstanceAutoBuildConfig instanceAutoBuildConfig : activateAutoBuildInstanceList) {
            Long instanceId = instanceAutoBuildConfig.getInstanceId();

            instanceAutoBuildConfig.setLastCheckTimestamp(nowTimestamp);
            instanceAutoBuildConfig.setNextCheckTimestamp(Instant.ofEpochMilli(instanceAutoBuildConfig.getNextCheckTimestamp())
                    .plus(instanceAutoBuildConfig.getCheckInterval(), ChronoUnit.MINUTES).toEpochMilli());
            log.debug("instanceAutoBuildConfig set NextCheckTimestamp {} {}", instanceId, instanceAutoBuildConfig);
            instanceAutoBuildConfig.setLastCheckResult(ModelStatus.InstanceAutoBuildCheckResult.DO_NOT_THING);
            // 判断代码是否有变化

            TbInstance instanceByIdFromDb = instanceDaoService.getById(instanceId);
            if (instanceByIdFromDb == null) {
                instanceAutoBuildConfigDaoService.updateById(instanceAutoBuildConfig);
                continue;
            }

            TbProject projectByIdFromCache = projectDaoService.getProjectByIdFromCache(instanceByIdFromDb.getProjectId());
            if (projectByIdFromCache == null) {
                instanceAutoBuildConfigDaoService.updateById(instanceAutoBuildConfig);
                continue;
            }

            String latestCommitId = projectBranchService.getLatestCommitId(projectByIdFromCache, instanceByIdFromDb);
            if (latestCommitId == null) {
                instanceAutoBuildConfigDaoService.updateById(instanceAutoBuildConfig);
                continue;
            }

            boolean isCodeChange = !latestCommitId.equals(instanceByIdFromDb.getLastBuildCommit());

            if (isCodeChange) {
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

    @Scheduled(cron = "0 0 2 ? * SUN")
    public void deleteDockerImage() {
        DynamicAppConfiguration dynamicAppConfiguration = appConfig.getConfig();
        if (Boolean.FALSE.equals(dynamicAppConfiguration.getDockerImageDeleteSwitch())) {
            return;
        }
        removeDockerImageService.removeDockerImage("3");
    }
}
