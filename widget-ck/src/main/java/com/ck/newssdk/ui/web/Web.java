package com.ck.newssdk.ui.web;


import android.app.Activity;

import com.ck.newssdk.beans.ArticleListBean;
import com.ck.newssdk.ui.base.BaseView;

import java.util.List;

/**
 * Created by wy on 2017/8/23.
 */

public interface Web {
    interface View extends BaseView {
        void onGetItemDetails(List<ArticleListBean> beans);
    }

    interface WebContact {
        void getItemDetails(Activity activity, long articleid);
    }
}
