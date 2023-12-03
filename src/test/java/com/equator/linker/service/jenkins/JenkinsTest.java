package com.equator.linker.service.jenkins;

import com.cdancy.jenkins.rest.JenkinsClient;
import com.cdancy.jenkins.rest.domain.common.IntegerResponse;
import com.cdancy.jenkins.rest.domain.common.RequestStatus;
import com.cdancy.jenkins.rest.domain.job.BuildInfo;
import com.cdancy.jenkins.rest.domain.job.JobInfo;
import com.cdancy.jenkins.rest.domain.system.SystemInfo;
import com.cdancy.jenkins.rest.features.JobsApi;
import com.equator.linker.SpringBaseTest;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;
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
            String config = jobsApi.config(null, "JenkinsPipeline1");
            log.info("testJenkinsGetJobConfig {}", config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateJob() {
        try (JenkinsClient jenkinsClient = jenkinsClientFactory.buildJenkinsClient()) {
            JobsApi jobsApi = jenkinsClient.api().jobsApi();
            jobsApi.create(null, "JenkinsPipeline2", "<flow-definition plugin=\"workflow-job@1360.vc6700e3136f5\">\n" +
                    "  <actions>\n" +
                    "    <org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobAction plugin=\"pipeline-model-definition@2.2150.v4cfd8916915c\"/>\n" +
                    "    <org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobPropertyTrackerAction plugin=\"pipeline-model-definition@2.2150.v4cfd8916915c\">\n" +
                    "      <jobProperties/>\n" +
                    "      <triggers/>\n" +
                    "      <parameters/>\n" +
                    "      <options/>\n" +
                    "    </org.jenkinsci.plugins.pipeline.modeldefinition.actions.DeclarativeJobPropertyTrackerAction>\n" +
                    "  </actions>\n" +
                    "  <description></description>\n" +
                    "  <keepDependencies>false</keepDependencies>\n" +
                    "  <properties>\n" +
                    "    <jenkins.model.BuildDiscarderProperty>\n" +
                    "      <strategy class=\"hudson.tasks.LogRotator\">\n" +
                    "        <daysToKeep>-1</daysToKeep>\n" +
                    "        <numToKeep>-1</numToKeep>\n" +
                    "        <artifactDaysToKeep>-1</artifactDaysToKeep>\n" +
                    "        <artifactNumToKeep>-1</artifactNumToKeep>\n" +
                    "      </strategy>\n" +
                    "    </jenkins.model.BuildDiscarderProperty>\n" +
                    "  </properties>\n" +
                    "  <definition class=\"org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition\" plugin=\"workflow-cps@3806.va_3a_6988277b_2\">\n" +
                    "    <script>pipeline {\n" +
                    "    agent any\n" +
                    "    stages {\n" +
                    "        stage(&apos;Clean&apos;) {\n" +
                    "            steps {\n" +
                    "                script {\n" +
                    "                    echo &quot;cleanWs &gt;&gt;&gt;&quot;\n" +
                    "                    echo &quot;${WORKSPACE}&quot;\n" +
                    "                    cleanWs()\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "        stage(&apos;Package&apos;) {\n" +
                    "            agent {\n" +
                    "                docker {\n" +
                    "                    image &apos;lsage/pnpm-circleci-node:16.13.1-pnpm7.5.1&apos;\n" +
                    "                }\n" +
                    "            }\n" +
                    "            steps {\n" +
                    "                sh &apos;git --version&apos;\n" +
                    "                sh &apos;echo &quot;${WORKSPACE}&quot;&apos;\n" +
                    "                sh &apos;rm -rf linker-fe&apos;\n" +
                    "                sh &apos;git clone http://equator:glpat-kFqFt1vEP_DGRHf3mxuE@gitlab.localhost.com/equator/linker-fe.git&apos;\n" +
                    "                dir(&apos;linker-fe&apos;) {\n" +
                    "                    sh &apos;pwd&apos;\n" +
                    "                    sh &apos;ls&apos;\n" +
                    "                    sh &apos;npm install&apos;\n" +
                    "                    sh &apos;npm run build&apos;\n" +
                    "                    sh &apos;ls&apos;\n" +
                    "                    stash(name:&apos;PackageOutput&apos;, includes: &apos;dist/**&apos;)\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "        stage(&apos;BuildImage&apos;) {\n" +
                    "            steps {\n" +
                    "                unstash(&quot;PackageOutput&quot;)\n" +
                    "                sh &apos;pwd&apos;\n" +
                    "                sh &apos;ls&apos;\n" +
                    "                sh &apos;curl -O http://172.16.8.2:29999/default.conf&apos;\n" +
                    "                sh &apos;curl -O http://172.16.8.2:29999/Dockerfile&apos;\n" +
                    "                sh &apos;ls&apos;\n" +
                    "                sh &apos;docker build -t my-nginx-image .&apos;\n" +
                    "                sh &apos;docker images&apos;\n" +
                    "            }\n" +
                    "        }\n" +
                    "        stage(&apos;RunContainer&apos;) {\n" +
                    "            steps {\n" +
                    "                sh &apos;docker rm -vf my-nginx&apos;\n" +
                    "                sh &apos;docker run -d -p 28888:80 --name my-nginx my-nginx-image&apos;\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}</script>\n" +
                    "    <sandbox>true</sandbox>\n" +
                    "  </definition>\n" +
                    "  <triggers/>\n" +
                    "  <disabled>false</disabled>\n" +
                    "</flow-definition>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testJenkinsBuildJob() {
        try (JenkinsClient jenkinsClient = jenkinsClientFactory.buildJenkinsClient()) {
            JobsApi jobsApi = jenkinsClient.api().jobsApi();
            IntegerResponse result = jobsApi.build(null, "JenkinsPipeline2");
            log.info("testJenkinsBuildJob {}", result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testJenkinsDeleteJob() {
        try (JenkinsClient jenkinsClient = jenkinsClientFactory.buildJenkinsClient()) {
            JobsApi jobsApi = jenkinsClient.api().jobsApi();
            RequestStatus result = jobsApi.delete(null, "JenkinsPipeline1");
            log.info("testJenkinsDeleteJob {}", result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testJenkinsGetJobInfo() {
        try (JenkinsClient jenkinsClient = jenkinsClientFactory.buildJenkinsClient()) {
            JobsApi jobsApi = jenkinsClient.api().jobsApi();
            // 不存在则返回null
            JobInfo jobInfo = jobsApi.jobInfo(null, "Pipeline_1725898110754353153");
            log.info("testJenkinsGetJobInfo {}", jobInfo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * BuildInfo{artifacts=[], actions=[Action{causes=[Cause{clazz=hudson.model.Cause$UserIdCause, shortDescription=Started by user api, userId=api, userName=api}], parameters=[], text=null, iconPath=null, _class=hudson.model.CauseAction}, Action{causes=[], parameters=[], text=null, iconPath=null, _class=org.jenkinsci.plugins.workflow.libs.LibrariesAction}, Action{causes=[], parameters=[], text=null, iconPath=null, _class=null}, Action{causes=[], parameters=[], text=null, iconPath=null, _class=org.jenkinsci.plugins.workflow.cps.EnvActionImpl}, Action{causes=[], parameters=[], text=null, iconPath=null, _class=null}, Action{causes=[], parameters=[], text=null, iconPath=null, _class=null}, Action{causes=[], parameters=[], text=null, iconPath=null, _class=null}, Action{causes=[], parameters=[], text=null, iconPath=null, _class=org.jenkinsci.plugins.displayurlapi.actions.RunDisplayAction}, Action{causes=[], parameters=[], text=null, iconPath=null, _class=org.jenkinsci.plugins.pipeline.modeldefinition.actions.RestartDeclarativePipelineAction}, Action{causes=[], parameters=[], text=null, iconPath=null, _class=null}, Action{causes=[], parameters=[], text=null, iconPath=null, _class=org.jenkinsci.plugins.workflow.job.views.FlowGraphAction}, Action{causes=[], parameters=[], text=null, iconPath=null, _class=null}, Action{causes=[], parameters=[], text=null, iconPath=null, _class=null}, Action{causes=[], parameters=[], text=null, iconPath=null, _class=null}], building=false, description=null, displayName=#1, duration=136055, estimatedDuration=136055, fullDisplayName=Pipeline_1725898110754353153 #1, id=1, keepLog=false, number=1, queueId=103, result=SUCCESS, timestamp=1700321165134, url=http://172.16.8.2:28080/job/Pipeline_1725898110754353153/1/, changeSets=[], builtOn=null, culprits=[]}
     */
    @Test
    public void testJenkinsGetJobBuildInfo() {
        try (JenkinsClient jenkinsClient = jenkinsClientFactory.buildJenkinsClient()) {
            JobsApi jobsApi = jenkinsClient.api().jobsApi();
            BuildInfo buildInfo = jobsApi.buildInfo(null, "Pipeline_1727915537415495682", 12);
            log.info("testJenkinsGetJobInfo {}", buildInfo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testJenkinsGetJobBuildFile() {
        try (JenkinsClient jenkinsClient = jenkinsClientFactory.buildJenkinsClient()) {
            System.out.println(jenkinsClient.authType());
            System.out.println(jenkinsClient.authValue());
            System.out.println(Credentials.basic("api", "JnqgsAK5uN6UyKs7l2w8fKP9AhOcx49N"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
