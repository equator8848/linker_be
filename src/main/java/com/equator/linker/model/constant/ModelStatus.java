package com.equator.linker.model.constant;

/**
 * @author libinkai
 * @date 2020/11/1 1:54 下午
 */
public class ModelStatus {
    /**
     * 无意义的主键
     */
    public static final Integer DUMMY = 0;

    public static final String UNKNOWN_USER_NAME = "unknown";

    /**
     * 大禹系统ID
     */
    public static final Integer DAYU_SYSTEM_ID = 0;

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

    public static class Base {
        public final static Short DELETED = 0;
        public final static Short NORMAL = 1;
    }

    public static class ClearStatus {
        public final static Short NO_NEED_CLEAR = 0;
        public final static Short NEED_CLEAR = 1;
        public final static Short CLEARED = 2;
    }

    public static class UserType {
        public final static Short ADMIN = 0;
        public final static Short USER = 1;
    }

    public static class UserStatus {
        public final static Short DISABLE = 0;
        public final static Short NORMAL = 1;

    }

    public static class SystemType {
        public final static Short WEB = 1;
        public final static Short ADMIN = 2;
    }

    public static class UserSystemType {
        public final static Short DAYU = 1;
        public final static Short MOFANG = 2;
    }

    public static class OperationType {
        // 创建
        public final static short CREATE_INSTANCE = 10;
        // 软删除
        public final static short SOFT_DELETE_INSTANCE = 11;
        // 更新实例状态
        public final static short START_INSTANCE_STATE = 12;

        public final static short STOP_INSTANCE_STATE = 13;

        public final static short RESTART_INSTANCE_STATE = 14;

        public final static short MIGRATE_INSTANCE = 15;

        // 硬删除
        public final static short HARD_DELETE_INSTANCE = 16;

        // 软删除
        public final static short SOFT_DELETE_INSTANCE_AND_VOLUME = 17;

        // 硬删除数据盘
        public final static short HARD_DELETE_VOLUME = 18;

        // 软删除数据卷
        public final static short SOFT_DELETE_VOLUME = 19;

        /**
         * 进入维护模式
         */
        public final static short ENTER_MAINTENANCE_MODE = 20;

        /**
         * 退出维护模式
         */
        public final static short EXIT_MAINTENANCE_MODE = 21;

        /**
         * 添加资源套餐
         */
        public final static short ADD_SPECIFICATION = 30;

        /**
         * 更新资源套餐
         */
        public final static short UPDATE_SPECIFICATION = 31;

        /**
         * 恢复软删除的实例
         */
        public final static short CANCEL_DELETE_INSTANCE = 32;


        public static String convert(short code) {
            if (CREATE_INSTANCE == code) {
                return "创建实例";
            } else if (START_INSTANCE_STATE == code) {
                return "启动实例";
            } else if (STOP_INSTANCE_STATE == code) {
                return "停止实例";
            } else if (RESTART_INSTANCE_STATE == code) {
                return "重启实例";
            } else if (MIGRATE_INSTANCE == code) {
                return "迁移实例";
            } else if (SOFT_DELETE_INSTANCE == code) {
                return "逻辑删除实例，保留数据盘，实例数据保留三天";
            } else if (SOFT_DELETE_INSTANCE_AND_VOLUME == code) {
                return "逻辑删除实例，不保留数据盘，数据保留三天";
            } else if (HARD_DELETE_INSTANCE == code) {
                return "物理删除实例，清空系统盘";
            } else if (HARD_DELETE_VOLUME == code) {
                return "物理删除数据盘，清空数据盘";
            } else if (SOFT_DELETE_VOLUME == code) {
                return "软删除数据盘，数据保留三天";
            } else if (ENTER_MAINTENANCE_MODE == code) {
                return "进入维护模式";
            } else if (EXIT_MAINTENANCE_MODE == code) {
                return "退出维护模式";
            } else if (ADD_SPECIFICATION == code) {
                return "添加资源套餐";
            } else if (UPDATE_SPECIFICATION == code) {
                return "更新资源套餐";
            } else if (CANCEL_DELETE_INSTANCE == code) {
                return "恢复逻辑删除的实例";
            }
            return "未知";
        }
    }

    public static final String DEFAULT_PHONE_PREFIX = "86";

    public static class MessageType {
        public final static Short ANNOUNCE = 1;
        public final static Short PRIVATE = 2;
    }

    /**
     * tb_msg_notice_log：notice_type
     * tb_msg_announce：announce_type
     */
    public static class NoticeType {
        /**
         * 过期实例提醒
         */
        public final static Short EXPIRE_INSTANCE = 1;
        /**
         * 节点资源紧张
         */
        public final static Short SERVER_RESOURCE = 2;

        /**
         * 节点通知
         */
        public final static Short NODE_NOTICE = 3;
    }

    public static class NoticeStatus {
        public final static Short UNKNOWN = 0;

        public final static Short SUCCESS = 1;

        public final static Short FAIL = 2;
    }

    /**
     * 0001 微信 1
     * 0010 邮箱 2
     * 0100 短信 4
     * 0011 微信+邮箱 3
     * 0101 微信+短信 5
     * 0110 邮箱+短信 6
     * 0111 微信+短信+电话 7
     */
    public static class NoticeChannel {
        /**
         * 微信
         */
        public final static int WX = 1;
        /**
         * 邮箱
         */
        public final static int EMAIL = 2;

        /**
         * 短信
         */
        public final static Short SMS = 4;
    }

    public static final class SyncLogType {
        public static final String USER_SYNC = "userSync";

        public static final String TICKET_SYNC = "ticketSync";
    }

    public static class UserSyncType {
        public final static short ADD = 1;
        public final static short UPDATE = 2;
    }

    /**
     * 角色类型
     */
    public static class RoleType {
        /**
         * 游客
         */
        public final static int VISITOR = 1;

        /**
         * 售前客服
         */
        public final static int BUSINESS_ADMIN = 2;

        /**
         * 技术客服
         */
        public final static Short TECHNOLOGY_ADMIN = 4;

        /**
         * 审计管理员
         */
        public final static Short AUDIT_ADMIN = 8;

        /**
         * 超级管理员
         */
        public final static Short SUPER_ADMIN = 16;
    }

    /**
     * 命令类型
     */
    public static class LxdCommandType {
        public final static int SIMPLE = 0;
        public final static int COMPLEX = 1;
    }
}
