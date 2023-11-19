package com.equator.linker.service;

import com.equator.linker.model.vo.PageData;
import com.equator.linker.model.vo.system.AppSettingVO;
import org.springframework.stereotype.Service;

@Service
public interface AppSettingService {
    PageData<AppSettingVO> list(Integer pageNum, Integer pageSize);

    void add(String settingKey, String settingValue, String remark);

    void update(Long id, String settingValue);

    void delete(Long id);
}
