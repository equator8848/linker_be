package xyz.equator8848.linker.service.template;

import xyz.equator8848.inf.core.model.exception.PreCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TemplateBuilderServiceHolder {
    private Map<String, TemplateBuilderService> templateBuilderServiceMap;

    @Autowired
    public void initTemplateBuilder(List<TemplateBuilderService> templateBuilderServices) {
        templateBuilderServiceMap = templateBuilderServices.stream().collect(Collectors.toMap(TemplateBuilderService::templateId, Function.identity()));
    }

    public TemplateBuilderService getTemplateBuilderServiceById(String templateId) {
        TemplateBuilderService templateBuilderService = templateBuilderServiceMap.get(templateId);
        PreCondition.isNotNull(templateBuilderService, "找不到模板%s对应的处理器".formatted(templateId));
        return templateBuilderService;
    }
}
