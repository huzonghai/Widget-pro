package com.ck.newssdk.widget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
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

    private static Bitmap bitmap;
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
                    remoteViews.setImageViewBitmap(R.id.imgv_pic, bitmap);
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

//        File file = new File(mContext.getFilesDir().getAbsolutePath() + "/" + "56218349.png");
        // android.os.FileUriExposedException: file:///data/data/com.ck.widget/files/56218356.png exposed beyond app through RemoteViews.setUri()
//        rv.setImageViewUri(R.id.imgv_pic, Uri.fromFile(file));
//        rv.setImageViewUri(R.id.imgv_pic, Uri.parse(file.getAbsolutePath()));

//        Download.downloadFile(mArticleListBeanList.get(num).getTitlepic(), mContext.getFilesDir().getAbsolutePath(),String.valueOf(mArticleListBeanList.get(num).getId()) + ".png");

//        if (mArticleListBeanList != null) {
//            for (int i = 0; i < mArticleListBeanList.size(); i++) {
//                num = i;
//                if (mArticleListBeanList.get(num).getItemType() == 3) {
//                    System.out.println("ListTitlepic---> " + mArticleListBeanList.get(num).getTitlepic());
//                    ExecutorServiceUtils.getInstance(mContext).pushRunnable(new Runnable() {
//                        @Override
//                        public void run() {
//                            bitmap = Download.downloadToBitmap(mArticleListBeanList.get(num).getTitlepic());
//                            Message message = mHandler.obtainMessage();
//                            message.what = SUCCESS;
//                            mHandler.sendMessageAtTime(message, 1000);
//                        }
//                    });
//                }
//            }
//        }

        bitmap = Download.downloadToBitmap("http://cdn.img.coolook.org/2019-01-07/bcba7dedbb909fd8664a4cdedbb2fd3c.jpg!240");
        Message message = mHandler.obtainMessage();
        message.what = SUCCESS;
        mHandler.sendMessageAtTime(message, 500);

        remoteViews.setTextViewText(R.id.tv_text, articleListBean.getTitle());


        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("Type", 0);
        fillInIntent.putExtra(NewAppWidget.COLLECTION_VIEW_EXTRA, position);
        remoteViews.setOnClickFillInIntent(R.id.rl_widget_item, fillInIntent);

        return remoteViews;
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
