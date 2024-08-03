package xyz.equator8848.linker.service.version;

import xyz.equator8848.linker.model.po.TbInstance;

import java.text.SimpleDateFormat;
import java.util.Date;

public interface ImageVersionGenerator {
    Integer getImageVersionType();

    void validate(String initVersion, String versionPrefix);

    String genNextVersion(TbInstance tbInstance);

    default String getTimestamp() {
        Date currentTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        return dateFormat.format(currentTime);
    }
}
