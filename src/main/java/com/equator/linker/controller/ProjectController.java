package com.equator.linker.controller;

import com.equator.core.http.model.Response;
import com.equator.linker.model.vo.project.ProjectCreateRequest;
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

    @GetMapping("/all")
    public Response all() {
        return Response.success(projectService.all());
    }
}
