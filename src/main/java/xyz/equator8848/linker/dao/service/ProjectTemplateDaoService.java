package xyz.equator8848.linker.dao.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.equator8848.inf.cache.common.LogVersionCacheLoader;
import xyz.equator8848.inf.cache.common.VersionCacheElement;
import xyz.equator8848.inf.cache.guava.VersionCacheBuilder;
import xyz.equator8848.inf.core.thread.ThreadPoolService;
import xyz.equator8848.linker.dao.mapper.TbProjectTemplateMapper;
import xyz.equator8848.linker.model.po.TbProjectTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Equator
 * @Date: 2022/9/18 11:47
 **/
@Component
public class ProjectTemplateDaoService extends ServiceImpl<TbProjectTemplateMapper, TbProjectTemplate> implements IService<TbProjectTemplate> {
    @Autowired
    private TbProjectTemplateMapper tbProjectTemplateMapper;
}
