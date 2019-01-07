package com.ck.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.ck.newssdk.Ck;
import com.ck.newssdk.beans.ArticleListBean;
import com.ck.newssdk.config.Configuration;
import com.ck.newssdk.http.RequestCallback;
import com.ck.newssdk.utils.DeviceUtils;
import com.ck.newssdk.utils.JsonUtil;
import com.ck.newssdk.utils.ThreadPoolExecutorUtils;
import com.ck.widget.api.HttpJsonTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ck.newssdk.ui.article.MultipleItemAdapter.BIG_IMG;
import static com.ck.newssdk.ui.article.MultipleItemAdapter.IMG;
import static com.ck.newssdk.ui.article.MultipleItemAdapter.TEXT;
import static com.ck.newssdk.ui.article.MultipleItemAdapter.THREE_IMG;


public class NewAppWidget extends AppWidgetProvider {
    public static final String TAG = "WIDGET";
    public static final String REFRESH_WIDGET = "refresh_widget";
    public static final String SEARCH_WIDGET = "serach_widget";
    public static final String CARD_WIDGET = "card_widget";
    public static final String CARD_FORM_ACT = "card_for_act";
    public static final String LOAD_MORE_NEWS_WIDGET = "load_more_news_widget";
    public static final String COLLECTION_VIEW_ACTION = "collection_view_action";
    public static final String COLLECTION_VIEW_EXTRA = "collection_view_extra";

    private static Context mContext;
    private final static int GETRECOMMEND_SUCCESS = 3;
    private static boolean isRefresh = false;

    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GETRECOMMEND_SUCCESS:
                    ListRemoteViewsFactory.setArticleData((List<ArticleListBean>) msg.obj);

                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
                    ComponentName componentName = new ComponentName(mContext, NewAppWidget.class);

                    RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.new_app_widget);

                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
                    if (isRefresh) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(componentName), R.id.lv_news);
                        isRefresh = false;
                    } else {
                        Intent serviceIntent = new Intent(mContext, ListWidgetService.class);
                        remoteViews.setRemoteAdapter(R.id.lv_news, serviceIntent);
                        Intent listIntent = new Intent();
                        listIntent.setAction(COLLECTION_VIEW_ACTION);
//                    listIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, listIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        remoteViews.setPendingIntentTemplate(R.id.lv_news, pendingIntent);
                    }

