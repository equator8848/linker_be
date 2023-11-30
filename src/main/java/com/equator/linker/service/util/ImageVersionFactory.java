package com.equator.linker.service.util;

import com.equator.core.model.exception.VerifyException;
import com.equator.linker.model.constant.ModelStatus;
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
