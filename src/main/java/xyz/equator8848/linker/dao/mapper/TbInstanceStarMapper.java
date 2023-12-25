package xyz.equator8848.linker.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.equator8848.linker.model.po.TbInstanceStar;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author equator
 * @since 2023-11-05
 */
@Mapper
public interface TbInstanceStarMapper extends BaseMapper<TbInstanceStar> {
    @Delete("DELETE FROM tb_instance_star WHERE project_id = #{projectId} AND star_user_id = #{starUserId} AND instance_id =#{instanceId}")
    Long unStarInstance(@Param("projectId") Long projectId, @Param("starUserId") Long starUserId, @Param("instanceId") Long instanceId);
}