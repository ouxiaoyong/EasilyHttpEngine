package com.oxy.easilyhttpengine.base;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * Created by BelleOU on 18/1/29.
 */

public interface IHttpClient<T> {
    interface IResponseListener{
        void onResponse(String response);
        void onError(String msg);
        void onProgress(float percentage);
    }
    interface Dns{
        List<InetAddress> lookup(String hostname) throws UnknownHostException;
    }
    enum Method{
        GET,
        POST,
        UPLOAD
    }
    T request(String url,Method method,Map<String,String> extraHeader, Map<String,String> params,
                 IResponseListener responseListener );
    T upload(String url,Map<String,String> extraHeader,Map<String,String> params,
                IResponseListener responseListener,String dispositionName,String filePath);
    void cancel(T call);

    IHttpClient setDns(Dns dns);
}
