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
@TableName("tb_instance_user_ref")
public class TbInstanceUserRef extends BaseEntityField {

	/**
	 * 
	 */
	@TableField(value="instance_id")
	private Long instanceId;

	/**
	 * 
	 */
	@TableField(value="user_id")
	private Long userId;

	/**
	 * 关联类型，1 自己创建、2 加入
	 */
	@TableField(value="ref_type")
	private Integer refType;

}
