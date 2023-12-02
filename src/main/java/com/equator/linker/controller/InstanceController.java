package com.equator.linker.controller;

import com.equator.core.http.model.Response;
import com.equator.linker.configuration.ApiPermission;
import com.equator.linker.model.constant.RoleType;
import com.equator.linker.model.vo.instance.*;
import com.equator.linker.service.InstanceService;
import com.equator.linker.service.PublicEntranceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/instance")
public class InstanceController {
    @Autowired
    private InstanceService instanceService;

    @Autowired
    private PublicEntranceService publicEntranceService;

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

    @ApiPermission(requireRoleType = RoleType.SYSTEM_ADMIN)
    @PostMapping("/all")
    public Response all(@RequestBody @Valid InstanceListRequest instanceListRequest) {
        return Response.success(instanceService.all(instanceListRequest));
    }

    @PutMapping("/build-pipeline")
    public Response buildPipeline(@RequestParam Long instanceId) {
        instanceService.buildPipeline(instanceId);
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

    @GetMapping("/public-entrance-details")
    public Response getPublicEntranceDetails(@RequestParam Long instanceId) {
        return Response.success(publicEntranceService.getDetails(instanceId));
    }

    @PostMapping("/update-public-entrance")
    public Response updatePublicEntrance(@RequestBody @Valid PublicEntranceUpdateRequest publicEntranceUpdateRequest) {
        publicEntranceService.update(publicEntranceUpdateRequest);
        return Response.success();
    }
}
