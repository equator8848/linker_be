package com.equator.linker.dao.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.equator.linker.dao.mapper.TbInstanceMapper;
import com.equator.linker.model.po.TbInstance;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Equator
 * @Date: 2022/9/18 11:47
 **/
@Component
public class InstanceDaoService extends ServiceImpl<TbInstanceMapper, TbInstance> implements IService<TbInstance> {

    public Set<Long> getInstanceIdsByAccessLevel(Integer accessLevel, Set<Long> ignoreInstanceIds) {
        return list(Wrappers.<TbInstance>lambdaQuery().eq(TbInstance::getAccessLevel, accessLevel)
                .notIn(!CollectionUtils.isEmpty(ignoreInstanceIds), TbInstance::getId, ignoreInstanceIds)).stream().map(TbInstance::getId)
                .collect(Collectors.toSet());
    }
}
