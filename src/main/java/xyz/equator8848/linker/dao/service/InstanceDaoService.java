package xyz.equator8848.linker.dao.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import xyz.equator8848.inf.cache.common.LogSimpleCacheLoader;
import xyz.equator8848.inf.cache.common.SimpleCacheElement;
import xyz.equator8848.inf.cache.guava.SimpleCacheBuilder;
import xyz.equator8848.inf.core.thread.ThreadPoolService;
import xyz.equator8848.linker.dao.mapper.TbInstanceMapper;
import xyz.equator8848.linker.dao.mapper.TbProjectMapper;
import xyz.equator8848.linker.model.po.TbInstance;
import xyz.equator8848.linker.model.vo.dashboard.BuildStatisticalResult;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: Equator
 * @Date: 2022/9/18 11:47
 **/
@Component
public class InstanceDaoService extends ServiceImpl<TbInstanceMapper, TbInstance> implements IService<TbInstance> {

    @Autowired
    private TbProjectMapper projectMapper;

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

    public TbInstance getInstanceByIdFromCache(Long instanceId) {
        return instanceCache.getUnchecked(instanceId).getData();
    }

    public Set<Long> getInstanceIdsByAccessLevel(Integer accessLevel, Set<Long> ignoreInstanceIds) {
        return list(Wrappers.<TbInstance>lambdaQuery().eq(TbInstance::getAccessLevel, accessLevel)
                .notIn(!CollectionUtils.isEmpty(ignoreInstanceIds), TbInstance::getId, ignoreInstanceIds)).stream().map(TbInstance::getId)
                .collect(Collectors.toSet());
    }

    public Set<Long> getInstanceIdsByGteAccessLevel(Integer accessLevel, Set<Long> ignoreInstanceIds) {
        return list(Wrappers.<TbInstance>lambdaQuery().ge(TbInstance::getAccessLevel, accessLevel)
                .notIn(!CollectionUtils.isEmpty(ignoreInstanceIds), TbInstance::getId, ignoreInstanceIds)).stream().map(TbInstance::getId)
                .collect(Collectors.toSet());
    }

    private final LoadingCache<Long, SimpleCacheElement<BuildStatisticalResult>> buildStaticalResultCache =
            SimpleCacheBuilder.newBuilder().refreshAfterWrite(10, TimeUnit.MINUTES).expireAfterWrite(6,
                    TimeUnit.HOURS).maximumSize(2).build(new LogSimpleCacheLoader<>() {

                @Override
                public BuildStatisticalResult loadData(Long projectId) throws Exception {
                    return buildBuildStatisticalResult(projectId);
                }

                @Override
                public String getCacheName() {
                    return "buildStaticalResultCache";
                }
            }, ThreadPoolService.getInstance());

    private BuildStatisticalResult buildBuildStatisticalResult(Long projectId) {
        BuildStatisticalResult buildStatisticalResult = new BuildStatisticalResult();
        buildStatisticalResult.setInstanceBuildTimes(Optional.ofNullable(baseMapper.getGlobalBuildCount()).orElse(0L));
        buildStatisticalResult.setProjectCount(Optional.ofNullable(projectMapper.selectCount(Wrappers.lambdaQuery())).orElse(0L));
        if (Long.MIN_VALUE != projectId) {
            buildStatisticalResult.setProjectInstanceBuildTimes(Optional.ofNullable(baseMapper.getProjectBuildCount(projectId)).orElse(0L));
        }
        return buildStatisticalResult;
    }

    public BuildStatisticalResult getBuildStatisticalResult(@Nullable Long projectId) {
        return buildStaticalResultCache.getUnchecked(Optional.ofNullable(projectId).orElse(Long.MIN_VALUE)).getData();
    }
}
