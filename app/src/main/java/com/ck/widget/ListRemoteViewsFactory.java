package com.ck.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;


class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final static String TAG = "widget";
    private Context mContext;
    private int mAppWidgetId;
    private static int i;

    private static List<Device> mDevices;

    /**
     * 构造ListRemoteViewsFactory
     */
    public ListRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        // 初始化“集合视图”中的数据
        initListViewData();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        //  HashMap<String, Object> map;

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_new_app_widget);

        // 设置 第position位的“视图”的数据
        Device device = mDevices.get(position);
        //  rv.setImageViewResource(R.id.iv_lock, ((Integer) map.get(IMAGE_ITEM)).intValue());
        rv.setTextViewText(R.id.tv_weather_centigrade, device.getName());

        // 设置 第position位的“视图”对应的响应事件
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("Type", 0);
        fillInIntent.putExtra(NewAppWidget.COLLECTION_VIEW_EXTRA, position);
        rv.setOnClickFillInIntent(R.id.rl_widget_item, fillInIntent);


        Intent lockIntent = new Intent();
        lockIntent.putExtra(NewAppWidget.COLLECTION_VIEW_EXTRA, position);
        lockIntent.putExtra("Type", 1);
        rv.setOnClickFillInIntent(R.id.tv_weather_centigrade, lockIntent);

        Intent unlockIntent = new Intent();
        unlockIntent.putExtra("Type", 2);
        unlockIntent.putExtra(NewAppWidget.COLLECTION_VIEW_EXTRA, position);
        rv.setOnClickFillInIntent(R.id.tv_weather_week, unlockIntent);

        return rv;
    }

    /**
     * 初始化ListView的数据
     */
    private void initListViewData() {
        mDevices = new ArrayList<>();
        mDevices.add(new Device("-26°C", 0));
        mDevices.add(new Device("-18°C", 1));
        mDevices.add(new Device("20°C", 2));
    }


    public static void refresh() {
        i++;
        mDevices.add(new Device("Refresh" + i, 1));
    }


    @Override
    public int getCount() {
        // 返回“集合视图”中的数据的总数
        return mDevices.size();
    }

    @Override
    public long getItemId(int position) {
        // 返回当前项在“集合视图”中的位置
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        // 只有一类 ListView
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
        mDevices.clear();
    }
}
