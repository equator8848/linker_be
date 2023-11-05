package com.equator.linker.model.po;


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
@TableName("tb_inf_login_log")
public class TbInfLoginLog extends BaseEntityField {
	@TableField("login_user_key")
	private String loginUserKey;

	/**
	 * 登录状态
	 */
	@TableField(value="login_status")
	private Integer loginStatus;

	/**
	 * 登录IP地址
	 */
	@TableField(value="remote_address")
	private String remoteAddress;

}
