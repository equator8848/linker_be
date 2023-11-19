package com.equator.linker.controller;


import com.equator.core.http.model.Response;
import com.equator.linker.configuration.ApiPermission;
import com.equator.linker.model.constant.RoleType;
import com.equator.linker.service.AppSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@ApiPermission(requireRoleType = RoleType.SYSTEM_ADMIN)
@RestController
@RequestMapping("/api/v1/app-settings")
public class AppSettingController {
    @Autowired
    private AppSettingService appSettingService;

    @GetMapping("/list")
    public Response list(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                         @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        return Response.success(appSettingService.list(pageNum, pageSize));
    }

    @PostMapping("/add")
    public Response add(@RequestParam String settingKey,
                        @RequestParam String settingValue,
                        @RequestParam(required = false) String remark) {
        appSettingService.add(settingKey, settingValue, remark);
        return Response.success();
    }

    @PutMapping("/update")
    public Response update(@RequestParam Long id, @RequestParam String settingValue) {
        appSettingService.update(id, settingValue);
        return Response.success();
    }
}
