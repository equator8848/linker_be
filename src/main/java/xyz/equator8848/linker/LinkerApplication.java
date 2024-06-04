package xyz.equator8848.linker;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@SpringBootApplication
@MapperScan({"xyz.equator8848.linker.dao.mapper"})
public class LinkerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(LinkerApplication.class).run(args);
        System.out.println("前后端联调平台 Linker 启动成功 >>>");
    }

}
