package com.ck.newssdk;

import android.content.Context;
import android.content.Intent;

import com.ck.newssdk.ui.CkActivity;
import com.ck.newssdk.utils.Iml;

/**
 * Created by mackson on 2017/7/15.
 */

public class Ck {
    public static void init(Context context,String countryCode) {
        Iml.initIml(context,countryCode);
    }
    public static void open(Context context) {
        context.startActivity(new Intent(context, CkActivity.class));
    }
}
