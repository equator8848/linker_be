package com.equator.linker.dao.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.equator.cache.guava.*;
import com.equator.core.model.exception.PreCondition;
import com.equator.linker.common.ThreadPoolService;
import com.equator.linker.dao.mapper.TbUserMapper;
import com.equator.linker.model.constant.BaseConstant;
import com.equator.linker.model.constant.ModelStatus;
import com.equator.linker.model.po.TbUser;
import com.equator.linker.model.vo.user.UserLoginDataVO;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.equator.linker.model.constant.ModelStatus.UNKNOWN_USER_NAME;


/**
 * @Author: Equator
 * @Date: 2022/9/18 11:47
 **/
@Slf4j
@Component
public class UserDaoService extends ServiceImpl<TbUserMapper, TbUser> implements IService<TbUser> {
    @Autowired
    private TbUserMapper tbUserMapper;

    private final LoadingCache<Integer, VersionCacheElement<Date, TbUser>> userIdInfoCache =
            VersionCacheBuilder.newBuilder().refreshAfterWrite(1, TimeUnit.MINUTES).expireAfterWrite(6,
                    TimeUnit.HOURS).maximumSize(512).build(new LogVersionCacheLoader<Integer, Date,
                                        TbUser>() {
                @Override
                public Date loadVersion(Integer key, TbUser data) {
                    return tbUserMapper.selectMaxUpdateTime(key);
                }

                @Override
                public TbUser loadData(Integer key) throws Exception {
                    return tbUserMapper.selectById(key);
                }

                @Override
                protected String getCacheName() {
                    return "userIdInfoCache";
                }
            }, ThreadPoolService.getInstance());

    private final LoadingCache<Integer, SimpleCacheElement<TbUser>> userRelationIdInfoCache =
            SimpleCacheBuilder.newBuilder().refreshAfterWrite(1, TimeUnit.MINUTES).expireAfterWrite(6,
                    TimeUnit.HOURS).maximumSize(1024).build(new LogSimpleCacheLoader<Integer, TbUser>() {


                @Override
                public TbUser loadData(Integer relationId) throws Exception {
                    if (relationId == null) {
                        TbUser tbUser = new TbUser();
                        tbUser.setUserName(UNKNOWN_USER_NAME);
                        return tbUser;
                    }
                    return tbUserMapper.selectOne(Wrappers.<TbUser>lambdaQuery().eq(TbUser::getRelationId, relationId));
                }

                @Override
                protected String getCacheName() {
                    return "userRelationIdInfoCache";
                }
            }, ThreadPoolService.getInstance());

    public TbUser getUserFromCacheByUid(Integer uid) {
        return userIdInfoCache.getUnchecked(uid).getData();
    }

    public TbUser getUserFromCacheByUserRelationId(Integer userRelationId) {
        return userRelationIdInfoCache.getUnchecked(userRelationId).getData();
    }

    public TbUser getUserByIdentification(UserLoginDataVO userLoginVO, Short userSystemType) {
        String userIdentification = userLoginVO.getUserIdentification();
        if (BaseConstant.UserIdentificationType.PHONE.equals(userLoginVO.getUserIdentificationType())) {
            // 手机登录
            String[] phonePrefixAndNumber = userIdentification.split("#");
            PreCondition.isTrue(phonePrefixAndNumber.length == 2, "手机号码格式为：区号#手机号");
            return getOneUser(tbUserMapper.selectList(Wrappers.<TbUser>lambdaQuery()
                    .eq(TbUser::getPhoneNumberPrefix, phonePrefixAndNumber[0])
                    .eq(TbUser::getPhoneNumber, phonePrefixAndNumber[1])
                    .eq(TbUser::getUserType, userSystemType)));
        } else if (BaseConstant.UserIdentificationType.EMAIL.equals(userLoginVO.getUserIdentificationType())) {
            return getOneUser(tbUserMapper.selectList(Wrappers.<TbUser>lambdaQuery()
                    .eq(TbUser::getEmail, userIdentification).eq(TbUser::getUserType, userSystemType)));
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
                .eq(!StringUtils.isEmpty(phoneNumber), TbUser::getPhoneNumberPrefix, ModelStatus.DEFAULT_PHONE_PREFIX)
                .eq(!StringUtils.isEmpty(phoneNumber), TbUser::getPhoneNumber, phoneNumber)
                .or(!StringUtils.isEmpty(email)).eq(TbUser::getEmail, email));
    }
}
