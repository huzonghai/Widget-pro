package com.ck.newssdk.http;

/**
 * Created by yt on 2016/12/1.
 */

public interface RequestCallback {
    void onSuccess(String data);

    void onFailure(String message);
}
