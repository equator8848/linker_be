package xyz.equator8848.linker.model.vo.project;


import xyz.equator8848.linker.model.po.BaseEntityField;
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
public class ProjectTemplateDetailsInfo extends BaseEntityField {
    private Long id;

    private String templateVersionId;

    private String intro;
}
