package com.ck.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    public static final String TAG = "widget";
    public static final String REFRESH_WIDGET = "refresh_widget";
    public static final String SEARCH_WIDGET = "serach_widget";
    public static final String CARD_WIDGET = "card_widget";
    public static final String COLLECTION_VIEW_ACTION = "collection_view_action";
    public static final String COLLECTION_VIEW_EXTRA = "collection_view_extra";
    private static Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            hideLoading(Utils.getApp());
            Toast.makeText(Utils.getApp(), "刷新成功", Toast.LENGTH_SHORT).show();
        }
    };

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        //搜索
        Intent serach = new Intent().setAction(SEARCH_WIDGET);
        PendingIntent pendingIntentser = PendingIntent.getBroadcast(context, 0, serach, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.re_search, pendingIntentser);

        //刷新
        Intent refresh = new Intent().setAction(REFRESH_WIDGET);
        PendingIntent pendingIntentre = PendingIntent.getBroadcast(context, 0, refresh, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.tv_refresh, pendingIntentre);

        //卡片管理
        Intent card = new Intent().setAction(CARD_WIDGET);
        PendingIntent pendingIntentcard = PendingIntent.getBroadcast(context, 0, card, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.tv_more, pendingIntentcard);


        // 设置 “ListView” 的adapter。
        // (01) intent: 对应启动 ListWidgetService(RemoteViewsService) 的intent
        // (02) setRemoteAdapter: 设置 ListView的适配器
        //    通过setRemoteAdapter将ListView和ListWidgetService关联起来，
        //    以达到通过 ListWidgetService 更新 ListView 的目的
        Intent serviceIntent = new Intent(context, ListWidgetService.class);
        remoteViews.setRemoteAdapter(R.id.lv_news, serviceIntent);

        // 设置响应 “ListView” 的intent模板
        // 说明：“集合控件(如GridView、ListView、StackView等)”中包含很多子元素。
        //     它们不能像普通的按钮一样通过 setOnClickPendingIntent 设置点击事件，必须先通过两步。
        //        (01) 通过 setPendingIntentTemplate 设置 “intent模板”，这是比不可少的！
        //        (02) 然后在处理该“集合控件”的RemoteViewsFactory类的getViewAt()接口中 通过 setOnClickFillInIntent 设置“集合控件的某一项的数据”
        Intent listIntent = new Intent();
        listIntent.setAction(COLLECTION_VIEW_ACTION);
        listIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, listIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 设置intent模板
        remoteViews.setPendingIntentTemplate(R.id.lv_news, pendingIntent);

        //更新widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //list
        if (action.equals(COLLECTION_VIEW_ACTION)) {
            // 接受“ListView”的点击事件的广播
            int type = intent.getIntExtra("Type", 0);
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            int index = intent.getIntExtra(COLLECTION_VIEW_EXTRA, 0);
            switch (type) {
                case 0:
                    Toast.makeText(context, "item" + index, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(context, "lock" + index, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context, "unlock" + index, Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
        } else if (action.equals(SEARCH_WIDGET)) {
            String url = "http://s.zlsite.com/?channel=50109";
            Intent startAcIntent = new Intent();
            startAcIntent.setComponent(new ComponentName("com.ck.widget", "com.ck.widget.MainActivity"));
            startAcIntent.putExtra("url", url);
            startAcIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startAcIntent);
        } else if (action.equals(REFRESH_WIDGET)) {
            Toast.makeText(context, "刷新...", Toast.LENGTH_SHORT).show();

            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, NewAppWidget.class);
            ListRemoteViewsFactory.refresh();
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.lv_news);
            mHandler.postDelayed(runnable, 2000);
            showLoading(context);
        } else if (action.equals(CARD_WIDGET)) {
            String url = "http://s.zlsite.com/?channel=50109";
            Intent startAcIntent = new Intent();
            startAcIntent.setComponent(new ComponentName("com.ck.widget", "com.ck.widget.MainActivity"));
            startAcIntent.putExtra("url", url);
            startAcIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startAcIntent);
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


    /**
     * 显示加载loading
     */
    private void showLoading(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        remoteViews.setViewVisibility(R.id.tv_refresh, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.progress_bar, View.VISIBLE);
        remoteViews.setTextViewText(R.id.tv_refresh, "正在刷新...");
        refreshWidget(context, remoteViews, false);
    }

    /**
     * 隐藏加载loading
     */
    private void hideLoading(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        remoteViews.setViewVisibility(R.id.progress_bar, View.GONE);
        remoteViews.setTextViewText(R.id.tv_refresh, "刷新");
        refreshWidget(context, remoteViews, false);
    }

    /**
     * 刷新Widget
     */
    private void refreshWidget(Context context, RemoteViews remoteViews, boolean refreshList) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, NewAppWidget.class);
        appWidgetManager.updateAppWidget(componentName, remoteViews);
        if (refreshList) {
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(componentName), R.id.lv_news);
        }
    }
}

