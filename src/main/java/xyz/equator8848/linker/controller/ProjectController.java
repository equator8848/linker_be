package xyz.equator8848.linker.controller;

import xyz.equator8848.linker.configuration.ApiPermission;
import xyz.equator8848.linker.model.constant.RoleType;
import xyz.equator8848.linker.model.vo.project.ProjectCreateRequest;
import xyz.equator8848.linker.model.vo.project.ProjectListRequest;
import xyz.equator8848.linker.model.vo.project.ProjectUpdateRequest;
import xyz.equator8848.linker.service.ProjectService;
import xyz.equator8848.linker.service.ProjectTemplateService;
import xyz.equator8848.inf.core.http.model.Response;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectTemplateService projectTemplateService;

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

    @GetMapping("/list")
    public Response list() {
        return Response.success(projectService.list());
    }

    @ApiPermission(requireRoleType = RoleType.SYSTEM_ADMIN)
    @PostMapping("/all")
    public Response all(@RequestBody @Valid ProjectListRequest projectListRequest) {
        return Response.success(projectService.all(projectListRequest));
    }

    @GetMapping("/details")
    public Response details(@RequestParam Long projectId) {
        return Response.success(projectService.details(projectId));
    }

    @GetMapping("/branches")
    public Response branches(@RequestParam Long projectId) {
        return Response.success(projectService.branches(projectId));
    }

    @GetMapping("/templates")
    public Response getTemplates() {
        return Response.success(projectTemplateService.all());
    }
}
