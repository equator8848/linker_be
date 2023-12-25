package xyz.equator8848.linker.service;

import xyz.equator8848.linker.model.vo.login.LoginLogVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LoginLogService {

    List<LoginLogVO> getLoginLogList(Integer days);
}
