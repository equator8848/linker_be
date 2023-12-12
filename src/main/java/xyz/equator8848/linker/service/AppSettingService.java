package xyz.equator8848.linker.service;

import xyz.equator8848.linker.model.vo.PageData;
import xyz.equator8848.linker.model.vo.system.AppSettingVO;
import org.springframework.stereotype.Service;

@Service
public interface AppSettingService {
    PageData<AppSettingVO> list(Integer pageNum, Integer pageSize);

    void add(String settingKey, String settingValue, String remark);

    void update(Long id, String settingValue);

    void delete(Long id);
}
