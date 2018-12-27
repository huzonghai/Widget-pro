package com.ck.newssdk.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;

/**
 * Created by wty on 2017/7/13.
 */

public class ApplicationUtil {
    public static void openActivity(Context aContent, Class<?> aClass, Map<String, String> aParam) {
        openActivity(aContent, aClass, aParam, null);
    }

    public static void openActivity(Context aContent, Class<?> aClass, Map<String, String> aParam, List<Integer> aFlags) {
        Intent lIntent = new Intent();
        if (aParam != null) {
            for (Map.Entry<String, String> lEntry : aParam.entrySet()) {
                lIntent.putExtra(lEntry.getKey(), lEntry.getValue());
            }
        }
        if (aFlags != null) {
            for (Integer lFlag : aFlags) {
                lIntent.addFlags(lFlag);
            }
        }
        lIntent.setClass(aContent, aClass);
        aContent.startActivity(lIntent);
    }

    public static void stringtoBitmap(ImageView imageView, String string) {
        //将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            if (bitmap!=null){
                imageView.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
