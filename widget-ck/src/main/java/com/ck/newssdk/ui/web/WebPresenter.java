package com.ck.newssdk.ui.web;


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

public class WebPresenter implements Web.WebContact {
    private Web.View v;

    public WebPresenter(Web.View v) {
        this.v = v;
    }

    @Override
    public void getItemDetails(final Activity activity, long articleid) {

        ApiServer.getInstance().getItemDetails(activity, articleid, new SDKCallBack.ItemListCallBack() {
            @Override
            public void onSuccess(ArrayList<ArticleListBean> list) {
                final List<ArticleListBean> beans = handleData(list);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        v.onGetItemDetails(beans);
                    }
                });
            }

            @Override
            public void onFailure(String msg) {
                v.fail(msg);
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
