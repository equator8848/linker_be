package xyz.equator8848.linker.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.equator8848.inf.core.http.model.Response;
import xyz.equator8848.linker.configuration.AppConfig;
import xyz.equator8848.linker.dao.service.InstanceDaoService;
import xyz.equator8848.linker.model.vo.dashboard.BuildStatisticalResult;
import xyz.equator8848.linker.service.FileService;
import xyz.equator8848.linker.service.PublicEntranceService;

@RestController
@RequestMapping("/api/v1/anonymous")
public class AnonymousController {
    @Autowired
    private AppConfig appConfig;

    @Autowired
    private PublicEntranceService publicEntranceService;

    @Autowired
    private FileService fileService;

    @Autowired
    private InstanceDaoService instanceDaoService;

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

    @GetMapping("/build-statistical-result")
    public Response<BuildStatisticalResult> getBuildStatisticalResult() {
        return Response.success(instanceDaoService.getBuildStatisticalResult());
    }

}
