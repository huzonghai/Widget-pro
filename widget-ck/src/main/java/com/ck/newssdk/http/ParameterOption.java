package com.ck.newssdk.http;

/**
 * Created by yt on 2016/12/19.
 */

public class ParameterOption {
    private static ParameterOption instance;
    public int GetParamType = 1;   //1表示传输格式是参数。 0标识json
    public int PostParamType = 1;
    public BaseResponseParser responseParser;

    public int connectTimeout;
    public int readTimeout;
    public int writeTimeout;

    public ParameterOption() {
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public ParameterOption setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public ParameterOption setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public ParameterOption setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public BaseResponseParser getResponseParser() {
        return responseParser;
    }

    public ParameterOption setResponseParser(BaseResponseParser responseParser) {
        this.responseParser = responseParser;
        return this;
    }

    public int getGetParamType() {
        return GetParamType;
    }

    public ParameterOption setGetParamType(int getParamType) {
        GetParamType = getParamType;
        return this;
    }

    public int getPostParamType() {
        return PostParamType;
    }

    public ParameterOption setPostParamType(int postParamType) {
        PostParamType = postParamType;
        return this;
    }

    public static ParameterOption getDefaultOption() {
        if (instance == null)
            instance = new ParameterOption().setGetParamType(1)
                    .setConnectTimeout(5000)
                    .setReadTimeout(10000)
                    .setWriteTimeout(500000)
                    .setPostParamType(1)
                    .setResponseParser(new BaseResponseParser());

        return instance;
    }

}
