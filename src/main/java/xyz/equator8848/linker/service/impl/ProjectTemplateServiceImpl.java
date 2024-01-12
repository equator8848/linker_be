package xyz.equator8848.linker.service.impl;

import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.equator8848.inf.cache.common.LogSimpleCacheLoader;
import xyz.equator8848.inf.cache.common.SimpleCacheElement;
import xyz.equator8848.inf.cache.guava.SimpleCacheBuilder;
import xyz.equator8848.inf.core.thread.ThreadPoolService;
import xyz.equator8848.linker.model.vo.project.ProjectTemplateDetailsInfo;
import xyz.equator8848.linker.service.ProjectTemplateService;
import xyz.equator8848.linker.service.template.TemplateBuilderService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ProjectTemplateServiceImpl implements ProjectTemplateService {
    @Autowired
    private List<TemplateBuilderService> templateBuilderServices;

    private final LoadingCache<String, SimpleCacheElement<List<ProjectTemplateDetailsInfo>>> templateCache =
            SimpleCacheBuilder.newBuilder().refreshAfterWrite(1, TimeUnit.DAYS)
                    .expireAfterWrite(1, TimeUnit.DAYS)
                    .maximumSize(2).build(new LogSimpleCacheLoader<>() {

                        @Override
                        public List<ProjectTemplateDetailsInfo> loadData(String key) throws Exception {
                            return loadTemplateInfo();
                        }

                        @Override
                        public String getCacheName() {
                            return "templateCache";
                        }
                    }, ThreadPoolService.getInstance());

    private List<ProjectTemplateDetailsInfo> loadTemplateInfo() {
        return templateBuilderServices.stream().map(templateBuilderService -> {
            ProjectTemplateDetailsInfo projectTemplateDetailsInfo = new ProjectTemplateDetailsInfo();
            projectTemplateDetailsInfo.setId(templateBuilderService.getId());
            projectTemplateDetailsInfo.setTemplateVersionId(templateBuilderService.templateId());
            projectTemplateDetailsInfo.setIntro(templateBuilderService.getIntro());
            return projectTemplateDetailsInfo;
        }).sorted(Comparator.comparing(ProjectTemplateDetailsInfo::getId)).collect(Collectors.toList());
    }

    @Override
    public List<ProjectTemplateDetailsInfo> allFromCode() {
        return templateCache.getUnchecked("").getData();
    }

    @Override
    public String getIntroFromCache(String templateId) {
        Optional<ProjectTemplateDetailsInfo> templateInfoOptional = allFromCode().stream()
                .filter(templateDetailsInfo -> templateId.equals(templateDetailsInfo.getTemplateVersionId())).findFirst();
        if (templateInfoOptional.isPresent()) {
            return templateInfoOptional.get().getIntro();
        }
        return "神秘模板";
    }


}
