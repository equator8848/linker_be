package com.equator.linker.model.constant;

/**
 * @author libinkai
 * @date 2020/11/1 1:54 下午
 */
public class ModelStatus {
    /**
     * 无意义的主键
     */
    public static final Long DUMMY_ID = 0L;

    public static final String UNKNOWN_USER_NAME = "unknown";

    public static final class BooleanVal {
        public final static Integer TRUE = 1;
        public final static Integer FALSE = 0;

        public static Boolean getBoolean(Integer val) {
            return TRUE.equals(val) ? Boolean.TRUE : Boolean.FALSE;
        }

        public static Integer getInteger(boolean val) {
            return val ? TRUE : FALSE;
        }
    }

    public static class DelFlag {
        public final static Short DELETED = 1;
        public final static Short NORMAL = 0;
    }

    public static class UserStatus {
        public final static Short DISABLE = 0;
        public final static Short NORMAL = 1;

    }

    public static final String DEFAULT_PHONE_PREFIX = "86";

    /**
     * 角色类型
     */
    public static class RoleType {
        /**
         * 游客
         */
        public final static Short VISITOR = 1;

        /**
         * 普通用户
         */
        public final static Short USER = 2;

        /**
         * 系统管理员
         */
        public final static Short SYSTEM_ADMIN = 4;

        /**
         * 超级管理员
         */
        public final static Short SUPER_ADMIN = 8;
    }
}
