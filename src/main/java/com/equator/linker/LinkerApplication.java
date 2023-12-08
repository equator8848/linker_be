package com.equator.linker;

import com.equator.linker.configuration.ProGuardBeanNameGenerator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
// @ComponentScan({"com.equator"})
@SpringBootApplication
@MapperScan({"com.equator.linker.dao.mapper"})
public class LinkerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(LinkerApplication.class)
                .beanNameGenerator(new ProGuardBeanNameGenerator())
                .run(args);
        System.out.println("前后端联调平台 Linker 启动成功 >>>");
    }

}
