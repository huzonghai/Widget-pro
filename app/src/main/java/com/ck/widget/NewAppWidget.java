package com.ck.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    public static final String TAG = "widget";

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        //分别绑定id
        remoteViews.setOnClickPendingIntent(R.id.re_search, getPendingIntent(context, R.id.re_search));
        remoteViews.setOnClickPendingIntent(R.id.re_weather, getPendingIntent(context, R.id.re_weather));
        remoteViews.setOnClickPendingIntent(R.id.lv_new, getPendingIntent(context, R.id.lv_new));
        //更新widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private static PendingIntent getPendingIntent(Context context, int resID) {
        Intent intent = new Intent();
        intent.setClass(context, NewAppWidget.class);//如果没有这一句，表示匿名的。加上表示是显式的。在单个按钮的时候是没啥区别的，但是多个的时候就有问题了
        intent.setAction("serarch");
        //设置data域的时候，把控件id一起设置进去，
        // 因为在绑定的时候，是将同一个id绑定在一起的，所以哪个控件点击，发送的intent中data中的id就是哪个控件的id
        intent.setData(Uri.parse("id:" + resID));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        return pendingIntent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // 接收到任意广播时触发，在其他方法之前被调用。
        if (intent.getAction().equals("serarch")) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            Uri data = intent.getData();
            int resId = -1;
            if (data != null) {
                resId = Integer.parseInt(data.getSchemeSpecificPart());
            }
            switch (resId) {
                case R.id.re_search:
                    String url = "http://s.zlsite.com/?channel=50109";
                    Intent startAcIntent = new Intent();
                    startAcIntent.setComponent(new ComponentName("com.ck.widget", "com.ck.widget.MainActivity"));
                    startAcIntent.putExtra("url", url);
                    startAcIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(startAcIntent);
                    break;
                case R.id.re_weather:
                    Toast.makeText(context, "天气", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.lv_new:
                    Toast.makeText(context, "列表", Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context, NewAppWidget.class);
            //更新
            manger.updateAppWidget(thisName, remoteViews);

        }
        Log.i(TAG, "onReceive: 执行");
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            Log.i(TAG, "onUpdate: 执行");
        }
    }

    @Override
    public void onEnabled(Context context) {
        Log.i(TAG, "onEnabled: 执行");

    }

    @Override
    public void onDisabled(Context context) {
        Log.i(TAG, "onDisabled: 执行");
    }
}

