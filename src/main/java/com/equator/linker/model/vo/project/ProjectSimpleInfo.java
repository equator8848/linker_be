package com.equator.linker.model.vo.project;


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
public class ProjectSimpleInfo {
    private Long id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目介绍
     */
    private String intro;


    private String accessLevel;

}
