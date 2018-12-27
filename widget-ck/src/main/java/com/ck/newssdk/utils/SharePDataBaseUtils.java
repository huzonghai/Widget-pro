package com.ck.newssdk.utils;

import android.content.Context;


/**
 * Created by hzh on 2017/3/15.
 * 共享参数工具类 临时存些变量
 */
public class SharePDataBaseUtils {

    //存储的时间值
    public static void saveTime(Context context, long time) {
        SharedPrefrencesUtil.saveData(context, "coolook", "time", time);
    }

    public static long getTime(Context context) {
        return SharedPrefrencesUtil.getData(context, "coolook", "time", 0l);
    }


    public static boolean getIsBrowseMode(Context context) {
        return SharedPrefrencesUtil.getData(context, "coolook", "isBrowse", false);
    }

    public static void saveIsBrowseMode(Context context, boolean isBrowse) {
        SharedPrefrencesUtil.saveData(context, "coolook", "isBrowse", isBrowse);
    }

    //当前选择的国家
    public static void saveSelectCountry(Context context, String country) {
        SharedPrefrencesUtil.saveData(context, "coolook", "country", country);
    }

    public static String getSelectCountry(Context context) {
        return SharedPrefrencesUtil.getData(context, "coolook", "country", "");
    }


    //当前选择的webView字体大小
    public static void saveWebTextSize(Context context, int size) {
        SharedPrefrencesUtil.saveData(context, "coolook", "webtextsize", size);
    }

    public static int getWebTextSize(Context context) {
        return SharedPrefrencesUtil.getData(context, "coolook", "webtextsize", 2);
    }


    //是不是第一次
    public static void saveShowHint(Context context, String isToday) {
        SharedPrefrencesUtil.saveData(context, "coolook", "ShowHint", isToday);

    }

    //是不是第一次
    public static String getShowHint(Context context) {
        return SharedPrefrencesUtil.getData(context, "coolook", "ShowHint", "");
    }

    public static boolean getIsFirstShowDialog(Context context) {
        return SharedPrefrencesUtil.getData(context, "coolook", "isFristDialog", false);
    }

    //是否是第一次加载Dialog
    public static void saveIsFirstShowDialog(Context context, boolean isFrist) {
        SharedPrefrencesUtil.saveData(context, "coolook", "isFristDialog", isFrist);
    }

    public static String getUUID(Context context) {
        return SharedPrefrencesUtil.getData(context, "coolook", "uuid", "");
    }

    //是否是第一次加载Dialog
    public static void saveUUID(Context context, String uuid) {
        SharedPrefrencesUtil.saveData(context, "coolook", "uuid", uuid);
    }
}
