package com.equator.linker.service;

import com.equator.linker.SpringBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class InstanceServiceTest extends SpringBaseTest {
    @Autowired
    private InstanceService instanceService;


    @Test
    public void test() {
        instanceService.getPipelineLog(1727915537415495682L);
    }
}
