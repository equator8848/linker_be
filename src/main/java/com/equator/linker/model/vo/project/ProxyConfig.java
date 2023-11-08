package com.equator.linker.model.vo.project;

import lombok.Data;

import java.util.List;

@Data
public class ProxyConfig {
    private List<ProxyPassConfig> proxyPassConfigs;
}
