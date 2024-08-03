package xyz.equator8848.linker.service.version.impl;

import xyz.equator8848.linker.model.constant.BaseConstant;
import xyz.equator8848.linker.model.constant.ModelStatus;
import xyz.equator8848.linker.model.po.TbInstance;
import xyz.equator8848.inf.core.model.exception.VerifyException;
import org.apache.commons.lang3.StringUtils;
import xyz.equator8848.linker.service.version.ImageVersionGenerator;

import java.util.Optional;

public class ImageVersionCustomGenerator implements ImageVersionGenerator {
    @Override
    public Integer getImageVersionType() {
        return ModelStatus.ImageVersionType.CUSTOM;
    }

    @Override
    public void validate(String initVersion, String versionPrefix) {
        if (StringUtils.isBlank(initVersion)) {
            throw new VerifyException("自定义镜像版本不能为空");
        }
    }

    @Override
    public String genNextVersion(TbInstance tbInstance) {
        return Optional.ofNullable(tbInstance.getImageVersion()).orElse(BaseConstant.DEFAULT_IMAGE_VERSION);
    }
}
