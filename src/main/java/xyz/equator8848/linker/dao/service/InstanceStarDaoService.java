package xyz.equator8848.linker.dao.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.equator8848.linker.dao.mapper.TbInstanceStarMapper;
import xyz.equator8848.linker.model.po.TbInstanceStar;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Equator
 * @Date: 2022/9/18 11:47
 **/
@Component
public class InstanceStarDaoService extends ServiceImpl<TbInstanceStarMapper, TbInstanceStar> implements IService<TbInstanceStar> {

    public Set<Long> getStarInstanceIds(Long projectId, Long userId) {
        return list(Wrappers.<TbInstanceStar>lambdaQuery().eq(TbInstanceStar::getProjectId, projectId)
                .eq(TbInstanceStar::getStarUserId, userId))
                .stream().map(TbInstanceStar::getInstanceId).collect(Collectors.toSet());
    }
}
