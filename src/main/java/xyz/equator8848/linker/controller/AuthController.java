package xyz.equator8848.linker.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.equator8848.inf.auth.annotation.SimpleRBACApi;
import xyz.equator8848.inf.auth.model.constant.RoleType;
import xyz.equator8848.inf.core.http.model.Response;
import xyz.equator8848.linker.model.vo.user.UserLoginDataVO;
import xyz.equator8848.linker.service.AuthService;
import xyz.equator8848.linker.service.UserService;
import xyz.equator8848.linker.service.captcha.CaptchaGenerator;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private CaptchaGenerator captchaGenerator;

    @PostMapping("/login")
    public Response login(@Validated @RequestBody UserLoginDataVO userLoginVO, HttpServletRequest request) {
        return Response.success(authService.login(userLoginVO, request));
    }

    @GetMapping("/captcha")
    public Response captcha() {
        return Response.success(captchaGenerator.codeGen());
    }

    @SimpleRBACApi(requireRoleType = RoleType.USER)
    @GetMapping("/check-token")
    public Response checkToken() {
        return Response.success();
    }
}
