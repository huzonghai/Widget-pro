package com.ck.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.ck.newssdk.Ck;
import com.ck.newssdk.beans.ArticleListBean;
import com.ck.newssdk.beans.CategoryBean;
import com.ck.newssdk.config.Configuration;
import com.ck.newssdk.ui.article.ArticlePresenter;
import com.ck.newssdk.ui.article.ArticleView;
import com.ck.newssdk.ui.base.CkPresenter;
import com.ck.newssdk.ui.base.CkView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CkView.View, ArticleView.View {
    private Button mB_loc, mB_test;
    private WebView mWebView;
    public static final String TAG = "Host_widget";
    private WebSettings mWebSettings;
    private int topicid;
    private CkPresenter presenter;
    private ArticlePresenter mArticlePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mB_loc = findViewById(R.id.bt_loc);
        mB_test = findViewById(R.id.bt_test);
        mWebView = findViewById(R.id.webview);
        mB_loc.setOnClickListener(this);
        mB_test.setOnClickListener(this);
        initWidgetUIData();

        Intent intent = getIntent();
        if (intent != null) {
            String url = intent.getStringExtra("url");
            if (!TextUtils.isEmpty(url)) {
                Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
                mWebSettings = mWebView.getSettings();
                mWebView.loadUrl(url);
                mWebSettings.setJavaScriptEnabled(true);
                mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
                mWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
                Log.i(TAG, url);
            }
        }

    }

    private void initWidgetUIData() {
        String countryCode = getResources().getConfiguration().locale.getCountry();
        Ck.init(MainActivity.this, countryCode);
        presenter = new CkPresenter(this);
        presenter.getTopics(this, Configuration.CurrentCountry);
        mArticlePresenter = new ArticlePresenter(this);


    }

    @Override
    public void onGetTopics(List<CategoryBean> categoryBeanList) {
        topicid = categoryBeanList.get(0).getTopicid();
        System.out.println("MainActivity.onGetTopics    " + topicid);
        if (topicid == 114) {
            mArticlePresenter.getRecommend(this, Configuration.CurrentCountry);
        } else {
            mArticlePresenter.getArticleList(this, 0, Configuration.CurrentCountry, topicid, 0, 0);
        }
    }

    @Override
    public void fail(String msg) {
        Log.e("ck", "fail: --->> " + msg);
    }

    @Override
    public void onGetArticle(List<ArticleListBean> articleListBean) {
        ListRemoteViewsFactory.setArticleData(articleListBean);
//        System.out.println("MainActivity.onGetArticle   " + articleListBean.size() + "      " + articleListBean.toString());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.bt_loc:
                Ck.init(MainActivity.this, getResources().getConfiguration().locale.getCountry());
                Ck.open(MainActivity.this);

                break;
            case R.id.bt_test:
                NewAppWidget newAppWidget = new NewAppWidget();
                newAppWidget.onReceive(MainActivity.this, new Intent("TOP"));

                break;
            default:
        }
    }


}
