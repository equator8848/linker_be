package xyz.equator8848.linker.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;
import xyz.equator8848.inf.auth.util.UserContextUtil;
import xyz.equator8848.linker.dao.mapper.TbInstanceBuildLogMapper;
import xyz.equator8848.linker.model.constant.ModelStatus;
import xyz.equator8848.linker.model.po.TbInstance;
import xyz.equator8848.linker.model.po.TbInstanceBuildLog;

/**
 * @Author: Equator
 * @Date: 2022/9/18 11:47
 **/
@Component
public class InstanceBuildLogDaoService extends ServiceImpl<TbInstanceBuildLogMapper, TbInstanceBuildLog> implements IService<TbInstanceBuildLog> {

    public void saveBuildLog(TbInstance tbInstance) {
        TbInstanceBuildLog tbInstanceBuildLog = new TbInstanceBuildLog();
        tbInstanceBuildLog.setProjectId(tbInstance.getProjectId());
        tbInstanceBuildLog.setInstanceId(tbInstance.getId());
        Long userId = UserContextUtil.getUserId();
        if (ModelStatus.DUMMY_ID.equals(userId)) {
            tbInstanceBuildLog.setBuildUserId(ModelStatus.DUMMY_ID);
            tbInstanceBuildLog.setRemark("系统自动触发");
        } else {
            tbInstanceBuildLog.setBuildUserId(userId);
            tbInstanceBuildLog.setRemark("用户手动触发");
        }
        save(tbInstanceBuildLog);
    }
}
