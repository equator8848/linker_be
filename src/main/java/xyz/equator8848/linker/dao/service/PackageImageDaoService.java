package xyz.equator8848.linker.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;
import xyz.equator8848.linker.dao.mapper.TbPackageImageMapper;
import xyz.equator8848.linker.model.po.TbPackageImage;

/**
 * @Author: Equator
 * @Date: 2022/9/18 11:47
 **/
@Component
public class PackageImageDaoService extends ServiceImpl<TbPackageImageMapper, TbPackageImage> implements IService<TbPackageImage> {

}
