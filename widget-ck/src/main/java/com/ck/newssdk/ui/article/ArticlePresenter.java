package com.ck.newssdk.ui.article;

import android.app.Activity;

import com.ck.newssdk.api.SDKCallBack;
import com.ck.newssdk.beans.ArticleListBean;
import com.ck.newssdk.http.ApiServer;

import java.util.ArrayList;
import java.util.List;

import static com.ck.newssdk.ui.article.MultipleItemAdapter.BIG_IMG;
import static com.ck.newssdk.ui.article.MultipleItemAdapter.IMG;
import static com.ck.newssdk.ui.article.MultipleItemAdapter.TEXT;
import static com.ck.newssdk.ui.article.MultipleItemAdapter.THREE_IMG;

public class ArticlePresenter implements ArticleView.CkContact {

    private ArticleView.View view;

    public ArticlePresenter(ArticleView.View view) {
        this.view = view;
    }

    @Override
    public void getRecommend(final Activity activity, String country) {
        ApiServer.getInstance().getRecommend(activity, country, new SDKCallBack.ItemListCallBack() {
            @Override
            public void onSuccess(ArrayList<ArticleListBean> list) {
                final List<ArticleListBean> articleListBeans = handleData(list);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.onGetArticle(articleListBeans);
                    }
                });
            }

            @Override
            public void onFailure(String msg) {
                view.fail(msg);
            }
        });
    }

    @Override
    public void getArticleList(final Activity activity, int direct, String country, int topicid, long max, long min) {
        ApiServer.getInstance().getArticleList(activity, direct, country, topicid, max, min, new SDKCallBack.ItemListCallBack() {
            @Override
            public void onSuccess(ArrayList<ArticleListBean> list) {
                final List<ArticleListBean> articleListBeans = handleData(list);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.onGetArticle(articleListBeans);
                    }
                });

            }

            @Override
            public void onFailure(String msg) {
                view.fail(msg);
            }
        });

    }

    private List<ArticleListBean> handleData(List<ArticleListBean> list) {
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
