package com.equator.linker.service.impl;

import com.equator.linker.service.OpenService;
import org.springframework.stereotype.Service;

@Service
public class OpenServiceImpl implements OpenService {
    @Override
    public String getNginxConf(String getNginxConfSecret) {
        return null;
    }

    @Override
    public String getDockerfile(String getDockerfileSecret) {
        return null;
    }
}
