package com.ck.newssdk.ui.web;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ck.newssdk.R;
import com.ck.newssdk.base.BaseQuickAdapter;
import com.ck.newssdk.beans.ArticleListBean;
import com.ck.newssdk.config.Configuration;
import com.ck.newssdk.ui.article.MultipleItemAdapter;
import com.ck.newssdk.utils.ConvertUtils;
import com.ck.newssdk.utils.DeviceUtils;
import com.ck.newssdk.utils.NetworkUtils;
import com.ck.newssdk.utils.SharePDataBaseUtils;

import java.util.ArrayList;
import java.util.List;


public class WebActivity extends BaseWebActivity implements WebDialog.OnWebDialogListener, Web.View, BaseQuickAdapter.OnItemClickListener {

    private CheckBox webSelectBrowsingMode;
    private WebView webWebViewCoolook;
    private WebView webWebViewOragin;
    private MyScrollView webScrollView;
    private CheckedTextView webQuickBorwsingBtn;
    private RelativeLayout adLayout;
    private RecyclerView webRelatedRecycler;
    private LinearLayout webRelatedLayout;
    private TextView webLoadMoreRelatedBtn;
    private ImageView goHome;

    private WebSettings webSettings;
    private boolean isCoolook = true;
    private boolean isOraginFinish = false;
    private boolean isQuick;
    private WebPresenter webPresenter;
    private MyWebViewClient webClient;
    private String webTitle;
    private int articleid;

    private List<ArticleListBean> data, list;

    private MultipleItemAdapter adapter;
    private int page = 3;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_web;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        coolookURL = intent.getStringExtra("linkurl");
        oraginURL = intent.getStringExtra("sourceurl");
        webTitle = intent.getStringExtra("title");
        articleid = intent.getIntExtra("articleid", 0);

        initView();
        initBase();
        initData();

        webPresenter = new WebPresenter(this);

        webPresenter.getItemDetails(this, articleid);

        LinearLayoutManager layout = new LinearLayoutManager(this);

        layout.setSmoothScrollbarEnabled(true);
        layout.setAutoMeasureEnabled(true);

        webRelatedRecycler.setLayoutManager(layout);
        webRelatedRecycler.setHasFixedSize(true);
        webRelatedRecycler.setNestedScrollingEnabled(false);

        list = new ArrayList<>();
        data = new ArrayList<>();
        adapter = new MultipleItemAdapter(list);
        webRelatedRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    private void initView() {
        webSelectBrowsingMode = findViewById(R.id.web_select_browsing_mode);
        webWebViewCoolook = findViewById(R.id.web_web_view_coolook);
        webWebViewOragin = findViewById(R.id.web_web_view_oragin);
        webScrollView = findViewById(R.id.web_scroll_view);
        webQuickBorwsingBtn = findViewById(R.id.web_quick_borwsing_btn);
        adLayout = findViewById(R.id.ad_container);
        webRelatedRecycler = findViewById(R.id.web_related_recycler);
        webRelatedLayout = findViewById(R.id.web_related_layout);
        webLoadMoreRelatedBtn = findViewById(R.id.web_load_more_related_btn);
        goHome = findViewById(R.id.web_back_home_btn);

//        webShareLayout.setOnClickListener(this);
        webQuickBorwsingBtn.setOnClickListener(this);
        goHome.setOnClickListener(this);
        webLoadMoreRelatedBtn.setOnClickListener(this);
    }

