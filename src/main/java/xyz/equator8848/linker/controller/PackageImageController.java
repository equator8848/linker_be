package xyz.equator8848.linker.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.equator8848.inf.auth.annotation.SimpleRBACApi;
import xyz.equator8848.inf.auth.model.constant.RoleType;
import xyz.equator8848.inf.core.http.model.Response;
import xyz.equator8848.linker.model.vo.project.PackageImageAddRequest;
import xyz.equator8848.linker.service.PackageImageService;

@Validated
@RestController
@RequestMapping("/api/v1/package-image")
public class PackageImageController {
    @Autowired
    private PackageImageService packageImageService;

    @SimpleRBACApi(requireRoleType = RoleType.SYSTEM_ADMIN)
    @PostMapping("/add-package-image")
    public Response addPackageImage(@RequestBody @Valid PackageImageAddRequest packageImageAddRequest) {
        return Response.success(packageImageService.addPackageImage(packageImageAddRequest));
    }

    @SimpleRBACApi(requireRoleType = RoleType.SYSTEM_ADMIN)
    @DeleteMapping("/delete-package-image")
    public Response deletePackageImage(@RequestParam Long packageImageId) {
        return Response.success(packageImageService.deletePackageImage(packageImageId));
    }

    @SimpleRBACApi(requireRoleType = RoleType.SYSTEM_ADMIN)
    @GetMapping("/list-package-image")
    public Response listPackageImage() {
        return Response.success(packageImageService.listPackageImage());
    }


    @SimpleRBACApi(requireRoleType = RoleType.USER)
    @GetMapping("/get-package-image-option")
    public Response getPackageImageOption() {
        return Response.success(packageImageService.getPackageImageOption());
    }
}