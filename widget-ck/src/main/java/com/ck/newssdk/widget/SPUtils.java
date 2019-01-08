package com.ck.newssdk.widget;

import android.content.Context;


public class SPUtils {

    public static void saveCbStateSearch(Context context, boolean cbs) {
        SharedPrefrencesUtil.saveData(context, "XDKP", "cbs", cbs);
    }

    public static boolean getCbStateSearch(Context context) {
        return SharedPrefrencesUtil.getData(context, "XDKP", "cbs", true);
    }

    public static void saveCbStateWeather(Context context, boolean cbw) {
        SharedPrefrencesUtil.saveData(context, "XDKP", "cbw", cbw);
    }

    public static boolean getCbStateWeather(Context context) {
        return SharedPrefrencesUtil.getData(context, "XDKP", "cbw", true);
    }

}
