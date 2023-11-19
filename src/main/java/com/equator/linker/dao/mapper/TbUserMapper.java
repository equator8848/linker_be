package com.equator.linker.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.equator.linker.model.po.TbUser;
import com.equator.linker.model.vo.dashboard.CountGroupResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author equator
 * @since 2023-11-05
 */
@Mapper
public interface TbUserMapper extends BaseMapper<TbUser> {
    @Select("SELECT MAX(update_time) FROM tb_user WHERE id = #{id}")
    Date selectMaxUpdateTime(@Param("id") Long id);

    @Select("SELECT status, COUNT(id) AS countNum FROM tb_user GROUP BY status")
    List<CountGroupResult> countUserStatus();
}