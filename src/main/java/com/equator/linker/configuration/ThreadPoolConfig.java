package com.equator.linker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

// @Configuration
public class ThreadPoolConfig {

    @Bean("linkerTaskExecutor")
    public Executor adminTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 设置线程池参数信息
        // 核心线程数
        taskExecutor.setCorePoolSize(10);
        // 最大线程数
        taskExecutor.setMaxPoolSize(50);
        // 线程队列数量
        taskExecutor.setQueueCapacity(1024);
        // 空闲线程存活时间
        taskExecutor.setKeepAliveSeconds(60);
        taskExecutor.setThreadNamePrefix("linkerTaskExecutor");
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(60);
        // 修改拒绝策略为使用当前线程执行
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化线程池
        taskExecutor.initialize();
        return taskExecutor;
    }
}
