package com.ck.newssdk.http;

import android.app.Activity;

import com.ck.newssdk.api.SDKCallBack;
import com.ck.newssdk.beans.ArticleListBean;
import com.ck.newssdk.beans.CategoryBean;
import com.ck.newssdk.config.Configuration;
import com.ck.newssdk.utils.DeviceUtils;
import com.ck.newssdk.utils.JsonUtil;
import com.ck.newssdk.utils.ThreadPoolExecutorUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ck.newssdk.config.Configuration.curCountry;

public class ApiServer {
    private static volatile ApiServer instance = null;

    private ApiServer() {

    }

    public static ApiServer getInstance() {
        if (instance == null) {
            synchronized (ApiServer.class) {
                if (instance == null) {
                    instance = new ApiServer();
                }
            }
        }
        return instance;
    }

    /**
     * 获取当前国家话题
     *
     * @param country
     * @return
     */
    public void getCategory(Activity activity, String country, final SDKCallBack.CategoryCallBack callBack) {
        Map map = new HashMap();
        map.put("countrycode", country);
        HttpJsonTask task = new HttpJsonTask(activity).setParams(curCountry, map, new RequestCallback() {
            @Override
            public void onSuccess(String json) {
                try {
                    JSONObject obj = new JSONObject(json);
                    JSONArray jsonArray = (JSONArray) obj.get("topics");
                    ArrayList<CategoryBean> list = JsonUtil.parseArray(jsonArray.toString(), CategoryBean.class);
                    callBack.onSuccess(list);
                } catch (JSONException e) {
                    callBack.onFailure(e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {
                callBack.onFailure(message);
            }
        });
        ThreadPoolExecutorUtils.getInstance().execute(task);
    }

    /**
     * 获取当前国家推荐
     *
     * @param countryCode
     * @param callBack
     */
    public void getRecommend(Activity activity, String countryCode, final SDKCallBack.ItemListCallBack callBack) {
        Map map = new HashMap();
        map.put("uuid", DeviceUtils.getUUID(activity));
        map.put("countrycode", countryCode);
        map.put("time", System.currentTimeMillis());
        HttpJsonTask task = new HttpJsonTask(activity)
                .setParams(Configuration.recommend, map, new RequestCallback() {
                    @Override
                    public void onSuccess(String data) {
                        try {
                            JSONObject obj = new JSONObject(data);
                            JSONArray jsonArray = (JSONArray) obj.get("articles");
                            ArrayList<ArticleListBean> list = JsonUtil.parseArray(jsonArray.toString(), ArticleListBean.class);
                            callBack.onSuccess(list);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        callBack.onFailure(message);
                    }
                });
        ThreadPoolExecutorUtils.getInstance().execute(task);
    }

    public void getArticleList(Activity activity, int direct, String country, int topicid, long max, long min, final SDKCallBack.ItemListCallBack callBack) {
        Map map = new HashMap();
        map.put("direct", direct);
        map.put("country", country);
        map.put("topicsid", topicid);
        map.put("attr", 1);
        map.put("max", max);
        map.put("min", min);
//        map.put("keywords", "");
        HttpJsonTask task = new HttpJsonTask(activity).setParams(Configuration.catagory, map, new RequestCallback() {
            @Override
            public void onSuccess(String json) {
                try {
                    JSONObject obj = new JSONObject(json);
                    JSONArray jsonObj = (JSONArray) obj.get("articles");
                    ArrayList<ArticleListBean> list = JsonUtil.parseArray(
                            jsonObj.toString(), ArticleListBean.class);
                    callBack.onSuccess(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {
                callBack.onFailure(message);
            }
        });
        ThreadPoolExecutorUtils.getInstance().execute(task);
    }

    /**
     * 相关文章
     *
     * @param articleid
     * @param callBack
     */
    public void getItemDetails(Activity activity, long articleid, final SDKCallBack.ItemListCallBack callBack) {
        Map map = new HashMap();
        map.put("articleid", articleid);

        HttpJsonTask task = new HttpJsonTask(activity).setParams(Configuration.articleDetail, map, new RequestCallback() {
            @Override
            public void onSuccess(String json) {
                try {
                    JSONObject obj = new JSONObject(json);
                    JSONObject object = obj.getJSONObject("conarticlecontentvo");
                    JSONArray jsonObj = (JSONArray) object.get("list");
                    ArrayList<ArticleListBean> list = JsonUtil.parseArray(
                            jsonObj.toString(), ArticleListBean.class);
                    callBack.onSuccess(list);
                } catch (JSONException e) {
                    callBack.onFailure(e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {
                callBack.onFailure(message);

            }
        });
        ThreadPoolExecutorUtils.getInstance().execute(task);
    }
}
