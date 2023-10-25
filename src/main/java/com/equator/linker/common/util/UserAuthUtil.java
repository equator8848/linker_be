package com.equator.linker.common.util;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.equator.core.model.exception.ForbiddenException;
import com.equator.core.model.exception.PreCondition;
import com.equator.core.util.json.JsonUtil;
import com.equator.core.util.jwt.JwtUtil;
import com.equator.linker.model.constant.ModelStatus;
import com.equator.linker.model.vo.LoginUser;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;

/**
 * 用户鉴权工具类
 */
public class UserAuthUtil {
    private final static String SYSTEM_KEY = "system";

    private final static String SYSTEM_VALUE = "dayu";

    private final static String LOGIN_USER_KEY = "loginUser";

    /**
     * 构建 JWT
     *
     * @param loginUser
     * @return
     */
    public static String buildToken(LoginUser loginUser) {
        return JwtUtil.code(ImmutableMap.of(SYSTEM_KEY, SYSTEM_VALUE, LOGIN_USER_KEY, JsonUtil.toJson(loginUser)));
    }

    public static Pair<String, Date> buildTokenWithExpiredTime(LoginUser loginUser) {
        return JwtUtil.codeWithExpiredTime(ImmutableMap.of(SYSTEM_KEY, SYSTEM_VALUE, LOGIN_USER_KEY, JsonUtil.toJson(loginUser)));
    }

    /**
     * 从 token 中获取 loginUser
     *
     * @param token
     * @return
     */
    public static LoginUser getLoginUserFromJWT(String token) {
        DecodedJWT decodedJWT = JwtUtil.decode(token);
        String loginUserStr = decodedJWT.getClaim(LOGIN_USER_KEY).asString();
        PreCondition.isNotNull(loginUserStr);
        return JsonUtil.fromJson(loginUserStr, LoginUser.class);
    }

    /**
     * 从 token 中获取 loginUser
     *
     * @param token
     * @return
     */
    public static Pair<LoginUser, Date> parseLoginUserFromJWT(String token) {
        DecodedJWT decodedJWT = JwtUtil.decode(token);
        String loginUserStr = decodedJWT.getClaim(LOGIN_USER_KEY).asString();
        PreCondition.isNotNull(loginUserStr);
        LoginUser loginUser = JsonUtil.fromJson(loginUserStr, LoginUser.class);
        return Pair.of(loginUser, decodedJWT.getExpiresAt());
    }

    /**
     * @param uid 操作的目标资源uid
     */
    public static void checkPermission(Integer uid) {
        LoginUser loginUser = UserContextUtil.getUser();
        if (!ModelStatus.UserType.ADMIN.equals(loginUser.getUserType())) {
            if (!loginUser.getUid().equals(uid)) {
                throw new ForbiddenException("你没有权限操作该资源");
            }
        }
    }

    /**
     * 判断是否是管理员
     *
     * @return
     */
    public static boolean isAdmin() {
        LoginUser loginUser = UserContextUtil.getUser();
        return ModelStatus.UserType.ADMIN.equals(loginUser.getUserType());
    }

    /**
     * 判断是否是普通用户
     *
     * @return
     */
    public static boolean isUser() {
        LoginUser loginUser = UserContextUtil.getUser();
        return ModelStatus.UserType.USER.equals(loginUser.getUserType());
    }
}