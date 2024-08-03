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
public class RemoveDockerImageService {
    private static final String FIXED_JOB_NAME = "RemoveDockerImage";

    @Autowired
    private JenkinsClientFactory jenkinsClientFactory;

    public void removeDockerImage(String createTimeThresholdDay) {
        try (JenkinsClient jenkinsClient = jenkinsClientFactory.buildJenkinsClient()) {
            JobsApi jobsApi = jenkinsClient.api().jobsApi();
            JobInfo jobInfo = jobsApi.jobInfo(null, FIXED_JOB_NAME);
            if (jobInfo == null) {
                // 创建任务
                log.info("removeDockerImage Service create job {}", createTimeThresholdDay);
                String jenkinsFileTemplateByName = TemplateUtil.getJenkinsFileTemplateByName(FIXED_JOB_NAME);
                RequestStatus requestStatus = jobsApi.create(null, FIXED_JOB_NAME, jenkinsFileTemplateByName);
                PreCondition.isTrue(requestStatus.value(), "Jenkins流水线创建失败");
            }
            log.info("removeDockerImage Service build job {}", createTimeThresholdDay);
            jobsApi.buildWithParameters(null, FIXED_JOB_NAME, ImmutableMap.of("createTimeThresholdDay",
                    Collections.singletonList(createTimeThresholdDay)));
        } catch (Exception e) {
            log.error("removeDockerImage error {}", createTimeThresholdDay, e);
        }
    }
}
