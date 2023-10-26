package com.equator.linker;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.equator.linker"})
@MapperScan({"com.equator.linker.dao.mapper"})
public class LinkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkerApplication.class, args);
        System.out.println("前后端联调平台 Linker 启动成功 >>>");
    }

}
