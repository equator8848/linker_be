package com.equator.linker.controller;

import com.equator.core.http.model.Response;
import com.equator.linker.model.vo.instance.InstanceCreateRequest;
import com.equator.linker.model.vo.instance.InstanceListRequest;
import com.equator.linker.model.vo.instance.InstanceStarRequest;
import com.equator.linker.model.vo.instance.InstanceUpdateRequest;
import com.equator.linker.service.InstanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/instance")
public class InstanceController {
    @Autowired
    private InstanceService instanceService;

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
}
