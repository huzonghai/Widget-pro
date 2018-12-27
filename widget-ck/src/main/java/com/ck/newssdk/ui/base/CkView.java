package com.ck.newssdk.ui.base;
import android.app.Activity;

import com.ck.newssdk.beans.CategoryBean;

import java.util.List;

public interface CkView {

    interface View extends BaseView {
        void onGetTopics(List<CategoryBean> categoryBeanList);
    }

    interface CkContact{
        void getTopics(Activity activity, String country);
    }
}
