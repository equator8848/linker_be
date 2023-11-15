package com.equator.linker.dao.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.equator.linker.dao.mapper.TbProjectMapper;
import com.equator.linker.model.po.TbProject;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Equator
 * @Date: 2022/9/18 11:47
 **/
@Component
public class ProjectDaoService extends ServiceImpl<TbProjectMapper, TbProject> implements IService<TbProject> {
    public Set<Long> getProjectIdsByAccessLevel(Integer accessLevel, Set<Long> ignoreProjectIds) {
        return list(Wrappers.<TbProject>lambdaQuery().eq(TbProject::getAccessLevel, accessLevel)
                .notIn(!CollectionUtils.isEmpty(ignoreProjectIds), TbProject::getId, ignoreProjectIds)).stream().map(TbProject::getId)
                .collect(Collectors.toSet());
    }
}
