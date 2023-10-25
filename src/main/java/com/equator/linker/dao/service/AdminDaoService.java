package com.equator.linker.dao.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.equator.cache.guava.*;
import com.equator.core.util.json.JsonUtil;
import com.equator.core.util.security.PasswordUtil;
import com.equator.linker.common.ThreadPoolService;
import com.equator.linker.dao.mapper.TbAdminMapper;
import com.equator.linker.model.constant.ModelStatus;
import com.equator.linker.model.po.TbAdmin;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Equator
 * @Date: 2022/9/18 11:47
 **/
@Component
public class AdminDaoService extends ServiceImpl<TbAdminMapper, TbAdmin> implements IService<TbAdmin> {
    @Autowired
    private TbAdminMapper tbAdminMapper;

    private final LoadingCache<Integer, SimpleCacheElement<TbAdmin>> relationIdAdminCache =
            SimpleCacheBuilder.newBuilder().refreshAfterWrite(1, TimeUnit.DAYS).expireAfterWrite(7,
                    TimeUnit.DAYS).maximumSize(8).build(new LogSimpleCacheLoader<Integer, TbAdmin>() {
                @Override
                public TbAdmin loadData(Integer relationId) throws Exception {

                    TbAdmin unknownAdmin = new TbAdmin();
                    unknownAdmin.setUserName("unknown");
                    if (relationId == null) {
                        return unknownAdmin;
                    }

                    List<TbAdmin> tbAdmins = tbAdminMapper.selectList(Wrappers.<TbAdmin>lambdaQuery()
                            .like(TbAdmin::getRelationIds, relationId));
                    Optional<TbAdmin> targetAdmin = tbAdmins.stream().filter(admin -> {
                        if (StringUtils.isEmpty(admin.getRelationIds())) {
                            return false;
                        }
                        List<Integer> relationIds = JsonUtil.fromJsonList(admin.getRelationIds(), Integer.class);
                        return relationIds.contains(relationId);
                    }).findFirst();

                    return targetAdmin.orElse(unknownAdmin);
                }

                @Override
                protected String getCacheName() {
                    return "relationIdAdminCache";
                }
            }, ThreadPoolService.getInstance());

    public TbAdmin getByRelationId(Integer relationId) {
        return relationIdAdminCache.getUnchecked(relationId).getData();
    }

    public TbAdmin getUserByName(String username) {
        return tbAdminMapper.selectOne(Wrappers.<TbAdmin>lambdaQuery().eq(TbAdmin::getUserName, username));
    }

    public TbAdmin addAdmin(String nickName, String username, String password, String email) {
        TbAdmin tbAdmin = new TbAdmin();
        tbAdmin.setStatus(ModelStatus.Base.NORMAL);
        tbAdmin.setNickname(nickName);
        tbAdmin.setUserName(username);
        tbAdmin.setUserPassword(PasswordUtil.generateSha512CryptPassword(password));
        tbAdmin.setEmail(email);
        tbAdminMapper.insert(tbAdmin);
        return tbAdmin;
    }

    private final LoadingCache<Integer, VersionCacheElement<Date, TbAdmin>> tbAdminUserCache =
            VersionCacheBuilder.newBuilder().refreshAfterWrite(1, TimeUnit.MINUTES).expireAfterWrite(6,
                    TimeUnit.HOURS).maximumSize(16).build(new LogVersionCacheLoader<Integer, Date,
                    TbAdmin>() {
                @Override
                public Date loadVersion(Integer key, TbAdmin data) {
                    return tbAdminMapper.selectMaxUpdateTime(key);
                }

                @Override
                public TbAdmin loadData(Integer key) throws Exception {
                    return tbAdminMapper.selectById(key);
                }

                @Override
                protected String getCacheName() {
                    return "tbAdminUserCache";
                }
            }, ThreadPoolService.getInstance());

    public TbAdmin getTbAdminByIdFromCache(Integer userId) {
        return tbAdminUserCache.getUnchecked(userId).getData();
    }
}
