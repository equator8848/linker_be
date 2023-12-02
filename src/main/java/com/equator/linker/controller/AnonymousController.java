package com.equator.linker.controller;

import com.equator.core.http.model.Response;
import com.equator.linker.configuration.AppConfig;
import com.equator.linker.service.PublicEntranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/anonymous")
public class AnonymousController {
    @Autowired
    private AppConfig appConfig;

    @Autowired
    private PublicEntranceService publicEntranceService;

    @GetMapping("/ping")
    public Response ping() {
        return Response.success(appConfig.getConfig());
    }

    @GetMapping("/public-entrance")
    public Response getPublicEntrance() {
        return Response.success(publicEntranceService.getPublicEntrance());
    }

}
