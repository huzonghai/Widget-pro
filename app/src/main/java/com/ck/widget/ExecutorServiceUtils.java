package com.ck.widget;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class ExecutorServiceUtils {


    private volatile static ExecutorServiceUtils instance;

    private Context context;

    private ExecutorService service;


    private ExecutorServiceUtils(Context context) {
        super();
        this.context = context;
        service = Executors.newCachedThreadPool();
    }

    public static ExecutorServiceUtils getInstance(Context context) {
        ExecutorServiceUtils tmp = instance;
        if (tmp == null) {
            synchronized (ExecutorServiceUtils.class) {
                tmp = instance;
                if (tmp == null) {
                    tmp = new ExecutorServiceUtils(context.getApplicationContext());
                    instance = tmp;
                }
            }
        }
        return tmp;
    }


    public void pushRunnable(Runnable runnable) {
        Future<?> submit = service.submit(runnable);
    }

}
