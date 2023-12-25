package xyz.equator8848.linker.service;

import org.springframework.stereotype.Service;
import xyz.equator8848.inf.core.model.page.PageData;
import xyz.equator8848.linker.model.vo.system.AppSettingVO;

@Service
public interface AppSettingService {
    PageData<AppSettingVO> list(Integer pageNum, Integer pageSize);

    void add(String settingKey, String settingValue, String remark);

    void update(Long id, String settingValue);

    void delete(Long id);
}
