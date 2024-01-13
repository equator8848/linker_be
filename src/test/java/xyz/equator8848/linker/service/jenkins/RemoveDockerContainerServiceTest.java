package xyz.equator8848.linker.service.jenkins;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.equator8848.linker.SpringBaseTest;

public class RemoveDockerContainerServiceTest extends SpringBaseTest {
    @Autowired
    private RemoveDockerContainerService removeDockerContainerService;

    @Test
    public void test() {
        removeDockerContainerService.removeDockerContainer("docker-container-1725898110754353153");
    }
}
