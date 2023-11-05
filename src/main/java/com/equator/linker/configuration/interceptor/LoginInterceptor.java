package com.equator.linker.configuration.interceptor;


import com.equator.core.http.model.Response;
import com.equator.core.http.model.ResponseCode;
import com.equator.core.model.exception.PreCondition;
import com.equator.core.util.json.JsonUtil;
import com.equator.linker.common.util.UserAuthUtil;
import com.equator.linker.common.util.UserContextUtil;
import com.equator.linker.dao.service.UserDaoService;
import com.equator.linker.model.constant.ModelStatus;
import com.equator.linker.model.po.TbUser;
import com.equator.linker.model.vo.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private UserDaoService userDaoService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse res, Object o) throws Exception {
        String token = request.getHeader("token");
        res.setContentType("application/json;charset=utf-8");
        if (token == null) {
            invalidToken(res);
            return false;
        }
        LoginUser loginUser = null;
        try {
            loginUser = UserAuthUtil.getLoginUserFromJWT(token);
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

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        UserContextUtil.clear();
    }
}

