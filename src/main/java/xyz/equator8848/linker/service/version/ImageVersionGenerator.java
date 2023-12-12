package xyz.equator8848.linker.service.version;

import xyz.equator8848.linker.model.po.TbInstance;

public interface ImageVersionGenerator {
    Integer getImageVersionType();

    void validate(String initVersion);

    String genNextVersion(TbInstance tbInstance);
}
