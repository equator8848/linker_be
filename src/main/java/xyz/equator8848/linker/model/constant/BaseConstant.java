package xyz.equator8848.linker.model.constant;

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

    public enum AccessLevel {
        PRIVATE(1),

        PROTECTED(2),

        PUBLIC(4);

        private int code;

        AccessLevel(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public enum ProjectInstanceRefType {
        /**
         * 自己创建
         */
        OWNER,
        /**
         * 加入
         */
        JOIN;
    }

    /**
     * 默认镜像版本
     */
    public static final String DEFAULT_IMAGE_VERSION = "latest";
}
