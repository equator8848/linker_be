package com.equator.linker.controller;


import com.equator.core.http.model.Response;
import com.equator.linker.configuration.ApiPermission;
import com.equator.linker.model.constant.RoleType;
import com.equator.linker.model.vo.user.UserCreateVO;
import com.equator.linker.model.vo.user.UserPasswordUpdateVO;
import com.equator.linker.model.vo.user.UserUpdateVO;
import com.equator.linker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    @ApiPermission(requireRoleType = RoleType.SYSTEM_ADMIN)
    public Response addUser(@RequestBody @Validated UserCreateVO userCreateVO) {
        userService.addUser(userCreateVO);
        return Response.success();
    }

    @PutMapping("/update")
    @ApiPermission(requireRoleType = RoleType.SYSTEM_ADMIN)
    public Response updateUser(@RequestBody @Validated UserUpdateVO updateVO) {
        userService.updateUser(updateVO);
        return Response.success();
    }

    @PutMapping("/update-status")
    @ApiPermission(requireRoleType = RoleType.SYSTEM_ADMIN)
    public Response updateUserStatus(@RequestParam Long userId, @RequestParam Short userStatus) {
        userService.updateUserStatus(userId, userStatus);
        return Response.success();
    }

    @GetMapping("/user-list")
    @ApiPermission(requireRoleType = RoleType.SYSTEM_ADMIN)
    public Response getUserList(@RequestParam(required = false) String search,
                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                @RequestParam(required = false, defaultValue = "16") Integer pageSize) {
        return Response.success(userService.getUserList(search, pageNum, pageSize));
    }

    @GetMapping("/user-info")
    @ApiPermission(requireRoleType = RoleType.SYSTEM_ADMIN)
    public Response getUserInfo(@RequestParam Long uid) {
        return Response.success(userService.getUserInfo(uid));
    }

    @PutMapping("/change-username")
    public Response changeUsername(@RequestParam String newUsername) {
        return Response.success(userService.changeUsername(newUsername));
    }

    @PutMapping("/update-user-password")
    public Response updateUserPassword(@RequestBody @Valid UserPasswordUpdateVO userPasswordUpdateVO) {
        userService.updateUserPassword(userPasswordUpdateVO);
        return Response.success();
    }
}
