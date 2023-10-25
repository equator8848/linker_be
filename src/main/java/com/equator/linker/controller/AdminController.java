package com.equator.linker.controller;


import com.equator.core.http.model.Response;
import com.equator.linker.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/ping")
    public Response ping(HttpServletRequest request) {
        return Response.success(adminService.ping(request));
    }
}
