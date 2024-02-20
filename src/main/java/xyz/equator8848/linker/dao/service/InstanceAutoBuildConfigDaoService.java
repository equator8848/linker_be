package xyz.equator8848.linker.dao.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;
import xyz.equator8848.linker.dao.mapper.TbInstanceAutoBuildConfigMapper;
import xyz.equator8848.linker.model.po.TbInstanceAutoBuildConfig;

import java.util.List;

@Component
public class InstanceAutoBuildConfigDaoService extends ServiceImpl<TbInstanceAutoBuildConfigMapper, TbInstanceAutoBuildConfig>
        implements IService<TbInstanceAutoBuildConfig> {

    public TbInstanceAutoBuildConfig getTbInstanceAutoBuildConfigByInstanceId(Long instanceId) {
        return getOne(Wrappers.<TbInstanceAutoBuildConfig>lambdaQuery().eq(TbInstanceAutoBuildConfig::getInstanceId, instanceId));
    }

    public boolean deleteByInstanceId(Long instanceId) {
        return remove(Wrappers.<TbInstanceAutoBuildConfig>lambdaQuery().eq(TbInstanceAutoBuildConfig::getInstanceId, instanceId));
    }

    public List<TbInstanceAutoBuildConfig> getActivateAutoBuildInstanceList(Long timestamp) {
        return list(Wrappers.<TbInstanceAutoBuildConfig>lambdaQuery()
                .eq(TbInstanceAutoBuildConfig::getEnabledSwitch, true)
                .lt(TbInstanceAutoBuildConfig::getNextCheckTimestamp, timestamp));
    }
}
