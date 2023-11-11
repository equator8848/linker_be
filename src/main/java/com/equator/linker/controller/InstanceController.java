package com.equator.linker.controller;

import com.equator.core.http.model.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/instance")
public class InstanceController {

    @GetMapping("/ping")
    public Response ping() {
        return Response.success();
    }
}
