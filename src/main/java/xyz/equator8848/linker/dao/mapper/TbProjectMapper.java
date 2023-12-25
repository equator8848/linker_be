package xyz.equator8848.linker.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.equator8848.linker.model.po.TbProject;
import xyz.equator8848.linker.model.vo.dashboard.CountGroupResult;
import org.apache.ibatis.annotations.Mapper;
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
public interface TbProjectMapper extends BaseMapper<TbProject> {
    @Select("SELECT del_flag AS status, COUNT(id) AS countNum FROM tb_project GROUP BY del_flag")
    List<CountGroupResult> countProjectDelStatus();
}