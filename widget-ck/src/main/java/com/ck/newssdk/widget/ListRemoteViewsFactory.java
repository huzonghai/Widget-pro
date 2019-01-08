package com.ck.newssdk.widget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
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
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

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

        remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.item_new_app_widget);

        ArticleListBean articleListBean = mArticleListBeanList.get(position);

        remoteViews.setImageViewBitmap(R.id.imgv_pic, Download.downloadToBitmap(articleListBean.getTitlepic()));
        remoteViews.setTextViewText(R.id.tv_text, articleListBean.getTitle());

//        if (mArticleListBeanList != null) {
//            for (int i = 0; i < mArticleListBeanList.size(); i++) {
//                num = i;
//                if (mArticleListBeanList.get(num).getItemType() == 3) {
//                    System.out.println("ListTitlepic---> " + mArticleListBeanList.get(num).getTitlepic());
//                    ExecutorServiceUtils.getInstance(mContext).pushRunnable(new Runnable() {
//                        @Override
//                        public void run() {
//                            Bitmap bitmap = Download.downloadToBitmap(mArticleListBeanList.get(num).getTitlepic());
//                            if (bitmap != null) {
//                                Message message = mHandler.obtainMessage();
//                                message.what = SUCCESS;
//                                NewBean newBean = new NewBean(bitmap, mArticleListBeanList.get(num).getTitle());
//                                message.obj = newBean;
//                                mHandler.sendMessageAtTime(message, 500);
//                            }
//
//                        }
//                    });
//                }
//            }
//        }

//        bitmap = Download.downloadToBitmap("http://cdn.img.coolook.org/2019-01-07/bcba7dedbb909fd8664a4cdedbb2fd3c.jpg!240");
//        Message message = mHandler.obtainMessage();
//        message.what = SUCCESS;
//        mHandler.sendMessage(message);
//        mHandler.sendMessageAtTime(message, 500);


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
        if (mArticleListBeanList != null) {
            mArticleListBeanList.clear();
        }
    }


}
