package xyz.equator8848.linker.dao.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.equator8848.inf.cache.common.LogSimpleCacheLoader;
import xyz.equator8848.inf.cache.common.SimpleCacheElement;
import xyz.equator8848.inf.cache.guava.SimpleCacheBuilder;
import xyz.equator8848.linker.util.ThreadPoolService;
import xyz.equator8848.linker.dao.mapper.TbProjectMapper;
import xyz.equator8848.linker.model.po.TbProject;
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
public class ProjectDaoService extends ServiceImpl<TbProjectMapper, TbProject> implements IService<TbProject> {
    private final LoadingCache<Long, SimpleCacheElement<TbProject>> projectCache =
            SimpleCacheBuilder.newBuilder().refreshAfterWrite(1, TimeUnit.MINUTES).expireAfterWrite(6,
                    TimeUnit.HOURS).maximumSize(512).build(new LogSimpleCacheLoader<>() {

                @Override
                public TbProject loadData(Long key) throws Exception {
                    return getById(key);
                }

                @Override
                public String getCacheName() {
                    return "projectCache";
                }
            }, ThreadPoolService.getInstance());

    public TbProject getProjectByIdFromCache(Long projectId){
        return projectCache.getUnchecked(projectId).getData();
    }

    public Set<Long> getProjectIdsByAccessLevel(Integer accessLevel, Set<Long> ignoreProjectIds) {
        return list(Wrappers.<TbProject>lambdaQuery().eq(TbProject::getAccessLevel, accessLevel)
                .notIn(!CollectionUtils.isEmpty(ignoreProjectIds), TbProject::getId, ignoreProjectIds)).stream().map(TbProject::getId)
                .collect(Collectors.toSet());
    }
}
