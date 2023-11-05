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
@TableName("tb_instance")
public class TbInstance extends BaseEntityField {

	/**
	 * 项目ID
	 */
	@TableField(value="project_id")
	private Long projectId;

	/**
	 * 实例名称
	 */
	private String name;

	/**
	 * 实例介绍
	 */
	private String intro;

	/**
	 * SCM分支
	 */
	@TableField(value="scm_branch")
	private String scmBranch;

	/**
	 * Nginx代理配置
	 */
	@TableField(value="proxy_config")
	private String proxyConfig;

	/**
	 * 完整访问链接
	 */
	@TableField(value="access_link")
	private String accessLink;

	/**
	 * 权限等级，1私有、2指定人可访问、4公开
	 */
	@TableField(value="access_level")
	private Integer accessLevel;

}
