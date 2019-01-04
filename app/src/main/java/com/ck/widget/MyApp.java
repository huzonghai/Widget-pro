package com.ck.widget;

import com.ck.netlib.utils.App;

public class MyApp extends App {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.mContext = this;
    }
}
