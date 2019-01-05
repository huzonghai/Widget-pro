package com.ck.widget;

import android.util.Log;

public class CKLogger {
    public static final String TAG = "TAG";
    public static boolean ENABLE = true;

    public static void setEnable(boolean enable) {
        Log.d(TAG, (enable ? "enable" : "disable") + " logger");
        ENABLE = enable;
    }

    public static void d(String message, Throwable throwable) {
        if (ENABLE) {
            if (throwable == null) {
                Log.d(TAG, message);
            } else {
                Log.d(TAG, message, throwable);
            }
        }
    }

    public static void i(String message) {
        if (ENABLE) {
            Log.i(TAG, message);
        }
    }

    public static void e(String message, Throwable throwable) {
        if (ENABLE) {
            if (throwable == null) {
                Log.e(TAG, message);
            } else {
                Log.e(TAG, message, throwable);
            }
        }
    }

    public static void w(String message, Throwable throwable) {
        if (ENABLE) {
            if (throwable == null) {
                Log.w(TAG, message);
            } else {
                Log.w(TAG, message, throwable);
            }
        }
    }

    public static void w(String message) {
        if (ENABLE) {
            Log.w(TAG, message);
        }
    }

    public static void e(String message) {
        if (ENABLE) {
            Log.e(TAG, message);
        }
    }

    public static void d(String message) {
        if (ENABLE) {
            Log.d(TAG, message);
        }
    }

}