//                    appWidgetManager.updateAppWidget(componentName, remoteViews);

                    appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
                    break;
                default:
            }
        }
    };
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

        //加载更多资讯
        Intent loadnews = new Intent().setAction(LOAD_MORE_NEWS_WIDGET);
        PendingIntent pendingIntentload = PendingIntent.getBroadcast(context, 0, loadnews, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.tv_more, pendingIntentload);

        //卡片管理
        Intent card = new Intent().setAction(CARD_WIDGET);
        PendingIntent pendingIntentcard = PendingIntent.getBroadcast(context, 0, card, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.tv_card, pendingIntentcard);


        //更新widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        String action = intent.getAction();
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        //list
        if (action.equals(COLLECTION_VIEW_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            int index = intent.getIntExtra(COLLECTION_VIEW_EXTRA, 0);

            Toast.makeText(context, "item" + index, Toast.LENGTH_SHORT).show();
        } else if (action.equals(SEARCH_WIDGET)) {
            String url = "http://s.zlsite.com/?channel=50109";
            Intent startAcIntent = new Intent();
            startAcIntent.setComponent(new ComponentName("com.ck.widget", "com.ck.widget.MainActivity"));
            startAcIntent.putExtra("url", url);
            startAcIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startAcIntent);
        } else if (action.equals(REFRESH_WIDGET)) {
//            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
//            ComponentName cn = new ComponentName(context, NewAppWidget.class);
//            ListRemoteViewsFactory.refresh(context);
            initListViewData();
            isRefresh = true;
//            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.lv_news);
            mHandler.postDelayed(runnable, 2000);
            showLoading(context);
        } else if (action.equals(LOAD_MORE_NEWS_WIDGET)) {
            Ck.init(context, "us");
            Intent startAcIntent = new Intent();
            startAcIntent.setComponent(new ComponentName("com.ck.widget", "com.ck.widget.CkActivity"));
            startAcIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startAcIntent);
        } else if (action.equals(CARD_WIDGET)) {
            Intent startAcIntent = new Intent();
            startAcIntent.setComponent(new ComponentName("com.ck.widget", "com.ck.widget.CardManageAct"));
            startAcIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startAcIntent);
        } else if (action.equals(CARD_FORM_ACT)) {
            if (intent != null) {
                int type = intent.getIntExtra("type", 2);
                switch (type) {
                    case 0:
                        boolean checkser = intent.getBooleanExtra("check", true);
                        remoteViews.setViewVisibility(R.id.re_search, checkser == true ? View.VISIBLE : View.GONE);
                        break;
                    case 1:
                        boolean checkwea = intent.getBooleanExtra("check", true);
                        remoteViews.setViewVisibility(R.id.re_weather, checkwea == true ? View.VISIBLE : View.GONE);
                        break;
                    default:
                }
            }
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, NewAppWidget.class);
            appWidgetManager.updateAppWidget(componentName, remoteViews);
        }

        Log.i(TAG, "onReceive: 执行");
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        mContext = context;
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            Log.i(TAG, "onUpdate: 执行");
        }
    }

    @Override
    public void onEnabled(Context context) {
        mContext = context;
        initListViewData();
        Log.i(TAG, "onEnabled: 执行");

    }

    @Override
    public void onDisabled(Context context) {
        Log.i(TAG, "onDisabled: 执行");
    }

    /**
     * 初始化数据
     */
    private static void initListViewData() {
        String countryCode = mContext.getResources().getConfiguration().locale.getCountry();
        Ck.init(mContext, countryCode);
        getRecommend(mContext, Configuration.CurrentCountry);
    }


    /**
     * 获取当前国家推荐
     *
     * @param countryCode
     */
    public static void getRecommend(Context context, String countryCode) {
        Map map = new HashMap();
        map.put("uuid", DeviceUtils.getUUID(context));
        map.put("countrycode", countryCode);
        map.put("time", System.currentTimeMillis());
        HttpJsonTask task = new HttpJsonTask(context)
                .setParams(Configuration.recommend, map, new RequestCallback() {
                    @Override
                    public void onSuccess(String data) {
                        try {
                            JSONObject obj = new JSONObject(data);
                            JSONArray jsonArray = (JSONArray) obj.get("articles");
                            ArrayList<ArticleListBean> list = JsonUtil.parseArray(jsonArray.toString(), ArticleListBean.class);
                            List<ArticleListBean> articleListBeans = handleData(list);
                            Message message = mHandler.obtainMessage();
                            message.what = GETRECOMMEND_SUCCESS;
                            message.obj = articleListBeans;
                            mHandler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                    }
                });
        ThreadPoolExecutorUtils.getInstance().execute(task);
    }

    private static List<ArticleListBean> handleData(List<ArticleListBean> list) {
        List<ArticleListBean> data = new ArrayList<>();
        for (ArticleListBean b : list) {
            String[] imgs = b.getTitlepic().split("\\|");
            if (imgs.length > 0) {
                if (imgs.length >= 3) {
                    b.setItemType(THREE_IMG);
                } else {
                    if (b.getCtype() == 1) {
                        b.setItemType(BIG_IMG);
                    } else {
                        b.setItemType(IMG);
                    }
                }
                b.setImgs(imgs);
            } else {
                b.setItemType(TEXT);
            }
            data.add(b);
        }
        return data;
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
//        progress.startAnimation(AnimationUtils.loadAnimation(context,R.anim.pull_pro_anim));
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

