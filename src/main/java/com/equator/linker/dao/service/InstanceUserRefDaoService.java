package com.equator.linker.dao.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.equator.linker.dao.mapper.TbInstanceUserRefMapper;
import com.equator.linker.model.po.TbInstanceUserRef;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Equator
 * @Date: 2022/9/18 11:47
 **/
@Component
public class InstanceUserRefDaoService extends ServiceImpl<TbInstanceUserRefMapper, TbInstanceUserRef> implements IService<TbInstanceUserRef> {
    public Set<Long> getInstanceIdByUserId(Long userId) {
        return list(Wrappers.<TbInstanceUserRef>lambdaQuery().select(TbInstanceUserRef::getInstanceId)
                .eq(TbInstanceUserRef::getUserId, userId)).stream().map(TbInstanceUserRef::getInstanceId)
                .collect(Collectors.toSet());
    }

}
