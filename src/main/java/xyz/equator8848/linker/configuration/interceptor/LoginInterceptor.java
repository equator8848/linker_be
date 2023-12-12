package xyz.equator8848.linker.configuration.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xyz.equator8848.inf.auth.model.bo.LoginUser;
import xyz.equator8848.inf.auth.util.UserAuthUtil;
import xyz.equator8848.inf.auth.util.UserContextUtil;
import xyz.equator8848.inf.core.http.model.Response;
import xyz.equator8848.inf.core.http.model.ResponseCode;
import xyz.equator8848.inf.core.model.exception.PreCondition;
import xyz.equator8848.inf.core.util.json.JsonUtil;
import xyz.equator8848.linker.configuration.ApiPermission;
import xyz.equator8848.linker.dao.service.UserDaoService;
import xyz.equator8848.linker.model.constant.ModelStatus;
import xyz.equator8848.linker.model.constant.RoleType;
import xyz.equator8848.linker.model.po.TbUser;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private UserDaoService userDaoService;

    @Autowired
    private UserAuthUtil userAuthUtil;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse res, Object handler) throws Exception {
        String token = request.getHeader("token");
        res.setContentType("application/json;charset=utf-8");
        if (token == null) {
            invalidToken(res);
            return false;
        }
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        LoginUser loginUser = null;
        try {
            loginUser = userAuthUtil.getLoginUserFromJWT(token);
        } catch (Exception e) {
            log.debug("JWT验证异常 {}", e.getMessage(), e);
            invalidToken(res);
            return false;
        }
        if (loginUser == null) {
            invalidToken(res);
            return false;
        } else {
            TbUser userFromCacheByUid = userDaoService.getUserFromCacheByUid(loginUser.getUid());
            PreCondition.isNotNull(userFromCacheByUid, "找不到对应用户");
            PreCondition.isTrue(ModelStatus.UserStatus.NORMAL.equals(userFromCacheByUid.getStatus()), "当前账号已被冻结，请联系管理员");
            // 检查角色权限
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            apiPermissionValidate(handlerMethod, userFromCacheByUid);
            UserContextUtil.addUser(loginUser);
            return true;
        }
    }

    /**
     * 返回token无效异常提示
     *
     * @param res
     * @throws IOException
     */
    private void invalidToken(HttpServletResponse res) throws IOException {
        Response response = new Response();
        response.setStatus(ResponseCode.UNAUTHORIZED.getStatus());
        response.setMsg(ResponseCode.UNAUTHORIZED.getMsg());
        res.getWriter().write(JsonUtil.toJson(response));
    }

    private void apiPermissionValidate(HandlerMethod handlerMethod, TbUser userFromCacheByUid) {
        ApiPermission classAnnotation = handlerMethod.getBeanType().getAnnotation(ApiPermission.class);
        int requireRoleType = RoleType.VISITOR;
        if (Objects.nonNull(classAnnotation)) {
            requireRoleType = classAnnotation.requireRoleType();
        }

        ApiPermission methodAnnotation = handlerMethod.getMethodAnnotation(ApiPermission.class);
        if (Objects.nonNull(methodAnnotation)) {
            requireRoleType = methodAnnotation.requireRoleType();
        }

        if (requireRoleType == RoleType.VISITOR) {
            return;
        }

        int userRoleType = Optional.ofNullable(userFromCacheByUid.getRoleType()).orElse(RoleType.VISITOR).intValue();

        PreCondition.isTrue(userRoleType >= requireRoleType, "权限不足");
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        UserContextUtil.clear();
    }
}

