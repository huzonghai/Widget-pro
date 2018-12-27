package com.ck.newssdk.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ck.newssdk.beans.CategoryBean;
import com.ck.newssdk.config.Configuration;
import com.ck.newssdk.ui.base.BaseActivity;
import com.ck.newssdk.ui.base.CkPresenter;
import com.ck.newssdk.ui.base.CkView;

import java.util.List;

public class CkActivity extends BaseActivity implements CkView.View {
    private CkPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CkPresenter(this);
        presenter.getTopics(this, Configuration.CurrentCountry);
    }

    @Override
    public void onGetTopics(List<CategoryBean> categoryBeanList) {
        for (CategoryBean b : categoryBeanList) {
            int topicid = b.getTopicid();
            if (topicid != 115) {
                topics.add(b.getTopicname());
                fragments.add(CkFragment.newInstance(topicid));
            }
        }
        initNavigator();
    }

    @Override
    public void fail(String msg) {
        Log.e("ck", "fail: --->> " + msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplication(), "Please check your network and Retry !", Toast.LENGTH_SHORT).show();
                noNet.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter = null;
        if (fragments != null) {
            fragments.clear();
            fragments = null;
        }
        if (topics != null) {
            topics.clear();
            topics = null;
        }
    }
}
