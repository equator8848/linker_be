package xyz.equator8848.linker.service.version;

import xyz.equator8848.linker.model.constant.ModelStatus;
import xyz.equator8848.inf.core.model.exception.PreCondition;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.equator8848.linker.service.version.impl.ImageVersionComparableVersionGenerator;
import xyz.equator8848.linker.service.version.impl.ImageVersionCustomGenerator;
import xyz.equator8848.linker.service.version.impl.ImageVersionCustomTimestampGenerator;
import xyz.equator8848.linker.service.version.impl.ImageVersionTimestampGenerator;

import java.util.Map;

@Slf4j
@Service
public class ImageVersionGeneratorHolder {
    private final Map<Integer, ImageVersionGenerator> imageVersionGeneratorMap = ImmutableMap
            .<Integer, ImageVersionGenerator>builder()
            .put(ModelStatus.ImageVersionType.CUSTOM, new ImageVersionCustomGenerator())
            .put(ModelStatus.ImageVersionType.COMPARABLE_VERSION, new ImageVersionComparableVersionGenerator())
            .put(ModelStatus.ImageVersionType.TIMESTAMP, new ImageVersionTimestampGenerator())
            .put(ModelStatus.ImageVersionType.CUSTOM_PREFIX_TIMESTAMP, new ImageVersionCustomTimestampGenerator())
            .build();

    public ImageVersionGenerator getImageVersionGenerator(Integer imageVersionType) {
        ImageVersionGenerator imageVersionGenerator = imageVersionGeneratorMap.get(imageVersionType);
        PreCondition.isNotNull(imageVersionGenerator, "找不到合适的镜像版本生成器");
        return imageVersionGenerator;
    }
}
