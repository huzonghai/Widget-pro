package com.ck.widget;

import android.content.Context;

public class Utils {
    private static Context mContext;

    public static void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public static Context getApp() {
        if (mContext != null) {
            return mContext;
        }
        return null;
    }
}
