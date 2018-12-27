package com.ck.newssdk.http;


import com.ck.newssdk.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yt on 2017/5/9.
 */

public class BaseResponseParser {

    public static int RESPONSE_OK_STATUS = 0;
    public String json;
    public int status;
    public String message;

    public BaseResponseParser() {
    }
    public void parseData(String json) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(json);
            if (obj.toString().contains("status")) {
                status = obj.getInt("status");
            } else if (obj.toString().contains("code")) {
                status = obj.getInt("code");
            } else
                status = RESPONSE_OK_STATUS == 1 ? 0 : 1;//失败。
            if (obj.toString().contains("messages"))
                message = obj.getString("messages");
            else if (obj.toString().contains("msg")) {
                message = obj.getString("msg");
            }
            if (!StringUtils.isEmpty(message))
                message = "Error code = " + String.valueOf(status) + " message = " + message;
            else
                message = "Error code = " + String.valueOf(status) + " message = null";
            this.json = json;

        } catch (JSONException e) {
            message = e.getMessage();
            e.printStackTrace();
        }
    }

    public static void setResponseOkStatus(int i) { //默认是0成功
        RESPONSE_OK_STATUS = i;
    }

}
