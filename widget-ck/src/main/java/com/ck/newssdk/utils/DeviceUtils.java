package com.ck.newssdk.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.UUID;

/**
 * Created by yt on 2016/12/8.
 */

public class DeviceUtils {

    //根据Wifi信息获取本地Mac  如果不是wifi环境 则获取 02:00:00:00:00:00
    public static String getLocalMacAddressFromWifiInfo(Context context) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    //通过 wifi的配置文件获取。   客户说可能为空。
    public static String getMac() {
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            String line;
            while ((line = input.readLine()) != null) {
                macSerial += line.trim();
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
/*        if (StringUtils.isEmpty(macSerial))
            return RandomMacAddress.getMacAddrWithFormat(":");*/

        return macSerial;
    }

    public static float displayDensity = 0.0F;

    public static float getDensity(Context context) {
        if (displayDensity == 0.0)
            displayDensity = getDisplayMetrics(context).density;
        return displayDensity;
    }


    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(
                displaymetrics);
        return displaymetrics;
    }

    public static float getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    public static float getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    @SuppressLint("HardwareIds")
    public static String getIMEI(Context context) {
        String id = "";
        try {
            TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            id = tel.getDeviceId();
        } catch (Exception e) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }

    public static String getVersionName(Context context) {
        String name = "";
        try {
            name = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            0).versionName;
        } catch (PackageManager.NameNotFoundException ex) {
            name = "";
        }
        return name;
    }

    public static int getVersionCode(Context context) {
        int code;
        try {
            code = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            code = 0;
        }
        return code;
    }

    public static String getUUID(Context context) {
        String uuid = null;
        try {
            uuid = SharePDataBaseUtils.getUUID(context);
            if (TextUtils.isEmpty(uuid)) {
                uuid = MD5.MD5(UUID.randomUUID().toString());
                SharePDataBaseUtils.saveUUID(context, uuid);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return uuid;
    }

    public static String getCountry(Context context) {
        String country = "";
        try {
            country = context.getResources().getConfiguration().locale.getCountry();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StringUtils.isEmpty(country) ? "us" : country;
    }

    public static String getLanguage(Context context) {
        String language = "";
        try {
            language = context.getResources().getConfiguration().locale.getLanguage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StringUtils.isEmpty(language) ? "en" : language;
    }

    @SuppressLint("MissingPermission")
    public static String getUniqueId(Context context) {
        String uniqueId = "";
        try {
            StringBuilder deviceId = new StringBuilder();
            //wifi mac地址
            WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String wifiMac = info.getMacAddress();
            if (wifiMac != null && !"".equals(wifiMac)) {
                deviceId.append("wifi");
                deviceId.append(wifiMac);
                uniqueId = deviceId.toString();
                return MD5.MD5(uniqueId);
            }
            //IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (imei != null && !"".equals(imei)) {
                deviceId.append("imei");
                deviceId.append(imei);
                uniqueId = deviceId.toString();
                return MD5.MD5(uniqueId);
            }
            //如果上面都没有， 则生成一个id：随机码
            uniqueId = UUID.randomUUID().toString();
        } catch (Exception e) {
            uniqueId = UUID.randomUUID().toString();
        }

        String uuid = SharePDataBaseUtils.getUUID(context);
        if (StringUtils.isEmpty(uuid)) {
            uuid = MD5.MD5(uniqueId);
            SharePDataBaseUtils.saveUUID(context, uuid);
        }
        return uuid;
    }

    /**
     * 获取渠道码
     *
     * @param context
     * @return
     */
    public static String getChannelCode(Context context) {
        try {
            String code = getMetaData(context, "CHANNEL");
            if (code != null) {
                return code;
            }
            return "";

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private static String getMetaData(Context context, String key) {

        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            Object value = ai.metaData.get(key);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
//            ExceptionUtil.handle(e);
        }
        return null;

    }

}
