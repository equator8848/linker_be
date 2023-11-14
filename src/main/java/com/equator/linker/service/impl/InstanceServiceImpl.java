package com.equator.linker.service.impl;

import cn.hutool.core.util.EnumUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.equator.core.model.exception.PreCondition;
import com.equator.core.util.json.JsonUtil;
import com.equator.linker.common.util.UserAuthUtil;
import com.equator.linker.common.util.UserContextUtil;
import com.equator.linker.configuration.AppConfig;
import com.equator.linker.dao.service.InstanceDaoService;
import com.equator.linker.dao.service.InstanceUserRefDaoService;
import com.equator.linker.dao.service.ProjectDaoService;
import com.equator.linker.model.constant.BaseConstant;
import com.equator.linker.model.dto.DynamicAppConfiguration;
import com.equator.linker.model.po.TbInstance;
import com.equator.linker.model.po.TbInstanceUserRef;
import com.equator.linker.model.po.TbProject;
import com.equator.linker.model.vo.instance.InstanceCreateRequest;
import com.equator.linker.model.vo.instance.InstanceDetailsInfo;
import com.equator.linker.model.vo.instance.InstanceListRequest;
import com.equator.linker.model.vo.instance.InstanceUpdateRequest;
import com.equator.linker.model.vo.project.ProxyConfig;
import com.equator.linker.model.vo.project.ScmConfig;
import com.equator.linker.service.InstanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InstanceServiceImpl implements InstanceService {
    @Autowired
    private ProjectDaoService projectDaoService;

    @Autowired
    private InstanceDaoService instanceDaoService;

    @Autowired
    private InstanceUserRefDaoService instanceUserRefDaoService;

    @Autowired
    private AppConfig appConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(InstanceCreateRequest instanceCreateRequest) {
        TbProject tbProject = projectDaoService.getById(instanceCreateRequest.getProjectId());
        PreCondition.isNotNull(tbProject, "项目不存在");

        DynamicAppConfiguration dynamicAppConfiguration = appConfig.getConfig();
        Integer nextAccessPort = instanceDaoService.getBaseMapper().getNextAccessPort(dynamicAppConfiguration.getMinAccessPort());

        TbInstance tbInstance = new TbInstance();
        tbInstance.setProjectId(instanceCreateRequest.getProjectId());
        tbInstance.setName(instanceCreateRequest.getName());
        tbInstance.setIntro(instanceCreateRequest.getIntro());

        if (StringUtils.isEmpty(instanceCreateRequest.getScmBranch())) {
            ScmConfig scmConfig = JsonUtil.fromJson(tbProject.getScmConfig(), ScmConfig.class);
            tbInstance.setScmBranch(scmConfig.getDefaultBranch());
        } else {
            tbInstance.setScmBranch(instanceCreateRequest.getScmBranch());
        }

        ProxyConfig requestProxyConfig = instanceCreateRequest.getProxyConfig();
        if (requestProxyConfig == null || CollectionUtils.isEmpty(requestProxyConfig.getProxyPassConfigs())) {
            ProxyConfig proxyConfig = JsonUtil.fromJson(tbProject.getProxyConfig(), ProxyConfig.class);
            tbInstance.setProxyConfig(JsonUtil.toJson(proxyConfig));
        } else {
            tbInstance.setProxyConfig(JsonUtil.toJson(requestProxyConfig));
        }

        tbInstance.setAccessPort(nextAccessPort);
        tbInstance.setAccessLink(String.format("%s:%s:%s",
                dynamicAppConfiguration.getAccessHost(), nextAccessPort,
                Optional.ofNullable(tbProject.getAccessEntrance()).orElse("/")));
        tbInstance.setAccessLevel(BaseConstant.AccessLevel.valueOf(instanceCreateRequest.getAccessLevel()).getCode());
        instanceDaoService.save(tbInstance);

        TbInstanceUserRef tbInstanceUserRef = new TbInstanceUserRef();
        tbInstanceUserRef.setInstanceId(tbInstance.getId());
        tbInstanceUserRef.setUserId(UserContextUtil.getUserId());
        tbInstanceUserRef.setRefType(BaseConstant.ProjectInstanceRefType.OWNER.ordinal());
        instanceUserRefDaoService.save(tbInstanceUserRef);
        return tbInstance.getId();
    }

    @Override
    public void update(InstanceUpdateRequest instanceUpdateRequest) {
        Long instanceId = instanceUpdateRequest.getId();
        TbInstance tbInstance = instanceDaoService.getById(instanceId);
        PreCondition.isNotNull(tbInstance, "实例不存在");
        UserAuthUtil.checkPermission(tbInstance.getCreateUserId());

        tbInstance.setName(instanceUpdateRequest.getName());
        tbInstance.setIntro(instanceUpdateRequest.getIntro());

        if (StringUtils.isNotEmpty(instanceUpdateRequest.getScmBranch())) {
            tbInstance.setScmBranch(instanceUpdateRequest.getScmBranch());
        }

        ProxyConfig requestProxyConfig = instanceUpdateRequest.getProxyConfig();
        if (requestProxyConfig != null && !CollectionUtils.isEmpty(requestProxyConfig.getProxyPassConfigs())) {
            tbInstance.setProxyConfig(JsonUtil.toJson(requestProxyConfig));
        }

        tbInstance.setAccessLevel(BaseConstant.AccessLevel.valueOf(instanceUpdateRequest.getAccessLevel()).getCode());
        instanceDaoService.updateById(tbInstance);

        if (BaseConstant.AccessLevel.PRIVATE.ordinal() == tbInstance.getAccessLevel()) {
            // 设置为私密实例，删除其它关联
            instanceUserRefDaoService.remove(Wrappers.<TbInstanceUserRef>lambdaQuery()
                    .eq(TbInstanceUserRef::getInstanceId, instanceId)
                    .eq(TbInstanceUserRef::getRefType, BaseConstant.ProjectInstanceRefType.JOIN.ordinal()));
        }
    }

    @Override
    public void delete(Long instanceId) {
        instanceDaoService.removeById(instanceId);
        TbInstance tbInstance = instanceDaoService.getById(instanceId);
        PreCondition.isNotNull(tbInstance, "实例不存在");
        UserAuthUtil.checkPermission(tbInstance.getCreateUserId());
        instanceUserRefDaoService.remove(Wrappers.<TbInstanceUserRef>lambdaQuery()
                .eq(TbInstanceUserRef::getInstanceId, instanceId));
    }

    @Override
    public List<InstanceDetailsInfo> list(InstanceListRequest instanceListRequest) {
        // 获取自己创建或加入的
        Set<Long> targetInstanceIds = instanceUserRefDaoService.getInstanceIdByUserId(UserContextUtil.getUserId());
        // 公开的
        Set<Long> publicInstanceIds = instanceDaoService
                .getInstanceIdsByAccessLevel(BaseConstant.AccessLevel.PUBLIC.getCode(), targetInstanceIds);
        targetInstanceIds.addAll(publicInstanceIds);

        return instanceDaoService.list(Wrappers.<TbInstance>lambdaQuery()
                        .like(StringUtils.isNotEmpty(instanceListRequest.getSearchKeyword()),
                                TbInstance::getName, instanceListRequest.getSearchKeyword())
                        .eq(TbInstance::getProjectId, instanceListRequest.getProjectId())
                        .in(TbInstance::getId, targetInstanceIds).orderByDesc(TbInstance::getId)).stream()
                .map(tbInstance -> {
                    InstanceDetailsInfo instanceDetailsInfo = new InstanceDetailsInfo();
                    BeanUtils.copyProperties(tbInstance, instanceDetailsInfo);
                    instanceDetailsInfo.setProxyConfig(JsonUtil.fromJson(tbInstance.getProxyConfig(), ProxyConfig.class));
                    instanceDetailsInfo.setAccessLevel(EnumUtil.getFieldBy(BaseConstant.AccessLevel::name,
                            BaseConstant.AccessLevel::getCode, tbInstance.getAccessLevel()));

                    boolean isOwner = tbInstance.getCreateUserId().equals(UserContextUtil.getUserId());
                    instanceDetailsInfo.setIsOwner(isOwner);
                    return instanceDetailsInfo;
                }).collect(Collectors.toList());
    }
}
