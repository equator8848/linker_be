package xyz.equator8848.linker.model.vo.project;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author equator
 * @since 2023-11-05
 */
@Data
public class PackageImageDetails {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String createUserName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private String updateUserName;
    /**
     * 镜像名称
     */
    @NotNull
    private String name;

    /**
     * 镜像介绍
     */
    @NotNull
    private String intro;

    /**
     * 镜像路径
     */
    @NotNull
    private String imagePath;
}
