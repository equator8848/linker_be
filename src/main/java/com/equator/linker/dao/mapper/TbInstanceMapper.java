package com.equator.linker.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.equator.linker.model.po.TbInstance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author equator
 * @since 2023-11-05
 */
@Mapper
public interface TbInstanceMapper extends BaseMapper<TbInstance> {
    @Select("SELECT IFNULL(MAX(access_port),#{minAccessPort})+1 FROM tb_instance WHERE del_flag=0 FOR UPDATE")
    Integer getNextAccessPort(@Param("minAccessPort") Integer minAccessPort);
}