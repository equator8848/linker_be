package com.equator.linker.service.jenkins;

import com.cdancy.jenkins.rest.JenkinsClient;
import com.equator.linker.configuration.AppConfig;
import com.equator.linker.model.dto.DynamicAppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JenkinsClientFactory {
    @Autowired
    private AppConfig appConfig;

    public JenkinsClient buildJenkinsClient() {
        DynamicAppConfiguration dynamicAppConfiguration = appConfig.getConfig();
        return JenkinsClient.builder()
                .endPoint(dynamicAppConfiguration.getJenkinsEndpoint())
                .credentials(dynamicAppConfiguration.getJenkinsCredentials())
                .build();
    }
}
