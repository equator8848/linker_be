package com.equator.linker.service;

import com.equator.linker.model.vo.login.LoginLogVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LoginLogService {

    List<LoginLogVO> getLoginLogList(Integer days);
}
