package xyz.equator8848.linker.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import xyz.equator8848.linker.model.po.TbPackageImage;

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
public interface TbPackageImageMapper extends BaseMapper<TbPackageImage> {
    @Select("SELECT MAX(update_time) FROM tb_package_image")
    Date selectMaxUpdateTime();
}