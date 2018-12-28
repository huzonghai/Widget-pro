package com.ck.netlib.utils;

import android.app.Application;
import android.os.Environment;

public class App extends Application {

    public static Application context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }


   public static String getCacheUri() {
        if (context.getExternalCacheDir()!=null &&
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return context.getExternalCacheDir().toString();
        }else {
            return context.getCacheDir().toString();
        }
    }
}
