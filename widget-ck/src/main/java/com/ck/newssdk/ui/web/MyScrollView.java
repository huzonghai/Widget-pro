package com.ck.newssdk.ui.web;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * wy
 */
public class MyScrollView extends ScrollView {
    private OnScrollListener onScrollListener;

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (onScrollListener != null && t > oldl) {
            onScrollListener.onScroll();
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    interface OnScrollListener {
        void onScroll();
    }

}