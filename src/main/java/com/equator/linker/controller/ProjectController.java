package com.equator.linker.controller;

import com.equator.core.http.model.Response;
import com.equator.linker.model.vo.project.ProjectCreateRequest;
import com.equator.linker.model.vo.project.ProjectUpdateRequest;
import com.equator.linker.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PostMapping("/create")
    public Response create(@RequestBody @Valid ProjectCreateRequest projectCreateRequest) {
        return Response.success(projectService.create(projectCreateRequest));
    }

    @DeleteMapping("delete")
    public Response delete(@RequestParam Long projectId) {
        projectService.delete(projectId);
        return Response.success();
    }

    @PutMapping("/update")
    public Response update(@RequestBody @Valid ProjectUpdateRequest projectUpdateRequest) {
        projectService.update(projectUpdateRequest);
        return Response.success();
    }

    @GetMapping("/all")
    public Response all() {
        return Response.success(projectService.all());
    }

    @GetMapping("/details")
    public Response details(@RequestParam Long projectId) {
        return Response.success(projectService.details(projectId));
    }

    @GetMapping("/branches")
    public Response branches(@RequestParam Long projectId) {
        return Response.success(projectService.branches(projectId));
    }
}
