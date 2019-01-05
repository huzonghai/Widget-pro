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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mB_loc, mB_test;
    private WebView mWebView;
    public static final String TAG = "Host_widget";
    private WebSettings mWebSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mB_loc = findViewById(R.id.bt_loc);
        mB_test = findViewById(R.id.bt_test);
        mWebView = findViewById(R.id.webview);
        mB_loc.setOnClickListener(this);
        mB_test.setOnClickListener(this);
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


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.bt_loc:
                Ck.init(MainActivity.this, "us");
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
