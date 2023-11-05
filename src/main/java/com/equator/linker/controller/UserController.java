package com.equator.linker.controller;


import com.equator.core.http.model.Response;
import com.equator.linker.model.vo.user.UserCreateVO;
import com.equator.linker.model.vo.user.UserUpdateVO;
import com.equator.linker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public Response addUser(@RequestBody @Validated UserCreateVO userCreateVO) {
        userService.addUser(userCreateVO);
        return Response.success();
    }

    @PutMapping("/update")
    public Response updateUser(@RequestBody @Validated UserUpdateVO updateVO) {
        userService.updateUser(updateVO);
        return Response.success();
    }

    @PutMapping("/update-status")
    public Response updateUserStatus(@RequestParam Integer userId, @RequestParam Short userStatus) {
        userService.updateUserStatus(userId, userStatus);
        return Response.success();
    }

    @GetMapping("/user-list")
    public Response getUserList(@RequestParam(required = false) String search,
                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                @RequestParam(required = false, defaultValue = "16") Integer pageSize) {
        return Response.success(userService.getUserList(search, pageNum, pageSize));
    }

    @GetMapping("/user-info")
    public Response getUserInfo(@RequestParam Integer uid) {
        return Response.success(userService.getUserInfo(uid));
    }
}
