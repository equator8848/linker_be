package xyz.equator8848.linker.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import xyz.equator8848.linker.dao.service.AppSettingDaoService;
import xyz.equator8848.linker.model.po.TbInfAppSetting;
import xyz.equator8848.linker.model.vo.PageData;
import xyz.equator8848.linker.model.vo.system.AppSettingVO;
import xyz.equator8848.linker.service.AppSettingService;
import xyz.equator8848.inf.core.model.exception.PreCondition;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AppSettingServiceImpl implements AppSettingService {
    @Autowired
    private AppSettingDaoService appSettingDaoService;

    @Override
    public PageData<AppSettingVO> list(Integer pageNum, Integer pageSize) {
        Page<TbInfAppSetting> page = appSettingDaoService.page(new Page<>(pageNum, pageSize), Wrappers.lambdaQuery());
        return PageData.wrap(page, page.getRecords().stream().map(po -> {
            AppSettingVO vo = new AppSettingVO();
            BeanUtils.copyProperties(po, vo);
            return vo;
        }).collect(Collectors.toList()));
    }

    @Override
    public void add(String settingKey, String settingValue, String remark) {
        TbInfAppSetting existedSetting = appSettingDaoService.getOne(Wrappers.<TbInfAppSetting>lambdaQuery().eq(TbInfAppSetting::getSettingKey, settingKey));
        PreCondition.isTrue(existedSetting == null, "配置已存在");
        TbInfAppSetting tbAppSetting = new TbInfAppSetting();
        tbAppSetting.setSettingKey(settingKey);
        tbAppSetting.setSettingValue(settingValue);
        tbAppSetting.setRemark(remark);
        appSettingDaoService.save(tbAppSetting);
    }

    @Override
    public void update(Long id, String settingValue) {
        TbInfAppSetting tbAppSetting = appSettingDaoService.getById(id);
        tbAppSetting.setSettingValue(settingValue);
        appSettingDaoService.updateById(tbAppSetting);
    }

    @Override
    public void delete(Long id) {
        appSettingDaoService.removeById(id);
    }
}
