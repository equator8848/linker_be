package xyz.equator8848.linker.service;

public interface OpenService {
    String getNginxConf(String getNginxConfSecret);

    String getDockerfile(String getDockerfileSecret);
}
