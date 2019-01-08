package com.ck.newssdk.widget;

import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Executor {
    private static final ScheduledThreadPoolExecutor mScheduledExecutor = new ScheduledThreadPoolExecutor(6);

    private Executor() {
    }

    public static ScheduledExecutorService getPool() {
        return mScheduledExecutor;
    }

    public static ScheduledFuture<?> schedule(Runnable runnable, long delay, TimeUnit unit) {
        return mScheduledExecutor.schedule(runnable, delay, unit);
    }

    public static <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return mScheduledExecutor.schedule(callable, delay, unit);
    }

    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long initialDelay, long period, TimeUnit unit) {
        return mScheduledExecutor.scheduleAtFixedRate(runnable, initialDelay, period, unit);
    }

    public static ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long initialDelay, long delay, TimeUnit unit) {
        return mScheduledExecutor.scheduleWithFixedDelay(runnable, initialDelay, delay, unit);
    }

    public static void execute(Runnable runnable) {
        mScheduledExecutor.execute(runnable);
    }

    static {
        mScheduledExecutor.setKeepAliveTime(120L, TimeUnit.SECONDS);
        mScheduledExecutor.allowCoreThreadTimeOut(true);
        mScheduledExecutor.setMaximumPoolSize(18);
        mScheduledExecutor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                CKLogger.d("execute rejected");
            }
        });
    }

}
