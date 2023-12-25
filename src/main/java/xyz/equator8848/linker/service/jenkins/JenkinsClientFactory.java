package xyz.equator8848.linker.service.jenkins;

import com.cdancy.jenkins.rest.JenkinsClient;
import xyz.equator8848.linker.model.dto.DynamicAppConfiguration;
import xyz.equator8848.linker.configuration.AppConfig;
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
