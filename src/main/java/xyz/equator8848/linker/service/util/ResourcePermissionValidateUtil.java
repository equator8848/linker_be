package xyz.equator8848.linker.service.util;

import xyz.equator8848.inf.auth.model.bo.LoginUser;
import xyz.equator8848.inf.auth.model.constant.RoleType;
import xyz.equator8848.inf.auth.util.UserAuthUtil;
import xyz.equator8848.inf.auth.util.UserContextUtil;
import xyz.equator8848.inf.core.util.spring.BeanManagerUtil;
import xyz.equator8848.linker.configuration.AppConfig;

public class ResourcePermissionValidateUtil {
    /**
     * 检查当前用户是否对某个资源有权限
     *
     * @param resourceUid
     */
    public static void permissionCheck(Long resourceUid) {
        LoginUser currentLoginUser = UserContextUtil.getUser();
        Short userRoleType = currentLoginUser.getRoleType();
        if (userRoleType == null) {
            UserAuthUtil.checkPermission(resourceUid);
            return;
        }
        if (userRoleType >= RoleType.SYSTEM_ADMIN) {
            AppConfig appConfig = BeanManagerUtil.getBean(AppConfig.class);
            if (appConfig.getConfig().getSystemAdminManageAllData()) {
                return;
            }
        }
        UserAuthUtil.checkPermission(resourceUid);
    }

    /**
     * 判断是否是某个资源的管理者
     *
     * @param resourceUid
     * @return
     */
    public static boolean isAdmin(Long resourceUid) {
        LoginUser currentLoginUser = UserContextUtil.getUser();
        Short userRoleType = currentLoginUser.getRoleType();
        if (userRoleType == null) {
            return currentLoginUser.getUid().equals(resourceUid);
        }
        if (userRoleType >= RoleType.SYSTEM_ADMIN) {
            AppConfig appConfig = BeanManagerUtil.getBean(AppConfig.class);
            if (appConfig.getConfig().getSystemAdminManageAllData()) {
                return true;
            }
        }
        return currentLoginUser.getUid().equals(resourceUid);
    }
}
