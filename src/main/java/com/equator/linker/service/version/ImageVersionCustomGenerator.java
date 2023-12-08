package com.equator.linker.service.version;

import com.equator.inf.core.model.exception.VerifyException;
import com.equator.linker.model.constant.ModelStatus;
import com.equator.linker.model.po.TbInstance;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static com.equator.linker.model.constant.BaseConstant.DEFAULT_IMAGE_VERSION;

public class ImageVersionCustomGenerator implements ImageVersionGenerator {
    @Override
    public Integer getImageVersionType() {
        return ModelStatus.ImageVersionType.CUSTOM;
    }

    @Override
    public void validate(String initVersion) {
        if (StringUtils.isBlank(initVersion)) {
            throw new VerifyException("自定义镜像版本不能为空");
        }
    }

    @Override
    public String genNextVersion(TbInstance tbInstance) {
        return Optional.ofNullable(tbInstance.getImageVersion()).orElse(DEFAULT_IMAGE_VERSION);
    }
}
