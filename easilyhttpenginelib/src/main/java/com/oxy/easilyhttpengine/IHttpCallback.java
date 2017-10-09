package com.oxy.easilyhttpengine;

/**
 * Created by Administrator on 2017/7/20.
 */

public interface IHttpCallback {
    void onHttpStart(AbsHttpCall httpCall);
    void onHttpResponse(String response);
    void onHttpFailure(String error);
}
