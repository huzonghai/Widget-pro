package com.ck.newssdk.config;


/**
 * Created by wy
 */

public class Configuration {

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

    //appid
    public static final String API_SERVICE_ID = "1";
    //sign key
    public static final String API_SERVICE_KEY = "aefewfrvgerb";//deasjk12e12!@#dss

//    public static final String API_DES_KEY = "!Q#E%T&U(O";

    public static final String API = "https://api-data.coolook.org/";  //APP 的正式环境
    //    public static final String api = "http://192.168.1.20:9999/";  // APP 的测试环境
//    public static final String IMAGE = "http://cdn.img.coolook.org";//图片

    public static String CurrentCountry = "us";
}
