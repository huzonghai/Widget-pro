package com.ck.newssdk.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ck.newssdk.R;
import com.ck.newssdk.beans.ArticleListBean;
import com.ck.newssdk.utils.imload.core.DisplayImageOptions;
import com.ck.newssdk.utils.imload.core.ImageLoader;
import com.ck.newssdk.utils.imload.core.assist.ImageScaleType;
import com.ck.newssdk.utils.imload.core.display.SimpleBitmapDisplayer;

import java.util.List;


class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static String TAG = "RemoteViewsFactory_widget";
    private static Context mContext;
    private static int mAppWidgetId;
    private static List<ArticleListBean> mArticleListBeanList;
    private static RemoteViews remoteViews;
    public static final int TEXT = 1;
    public static final int IMG = 2;
    public static final int BIG_IMG = 3;
    public static final int THREE_IMG = 4;
    private static DisplayImageOptions options;


    /**
     * 构造ListRemoteViewsFactory
     */
    public ListRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    public static void setArticleData(List<ArticleListBean> articleListBean) {
        mArticleListBeanList = articleListBean;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.placeholder_figure)
                .showImageForEmptyUri(R.mipmap.placeholder_figure)
                .showImageOnFail(R.mipmap.placeholder_figure)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer())
                .build();
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
//        initListViewData();
    }

    /**
     * 初始化数据
     */
    private static void initListViewData() {
        mArticleListBeanList = NewAppWidget.getArticListData();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.placeholder_figure)
                .showImageForEmptyUri(R.mipmap.placeholder_figure)
                .showImageOnFail(R.mipmap.placeholder_figure)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer())
                .build();
    }


    @Override
    public RemoteViews getViewAt(int position) {
        ArticleListBean articleListBean = mArticleListBeanList.get(position);
        int itemType = articleListBean.getItemType();
        switch (itemType) {
            case IMG:
                remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.item0_new_app_widget);
                remoteViews.setImageViewBitmap(R.id.imgv_pic, ImageLoader.getInstance().loadImageSync(articleListBean.getImgs()[0], options));
                remoteViews.setTextViewText(R.id.tv_text, articleListBean.getTitle());
                break;
            case BIG_IMG:
                remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.item1_new_app_widget);
                remoteViews.setImageViewBitmap(R.id.imgv_pic_1, ImageLoader.getInstance().loadImageSync(articleListBean.getImgs()[0], options));
                remoteViews.setTextViewText(R.id.tv_text_1, articleListBean.getTitle());
                break;
            case THREE_IMG:
                remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.item2_new_app_widget);
                remoteViews.setImageViewBitmap(R.id.imgv_pic_2_0, ImageLoader.getInstance().loadImageSync(articleListBean.getImgs()[0], options));
                remoteViews.setImageViewBitmap(R.id.imgv_pic_2_1, ImageLoader.getInstance().loadImageSync(articleListBean.getImgs()[1], options));
                remoteViews.setImageViewBitmap(R.id.imgv_pic_2_2, ImageLoader.getInstance().loadImageSync(articleListBean.getImgs()[2], options));
                remoteViews.setTextViewText(R.id.tv_text_2, articleListBean.getTitle());
                break;
            default:
        }

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("Type", 0);
        fillInIntent.putExtra(NewAppWidget.COLLECTION_VIEW_EXTRA, position);
        fillInIntent.putExtra(NewAppWidget.COLLECTION_VIEW_BEAN_EXTRA, articleListBean);
        fillInIntent.setComponent(new

                ComponentName(mContext, NewAppWidget.class));
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
