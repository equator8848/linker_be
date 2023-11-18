package com.equator.linker.controller;

import com.equator.linker.service.OpenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/open-api")
public class OpenController {
    @Autowired
    private OpenService openService;

    @GetMapping("/nginx-conf")
    public String getNginxConf(@RequestParam String getNginxConfSecret) {
        return openService.getNginxConf(getNginxConfSecret);
    }

    @GetMapping("/dockerfile")
    public String getDockerfile(@RequestParam String getDockerfileSecret) {
        return openService.getDockerfile(getDockerfileSecret);
    }
}
