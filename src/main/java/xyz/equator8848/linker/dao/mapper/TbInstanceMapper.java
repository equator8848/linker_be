package xyz.equator8848.linker.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.equator8848.linker.model.po.TbInstance;
import xyz.equator8848.linker.model.vo.dashboard.CountGroupResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
public interface TbInstanceMapper extends BaseMapper<TbInstance> {
    @Select("SELECT IFNULL(MAX(access_port),#{minAccessPort})+1 FROM tb_instance FOR UPDATE")
    Integer getNextAccessPort(@Param("minAccessPort") Integer minAccessPort);

    @Select("SELECT del_flag AS status, COUNT(id) AS countNum FROM tb_instance GROUP BY del_flag")
    List<CountGroupResult> countInstanceDelStatus();

    @Select("SELECT SUM(IFNULL(latest_build_number,0)) FROM tb_instance")
    Long getGlobalBuildCount();

    @Select("SELECT SUM(IFNULL(latest_build_number,0)) FROM tb_instance WHERE project_id = #{projectId}")
    Long getProjectBuildCount(@Param("projectId") Long projectId);
}