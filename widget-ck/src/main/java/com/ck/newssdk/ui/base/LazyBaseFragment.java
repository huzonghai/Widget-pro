package com.ck.newssdk.ui.base;

import android.support.v4.app.Fragment;
import android.view.View;


public abstract class LazyBaseFragment extends Fragment {
    public View view;

    protected boolean isVisible ;

    public abstract void initData();

    //先于oncreatview执行的方法
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {
    }

    protected abstract void lazyLoad();

}