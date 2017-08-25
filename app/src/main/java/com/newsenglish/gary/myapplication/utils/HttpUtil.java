package com.newsenglish.gary.myapplication.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Gary on 2017/8/24.
 */

public class HttpUtil {
    public static void sendOkHttp(String url,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
}
