package com.equator.linker.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author: Equator
 * @Date: 2021/11/22 22:14
 **/
@Slf4j
public final class ThrottleDelayThreadPoolService {
    private static final ScheduledThreadPoolExecutor INSTANCE = build();

    private static final Map<String, ScheduledFuture<?>> map = new ConcurrentHashMap<>();

    private ThrottleDelayThreadPoolService() {

    }

    public static ExecutorService getInstance() {
        return INSTANCE;
    }

    public static void execute(String key, Runnable runnable, long delaySeconds) {
        ScheduledFuture<?> task = map.get(key);
        if (task != null) {
            log.warn("ThrottleDelayThreadPoolService ignore task {} {}", key, runnable);
            return;
        }
        ScheduledFuture<?> schedule = INSTANCE.schedule(() -> {
            runnable.run();
            map.remove(key);
        }, delaySeconds, TimeUnit.SECONDS);
        log.info("ThrottleDelayThreadPoolService submit task {} {}", key, runnable);
        map.put(key, schedule);
    }

    private static ScheduledThreadPoolExecutor build() {
        return new ScheduledThreadPoolExecutor(8, new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
