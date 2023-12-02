package com.equator.linker.dao.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.equator.cache.common.LogSimpleCacheLoader;
import com.equator.cache.common.SimpleCacheElement;
import com.equator.cache.guava.SimpleCacheBuilder;
import com.equator.linker.common.ThreadPoolService;
import com.equator.linker.dao.mapper.TbInstanceMapper;
import com.equator.linker.model.po.TbInstance;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: Equator
 * @Date: 2022/9/18 11:47
 **/
@Component
public class InstanceDaoService extends ServiceImpl<TbInstanceMapper, TbInstance> implements IService<TbInstance> {
    private final LoadingCache<Long, SimpleCacheElement<TbInstance>> instanceCache =
            SimpleCacheBuilder.newBuilder().refreshAfterWrite(1, TimeUnit.MINUTES).expireAfterWrite(6,
                    TimeUnit.HOURS).maximumSize(512).build(new LogSimpleCacheLoader<>() {

                @Override
                public TbInstance loadData(Long key) throws Exception {
                    return getById(key);
                }

                @Override
                public String getCacheName() {
                    return "instanceCache";
                }
            }, ThreadPoolService.getInstance());

    public TbInstance getInstanceByIdFromCache(Long instanceId){
        return instanceCache.getUnchecked(instanceId).getData();
    }

    public Set<Long> getInstanceIdsByAccessLevel(Integer accessLevel, Set<Long> ignoreInstanceIds) {
        return list(Wrappers.<TbInstance>lambdaQuery().eq(TbInstance::getAccessLevel, accessLevel)
                .notIn(!CollectionUtils.isEmpty(ignoreInstanceIds), TbInstance::getId, ignoreInstanceIds)).stream().map(TbInstance::getId)
                .collect(Collectors.toSet());
    }
}
