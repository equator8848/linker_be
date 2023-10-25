package com.equator.linker.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.equator.linker.model.po.TbLoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author equator
 * @since 2022-11-08
 */
@Mapper
@Component
public interface TbLoginLogMapper extends BaseMapper<TbLoginLog> {

}