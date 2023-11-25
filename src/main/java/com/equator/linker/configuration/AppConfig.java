package com.equator.linker.configuration;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.equator.cache.guava.LogVersionCacheLoader;
import com.equator.cache.guava.VersionCacheBuilder;
import com.equator.cache.guava.VersionCacheElement;
import com.equator.core.dynamic.config.ModelTransformer;
import com.equator.linker.dao.mapper.TbInfAppSettingMapper;
import com.equator.linker.model.dto.DynamicAppConfiguration;
import com.equator.linker.model.po.TbInfAppSetting;
import com.google.common.cache.LoadingCache;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author libinkai
 * @date 2020/11/1 2:06 下午
 */
@Slf4j
@Order(1)
@Component
public class AppConfig {
    @Autowired
    private TbInfAppSettingMapper settingMapper;

    @PostConstruct
    public void init() {
        DynamicAppConfiguration config = this.getConfig();
        log.info("load config {}", config);
    }

    private final LoadingCache<Integer, VersionCacheElement<Date, DynamicAppConfiguration>> dynamicConfigCache =
            VersionCacheBuilder.newBuilder().refreshAfterWrite(10, TimeUnit.SECONDS).expireAfterWrite(6,
                    TimeUnit.HOURS).maximumSize(2).build(new LogVersionCacheLoader<>() {
                @Override
                public Date loadVersion(Integer key, DynamicAppConfiguration data) {
                    return settingMapper.selectMaxUpdateTime();
                }

                @Override
                public DynamicAppConfiguration loadData(Integer key) throws Exception {
                    List<TbInfAppSetting> appSettings = settingMapper.selectList(Wrappers.<TbInfAppSetting>lambdaQuery().isNotNull(TbInfAppSetting::getSettingKey));

                    Map<String, String> settingMap;
                    if (CollectionUtils.isEmpty(appSettings)) {
                        settingMap = new HashMap<>();
                    } else {
                        settingMap = appSettings.stream().collect(Collectors.toMap(TbInfAppSetting::getSettingKey, TbInfAppSetting::getSettingValue));
                    }

                    DynamicAppConfiguration configuration = new DynamicAppConfiguration();
                    ModelTransformer.stringMapToObject(settingMap, configuration);
                    // 设置版本
                    configuration.setVersion(Optional.ofNullable(settingMapper.selectMaxUpdateTime()).orElse(new Date()).getTime());
                    return configuration;
                }

                @Override
                protected String getCacheName() {
                    return "dynamicConfigCache";
                }
            });

    public DynamicAppConfiguration getConfig() {
        return dynamicConfigCache.getUnchecked(0).getData();
    }
}
