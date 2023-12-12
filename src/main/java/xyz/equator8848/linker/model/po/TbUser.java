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
@TableName("tb_user")
public class TbUser extends BaseEntityField {

    /**
     *
     */
    @TableField(value = "status")
    private Short status;

    /**
     *
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     *
     */
    @TableField(value = "user_password")
    private String userPassword;

    /**
     *
     */
    @TableField(value = "email")
    private String email;

    /**
     *
     */
    @TableField(value = "phone_number")
    private String phoneNumber;

    /**
     * 微信ID
     */
    @TableField(value = "wx_open_id")
    private String wxOpenId;

    /**
     * 邮箱通知开关
     */
    @TableField(value = "email_notice_switch")
    private Integer emailNoticeSwitch;

    /**
     * 微信通知开关
     */
    @TableField(value = "wx_notice_switch")
    private Integer wxNoticeSwitch;

    /**
     * 角色类型
     */
    @TableField(value = "role_type")
    private Short roleType;
}
