package xyz.equator8848.linker.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.equator8848.inf.auth.annotation.SimpleRBACApi;
import xyz.equator8848.inf.auth.model.constant.RoleType;
import xyz.equator8848.inf.core.http.model.Response;
import xyz.equator8848.linker.model.vo.instance.*;
import xyz.equator8848.linker.service.InstanceAutoBuildConfigService;
import xyz.equator8848.linker.service.InstanceService;
import xyz.equator8848.linker.service.PublicEntranceService;

@SimpleRBACApi(requireRoleType = RoleType.USER)
@RestController
@RequestMapping("/api/v1/instance")
public class InstanceController {
    @Autowired
    private InstanceService instanceService;

    @Autowired
    private PublicEntranceService publicEntranceService;

    @Autowired
    private InstanceAutoBuildConfigService instanceAutoBuildConfigService;

    @PostMapping("/create")
    public Response create(@RequestBody @Valid InstanceCreateRequest instanceCreateRequest) {
        return Response.success(instanceService.create(instanceCreateRequest));
    }

    @DeleteMapping("delete")
    public Response delete(@RequestParam Long instanceId) {
        instanceService.delete(instanceId);
        return Response.success();
    }

    @PutMapping("/update")
    public Response update(@RequestBody @Valid InstanceUpdateRequest instanceUpdateRequest) {
        instanceService.update(instanceUpdateRequest);
        return Response.success();
    }

    @PostMapping("/list")
    public Response list(@RequestBody @Valid InstanceListRequest instanceListRequest) {
        return Response.success(instanceService.list(instanceListRequest));
    }

    @SimpleRBACApi(requireRoleType = RoleType.SYSTEM_ADMIN)
    @PostMapping("/all")
    public Response all(@RequestBody @Valid InstanceListRequest instanceListRequest) {
        return Response.success(instanceService.all(instanceListRequest));
    }

    @PutMapping("/build-pipeline")
    public Response buildPipeline(@RequestParam Long instanceId) {
        instanceService.buildPipeline(instanceId);
        return Response.success();
    }

    @PutMapping("/stop-pipeline")
    public Response stopPipeline(@RequestParam Long instanceId) {
        instanceService.stopPipeline(instanceId);
        return Response.success();
    }

    @PostMapping("/star")
    public Response star(@RequestBody @Valid InstanceStarRequest instanceStarRequest) {
        instanceService.instanceStarAction(instanceStarRequest);
        return Response.success();
    }

    @GetMapping("/pipeline-build-log")
    public Response getPipelineLog(@RequestParam Long instanceId) {
        return Response.success(instanceService.getPipelineLog(instanceId));
    }

    @PostMapping("/copy")
    public Response copy(Long instanceId) {
        return Response.success(instanceService.copy(instanceId));
    }

    @GetMapping("/get-instance-build-log")
    public Response getInstanceBuildLog(@RequestParam Long instanceId,
                                        @RequestParam(required = false, defaultValue = "16") Integer logSize) {
        return Response.success(instanceService.getInstanceBuildLog(instanceId, logSize));
    }

    @GetMapping("/public-entrance-details")
    public Response getPublicEntranceDetails(@RequestParam Long instanceId) {
        return Response.success(publicEntranceService.getDetails(instanceId));
    }

    @PostMapping("/update-public-entrance")
    public Response updatePublicEntrance(@RequestBody @Valid PublicEntranceUpdateRequest publicEntranceUpdateRequest) {
        publicEntranceService.update(publicEntranceUpdateRequest);
        return Response.success();
    }

    @GetMapping("/auto-build-config-details")
    public Response getAutoBuildConfigDetails(@RequestParam Long instanceId) {
        return Response.success(instanceAutoBuildConfigService.getDetails(instanceId));
    }

    @PostMapping("/update-auto-build-config")
    public Response updateAutoBuildConfig(@RequestBody @Valid InstanceAutoBuildConfigUpdateRequest instanceAutoBuildConfigUpdateRequest) {
        instanceAutoBuildConfigService.update(instanceAutoBuildConfigUpdateRequest);
        return Response.success();
    }
}
