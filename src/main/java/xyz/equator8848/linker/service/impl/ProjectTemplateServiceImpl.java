package xyz.equator8848.linker.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.equator8848.linker.model.vo.project.ProjectTemplateDetailsInfo;
import xyz.equator8848.linker.service.ProjectTemplateService;
import xyz.equator8848.linker.service.template.TemplateBuilderService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectTemplateServiceImpl implements ProjectTemplateService {
    @Autowired
    private List<TemplateBuilderService> templateBuilderServices;

    @Override
    public List<ProjectTemplateDetailsInfo> allFromCode() {
        return templateBuilderServices.stream().map(templateBuilderService -> {
            ProjectTemplateDetailsInfo projectTemplateDetailsInfo = new ProjectTemplateDetailsInfo();
            projectTemplateDetailsInfo.setId(templateBuilderService.getId());
            projectTemplateDetailsInfo.setTemplateVersionId(templateBuilderService.templateId());
            projectTemplateDetailsInfo.setIntro(templateBuilderService.getIntro());
            return projectTemplateDetailsInfo;
        }).collect(Collectors.toList());
    }

    @Override
    public String getIntroFromCache(String templateId) {
        Optional<TemplateBuilderService> templateBuilderServiceOptional = templateBuilderServices.stream()
                .filter(templateBuilderService -> templateId.equals(templateBuilderService.templateId())).findFirst();
        if (templateBuilderServiceOptional.isPresent()) {
            return templateBuilderServiceOptional.get().getIntro();
        }
        return "神秘模板";
    }


}
