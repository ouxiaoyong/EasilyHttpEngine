package com.oxy.easilyhttpengine;

/**
 * Created by Administrator on 2017/7/21.
 */

public interface IHttpListener<T>{
    void onRequestStart(HttpConfig config, String loadingText, AbsHttpCall httpCall);
    void onRequestFailure(HttpConfig config, String error, String response);
    void onRequestSuccess(HttpConfig config, T data, String succesText, String response);
    void onProgress(HttpConfig config, float percentage);
}
