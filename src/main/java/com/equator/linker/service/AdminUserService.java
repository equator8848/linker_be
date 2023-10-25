package com.equator.linker.service;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.equator.core.util.json.JsonUtil;
import com.equator.core.util.security.PasswordUtil;
import com.equator.linker.dao.service.AdminDaoService;
import com.equator.linker.model.constant.ModelStatus;
import com.equator.linker.model.po.TbAdmin;
import com.equator.linker.model.vo.PageData;
import com.equator.linker.model.vo.user.AdminUserVO;
import com.equator.linker.model.vo.user.CreateAdminUserVO;
import com.equator.linker.model.vo.user.UpdateAdminUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;


/**
 * @Author: Equator
 * @Date: 2021/2/10 10:32
 **/
@Slf4j
@Service
public class AdminUserService {
    @Autowired
    private AdminDaoService adminDaoService;

    public PageData<AdminUserVO> list(String search, Integer pageNum, Integer pageSize) {
        Page<TbAdmin> userPage = adminDaoService.page(new Page<>(pageNum, pageSize),
                Wrappers.<TbAdmin>lambdaQuery()
                        .like(!StringUtils.isEmpty(search), TbAdmin::getUserName, search)
                        .or()
                        .like(!StringUtils.isEmpty(search), TbAdmin::getNickname, search));
        return PageData.wrap(userPage, userPage.getRecords().stream().map(user -> {
            AdminUserVO adminUserVO = new AdminUserVO();
            BeanUtils.copyProperties(user, adminUserVO);
            adminUserVO.setRelationIds(JsonUtil.fromJsonList(user.getRelationIds(), Integer.class));
            return adminUserVO;
        }).collect(Collectors.toList()));
    }

    public Integer add(CreateAdminUserVO createAdminUserVO) {
        TbAdmin tbAdmin = new TbAdmin();
        BeanUtils.copyProperties(createAdminUserVO, tbAdmin);
        tbAdmin.setUserPassword(PasswordUtil.generateSha512CryptPassword(createAdminUserVO.getUserPassword()));
        tbAdmin.setRelationIds(JsonUtil.toJson(createAdminUserVO.getRelationIds()));
        tbAdmin.setStatus(ModelStatus.UserStatus.NORMAL);
        adminDaoService.save(tbAdmin);
        return tbAdmin.getId();
    }

    public void update(UpdateAdminUserVO updateAdminUserVO) {
        TbAdmin tbAdmin = adminDaoService.getById(updateAdminUserVO.getId());
        BeanUtils.copyProperties(updateAdminUserVO, tbAdmin);
        if (!StringUtils.isEmpty(updateAdminUserVO.getUserPassword())) {
            tbAdmin.setUserPassword(PasswordUtil.generateSha512CryptPassword(updateAdminUserVO.getUserPassword()));
        }
        if (!CollectionUtils.isEmpty(updateAdminUserVO.getRelationIds())) {
            tbAdmin.setRelationIds(JsonUtil.toJson(updateAdminUserVO.getRelationIds()));
        }
        tbAdmin.setStatus(updateAdminUserVO.getStatus());
        adminDaoService.updateById(tbAdmin);
    }
}
