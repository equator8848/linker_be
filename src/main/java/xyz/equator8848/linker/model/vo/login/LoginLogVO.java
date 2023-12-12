package xyz.equator8848.linker.model.vo.login;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author equator
 * @since 2022-11-08
 */
@Data
public class LoginLogVO {


    private Integer id;

    /**
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 登录类型
     */
    private Short loginType;

    /**
     * 用户ID
     */
    private String userKey;

    /**
     * 登录状态
     */
    private Integer loginStatus;

    /**
     * IP地址
     */
    private String remoteAddress;
}
