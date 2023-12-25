package xyz.equator8848.linker.service.version;

import xyz.equator8848.linker.model.constant.ModelStatus;
import xyz.equator8848.linker.model.po.TbInstance;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageVersionTimestampGenerator implements ImageVersionGenerator {
    @Override
    public Integer getImageVersionType() {
        return ModelStatus.ImageVersionType.TIMESTAMP;
    }


    @Override
    public void validate(String initVersion) {
        // 无需校验
    }

    @Override
    public String genNextVersion(TbInstance tbInstance) {
        Date currentTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        return dateFormat.format(currentTime);
    }
}
