package com.equator.linker.service.jenkins;

import com.cdancy.jenkins.rest.JenkinsClient;
import com.cdancy.jenkins.rest.domain.system.SystemInfo;
import com.cdancy.jenkins.rest.features.JobsApi;
import com.equator.linker.SpringBaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Slf4j
public class JenkinsTest extends SpringBaseTest {
    @Autowired
    private JenkinsClientFactory jenkinsClientFactory;

    @Test
    public void testJenkinsBase() {
        try (JenkinsClient jenkinsClient = jenkinsClientFactory.buildJenkinsClient()) {
            SystemInfo systemInfo = jenkinsClient.api().systemApi().systemInfo();
            log.info("Jenkins SystemInfo {}", systemInfo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testJenkinsGetJobConfig() {
        try (JenkinsClient jenkinsClient = jenkinsClientFactory.buildJenkinsClient()) {
            JobsApi jobsApi = jenkinsClient.api().jobsApi();
            String config = jobsApi.config(null, "nginx-deply-demo");
            log.info("testJenkinsGetJobConfig {}", config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
