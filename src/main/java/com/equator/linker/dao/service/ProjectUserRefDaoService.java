package com.equator.linker.dao.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.equator.linker.dao.mapper.TbProjectUserRefMapper;
import com.equator.linker.model.po.TbProjectUserRef;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Equator
 * @Date: 2022/9/18 11:47
 **/
@Component
public class ProjectUserRefDaoService extends ServiceImpl<TbProjectUserRefMapper, TbProjectUserRef> implements IService<TbProjectUserRef> {
    public Set<Long> getProjectIdByUserId(Long userId) {
        return list(Wrappers.<TbProjectUserRef>lambdaQuery().select(TbProjectUserRef::getProjectId)
                .eq(TbProjectUserRef::getUserId, userId)).stream().map(TbProjectUserRef::getProjectId)
                .collect(Collectors.toSet());
    }
}
