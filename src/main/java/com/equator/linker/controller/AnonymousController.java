package com.equator.linker.controller;

import com.equator.inf.core.http.model.Response;
import com.equator.linker.configuration.AppConfig;
import com.equator.linker.service.FileService;
import com.equator.linker.service.PublicEntranceService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/anonymous")
public class AnonymousController {
    @Autowired
    private AppConfig appConfig;

    @Autowired
    private PublicEntranceService publicEntranceService;

    @Autowired
    private FileService fileService;

    @GetMapping("/ping")
    public Response ping() {
        return Response.success(appConfig.getConfig());
    }

    @GetMapping("/public-entrance")
    public Response getPublicEntrance() {
        return Response.success(publicEntranceService.getPublicEntrance());
    }

    @GetMapping("/download-instance-latest-artifact/{instanceId}")
    public void file(@PathVariable("instanceId") Long instanceId,
                     @RequestParam(required = false) String fileName,
                     HttpServletResponse resp) {
        fileService.downloadInstanceArtifact(instanceId, fileName, resp);
    }

}
