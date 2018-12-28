package com.ck.netlib;

import android.support.annotation.NonNull;

import com.ck.netlib.utils.Config;
import com.ck.netlib.utils.EncryptUtils;
import com.ck.netlib.utils.NetworkUtils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        if (NetworkUtils.isConnected()) {
            Request originalRequest = chain.request();

            //添加新参数
            HttpUrl.Builder builder = originalRequest.url().newBuilder()
                    .scheme(originalRequest.url().scheme())
                    .host(originalRequest.url().host());

            String url = builder.build().toString();

            if (!url.contains("sign=")) {
                String sign = Config.API_SERVICE_ID + Config.API_SERVICE_KEY
                        + String.valueOf(System.currentTimeMillis() / 1000 / 60);
                String md5Sign = EncryptUtils.encryptMD5ToString(sign).toLowerCase();
                builder.addEncodedQueryParameter("sign", md5Sign);
                builder.addEncodedQueryParameter("appid", Config.API_SERVICE_ID);
            }

            Request newRequest = originalRequest.newBuilder()
                    .method(originalRequest.method(), originalRequest.body())
                    .url(builder.build())
                    .build();

            Response response = chain.proceed(newRequest);

            String cacheControl = response.header("Cache-Control");

            if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                    cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
                return response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + 10)
                        .build();
            } else {
                return response;
            }
        } else {
            Request request = chain.request();

            if (!NetworkUtils.isOnline()) {
                int maxStale = 60 * 60 * 24 * 28;
                request = request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return chain.proceed(request);
        }

    }
}
