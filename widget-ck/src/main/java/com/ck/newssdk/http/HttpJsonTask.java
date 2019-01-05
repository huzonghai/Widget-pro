package com.ck.newssdk.http;

import android.app.Activity;
import android.util.Log;

import com.ck.newssdk.config.Configuration;
import com.ck.newssdk.utils.EncryptUtils;
import com.ck.newssdk.utils.RequestParamsUtils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by yt on 2017/3/9.
 */

public class HttpJsonTask implements Runnable {

    //    private Handler handler;
    private Activity context;
    //    RequestType requestType;
    private String method;
    private String url;
    private Map params;
    //    private Map headersParams;
    private RequestCallback callback;
    private ParameterOption option;


    public HttpJsonTask setMethod(String method) {
        this.method = method;
        return this;
    }

    public HttpJsonTask setUrl(String url) {
        this.url = url;
        return this;
    }

    public HttpJsonTask setParams(Map params) {
        this.params = params;
        return this;
    }

//    public HttpJsonTask setHeadersParams(Map headersParams) {
//        this.headersParams = headersParams;
//        return this;
//    }

    public HttpJsonTask setCallback(RequestCallback callback) {
        this.callback = callback;
        return this;
    }

    public HttpJsonTask setOption(ParameterOption option) {
        this.option = option;
        return this;
    }

    public HttpJsonTask setParams(String url, final RequestCallback callback) {
        setParams("GET", url, new HashMap(), callback, ParameterOption.getDefaultOption());
        return this;
    }

    public HttpJsonTask setParams(String url, Map params, final RequestCallback callback) {
        setParams("GET", url, params, callback, ParameterOption.getDefaultOption());
        return this;
    }


    public HttpJsonTask setParams(String url, Map params, final RequestCallback callback, ParameterOption option) { //如果只有一个对象，那么就只能通过参数来约束。而不能用成员变量的。 也无法将此对象放到队列中去做并发控制。
        setParams("GET", url, params, callback, option);
        return this;
    }

    public HttpJsonTask setParams(String method, String url, Map params, final RequestCallback callback) {
        setParams(method, url, params, callback, ParameterOption.getDefaultOption());
        return this;
    }

    private void setParams(String method, String url, Map params, final RequestCallback callback, ParameterOption option) {
        this.method = method;
        this.url = Configuration.API + url;
        this.params = params;
        this.callback = callback;
        this.option = option;
    }

//    public enum RequestType {
//        File,
//        Json
//    }

    @Override
    public void run() {
//        switch (requestType) {
//            case File:
//                //doRequestFile();
//                break;
//            case Json:
        doRequest();
//                break;
//        }
    }


    public HttpJsonTask(Activity context) {
        this.context = context;
//        requestType = type;
    }

    synchronized private void doRequest() {

        String time = String.valueOf(System.currentTimeMillis() / 1000 / 60);
        String sign = Configuration.API_SERVICE_ID + Configuration.API_SERVICE_KEY + time;
        String token = EncryptUtils.encryptMD5ToString(sign).toLowerCase();

        params.put("sign", token);
        params.put("appid", Configuration.API_SERVICE_ID);

        if (method.equalsIgnoreCase("Get")) {
            url = url + "?" + RequestParamsUtils.getParams(params);
        }
        HttpsURLConnection connection;
        try {
            URL urlObj = new URL(url);
            System.out.println("HttpJsonTask.doRequest--->   " + url);
            connection = (HttpsURLConnection) urlObj.openConnection();
            connection.setSSLSocketFactory(HttpsFactroy.getSSLSocketFactory(context));

            if (method.equalsIgnoreCase("Get")) {
                connection.setRequestMethod("GET");
            } else {
                connection.setRequestMethod("POST");
            }

            //1.设定通用的Http头信息
            //设置Accept头信息，告诉服务器，客户端能够接受啥样的数据
            // conn.setRequestProperty("Accept", "application/*,text/*,image/*,*/*");
            //2.设置接受的内容压缩编码算法
            //  connection.setRequestProperty("Accept-Encoding", "gzip");
            //设置User-Agent
            // connection.setRequestProperty("User-Agent", USER_AGENT);
            //  connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");

            connection.setConnectTimeout(option.connectTimeout);
            connection.setReadTimeout(option.readTimeout);
            //   connection.setDoOutput(true);
            //    connection.setDoInput(true);
            if (method.equalsIgnoreCase("POST")) {
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(RequestParamsUtils.getParams(params));
                Log.d("HttpTask", RequestParamsUtils.getParams(params));
            }
            if (connection.getResponseCode() == 200) {
                //Log.d("HttpTask", "success");
                InputStream in = connection.getInputStream();
                final String json = InputStreamToString(in);
//                Log.i("HttpTask", "doRequest: -> " + json);
                BaseResponseParser responseParser = option.responseParser;
                responseParser.parseData(json);
                final int finalStatus = responseParser.status;
                final String finalErrorMsg = responseParser.message;
                final String finalJson = responseParser.json;
//                Handler handler = new Handler();
//                if (context != null)
//                    context.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
                if (context != null)
                    if (finalStatus == BaseResponseParser.RESPONSE_OK_STATUS)
                        callback.onSuccess(finalJson);
                    else
                        callback.onFailure(finalErrorMsg);
//                        }
//                    });
            } else {
                final String error = Integer.toString(connection.getResponseCode());
                Log.e("HttpTask", Integer.toString(connection.getResponseCode()));
                Log.e("HttpTask", "fail");
                if (context != null)
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (context != null)
                                callback.onFailure(error);
                        }
                    });
            }


//            else {
//                final String error = Integer.toString(connection.getResponseCode());
//                Log.e("HttpTask", Integer.toString(connection.getResponseCode()));
//                Log.e("HttpTask", "fail");
//                if (context != null)
//                    context.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (context != null)
//                                callback.onFailure(error);
//                        }
//                    });
//            }
        } catch (final MalformedURLException e) {
            e.printStackTrace();
            if (context != null)
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (context != null)
                            callback.onFailure(e.toString());
                    }
                });
        } catch (final IOException e) {
            e.printStackTrace();
            if (context != null)
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (context != null)
                            callback.onFailure(e.toString());
                    }
                });
        }
    }

    private static String InputStreamToString(InputStream inputStream) {
        String content = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String temp = null;
            while ((temp = reader.readLine()) != null) {
                content += temp;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeSilently(reader);
        }
        return content;
    }

    private static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignored) {
            }
        }
    }

}
