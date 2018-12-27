package com.ck.newssdk.ui.web;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ck.newssdk.R;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by wy on 2017/8/28.
 */

public abstract class BaseWebActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    protected LinearLayout mContent;
    protected ImageView mBackBtn;
    protected ImageView mSelectTextSizeBtn;
    protected TextView mTitleUrl;
    protected CheckBox mSelectUrl;

    public String coolookURL = "";
    public String oraginURL = "";
    public boolean isBrowseMode;//HomeActivity中拿到是否是快速浏览模式
    public boolean isUrl;
    public WebDialog webDialog;

    //通用Context
    protected Context context;
//    protected WebBrowsingSelectDialog browsingSelectDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_web_title_lauout);
        initBase();
        context = this;
    }

    private void initBase() {
        mContent = findViewById(R.id.content);
        mBackBtn = findViewById(R.id.web_title_bar_back_btn);
        mSelectTextSizeBtn = findViewById(R.id.web_select_text_size);
        mTitleUrl = findViewById(R.id.web_title_url);
        mSelectUrl = findViewById(R.id.web_select_url);

        mBackBtn.setOnClickListener(this);
        mSelectTextSizeBtn.setOnClickListener(this);
        mTitleUrl.setOnClickListener(this);
        mSelectUrl.setOnCheckedChangeListener(this);

        webDialog = new WebDialog(this);

        if (getContentViewId() != 0) {
            mContent.removeAllViews();
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(getContentViewId(), null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            mContent.addView(view, params);
        }
        registerBus();
    }

    //布局文件ID
    protected abstract int getContentViewId();

    /**
     * 如果要用到事件总线时，在这里注册
     * RxBus.get().register(this);
     * 注册了，那么必须取消
     */
    protected abstract void registerBus();

    /**
     * 取消RxBus
     * RxBus.get().unregister(this);
     * Presenter解除绑定的view也可以在这里
     */
    protected abstract void unregisterBus();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBus();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.web_select_url) {
            if (isChecked) {
//                mTitleUrl.setText(getHostUrl(coolookURL));
                mTitleUrl.setText(getHostUrl(oraginURL));
                isUrl = true;
            } else {
                mTitleUrl.setText(getHostUrl(oraginURL));
                isUrl = false;
            }
        }
    }

    /**
     * 获取完整的域名
     *
     * @param text url
     */
    public String getHostUrl(String text) {
        String host = "";
        try {
            URL url = new URL(text);
            host = url.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return host;
    }

    /**
     * 错误提示
     *
     * @param error
     */
    public void fail(String error) {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
