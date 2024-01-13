package xyz.equator8848.linker.service.jenkins;

import com.cdancy.jenkins.rest.JenkinsClient;
import com.cdancy.jenkins.rest.domain.common.RequestStatus;
import com.cdancy.jenkins.rest.domain.job.JobInfo;
import com.cdancy.jenkins.rest.features.JobsApi;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.equator8848.inf.core.model.exception.PreCondition;
import xyz.equator8848.linker.service.template.TemplateUtil;

import java.util.Collections;

@Slf4j
@Service
public class RemoveDockerContainerService {
    private static final String FIXED_JOB_NAME = "removeDockerContainer";

    @Autowired
    private JenkinsClientFactory jenkinsClientFactory;

    public void removeDockerContainer(String containerName) {
        try (JenkinsClient jenkinsClient = jenkinsClientFactory.buildJenkinsClient()) {
            JobsApi jobsApi = jenkinsClient.api().jobsApi();
            JobInfo jobInfo = jobsApi.jobInfo(null, FIXED_JOB_NAME);
            if (jobInfo == null) {
                // 创建任务
                log.info("removeDockerContainer Service create job {}", containerName);
                String jenkinsFileTemplateByName = TemplateUtil.getJenkinsFileTemplateByName(FIXED_JOB_NAME);
                RequestStatus requestStatus = jobsApi.create(null, FIXED_JOB_NAME, jenkinsFileTemplateByName);
                PreCondition.isTrue(requestStatus.value(), "Jenkins流水线创建失败");
            }
            log.info("removeDockerContainer Service build job {}", containerName);
            jobsApi.buildWithParameters(null, FIXED_JOB_NAME, ImmutableMap.of("containerName",
                    Collections.singletonList(containerName)));
        } catch (Exception e) {
            log.error("removeDockerContainer error {}", containerName, e);
        }
    }
}
