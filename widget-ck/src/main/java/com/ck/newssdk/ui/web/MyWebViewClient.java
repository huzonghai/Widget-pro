package com.ck.newssdk.ui.web;

import android.app.Activity;
import android.graphics.Bitmap;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by wy on 2017/8/25.
 */

public class MyWebViewClient extends WebViewClient {

    private Activity mContext;
    private MyProgressDialog progressDialog;
    private OnWebProgressDismiss onWebProgressDismiss;
    private boolean isCoolook;

    public void setOnWebProgressDismiss(OnWebProgressDismiss onWebProgressDismiss) {
        this.onWebProgressDismiss = onWebProgressDismiss;
    }

    public MyWebViewClient(Activity context, boolean isCoolook) {
        mContext = context;
        this.isCoolook = isCoolook;
        progressDialog = new MyProgressDialog(mContext);
    }

    //Webiew显示网页
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return super.shouldOverrideUrlLoading(view, request);
    }

    //开始载入页面调用的，我们可以设定一个loading的页面，告诉用户程序在等待网络响应
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (!mContext.isFinishing()) {
            //设定加载开始的操作
            progressDialog.show();
        }
    }

    //在页面加载结束时调用。我们可以关闭loading 条，切换程序动作
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (!mContext.isFinishing()) {
            //设定加载结束的操作
            progressDialog.dismiss();
            if (isCoolook) {
                onWebProgressDismiss.onWebProgressDismiss();
            }
        }
    }

    //在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次
    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
        //设定加载资源的操作
    }

    //加载页面的服务器出现错误时（如404）调用
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
    }

    interface OnWebProgressDismiss {
        void onWebProgressDismiss();
    }

}
