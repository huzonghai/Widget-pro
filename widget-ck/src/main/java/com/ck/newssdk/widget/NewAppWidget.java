package com.ck.newssdk.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.ck.newssdk.R;
import com.ck.newssdk.beans.ArticleListBean;
import com.ck.newssdk.config.Configuration;
import com.ck.newssdk.http.RequestCallback;
import com.ck.newssdk.ui.web.WebActivity;
import com.ck.newssdk.utils.DeviceUtils;
import com.ck.newssdk.utils.Iml;
import com.ck.newssdk.utils.JsonUtil;
import com.ck.newssdk.utils.ThreadPoolExecutorUtils;
import com.ck.newssdk.widget.api.HttpJsonTask;

import org.json.JSONArray;
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
    public static final String LOAD_MORE_NEWS_WIDGET = "load_more_news_widget";
    public static final String COLLECTION_VIEW_ACTION = "collection_view_action";

    public static final String CARD_FORM_ACT = "card_for_act";

    public static final String COLLECTION_VIEW_EXTRA = "collection_view_extra";
    public static final String COLLECTION_VIEW_BEAN_EXTRA = "collection_view_bean_extra";

    private static Context mContext;
    private final static int GETRECOMMEND_SUCCESS = 3;
    private final static int GETRECOMMEND_FAIL = 4;
    private final static int WEATHER_SUCCESS = 5;
    private final static int WEATHER_FAIL = 6;
    private static boolean isRefresh = false;
    private static RemoteViews remoteViews;
    private static AppWidgetManager appWidgetManager;
    private static ComponentName componentName;

    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case GETRECOMMEND_SUCCESS:
                    //设置数据
                    ListRemoteViewsFactory.setArticleData((List<ArticleListBean>) msg.obj);

                    appWidgetManager = AppWidgetManager.getInstance(mContext);
                    componentName = new ComponentName(mContext, NewAppWidget.class);
                    remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.new_app_widget);
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);

                    if (isRefresh) {
                        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(componentName), R.id.lv_news);
                        hideLoading(mContext);
                        isRefresh = false;
                    }
                    Intent serviceIntent = new Intent(mContext, ListWidgetService.class);
                    remoteViews.setRemoteAdapter(R.id.lv_news, serviceIntent);

                    Intent listIntent = new Intent();
                    listIntent.setAction(COLLECTION_VIEW_ACTION);
