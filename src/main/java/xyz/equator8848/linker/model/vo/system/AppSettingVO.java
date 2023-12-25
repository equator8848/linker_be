package xyz.equator8848.linker.model.vo.system;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 服务器配置
 * </p>
 *
 * @author equator
 * @since 2022-09-18
 */
@Data
public class AppSettingVO {


    /**
     *
     */
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     *
     */
    private String settingKey;

    /**
     *
     */
    private String settingValue;

    /**
     * 备注
     */
    private String remark;
}
