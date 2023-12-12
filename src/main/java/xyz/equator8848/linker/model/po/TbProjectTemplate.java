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
@TableName("tb_project_template")
public class TbProjectTemplate extends BaseEntityField {

	/**
	 * 
	 */
	@TableField(value="template_version_id")
	private String templateVersionId;

	/**
	 * 
	 */
	@TableField(value="intro")
	private String intro;

}
