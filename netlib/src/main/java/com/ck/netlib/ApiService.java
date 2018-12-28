package com.ck.netlib;

import com.ck.netlib.beans.ArticleListBean;
import com.ck.netlib.beans.CategoryBean;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    //文章推荐
    public static final String recommend = "/v4/conarticle/content/recommend";

    //获取分类文章
    public static final String catagory = "/v4/conarticle/content/list";

    //文章详情
    public static final String articleDetail = "/v4/conarticle/content/articlecontent";

    //根据IP获取当前国家分类
    //    public static final String curCountry = "http://api-center.coolook.org/v3/system/querytopicBycountry.json";

    //当前国家话题
    public static final String curCountry = "/v4/country/findTopicsByCode";


    @GET("/v4/conarticle/content/recommend")
    Observable<ArticleListBean>
    recommend(@Query("uuid") String uuid, @Query("countrycode") String countrycode, @Query("time") long time);

    @GET("/v4/country/findTopicsByCode")
    Observable<CategoryBean>
    findTopicsByCode(@Query("countrycode") String countrycode);

}
