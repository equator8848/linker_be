package xyz.equator8848.linker.service.impl;

import xyz.equator8848.linker.dao.service.LoginLogDaoService;
import xyz.equator8848.linker.model.po.TbInfLoginLog;
import xyz.equator8848.linker.model.vo.login.LoginLogVO;
import xyz.equator8848.linker.service.LoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LoginLogServiceImpl implements LoginLogService {
    @Autowired
    private LoginLogDaoService loginLogDaoService;

    @Override
    public List<LoginLogVO> getLoginLogList(Integer days) {
        List<TbInfLoginLog> loginLogList = loginLogDaoService.getLoginLogList(days);
        return loginLogList.stream().map(loginLog -> {
            LoginLogVO loginLogVO = new LoginLogVO();
            BeanUtils.copyProperties(loginLog, loginLogVO);
            loginLogVO.setUserKey(loginLog.getLoginUserKey());
            return loginLogVO;
        }).collect(Collectors.toList());
    }
}
