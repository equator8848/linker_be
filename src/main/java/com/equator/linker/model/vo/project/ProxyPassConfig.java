package com.equator.linker.model.vo.project;

import lombok.Data;

@Data
public class ProxyPassConfig {
    private String location;

    private String proxyPass;

    private String rewriteConfig;
}
