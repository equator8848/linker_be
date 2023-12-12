package xyz.equator8848.linker.controller;


import xyz.equator8848.linker.model.vo.user.UserLoginDataVO;
import xyz.equator8848.linker.service.AuthService;
import xyz.equator8848.linker.service.UserService;
import xyz.equator8848.linker.service.captcha.CaptchaGenerator;
import xyz.equator8848.inf.core.http.model.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/check-token")
    public Response checkToken() {
        return Response.success();
    }

    @GetMapping("/captcha")
    public Response captcha() {
        return Response.success(captchaGenerator.codeGen());
    }
}
