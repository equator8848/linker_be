package xyz.equator8848.linker.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.equator8848.inf.auth.annotation.SimpleRBACApi;
import xyz.equator8848.inf.auth.model.constant.RoleType;
import xyz.equator8848.inf.core.http.model.Response;
import xyz.equator8848.linker.service.AppSettingService;

@SimpleRBACApi(requireRoleType = RoleType.SUPER_ADMIN)
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

    @DeleteMapping("/delete")
    public Response delete(@RequestParam Long id) {
        appSettingService.delete(id);
        return Response.success();
    }
}
