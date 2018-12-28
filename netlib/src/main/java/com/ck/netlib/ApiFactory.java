package com.ck.netlib;

public enum  ApiFactory {
    INSTANCE;

    private final ApiService apiService;

    ApiFactory() {
        apiService=RetrofitClient.INSTANCE.getRetrofit().create(ApiService.class);
    }

    public ApiService getApiService() {
        return apiService;
    }
}
