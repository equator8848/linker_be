package xyz.equator8848.linker.service.template.impl;

import org.springframework.stereotype.Service;

@Service
public class TemplateBuilderService20231128 extends AbstractTemplateBuilderService {

    @Override
    public String templateId() {
        return "20231128";
    }

    @Override
    public String getIntro() {
        return "基础联调流水线+生产环境打包模板";
    }
}
