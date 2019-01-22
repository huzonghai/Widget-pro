package com.ck.newssdk.utils;

import android.content.Context;


/**
 * Created by hzh on 2017/3/15.
 * 共享参数工具类 临时存些变量
 */
public class SharePDataBaseUtils {

    //存储的时间值
    public static void saveTime(Context context, long time) {
        SharedPrefrencesUtil.saveData(context, "XDKP", "time", time);
    }

    public static long getTime(Context context) {
        return SharedPrefrencesUtil.getData(context, "XDKP", "time", 0l);
    }


    public static boolean getIsBrowseMode(Context context) {
        return SharedPrefrencesUtil.getData(context, "XDKP", "isBrowse", false);
    }

    public static void saveIsBrowseMode(Context context, boolean isBrowse) {
        SharedPrefrencesUtil.saveData(context, "XDKP", "isBrowse", isBrowse);
    }

    //当前选择的国家
    public static void saveSelectCountry(Context context, String country) {
        SharedPrefrencesUtil.saveData(context, "XDKP", "country", country);
    }

    public static String getSelectCountry(Context context) {
        return SharedPrefrencesUtil.getData(context, "XDKP", "country", "");
    }


    //当前选择的webView字体大小
    public static void saveWebTextSize(Context context, int size) {
        SharedPrefrencesUtil.saveData(context, "XDKP", "webtextsize", size);
    }

    public static int getWebTextSize(Context context) {
        return SharedPrefrencesUtil.getData(context, "XDKP", "webtextsize", 2);
    }


    //是不是第一次
    public static void saveShowHint(Context context, String isToday) {
        SharedPrefrencesUtil.saveData(context, "XDKP", "ShowHint", isToday);

    }

    //是不是第一次
    public static String getShowHint(Context context) {
        return SharedPrefrencesUtil.getData(context, "XDKP", "ShowHint", "");
    }


    public static String getUUID(Context context) {
        return SharedPrefrencesUtil.getData(context, "XDKP", "uuid", "");
    }

    public static void saveUUID(Context context, String uuid) {
        SharedPrefrencesUtil.saveData(context, "XDKP", "uuid", uuid);
    }

    //widget是否是首次
    public static boolean getIsFirstShowWidget(Context context) {
        return SharedPrefrencesUtil.getData(context, "XDKP", "isFristWidget", false);
    }

    public static void saveIsFirstShowWidget(Context context, boolean isFrist) {
        SharedPrefrencesUtil.saveData(context, "XDKP", "isFristWidget", isFrist);
    }

}
