package xyz.equator8848.linker.configuration;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import xyz.equator8848.linker.model.constant.ModelStatus;
import xyz.equator8848.linker.model.vo.LoginUser;
import xyz.equator8848.linker.util.UserContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Objects;

@Slf4j
@Configuration
public class MybatisMetaObjectHandler implements MetaObjectHandler {

    private static class FieldConstant {
        public static final String createTime = "createTime";
        public static final String createBy = "createUserId";
        public static final String updateTime = "updateTime";
        public static final String updateBy = "updateUserId";
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        Date now = new Date();
        Long userId = getUserId();
        if (metaObject.hasSetter(FieldConstant.createTime)) {
            metaObject.setValue(FieldConstant.createTime, now);
        }
        if (metaObject.hasSetter(FieldConstant.createBy) && metaObject.getValue(FieldConstant.createBy) == null) {
            metaObject.setValue(FieldConstant.createBy, userId);
        }
        if (metaObject.hasSetter(FieldConstant.updateTime)) {
            metaObject.setValue(FieldConstant.updateTime, now);
        }
        if (metaObject.hasSetter(FieldConstant.updateBy) && metaObject.getValue(FieldConstant.updateBy) == null) {
            metaObject.setValue(FieldConstant.updateBy, userId);
        }
    }


    @Override
    public void updateFill(MetaObject metaObject) {
        Date now = new Date();
        if (metaObject.hasSetter(FieldConstant.updateTime)) {
            metaObject.setValue(FieldConstant.updateTime, now);
        }
        if (metaObject.hasSetter(FieldConstant.updateBy) && metaObject.getValue(FieldConstant.updateBy) == null) {
            metaObject.setValue(FieldConstant.updateBy, getUserId());
        }
    }

    private Long getUserId() {
        LoginUser loginUser = UserContextUtil.getUser();
        Long userId = ModelStatus.DUMMY_ID;
        if (!ObjectUtils.isEmpty(loginUser)) {
            userId = Objects.isNull(loginUser.getUid()) ? ModelStatus.DUMMY_ID : loginUser.getUid();
        }
        return userId;
    }

}

