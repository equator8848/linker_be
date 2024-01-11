package xyz.equator8848.linker.service;

import xyz.equator8848.linker.model.vo.project.ProjectTemplateDetailsInfo;

import java.util.List;

public interface ProjectTemplateService {
    /**
     * 从代码中动态获取模板信息
     *
     * @return
     */
    List<ProjectTemplateDetailsInfo> allFromCode();

    /**
     * 通过模板ID获取模板介绍
     *
     * @param templateId
     * @return
     */
    String getIntroFromCache(String templateId);
}
