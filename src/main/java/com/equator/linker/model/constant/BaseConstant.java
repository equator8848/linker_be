package com.equator.linker.model.constant;

import lombok.Data;

/**
 * @author libinkai
 * @date 2020/11/1 1:05 上午
 */
@Data
public class BaseConstant {
    public static final String ERROR_RESULT = "ERROR";

    /**
     * 自动化操作时线程等待时间
     */
    public static final long WAIT_TIME_MS = 5000;

    /**
     * 单位转换
     */
    public static final class SizeTransformer {
        public static final int BYTES_GB = 1073741824;
    }

    public static final class UserIdentificationType {
        public static final String PHONE = "phone";
        public static final String EMAIL = "email";
        public static final String USERNAME = "username";
    }

    /**
     * 付费计划
     */
    public static final class Planing {
        /**
         * 测试
         */
        public static final int TEST = 1;
        /**
         * 试用版
         */
        public static final int EXPLORER = 2;
        /**
         * 标准版
         */
        public static final int STANDARD = 4;
        /**
         * 专业版
         */
        public static final int PROFESSION = 8;
        /**
         * 旗舰版
         */
        public static final int FLAGSHIP = 16;
        /**
         * 定制版
         */
        public static final int CUSTOM = 32;
    }
}
