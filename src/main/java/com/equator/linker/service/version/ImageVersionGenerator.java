package com.equator.linker.service.version;

import com.equator.linker.model.po.TbInstance;

public interface ImageVersionGenerator {
    Integer getImageVersionType();

    void validate(String initVersion);

    String genNextVersion(TbInstance tbInstance);
}
