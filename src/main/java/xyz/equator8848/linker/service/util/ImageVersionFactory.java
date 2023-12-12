package xyz.equator8848.linker.service.util;

import xyz.equator8848.linker.model.constant.ModelStatus;
import xyz.equator8848.inf.core.model.exception.VerifyException;
import org.apache.commons.lang3.StringUtils;

public class ImageVersionFactory {
    public void initVersionValidate(Integer imageVersionType, String initVersion) {
        if (ModelStatus.ImageVersionType.CUSTOM.equals(imageVersionType)) {
            if (StringUtils.isBlank(initVersion)){
                throw new VerifyException("");
            }
        }
    }
}
