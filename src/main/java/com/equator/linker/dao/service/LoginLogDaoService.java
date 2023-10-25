package com.equator.linker.dao.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.equator.linker.dao.mapper.TbLoginLogMapper;
import com.equator.linker.model.constant.ModelStatus;
import com.equator.linker.model.po.TbLoginLog;
import com.equator.linker.service.user.LoginStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @Author: Equator
 * @Date: 2022/9/18 11:47
 **/
@Component
public class LoginLogDaoService extends ServiceImpl<TbLoginLogMapper, TbLoginLog> implements IService<TbLoginLog> {
    @Autowired
    private TbLoginLogMapper loginLogMapper;

    public void appendLoginLog(String userKey, LoginStatus loginStatus, String remoteAddress) {
        TbLoginLog tbLoginLog = new TbLoginLog();
        tbLoginLog.setStatus(ModelStatus.Base.NORMAL);
        tbLoginLog.setUserKey(userKey);
        tbLoginLog.setLoginStatus(loginStatus.ordinal());
        tbLoginLog.setRemoteAddress(remoteAddress);
        loginLogMapper.insert(tbLoginLog);
    }

    /**
     * 获取最近几天的登录记录
     *
     * @param days
     * @return
     */
    public List<TbLoginLog> getLoginLogList(Integer days) {
        return loginLogMapper.selectList(Wrappers.<TbLoginLog>lambdaQuery().ge(TbLoginLog::getCreateTime, Instant.now().minus(days, ChronoUnit.DAYS)));
    }
}
