package com.equator.linker.controller;


import com.equator.inf.core.http.model.Response;
import com.equator.linker.configuration.ApiPermission;
import com.equator.linker.model.constant.RoleType;
import com.equator.linker.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@ApiPermission(requireRoleType = RoleType.SYSTEM_ADMIN)
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
