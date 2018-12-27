package com.ck.newssdk.ui.article;
import android.app.Activity;

import com.ck.newssdk.beans.ArticleListBean;
import com.ck.newssdk.ui.base.BaseView;

import java.util.List;

public interface ArticleView {

    interface View extends BaseView {
        void onGetArticle(List<ArticleListBean> articleListBean);
    }

    interface CkContact{
        void getRecommend(Activity activity, String country);
        void getArticleList(Activity activity, int direct, String country, int topicid, long max, long min);
    }
}
