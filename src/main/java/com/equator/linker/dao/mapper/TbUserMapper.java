package com.equator.linker.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.equator.linker.model.po.TbUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
public interface TbUserMapper extends BaseMapper<TbUser> {
    @Select("SELECT MAX(update_time) FROM tb_user WHERE id = #{id}")
    Date selectMaxUpdateTime(@Param("id") Integer id);
}