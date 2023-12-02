package com.equator.linker.dao.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.equator.linker.dao.mapper.TbPublicEntranceMapper;
import com.equator.linker.model.po.TbPublicEntrance;
import org.springframework.stereotype.Component;

/**
 * @Author: Equator
 * @Date: 2022/9/18 11:47
 **/
@Component
public class PublicEntranceDaoService extends ServiceImpl<TbPublicEntranceMapper, TbPublicEntrance> implements IService<TbPublicEntrance> {
    public TbPublicEntrance getTbPublicEntranceByInstanceId(Long instanceId) {
        return getOne(Wrappers.<TbPublicEntrance>lambdaQuery().eq(TbPublicEntrance::getInstanceId, instanceId));
    }

    public boolean deleteByInstanceId(Long instanceId) {
        return remove(Wrappers.<TbPublicEntrance>lambdaQuery().eq(TbPublicEntrance::getInstanceId, instanceId));
    }
}
