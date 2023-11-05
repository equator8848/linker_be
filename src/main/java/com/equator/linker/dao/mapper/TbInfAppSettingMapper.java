package com.equator.linker.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.equator.linker.model.po.TbInfAppSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author equator
 * @since 2023-11-05
 */
@Mapper
public interface TbInfAppSettingMapper extends BaseMapper<TbInfAppSetting> {
    @Select("SELECT MAX(update_time) FROM tb_inf_app_setting")
    Date selectMaxUpdateTime();
}