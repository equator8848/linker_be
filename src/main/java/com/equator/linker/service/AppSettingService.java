package com.equator.linker.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.equator.core.model.exception.PreCondition;
import com.equator.linker.dao.service.AppSettingDaoService;
import com.equator.linker.model.po.TbAppSetting;
import com.equator.linker.model.vo.PageData;
import com.equator.linker.model.vo.system.AppSettingVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AppSettingService {
    @Autowired
    private AppSettingDaoService appSettingDaoService;

    public PageData<AppSettingVO> list(Integer pageNum, Integer pageSize) {
        Page<TbAppSetting> page = appSettingDaoService.page(new Page<>(pageNum, pageSize), Wrappers.lambdaQuery());
        return PageData.wrap(page, page.getRecords().stream().map(po -> {
            AppSettingVO vo = new AppSettingVO();
            BeanUtils.copyProperties(po, vo);
            return vo;
        }).collect(Collectors.toList()));
    }

    public void add(String settingKey, String settingValue, String remark) {
        TbAppSetting existedSetting = appSettingDaoService.getOne(Wrappers.<TbAppSetting>lambdaQuery().eq(TbAppSetting::getSettingKey, settingKey));
        PreCondition.isTrue(existedSetting == null, "配置已存在");
        TbAppSetting tbAppSetting = new TbAppSetting();
        tbAppSetting.setSettingKey(settingKey);
        tbAppSetting.setSettingValue(settingValue);
        tbAppSetting.setRemark(remark);
        appSettingDaoService.save(tbAppSetting);
    }

    public void update(Integer id, String settingValue) {
        TbAppSetting tbAppSetting = appSettingDaoService.getById(id);
        tbAppSetting.setSettingValue(settingValue);
        appSettingDaoService.updateById(tbAppSetting);
    }
}
