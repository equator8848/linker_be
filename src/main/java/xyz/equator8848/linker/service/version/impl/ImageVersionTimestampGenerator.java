package xyz.equator8848.linker.service.version.impl;

import xyz.equator8848.linker.model.constant.ModelStatus;
import xyz.equator8848.linker.model.po.TbInstance;
import xyz.equator8848.linker.service.version.ImageVersionGenerator;

public class ImageVersionTimestampGenerator implements ImageVersionGenerator {
    @Override
    public Integer getImageVersionType() {
        return ModelStatus.ImageVersionType.TIMESTAMP;
    }


    @Override
    public void validate(String initVersion, String versionPrefix) {
        // 无需校验
    }

    @Override
    public String genNextVersion(TbInstance tbInstance) {
        return getTimestamp();
    }
}