    private void initBase() {
        isBrowseMode = SharePDataBaseUtils.getIsBrowseMode(this);
        initWebSetting();
        webClient = new MyWebViewClient(this, true);
        webWebViewCoolook.setWebViewClient(webClient);

        setFontSize(SharePDataBaseUtils.getWebTextSize(this));

        webSelectBrowsingMode.setOnCheckedChangeListener(this);

        //ScrollView滑动监听
        webScrollView.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll() {
                if (isCoolook) {
                    if (isOraginFinish) {
                        scroll();
                        mSelectUrl.setChecked(false);
                        isOraginFinish = false;
                    } else {
                        scroll();
                        mSelectUrl.setChecked(true);
                        webWebViewCoolook.setVisibility(View.VISIBLE);
                        webWebViewOragin.setVisibility(View.GONE);
                    }
                    isCoolook = false;
                }
            }
        });

        //coolook完成加载回调，显示相关文章、分享
        webClient.setOnWebProgressDismiss(new MyWebViewClient.OnWebProgressDismiss() {
            @Override
            public void onWebProgressDismiss() {
                isQuick = true;
                adLayout.setVisibility(View.VISIBLE);
                webRelatedLayout.setVisibility(View.VISIBLE);

                webWebViewCoolook.setVisibility(View.VISIBLE);
                webWebViewOragin.setVisibility(View.GONE);

                if (isBrowseMode && oraginURL != null) {
                    webWebViewOragin.loadUrl(oraginURL);
                }
            }
        });

        //原网页加载完成监听
        webWebViewOragin.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isOraginFinish = true;
                if (isCoolook) {
                    scroll();
                    mSelectUrl.setChecked(false);
                    adLayout.setVisibility(View.GONE);
                    webRelatedLayout.setVisibility(View.GONE);

                    webWebViewCoolook.setVisibility(View.GONE);
                    webWebViewOragin.setVisibility(View.VISIBLE);
                }
            }
        });

        webDialog.setListener(this);
    }

    private void initData() {
        if (oraginURL != null) {
            if (!oraginURL.equals("")) {
                mTitleUrl.setText(getHostUrl(oraginURL));
            }
        }
        if (isBrowseMode) {
            scroll();
            mSelectUrl.setChecked(true);
            webWebViewCoolook.setVisibility(View.VISIBLE);
            webWebViewOragin.setVisibility(View.GONE);
            webQuickBorwsingBtn.setChecked(true);
            isQuick = true;
        }
//        else {
//            //弹出一次选择浏览模式弹窗
//            if (!SharePDataBaseUtils.getIsFirstShowDialog(this)) {
//                SharePDataBaseUtils.saveIsFirstShowDialog(this, true);
//                browsingSelectDialog = new WebBrowsingSelectDialog(this);
//                browsingSelectDialog.show(getSupportFragmentManager(), "webDialog");
//                browsingSelectDialog.setDialogMiss(this);
//            }
//        }
        webWebViewCoolook.loadUrl(coolookURL);
    }

    /**
     * 双CheckBox联动
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        super.onCheckedChanged(buttonView, isChecked);
        if (buttonView.getId() == R.id.web_select_browsing_mode && isChecked) {
            SharePDataBaseUtils.saveIsBrowseMode(getApplicationContext(), true);
        }
        changeView(isChecked);
    }

    private void changeView(boolean isChecked) {
        if (isChecked) {
            scroll();
            mSelectUrl.setChecked(true);
            webWebViewCoolook.setVisibility(View.VISIBLE);
            webWebViewOragin.setVisibility(View.GONE);
            webQuickBorwsingBtn.setChecked(true);
            if (isQuick) {
                adLayout.setVisibility(View.VISIBLE);
                webRelatedLayout.setVisibility(View.VISIBLE);
            }
        } else {
            webWebViewCoolook.setVisibility(View.GONE);
            webWebViewOragin.setVisibility(View.VISIBLE);
            adLayout.setVisibility(View.GONE);
            webRelatedLayout.setVisibility(View.GONE);

            webQuickBorwsingBtn.setChecked(false);
            if (isBrowseMode) {
                webWebViewOragin.loadUrl(oraginURL);
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebSetting() {
        webSettings = webWebViewCoolook.getSettings();
        //将图片调整到适合webview的大小
        webSettings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);
        //支持自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        //设置编码格式
        webSettings.setDefaultTextEncodingName("utf-8");
        // webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        if (NetworkUtils.isConnected(getApplicationContext())) {
            //根据cache-control决定是否从网络上取数据。
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            //没网，则从本地获取，即离线加载
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        webWebViewOragin.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    protected void registerBus() {

    }

    @Override
    protected void unregisterBus() {
        if (webPresenter != null) {
            webPresenter = null;
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
//        if (i == R.id.web_share_layout) {
//            shareArticle();
//        }else
        if (i == R.id.web_quick_borwsing_btn) {
            if (!isQuick) {
                webSelectBrowsingMode.setChecked(true);
                webQuickBorwsingBtn.setChecked(true);
                adLayout.setVisibility(View.VISIBLE);
                webRelatedLayout.setVisibility(View.VISIBLE);
                changeView(true);
                isQuick = true;
            } else {
                mSelectUrl.setChecked(false);
                webQuickBorwsingBtn.setChecked(false);
                changeView(false);
                isQuick = false;
            }

        } else if (i == R.id.web_back_home_btn) {
            finish();
        } else if (i == R.id.web_title_bar_back_btn) {
            if (webWebViewCoolook.canGoBack()) {
                webWebViewCoolook.goBack();
            } else {
                finish();
            }

        } else if (i == R.id.web_select_text_size) {
            webDialog.show();

        } else if (i == R.id.web_title_url) {
            if (isUrl) {
                mTitleUrl.setText(coolookURL);
            } else {
                mTitleUrl.setText(oraginURL);
            }

        } else if (i == R.id.web_load_more_related_btn) {
            if (data.size() > 0) {
                page += 3;

                adapter.setNewData(handleData(page));
            }
        }
    }

    /**
     * 快速浏览模式滑动
     */
    private void scroll() {
        final RelativeLayout.LayoutParams lay2 = (RelativeLayout.LayoutParams) webScrollView.getLayoutParams();
        if (isBrowseMode) {
            lay2.setMargins(lay2.leftMargin, -ConvertUtils.dip2px(110), lay2.rightMargin, lay2.bottomMargin);
            webScrollView.setLayoutParams(lay2);
        } else {
            ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
            anim.setDuration(300);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float currentValue = (float) animation.getAnimatedValue();
                    lay2.setMargins(lay2.leftMargin, -(int) (ConvertUtils.dip2px(110) * currentValue), lay2.rightMargin, lay2.bottomMargin);
                    webScrollView.setLayoutParams(lay2);
                }
            });
            anim.start();
        }
    }

    /**
     * 分享回调
     */
    @Override
    public void onWebShareListener() {
        shareArticle();
    }

    /**
     * webView文字改变大小监听
     *
     * @param fontSize 1小，2中，3大
     */
    @Override
    public void onWebFontSelectListener(int fontSize) {
        setFontSize(fontSize);

        SharePDataBaseUtils.saveWebTextSize(this, fontSize);
    }

    /**
     * 设置webView字体大小
     *
     * @param fontSize
     */
    private void setFontSize(int fontSize) {
        switch (fontSize) {
            case 1:
                webSettings.setTextSize(WebSettings.TextSize.SMALLER);
                break;
            case 2:
                webSettings.setTextSize(WebSettings.TextSize.NORMAL);
                break;
            case 3:
                webSettings.setTextSize(WebSettings.TextSize.LARGER);
                break;
        }
    }


    @Override
    public void onGetItemDetails(List<ArticleListBean> beans) {
        webLoadMoreRelatedBtn.setText("More +");
        data.clear();
        data.addAll(beans);

        adapter.setNewData(handleData(3));

        if (data.size() == 0 || data == null) {
            webLoadMoreRelatedBtn.setText("No More");
        }

//        oraginURL = curArticle.getSourceurl();
//        if (!isBrowseMode) {
//            webWebViewOragin.loadUrl(oraginURL);
//        }

    }

    /**
     * 相关文章一次加载三条
     *
     * @param articles
     * @return
     */
    private List<ArticleListBean> handleData(int articles) {
        int size = data.size();
        list.clear();
        int i1 = articles % 3;
        int i2 = articles / 3;
        if (articles < size) {
            for (int i = 0; i < i2 * 3 + i1; i++) {
                list.add(data.get(i));
            }
        } else {
            webLoadMoreRelatedBtn.setText("No More");
            return data;
        }
        return list;
    }


    /**
     * 点击相关文章
     *
     * @param adapter
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ArticleListBean articlesBean = data.get(position);

        webPresenter.getItemDetails(this, articlesBean.getId());

        webWebViewCoolook.loadUrl(articlesBean.getLinkurl());
        webScrollView.scrollTo(0, 0);
    }

    /**
     * 点击返回上一页面而不是退出浏览器
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webWebViewCoolook.canGoBack()) {
            webWebViewCoolook.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 分享
     */
    public void shareArticle() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
//        if (isUrl) {
        intent.putExtra(Intent.EXTRA_TEXT, webTitle + " ## " + coolookURL +
                "?c=" + DeviceUtils.getChannelCode(this) + "&r=" + Configuration.CurrentCountry + "(from NewsCoolook)");
//        } else {
//            intent.putExtra(Intent.EXTRA_TEXT, webTitle + " ## " + oraginURL);
//        }
        WebActivity.this.startActivity(Intent.createChooser(intent, "选择分享"));
    }

//    /**
//     * 选择浏览方式弹窗消失回调
//     */
//    @Override
//    public void onWebSelectDialogMiss() {
//        webSelectBrowsingMode.setChecked(true);
//    }

    @Override
    protected void onDestroy() {
        if (webWebViewCoolook != null) {
            webWebViewCoolook.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webWebViewCoolook.clearHistory();

            webWebViewCoolook.destroy();
            webWebViewCoolook = null;
        }
        if (webWebViewOragin != null) {
            webWebViewOragin.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webWebViewOragin.clearHistory();

            webWebViewOragin.destroy();
            webWebViewOragin = null;
        }
        super.onDestroy();
    }
}
