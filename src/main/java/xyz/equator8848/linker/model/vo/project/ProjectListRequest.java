package xyz.equator8848.linker.model.vo.project;


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
public class ProjectListRequest {
    private String searchKeyword;

    private Integer pageNum = 1;

    private Integer pageSize = 16;
}
