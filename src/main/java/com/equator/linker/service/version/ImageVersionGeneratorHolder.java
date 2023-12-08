package com.equator.linker.service.version;

import com.equator.inf.core.model.exception.PreCondition;
import com.equator.linker.model.constant.ModelStatus;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ImageVersionGeneratorHolder {
    private final Map<Integer, ImageVersionGenerator> imageVersionGeneratorMap = ImmutableMap
            .<Integer, ImageVersionGenerator>builder()
            .put(ModelStatus.ImageVersionType.CUSTOM, new ImageVersionCustomGenerator())
            .put(ModelStatus.ImageVersionType.COMPARABLE_VERSION, new ImageVersionComparableVersionGenerator())
            .put(ModelStatus.ImageVersionType.TIMESTAMP, new ImageVersionTimestampGenerator())
            .build();

    public ImageVersionGenerator getImageVersionGenerator(Integer imageVersionType) {
        ImageVersionGenerator imageVersionGenerator = imageVersionGeneratorMap.get(imageVersionType);
        PreCondition.isNotNull(imageVersionGenerator, "找不到合适的镜像版本生成器");
        return imageVersionGenerator;
    }
}
