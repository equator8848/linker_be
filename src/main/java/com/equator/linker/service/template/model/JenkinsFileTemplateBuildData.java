package com.equator.linker.service.template.model;

import lombok.Data;


@Data
public class JenkinsFileTemplateBuildData {
    private String dockerFileUrl;

    private String nginxConfUrl;
}
