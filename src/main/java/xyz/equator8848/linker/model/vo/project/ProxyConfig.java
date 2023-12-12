package xyz.equator8848.linker.model.vo.project;

import lombok.Data;

import java.util.List;

@Data
public class ProxyConfig {
    private List<ProxyPassConfig> proxyPassConfigs;
}
