package xyz.equator8848.linker.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.equator8848.inf.auth.annotation.SimpleRBACApi;
import xyz.equator8848.inf.auth.model.constant.RoleType;
import xyz.equator8848.inf.core.http.model.Response;
import xyz.equator8848.linker.scheduler.InstanceScheduleService;

@SimpleRBACApi(requireRoleType = RoleType.SUPER_ADMIN)
@RestController
@RequestMapping("/api/v1/admin-script")
public class AdminScriptController {
    @Autowired
    private InstanceScheduleService instanceScheduleService;

    @PostMapping("/delete-docker-image")
    public Response deleteDockerImage() {
        instanceScheduleService.deleteDockerImage();
        return Response.success();
    }

}
