package com.equator.linker.dao.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.equator.inf.cache.common.LogVersionCacheLoader;
import com.equator.inf.cache.common.VersionCacheElement;
import com.equator.inf.cache.guava.VersionCacheBuilder;
import com.equator.inf.core.model.exception.PreCondition;
import com.equator.linker.common.ThreadPoolService;
import com.equator.linker.dao.mapper.TbUserMapper;
import com.equator.linker.model.constant.BaseConstant;
import com.equator.linker.model.constant.ModelStatus;
import com.equator.linker.model.po.TbUser;
import com.equator.linker.model.vo.user.UserLoginDataVO;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**
 * @Author: Equator
 * @Date: 2022/9/18 11:47
 **/
@Slf4j
@Component
public class UserDaoService extends ServiceImpl<TbUserMapper, TbUser> implements IService<TbUser> {
    @Autowired
    private TbUserMapper tbUserMapper;

    private final LoadingCache<Long, VersionCacheElement<Date, TbUser>> userIdInfoCache =
            VersionCacheBuilder.newBuilder().refreshAfterWrite(1, TimeUnit.MINUTES).expireAfterWrite(6,
                    TimeUnit.HOURS).maximumSize(512).build(new LogVersionCacheLoader<>() {
                @Override
                public Date loadVersion(Long key, TbUser data) {
                    return tbUserMapper.selectMaxUpdateTime(key);
                }

                @Override
                public TbUser loadData(Long key) throws Exception {
                    return getById(key);
                }

                @Override
                public String getCacheName() {
                    return "userIdInfoCache";
                }
            }, ThreadPoolService.getInstance());

    public TbUser getUserFromCacheByUid(Long uid) {
        return userIdInfoCache.getUnchecked(uid).getData();
    }

    public String getUsernameFromCache(Long uid) {
        TbUser tbUser = userIdInfoCache.getUnchecked(uid).getData();
        if (tbUser == null) {
            return "神秘用户";
        }
        return tbUser.getUserName();
    }

    public TbUser getUserByIdentification(UserLoginDataVO userLoginVO) {
        String userIdentification = userLoginVO.getUserIdentification();
        if (BaseConstant.UserIdentificationType.PHONE.equals(userLoginVO.getUserIdentificationType())) {
            // 手机登录
            String[] phonePrefixAndNumber = userIdentification.split("#");
            PreCondition.isTrue(phonePrefixAndNumber.length == 2, "手机号码格式为：区号#手机号");
            return getOneUser(tbUserMapper.selectList(Wrappers.<TbUser>lambdaQuery()
                    .eq(TbUser::getPhoneNumber, phonePrefixAndNumber[1])));
        } else if (BaseConstant.UserIdentificationType.EMAIL.equals(userLoginVO.getUserIdentificationType())) {
            return getOneUser(tbUserMapper.selectList(Wrappers.<TbUser>lambdaQuery()
                    .eq(TbUser::getEmail, userIdentification)));
        }
        return null;
    }

    private TbUser getOneUser(List<TbUser> tbUsers) {
        if (CollectionUtils.isEmpty(tbUsers)) {
            return null;
        }
        if (tbUsers.size() == 1) {
            return tbUsers.get(0);
        }
        // 找到第一个正常的用户后返回
        Optional<TbUser> firstNormalUser = tbUsers.stream().filter(tbUser -> ModelStatus.UserStatus.NORMAL.equals(tbUser.getStatus())).findFirst();
        return firstNormalUser.orElse(null);
    }

    public TbUser getUser(String phoneNumber, String email) {
        return tbUserMapper.selectOne(Wrappers.<TbUser>lambdaQuery()
                .eq(!StringUtils.isEmpty(phoneNumber), TbUser::getPhoneNumber, phoneNumber)
                .or(!StringUtils.isEmpty(email)).eq(TbUser::getEmail, email));
    }
}
