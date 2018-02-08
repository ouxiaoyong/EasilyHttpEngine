package com.oxy.easilyhttpengine.base;

import java.util.HashMap;

/**
 * Created by BelleOU on 18/1/29.
 */

public interface IHttpClient<T> {
    interface IResponseListener{
        void onResponse(String response);
        void onError(String msg);
        void onProgress(float percentage);
    }
    enum Method{
        GET,
        POST,
        UPLOAD
    }
    T request(String url,Method method,HashMap<String,String> extraHeader, HashMap<String,String> params,
                 IResponseListener responseListener );
    T upload(String url,HashMap<String,String> extraHeader,HashMap<String,String> params,
                IResponseListener responseListener,String dispositionName,String filePath);
    void cancel(T call);
}
