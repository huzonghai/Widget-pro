package com.ck.newssdk.api;

import com.ck.newssdk.beans.ArticleListBean;
import com.ck.newssdk.beans.CategoryBean;

import java.util.ArrayList;

/**
 * Created by mackson on 2017/7/15.
 */

public class SDKCallBack {

    public interface ItemListCallBack {
        void onSuccess(ArrayList<ArticleListBean> list);

        void onFailure(String msg);
    }


    public interface CategoryCallBack {
        void onSuccess(ArrayList<CategoryBean> list);

        void onFailure(String msg);
    }

}
