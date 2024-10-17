package xyz.equator8848.linker.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.equator8848.linker.dao.service.PackageImageDaoService;
import xyz.equator8848.linker.model.po.TbPackageImage;
import xyz.equator8848.linker.model.vo.project.PackageImageAddRequest;
import xyz.equator8848.linker.model.vo.project.PackageImageDetails;
import xyz.equator8848.linker.model.vo.project.PackageImageOption;
import xyz.equator8848.linker.service.PackageImageService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PackageImageServiceImpl implements PackageImageService {
    @Autowired
    private PackageImageDaoService packageImageDaoService;

    @Override
    public Long addPackageImage(PackageImageAddRequest packageImageAddRequest) {
        TbPackageImage tbPackageImage = new TbPackageImage();
        tbPackageImage.setName(packageImageAddRequest.getName());
        tbPackageImage.setIntro(packageImageAddRequest.getIntro());
        tbPackageImage.setImagePath(packageImageAddRequest.getImagePath());
        packageImageDaoService.save(tbPackageImage);
        return tbPackageImage.getId();
    }

    @Override
    public Boolean deletePackageImage(Long packageImageId) {
        return packageImageDaoService.removeById(packageImageId);
    }

    @Override
    public List<PackageImageDetails> list() {
        return packageImageDaoService.list().stream().map(tbPackageImage -> {
            PackageImageDetails packageImageDetails = new PackageImageDetails();
            BeanUtils.copyProperties(tbPackageImage, packageImageDetails);
            return packageImageDetails;
        }).collect(Collectors.toList());
    }

    @Override
    public List<PackageImageOption> getPackageImageOption() {
        List<PackageImageOption> packageImageOptions = new java.util.ArrayList<>(packageImageDaoService.list().stream().map(tbPackageImage -> {
            PackageImageOption packageImageOption = new PackageImageOption();
            packageImageOption.setName(tbPackageImage.getName());
            packageImageOption.setValue(tbPackageImage.getImagePath());
            return packageImageOption;
        }).toList());

        if (CollectionUtils.isEmpty(packageImageOptions)) {
            PackageImageOption packageImageOption = new PackageImageOption();
            packageImageOption.setName("node 16.13.1 + pnpm 7.5.1");
            packageImageOption.setValue("lsage/pnpm-circleci-node:16.13.1-pnpm7.5.1");
            packageImageOptions.add(packageImageOption);
        }
        return packageImageOptions;
    }
}
