package com.equator.linker.controller;

import com.equator.core.http.model.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/open")
public class OpenController {

    @GetMapping("/ping")
    public Response ping() {
        return Response.success();
    }
}
