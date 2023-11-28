package com.equator.linker.model.vo.project;


import com.equator.linker.model.po.BaseEntityField;
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
