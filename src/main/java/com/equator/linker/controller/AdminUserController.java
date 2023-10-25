package com.equator.linker.controller;


import com.equator.core.http.model.Response;
import com.equator.linker.model.vo.user.CreateAdminUserVO;
import com.equator.linker.model.vo.user.UpdateAdminUserVO;
import com.equator.linker.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin-user")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @PostMapping("/add")
    public Response addUser(@RequestBody @Validated CreateAdminUserVO createAdminUserVO) {
        return Response.success(adminUserService.add(createAdminUserVO));
    }

    @PutMapping("/update")
    public Response updateUser(@RequestBody @Validated UpdateAdminUserVO updateAdminUserVO) {
        adminUserService.update(updateAdminUserVO);
        return Response.success();
    }

    @GetMapping("/list")
    public Response getUserList(@RequestParam(required = false) String search,
                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                @RequestParam(required = false, defaultValue = "16") Integer pageSize) {
        return Response.success(adminUserService.list(search, pageNum, pageSize));
    }
}
