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
@TableName("tb_inf_app_setting")
public class TbInfAppSetting extends BaseEntityField {

    /**
     *
     */
    @TableField(value = "setting_key")
    private String settingKey;

    /**
     *
     */
    @TableField(value = "setting_value")
    private String settingValue;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;
}
