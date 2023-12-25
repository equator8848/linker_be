package xyz.equator8848.linker.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.equator8848.inf.auth.annotation.SimpleRBACApi;
import xyz.equator8848.inf.core.http.model.Response;
import xyz.equator8848.linker.model.constant.RoleType;
import xyz.equator8848.linker.service.LoginLogService;

@SimpleRBACApi(requireRoleType = RoleType.SYSTEM_ADMIN)
@RestController
@RequestMapping("/api/v1/login-log")
public class LoginLogController {

    @Autowired
    private LoginLogService loginLogService;

    @GetMapping("/login-log")
    public Response getLoginLog(@RequestParam Integer days) {
        return Response.success(loginLogService.getLoginLogList(days));
    }
}
