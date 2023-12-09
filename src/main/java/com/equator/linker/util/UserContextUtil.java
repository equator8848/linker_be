package com.equator.linker.util;


import com.equator.linker.model.vo.LoginUser;
import org.apache.commons.lang3.tuple.Pair;

/**
 * 用户上下文工具类
 */
public class UserContextUtil {

    private static ThreadLocal<LoginUser> entrySet = new ThreadLocal<>();

    /**
     * 添加用户信息
     *
     * @param loginUser
     */
    public static void addUser(LoginUser loginUser) {
        entrySet.set(loginUser);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static LoginUser getUser() {
        return entrySet.get();
    }

    /**
     * 获取用户信息、以及是否是管理员
     *
     * @return
     */
    public static Pair<Boolean, LoginUser> getUserPair() {
        LoginUser loginUser = entrySet.get();
        return Pair.of(UserAuthUtil.isAdmin(loginUser), loginUser);
    }

    /***
     * 直接获取UID
     * @return
     */
    public static Long getUserId() {
        LoginUser loginUser = entrySet.get();
        return loginUser == null ? 0L : loginUser.getUid();
    }

    /**
     * 清除用户信息
     */
    public static void clear() {
        entrySet.remove();
    }
}