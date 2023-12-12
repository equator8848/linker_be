package xyz.equator8848.linker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: Equator
 * @Date: 2022/9/20 11:30
 **/
@SpringBootTest
@ActiveProfiles("local")
@RunWith(SpringRunner.class)
public class SpringBaseTest {
    @Test
    public void test() {
        System.out.println("Linker SpringBaseTest ...");
    }
}
