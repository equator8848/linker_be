package xyz.equator8848.linker.dao.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.equator8848.linker.dao.mapper.TbInfLoginLogMapper;
import xyz.equator8848.linker.model.po.TbInfLoginLog;
import xyz.equator8848.linker.service.user.LoginStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @Author: Equator
 * @Date: 2022/9/18 11:47
 **/
@Component
public class LoginLogDaoService extends ServiceImpl<TbInfLoginLogMapper, TbInfLoginLog> implements IService<TbInfLoginLog> {

    public void appendLoginLog(String userKey, LoginStatus loginStatus, String remoteAddress) {
        TbInfLoginLog tbLoginLog = new TbInfLoginLog();
        tbLoginLog.setLoginUserKey(userKey);
        tbLoginLog.setLoginStatus(loginStatus.ordinal());
        tbLoginLog.setRemoteAddress(remoteAddress);
        save(tbLoginLog);
    }

    /**
     * 获取最近几天的登录记录
     *
     * @param days
     * @return
     */
    public List<TbInfLoginLog> getLoginLogList(Integer days) {
        return list(Wrappers.<TbInfLoginLog>lambdaQuery().ge(TbInfLoginLog::getCreateTime, Instant.now().minus(days, ChronoUnit.DAYS)));
    }
}