//                    listIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                    listIntent.setComponent(new ComponentName(mContext, NewAppWidget.class));
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, listIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    remoteViews.setPendingIntentTemplate(R.id.lv_news, pendingIntent);
//                    appWidgetManager.updateAppWidget(componentName, remoteViews);
                    appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
                    break;
                case GETRECOMMEND_FAIL:
                    hideLoading(mContext);
                    Toast.makeText(mContext, "Refresh requires network", Toast.LENGTH_SHORT).show();
                    break;
                case WEATHER_SUCCESS:
                    appWidgetManager = AppWidgetManager.getInstance(mContext);
                    componentName = new ComponentName(mContext, NewAppWidget.class);
                    remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.new_app_widget);
                    Weather weather = (Weather) msg.obj;
                    remoteViews.setImageViewBitmap(R.id.imgv_weather, weather.bitmap);
                    remoteViews.setTextViewText(R.id.tv_weather_centigrade, weather.temp);
                    remoteViews.setTextViewText(R.id.tv_weather_week, weather.cityname);
                    appWidgetManager.updateAppWidget(componentName, remoteViews);
                    break;
                case WEATHER_FAIL://展示默认图标

                    break;
                default:
            }
        }
    };


    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        //搜索
        Intent serach = new Intent().setAction(SEARCH_WIDGET);
        serach.setComponent(new ComponentName(context, NewAppWidget.class));
        PendingIntent pendingIntentser = PendingIntent.getBroadcast(context, 0, serach, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.re_search, pendingIntentser);
        //刷新
        Intent refresh = new Intent().setAction(REFRESH_WIDGET);
        refresh.setComponent(new ComponentName(context, NewAppWidget.class));
        PendingIntent pendingIntentre = PendingIntent.getBroadcast(context, 0, refresh, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.tv_refresh, pendingIntentre);
        //加载更多资讯
        Intent loadnews = new Intent().setAction(LOAD_MORE_NEWS_WIDGET);
        loadnews.setComponent(new ComponentName(context, NewAppWidget.class));
        PendingIntent pendingIntentload = PendingIntent.getBroadcast(context, 0, loadnews, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.tv_more, pendingIntentload);
        //卡片管理
        Intent card = new Intent().setAction(CARD_WIDGET);
        card.setComponent(new ComponentName(context, NewAppWidget.class));
        PendingIntent pendingIntentcard = PendingIntent.getBroadcast(context, 0, card, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.tv_card, pendingIntentcard);

        //更新widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private void jumpToWeb(ArticleListBean news) {

        Intent intent = new Intent(mContext, WebActivity.class);
        intent.putExtra("linkurl", news.getLinkurl());
        intent.putExtra("sourceurl", news.getSourceurl());
        intent.putExtra("title", news.getTitle());
        intent.putExtra("articleid", news.getId());
        mContext.startActivity(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        String action = intent.getAction();
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        if (action.equals(COLLECTION_VIEW_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            int index = intent.getIntExtra(COLLECTION_VIEW_EXTRA, 0);
            ArticleListBean articleListBean = (ArticleListBean) intent.getSerializableExtra(COLLECTION_VIEW_BEAN_EXTRA);
            jumpToWeb(articleListBean);
        } else if (action.equals(SEARCH_WIDGET)) {
            String url = "http://s.zlsite.com/?channel=50109";
            Intent startAcIntent = new Intent();
            //launcher:packageName="com.ssui.launcher3"
//            launcher:className="com.ck.newssdk.widget.NewAppWidget"
//            startAcIntent.setComponent(new ComponentName("com.ssui.launcher3", "com.ck.newssdk.widget.SearchAct"));
            startAcIntent.setComponent(new ComponentName("com.ck.widget", "com.ck.newssdk.widget.SearchAct"));
            startAcIntent.putExtra("url", url);
            startAcIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startAcIntent);
        } else if (action.equals(REFRESH_WIDGET)) {
            initListViewData();
            initWeatherData();
            isRefresh = true;
            showLoading(context);
        } else if (action.equals(LOAD_MORE_NEWS_WIDGET)) {
            String countryCode = mContext.getResources().getConfiguration().locale.getCountry();
            Iml.setCountry(countryCode);
            Intent startAcIntent = new Intent();

//            startAcIntent.setComponent(new ComponentName("com.ssui.launcher3", "com.ck.newssdk.ui.CkActivity"));
            startAcIntent.setComponent(new ComponentName("com.ck.widget", "com.ck.newssdk.ui.CkActivity"));
            startAcIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startAcIntent);
        } else if (action.equals(CARD_WIDGET)) {
            Intent startAcIntent = new Intent();
//            startAcIntent.setComponent(new ComponentName("com.ssui.launcher3", "com.ck.newssdk.widget.CardManageAct"));
            startAcIntent.setComponent(new ComponentName("com.ck.widget", "com.ck.newssdk.widget.CardManageAct"));
            startAcIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startAcIntent);
        } else if (action.equals(CARD_FORM_ACT)) {
            if (intent != null) {
                remoteViews.setViewVisibility(R.id.re_search,
                        intent.getBooleanExtra("isCheckSearch", true) == true
                                ? View.VISIBLE : View.GONE);
                remoteViews.setViewVisibility(R.id.re_weather,
                        intent.getBooleanExtra("isCheckWeather", true) == true
                                ? View.VISIBLE : View.GONE);
                appWidgetManager = AppWidgetManager.getInstance(context);
                componentName = new ComponentName(context, NewAppWidget.class);
                appWidgetManager.updateAppWidget(componentName, remoteViews);
            }
        }


        Log.i(TAG, "onReceive: 执行");
        Iml.initIml(mContext);
        super.onReceive(context, intent);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        mContext = context;
        Iml.initIml(mContext);
        //初始化列表数据
        initListViewData();
        //天气
        initWeatherData();
        Log.i(TAG, "onEnabled: 执行");

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            Log.i(TAG, "onUpdate: 执行");
        }
    }

    @Override
    public void onEnabled(Context context) {
        mContext = context;
        Iml.initIml(mContext);
    }

    private void initWeatherData() {
        Executor.execute(new Runnable() {
            @Override
            public void run() {
                String weatherjson = Download.doGet("https://weatherapi.coolook.org/getweather");
                CKLogger.d(weatherjson);
                if (!TextUtils.isEmpty(weatherjson)) {
                    try {
                        JSONObject jsonObject = new JSONObject(weatherjson);
                        int count = jsonObject.optInt("count");
                        JSONArray weather = jsonObject.optJSONArray("data");
                        for (int i = 0; i < weather.length(); i++) {
                        }
                        JSONObject weatherbean = weather.optJSONObject(0);
                        Bitmap bitmap = Download.downloadToBitmap(new StringBuilder()
                                .append("https://weathericon.coolook.org/icon/")
                                .append(weatherbean.optJSONObject("weather").optString("icon"))
                                .append(".png")
                                .toString());
                        Message message = mHandler.obtainMessage();
                        message.what = WEATHER_SUCCESS;
                        Weather weaben = new Weather(bitmap, new StringBuilder()
                                .append(weatherbean.optInt("temp")).
                                        append("℃").toString(),
                                weatherbean.optString("city_name"));
                        message.obj = weaben;
                        mHandler.sendMessage(message);
                    } catch (Exception e) {
                        ;
                    }
                }
            }
        });
    }



    class Weather {
        Bitmap bitmap;
        String temp;
        String cityname;

        public Weather(Bitmap bitmap, String temp, String cityname) {
            this.bitmap = bitmap;
            this.temp = temp;
            this.cityname = cityname;
        }
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
        Iml.setCountry(countryCode);
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
                        } catch (Exception e) {
                            Message messagefail = mHandler.obtainMessage();
                            messagefail.what = GETRECOMMEND_FAIL;
                            mHandler.sendMessage(messagefail);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        Message messagefail = mHandler.obtainMessage();
                        messagefail.what = GETRECOMMEND_FAIL;
                        mHandler.sendMessage(messagefail);
                    }
                });
        ThreadPoolExecutorUtils.getInstance().execute(task);
    }


    /**
     * 显示加载loading
     */
    private static void showLoading(Context context) {
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        remoteViews.setViewVisibility(R.id.tv_refresh, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.progress_bar, View.VISIBLE);
        remoteViews.setTextViewText(R.id.tv_refresh, "正在刷新...");
        refreshWidget(context, remoteViews, false);
    }


    private static void hideLoading(Context context) {
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
//        progress.startAnimation(AnimationUtils.loadAnimation(context,R.anim.pull_pro_anim));
        remoteViews.setViewVisibility(R.id.progress_bar, View.GONE);
        remoteViews.setTextViewText(R.id.tv_refresh, "刷新");
        refreshWidget(context, remoteViews, false);
    }


    private static void refreshWidget(Context context, RemoteViews remoteViews, boolean refreshList) {
        appWidgetManager = AppWidgetManager.getInstance(context);
        componentName = new ComponentName(context, NewAppWidget.class);
        appWidgetManager.updateAppWidget(componentName, remoteViews);
        if (refreshList) {
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(componentName), R.id.lv_news);
        }
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

}

