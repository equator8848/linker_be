package com.equator.linker.controller;

import com.equator.core.http.model.Response;
import com.equator.linker.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {


    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/count-project-del-status")
    public Response countVolumeClearStatus() {
        return Response.success(dashboardService.countProjectDelStatus());
    }


    @GetMapping("/count-instance-del-status")
    public Response countInstanceClearStatus() {
        return Response.success(dashboardService.countInstanceDelStatus());
    }


    @GetMapping("/count-user-status")
    public Response countUserStatus() {
        return Response.success(dashboardService.countUserStatus());
    }
}
