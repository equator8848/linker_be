package xyz.equator8848.linker.util;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.equator8848.linker.SpringBaseTest;
import xyz.equator8848.linker.service.external.gitlab.GitlabScmService;

/**
 * @author Equator
 * @date 2024/1/9 10:50
 */
public class ScmUtilTest extends SpringBaseTest {
    @Autowired
    private GitlabScmService gitlabScmService;

    @Test
    public void testGetProjectPathFromRepositoryUrl() {
        Assert.assertEquals("train-center%2Fworkspace%2Fworkspace-frontend", gitlabScmService.getProjectPathFromRepositoryUrl("http://gitlab.cqlvc.com/train-center/workspace/workspace-frontend.git"));
        Assert.assertEquals("floworange%2Flow-code-system-frontend", gitlabScmService.getProjectPathFromRepositoryUrl("http://gitlab.cqlvc.com/floworange/low-code-system-frontend.git"));
        Assert.assertEquals("low-code-system-frontend", gitlabScmService.getProjectPathFromRepositoryUrl("http://gitlab.cqlvc.com/low-code-system-frontend.git"));
    }
}
