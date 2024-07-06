package xyz.equator8848.linker.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.equator8848.inf.auth.annotation.SimpleRBACApi;
import xyz.equator8848.inf.auth.model.constant.RoleType;
import xyz.equator8848.inf.core.http.model.Response;
import xyz.equator8848.linker.model.vo.project.*;
import xyz.equator8848.linker.service.ProjectBranchService;
import xyz.equator8848.linker.service.ProjectService;
import xyz.equator8848.linker.service.ProjectTemplateService;

@SimpleRBACApi(requireRoleType = RoleType.USER)
@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectTemplateService projectTemplateService;

    @Autowired
    private ProjectBranchService projectBranchService;

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
        return Response.success(projectBranchService.branches(projectId));
    }

    /**
     * 项目使用
     *
     * @param projectBranchesRequest
     * @return
     */
    @PostMapping("/peek-branches-with-tips")
    public Response branchesWithTips(@Valid @RequestBody ProjectBranchesRequest projectBranchesRequest) {
        return Response.success(projectBranchService.branchesWithTips(projectBranchesRequest));
    }

    /**
     * 实例使用
     *
     * @param projectId
     * @param searchKeyword
     * @return
     */
    @GetMapping("/branches-with-tips")
    public Response branchesWithTips(@RequestParam Long projectId, @RequestParam(required = false) String searchKeyword) {
        return Response.success(projectBranchService.branchesWithTips(projectId, searchKeyword));
    }

    /**
     * 实例使用
     *
     * @param projectCommitsRequest
     * @return
     */
    @PostMapping("/commits-info")
    public Response commitsInfo(@Valid @RequestBody ProjectCommitsRequest projectCommitsRequest) {
        return Response.success(projectBranchService.commitsInfo(projectCommitsRequest));
    }

    @GetMapping("/templates")
    public Response getTemplates() {
        return Response.success(projectTemplateService.allFromCode());
    }
}
