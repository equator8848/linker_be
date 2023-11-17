package com.equator.linker.service;

public interface OpenService {
    String getNginxConf(String getNginxConfSecret);

    String getDockerfile(String getDockerfileSecret);
}
