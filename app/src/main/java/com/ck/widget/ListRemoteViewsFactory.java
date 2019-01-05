package com.ck.widget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ck.newssdk.beans.ArticleListBean;

import java.util.ArrayList;
import java.util.List;


class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static String TAG = "RemoteViewsFactory_widget";
    private Context mContext;
    private static int mAppWidgetId;
    private static int i;
    private final static int SUCCESS = 0;
    private final static int FAIL = 1;
    private static List<Device> mDevices;
    private Message message;
    private static List<ArticleListBean> mArticleListBeanList;

    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    break;
                case FAIL:
                    break;
                default:
            }
        }
    };

    public static void setArticleData(List<ArticleListBean> articleData) {
        mArticleListBeanList = articleData;
        System.out.println("ListRemoteViewsFactory.setArticleData   " + mArticleListBeanList.size() + "      " + mArticleListBeanList.toString());
    }

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

        initListViewData();
    }

    /**
     * 初始化数据
     */
    private void initListViewData() {
        mDevices = new ArrayList<>();
        mDevices.add(new Device("-26°C", 0));
        mDevices.add(new Device("-18°C", 1));
        mDevices.add(new Device("20°C", 2));


    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_new_app_widget);
        if (mArticleListBeanList != null) {

            ArticleListBean articleListBean = mArticleListBeanList.get(position);
            rv.setImageViewUri(R.id.imgv_pic, Uri.parse(articleListBean.getTitlepic()));
            rv.setTextViewText(R.id.tv_text, articleListBean.getTitle());



            Intent fillInIntent = new Intent();
            fillInIntent.putExtra("Type", 0);
            fillInIntent.putExtra(NewAppWidget.COLLECTION_VIEW_EXTRA, position);
            rv.setOnClickFillInIntent(R.id.rl_widget_item, fillInIntent);


            Intent lockIntent = new Intent();
            lockIntent.putExtra(NewAppWidget.COLLECTION_VIEW_EXTRA, position);
            lockIntent.putExtra("Type", 1);
            rv.setOnClickFillInIntent(R.id.imgv_pic, lockIntent);

            Intent unlockIntent = new Intent();
            unlockIntent.putExtra("Type", 2);
            unlockIntent.putExtra(NewAppWidget.COLLECTION_VIEW_EXTRA, position);
            rv.setOnClickFillInIntent(R.id.tv_text, unlockIntent);
        }
        return rv;
    }


    public static void refresh() {
        i++;
        mDevices = new ArrayList<>();
        mDevices.add(new Device("-26°C", 0));
        mDevices.add(new Device("-18°C", 1));
        mDevices.add(new Device("20°C", 2));
    }


    @Override
    public int getCount() {
        // 返回“集合视图”中的数据的总数
        return mArticleListBeanList.size();
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
        mArticleListBeanList.clear();
    }


}
