package xyz.equator8848.linker.model.po;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author equator
 * @since 2023-11-05
 */
@Data
@TableName("tb_package_image")
public class TbPackageImage extends BaseEntityField {

    /**
     * 镜像名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 镜像介绍
     */
    @TableField(value = "intro")
    private String intro;

    /**
     * 镜像路径
     */
    @TableField(value = "image_path")
    private String imagePath;
}
