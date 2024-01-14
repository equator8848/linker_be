package xyz.equator8848.linker.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.equator8848.inf.auth.annotation.SimpleRBACApi;
import xyz.equator8848.inf.auth.model.constant.RoleType;
import xyz.equator8848.inf.core.http.model.Response;
import xyz.equator8848.linker.model.vo.project.ProjectBranchesRequest;
import xyz.equator8848.linker.model.vo.project.ProjectCreateRequest;
import xyz.equator8848.linker.model.vo.project.ProjectListRequest;
import xyz.equator8848.linker.model.vo.project.ProjectUpdateRequest;
import xyz.equator8848.linker.service.ProjectService;
import xyz.equator8848.linker.service.ProjectTemplateService;

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

    @SimpleRBACApi(requireRoleType = RoleType.SYSTEM_ADMIN)
    @PostMapping("/all")
    public Response all(@RequestBody @Valid ProjectListRequest projectListRequest) {
        return Response.success(projectService.all(projectListRequest));
    }

    @GetMapping("/details")
    public Response details(@RequestParam Long projectId) {
        return Response.success(projectService.details(projectId));
    }

    @GetMapping("/branches")
    @Deprecated
    public Response branches(@RequestParam Long projectId) {
        return Response.success(projectService.branches(projectId));
    }

    @GetMapping("/branches-with-tips")
    public Response branchesWithTips(@RequestParam Long projectId) {
        return Response.success(projectService.branchesWithTips(projectId));
    }

    @PostMapping("/peek-branches-with-tips")
    public Response branchesWithTips(@Valid @RequestBody ProjectBranchesRequest projectBranchesRequest) {
        return Response.success(projectService.branchesWithTips(projectBranchesRequest));
    }

    @GetMapping("/templates")
    public Response getTemplates() {
        return Response.success(projectTemplateService.allFromCode());
    }
}
