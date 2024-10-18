package xyz.equator8848.linker.service;

import xyz.equator8848.linker.model.vo.project.PackageImageAddRequest;
import xyz.equator8848.linker.model.vo.project.PackageImageDetails;
import xyz.equator8848.linker.model.vo.project.PackageImageOption;

import java.util.List;

public interface PackageImageService {
    Long addPackageImage(PackageImageAddRequest packageImageAddRequest);

    Boolean deletePackageImage(Long packageImageId);

    List<PackageImageDetails> listPackageImage();

    List<PackageImageOption> getPackageImageOption();
}
