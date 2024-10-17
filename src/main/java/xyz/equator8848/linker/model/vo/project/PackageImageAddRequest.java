package xyz.equator8848.linker.model.vo.project;


import jakarta.validation.constraints.NotNull;
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
public class PackageImageAddRequest {
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
