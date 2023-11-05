package com.equator.linker.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.equator.linker.dao.mapper.TbInfAppSettingMapper;
import com.equator.linker.model.po.TbInfAppSetting;
import org.springframework.stereotype.Component;

/**
 * @Author: Equator
 * @Date: 2022/9/18 11:47
 **/
@Component
public class AppSettingDaoService extends ServiceImpl<TbInfAppSettingMapper, TbInfAppSetting> implements IService<TbInfAppSetting> {
}
