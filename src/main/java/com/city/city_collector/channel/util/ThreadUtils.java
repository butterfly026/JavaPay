package com.city.city_collector.channel.util;

import java.util.concurrent.*;

public class ThreadUtils {

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(5, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);

    private static ThreadUtils instance;

    public static ThreadUtils getInstance() {
        if (instance == null) {
            synchronized (ThreadUtils.class) {
                if (instance == null) {
                    return new ThreadUtils();
                }
            }
        }
        return instance;
    }

    public void run(Runnable runnable) {
        executor.execute(runnable);
    }

    public void runDelay(Runnable runnable) {
        scheduledExecutorService.schedule(runnable, 5, TimeUnit.SECONDS);
    }

}
