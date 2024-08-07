package xyz.equator8848.linker.service;

import xyz.equator8848.linker.model.po.TbInstance;
import xyz.equator8848.linker.service.version.impl.ImageVersionComparableVersionGenerator;
import xyz.equator8848.linker.service.version.ImageVersionGenerator;
import org.junit.Assert;
import org.junit.Test;

public class ImageVersionGeneratorTestService {
    @Test
    public void test1() {
        TbInstance tbInstance = new TbInstance();
        ImageVersionGenerator imageVersionGenerator = new ImageVersionComparableVersionGenerator();

        tbInstance.setImageVersion("1.0.0");
        String nextVersion = imageVersionGenerator.genNextVersion(tbInstance);
        Assert.assertEquals("1.0.1", nextVersion);

        tbInstance.setImageVersion("1.0.998");
        nextVersion = imageVersionGenerator.genNextVersion(tbInstance);
        Assert.assertEquals("1.0.999", nextVersion);

        tbInstance.setImageVersion("1.0.999");
        nextVersion = imageVersionGenerator.genNextVersion(tbInstance);
        Assert.assertEquals("1.1.0", nextVersion);

        tbInstance.setImageVersion("998.0.998");
        nextVersion = imageVersionGenerator.genNextVersion(tbInstance);
        Assert.assertEquals("998.0.999", nextVersion);


        tbInstance.setImageVersion("999.999.998");
        nextVersion = imageVersionGenerator.genNextVersion(tbInstance);
        Assert.assertEquals("999.999.999", nextVersion);
    }
}
