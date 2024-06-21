package xyz.equator8848.linker.service.jenkins;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.equator8848.linker.SpringBaseTest;

public class RemoveDockerContainerServiceTest extends SpringBaseTest {
    @Autowired
    private RemoveDockerContainerService removeDockerContainerService;

    @Autowired
    private RemoveDockerImageService removeDockerImageService;

    @Test
    public void test1() {
        removeDockerContainerService.removeDockerContainer("docker-container-1725898110754353153");
    }

    @Test
    public void test2() {
        removeDockerImageService.removeDockerImage("3");
    }
}
