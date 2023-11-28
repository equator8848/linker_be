package com.equator.linker.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.equator.linker.dao.service.ProjectTemplateDaoService;
import com.equator.linker.model.vo.project.ProjectTemplateDetailsInfo;
import com.equator.linker.service.ProjectTemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectTemplateServiceImpl implements ProjectTemplateService {
    @Autowired
    private ProjectTemplateDaoService projectTemplateDaoService;

    @Override
    public List<ProjectTemplateDetailsInfo> all() {
        return projectTemplateDaoService.list(Wrappers.lambdaQuery())
                .stream().map(tbProjectTemplate -> {
                    ProjectTemplateDetailsInfo projectTemplateDetailsInfo = new ProjectTemplateDetailsInfo();
                    BeanUtils.copyProperties(tbProjectTemplate, projectTemplateDetailsInfo);
                    return projectTemplateDetailsInfo;
                }).collect(Collectors.toList());
    }
}
