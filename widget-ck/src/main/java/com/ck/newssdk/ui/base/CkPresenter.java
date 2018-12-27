package com.ck.newssdk.ui.base;

import android.app.Activity;

import com.ck.newssdk.api.SDKCallBack;
import com.ck.newssdk.beans.CategoryBean;
import com.ck.newssdk.http.ApiServer;

import java.util.ArrayList;

public class CkPresenter implements CkView.CkContact {
    private CkView.View v;

    public CkPresenter(CkView.View v) {
        this.v = v;
    }

    @Override
    public void getTopics(final Activity activity, String country) {
        ApiServer.getInstance().getCategory(activity, country, new SDKCallBack.CategoryCallBack() {
            @Override
            public void onSuccess(final ArrayList<CategoryBean> list) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (list.size() != 0) {
                            v.onGetTopics(list);
                        }
                    }
                });
            }

            @Override
            public void onFailure(String msg) {
                v.fail(msg);
            }
        });

    }
}
