package com.ck.netlib;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ck.netlib.utils.Config.API;

enum RetrofitClient {
    INSTANCE;
    private Retrofit retrofit;

    RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .client(OkHttpFactory.INSTANCE.getOkHttpClient())
                .baseUrl(API)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public Retrofit getRetrofit() {
        return retrofit;
    }
}
