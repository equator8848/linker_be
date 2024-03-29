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
        /**
         * 私有
         */
        PRIVATE(1, "私有"),

        /**
         * 邀请访问
         */
        PROTECTED(2, "邀请访问"),

        /**
         * 公开访问
         */
        PUBLIC(4, "公开访问"),

        /**
         * 公开编辑
         */
        PUBLIC_WRITE(8, "公开编辑");

        private int code;

        private String cnName;

        AccessLevel(int code, String cnName) {
            this.code = code;
            this.cnName = cnName;
        }

        public int getCode() {
            return code;
        }

        public String getCnName() {
            return cnName;
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
