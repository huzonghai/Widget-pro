package com.ck.newssdk.widget;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ck.newssdk.R;
import com.ck.newssdk.beans.ArticleListBean;

import java.util.List;


class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static String TAG = "RemoteViewsFactory_widget";
    private static Context mContext;
    private static int mAppWidgetId;
    private static int i;

    private static List<ArticleListBean> mArticleListBeanList;
    private static int num;
    private final static int SUCCESS = 0;
    private final static int FAIL = 1;
    private static RemoteViews remoteViews;

    public static final int TEXT = 1;
    public static final int IMG = 2;
    public static final int BIG_IMG = 3;
    public static final int THREE_IMG = 4;
    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    NewBean newBean = (NewBean) msg.obj;
                    remoteViews.setImageViewBitmap(R.id.imgv_pic, newBean.getBitmap());
                    remoteViews.setTextViewText(R.id.tv_text, newBean.getTitle());
                    break;
                case FAIL:
                    break;
                default:
            }
        }
    };

    /**
     * 构造ListRemoteViewsFactory
     */
    public ListRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
//        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

    }

    public static void setArticleData(List<ArticleListBean> articleListBean) {
        mArticleListBeanList = articleListBean;

    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        initListViewData();
    }

    /**
     * 初始化数据
     */
    private static void initListViewData() {
    }


    @Override
    public RemoteViews getViewAt(int position) {

        ArticleListBean articleListBean = mArticleListBeanList.get(position);
        int itemType = articleListBean.getItemType();
        switch (itemType) {
            case IMG:
                remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.item0_new_app_widget);
                remoteViews.setImageViewBitmap(R.id.imgv_pic, Download.downloadToBitmap(articleListBean.getImgs()[0]));
                remoteViews.setTextViewText(R.id.tv_text, articleListBean.getTitle());
                break;
            case BIG_IMG:
                remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.item1_new_app_widget);
                remoteViews.setImageViewBitmap(R.id.imgv_pic_1, Download.downloadToBitmap(articleListBean.getImgs()[0]));
                remoteViews.setTextViewText(R.id.tv_text_1, articleListBean.getTitle());
                break;
            case THREE_IMG:
                remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.item2_new_app_widget);
                remoteViews.setImageViewBitmap(R.id.imgv_pic_2_0, Download.downloadToBitmap(articleListBean.getImgs()[0]));
                remoteViews.setImageViewBitmap(R.id.imgv_pic_2_1, Download.downloadToBitmap(articleListBean.getImgs()[1]));
                remoteViews.setImageViewBitmap(R.id.imgv_pic_2_2, Download.downloadToBitmap(articleListBean.getImgs()[2]));
                remoteViews.setTextViewText(R.id.tv_text_2, articleListBean.getTitle());
                break;
            default:
        }

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("Type", 0);
        fillInIntent.putExtra(NewAppWidget.COLLECTION_VIEW_EXTRA, position);
        fillInIntent.putExtra(NewAppWidget.COLLECTION_VIEW_BEAN_EXTRA, articleListBean);
        fillInIntent.setComponent(new ComponentName(mContext, NewAppWidget.class));
        remoteViews.setOnClickFillInIntent(R.id.rl_widget_item, fillInIntent);

        return remoteViews;
    }

    class NewBean {
        Bitmap bitmap;
        String title;

        public NewBean(Bitmap bitmap, String title) {
            this.bitmap = bitmap;
            this.title = title;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static void refresh(Context context) {
        mContext = context;
        if (mArticleListBeanList != null) {
            mArticleListBeanList.clear();
        }
        initListViewData();
    }

    @Override
    public int getCount() {
        return mArticleListBeanList != null ? mArticleListBeanList.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
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
        if (mArticleListBeanList != null) {
            mArticleListBeanList.clear();
        }
    }


}
