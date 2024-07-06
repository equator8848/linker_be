package xyz.equator8848.linker.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.equator8848.inf.auth.annotation.SimpleRBACApi;
import xyz.equator8848.inf.auth.model.constant.RoleType;
import xyz.equator8848.inf.core.http.model.Response;
import xyz.equator8848.linker.model.vo.user.UserCreateVO;
import xyz.equator8848.linker.model.vo.user.UserPasswordUpdateVO;
import xyz.equator8848.linker.model.vo.user.UserUpdateVO;
import xyz.equator8848.linker.service.UserService;

@SimpleRBACApi(requireRoleType = RoleType.USER)
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    @SimpleRBACApi(requireRoleType = RoleType.SYSTEM_ADMIN)
    public Response addUser(@RequestBody @Validated UserCreateVO userCreateVO) {
        userService.addUser(userCreateVO);
        return Response.success();
    }

    @PutMapping("/update")
    @SimpleRBACApi(requireRoleType = RoleType.SYSTEM_ADMIN)
    public Response updateUser(@RequestBody @Validated UserUpdateVO updateVO) {
        userService.updateUser(updateVO);
        return Response.success();
    }

    @PutMapping("/update-status")
    @SimpleRBACApi(requireRoleType = RoleType.SYSTEM_ADMIN)
    public Response updateUserStatus(@RequestParam Long userId, @RequestParam Short userStatus) {
        userService.updateUserStatus(userId, userStatus);
        return Response.success();
    }

    @GetMapping("/user-list")
    @SimpleRBACApi(requireRoleType = RoleType.SYSTEM_ADMIN)
    public Response getUserList(@RequestParam(required = false) String search,
                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                @RequestParam(required = false, defaultValue = "16") Integer pageSize) {
        return Response.success(userService.getUserList(search, pageNum, pageSize));
    }

    @GetMapping("/user-info")
    @SimpleRBACApi(requireRoleType = RoleType.SYSTEM_ADMIN)
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
