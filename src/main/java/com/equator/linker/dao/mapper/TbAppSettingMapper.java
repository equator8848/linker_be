package com.equator.linker.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.equator.linker.model.po.TbAppSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author equator
 * @since 2022-09-18
 */
@Mapper
@Component
public interface TbAppSettingMapper extends BaseMapper<TbAppSetting> {
    @Update("UPDATE tb_inf_app_setting SET setting_value=#{value}, update_time=NOW() WHERE setting_key=#{key}")
    int updateValueByKey(@Param("key") String key, @Param("value") String value);

    @Select("SELECT MAX(update_time) FROM tb_inf_app_setting")
    Date selectMaxUpdateTime();
}