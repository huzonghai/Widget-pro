package com.ck.newssdk.widget;

import android.content.Context;

import com.ck.newssdk.utils.SharedPrefrencesUtil;


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

    public static void saveListData(Context context, String jsonArrayStr) {
        SharedPrefrencesUtil.saveData(context, "XDKP", "jsonArrayStr", jsonArrayStr);
    }

    public static String getListData(Context context) {
        return SharedPrefrencesUtil.getData(context, "XDKP", "jsonArrayStr", "");
    }

}
