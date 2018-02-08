package com.oxy.easilyhttpengine.base;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.oxy.easilyhttpengine.log.Logger;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by BelleOU on 18/1/26.
 */

public class RequestInstance<T> extends AbsRequest<T> {
    Object call;
    @Override
    public void execute(Context context) {
        setCanceled(false);
        start(responseCallback);
        if(!hasNetwork(context)){
            error(null,"无网络，请稍后再试...", responseCallback);
            return;
        }

        if(httpClient == null){
            httpClient = new OKHttpClient();
        }

        String paramsStr = httpParams == null ? "":httpParams.toString();
        if(dataCipher != null){
            try {
                paramsStr = dataCipher.encrypt(paramsStr);
            } catch (Exception e) {
                error(null,"请求数据加密失败！", responseCallback);
                return;
            }
        }
        HashMap<String,String> paramsMap = new HashMap<>();
        if(extraParams != null){
            paramsMap.putAll(extraParams);
        }
        if(!TextUtils.isEmpty(paramsKey)){
            paramsMap.put(paramsKey,paramsStr);
        }
        IHttpClient.IResponseListener responseListener = new IHttpClient.IResponseListener() {
            @Override
            public void onProgress(final float percentage) {
                if(Looper.myLooper() == Looper.getMainLooper()){
                    if(responseCallback != null){
                        responseCallback.onProgress(RequestInstance.this,percentage);
                    }
                    if(requestListener != null){
                        requestListener.onProgress(RequestInstance.this,percentage);
                    }
                }else{
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if(responseCallback != null){
                                responseCallback.onProgress(RequestInstance.this,percentage);
                            }
                            if(requestListener != null){
                                requestListener.onProgress(RequestInstance.this,percentage);
                            }
                        }
                    });
                }
            }

            @Override
            public void onResponse(String response) {
                if(isCanceled()){
                    error(response,null, responseCallback);
                    return;
                }
                if(responseHandler == null){
                    success(response,null, responseCallback);
                    return;
                }
                if(dataCipher != null){
                    try {
                        response = dataCipher.decrypt(response);
                    } catch (Exception e) {
                        error(response,"响应数据解密失败！", responseCallback);
                        return;
                    }
                }
                JSONObject object = null;
                try {
                    object = responseHandler.parseResponseObject(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(object == null){
                    error(response,"响应数据JSON格式错误！", responseCallback);
                    return;
                }
                if(responseHandler.isSuccess(object)){
                    success(response,object, responseCallback);
                }else{
                    error(response,responseHandler.getMessge(object,false), responseCallback);
                }
            }

            @Override
            public void onError(String msg) {
                error(null,msg, responseCallback);
            }
        };
        if(getMethod() == IHttpClient.Method.UPLOAD){
            call = httpClient.upload(apiUrl,extraHeader,paramsMap,responseListener,uploadDispositionName,uploadFilePath);
        }else {
            call = httpClient.request(apiUrl, getMethod(), extraHeader, paramsMap,responseListener);
        }
    }

    @Override
    public void cancel() {
        if(responseCallback != null){
            responseCallback.onFailure(this,null);
        }
        setCanceled(true);
        httpClient.cancel(call);
    }

    Object transition(JSONObject object){
        if(responseHandler != null && typeClass != null){
            String dataStr = responseHandler.getDataString(object);
            if(responseHandler.isListData()){
                List<T> listData = responseHandler.parseDataString2List(typeClass,dataStr);
                return listData;
            }else{
                T data = null;
                try {
                    data = responseHandler.parseDataString2Object(typeClass,dataStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return data;
            }
        }else{
            return null;
        }
    }

    void success(final String response, final JSONObject object, final IResponseCallback callback){
        Logger.d("response :"+ StringUtils.unicode2String(response));
        Object data = null;
        String sucMsg = "";
        data = transition(object);
        if(responseHandler != null){
            sucMsg = responseHandler.getMessge(object,true);
        }

        if(Looper.myLooper() == Looper.getMainLooper()){
            if(requestListener != null){
                requestListener.onSuccess(data,this,sucMsg,response);
            }
            if(callback != null){
                callback.onSuccess(this,sucMsg);
            }

        }else{
            final Object _data = data;
            final String _sucMsg = sucMsg;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if(requestListener != null){
                        requestListener.onSuccess(_data,RequestInstance.this,_sucMsg,response);
                    }
                    if(callback != null){
                        callback.onSuccess(RequestInstance.this,_sucMsg);
                    }
                }
            });
        }
    }

    void error(final String response, final String errorMsg, final IResponseCallback callback){
        Logger.d("error: "+StringUtils.unicode2String(errorMsg) + "  response :"+StringUtils.unicode2String(response));
        if(Looper.myLooper() == Looper.getMainLooper()){
            if(requestListener != null){
                requestListener.onFailure(this,errorMsg,response);
            }

            if(callback != null){
                callback.onFailure(this,errorMsg);
            }
        }else{
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if(requestListener != null){
                        requestListener.onFailure(RequestInstance.this,errorMsg,response);
                    }

                    if(callback != null){
                        callback.onFailure(RequestInstance.this,errorMsg);
                    }
                }
            });
        }

    }

    void start(final IResponseCallback callback){
        if(Looper.myLooper() == Looper.getMainLooper()){
            if(requestListener != null){
                requestListener.onStart(this);
            }
            if(callback != null){
                callback.onStart(RequestInstance.this);
            }
        }else{
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if(requestListener != null){
                        requestListener.onStart(RequestInstance.this);
                    }
                    if(callback != null){
                        callback.onStart(RequestInstance.this);
                    }
                }
            });
        }

    }

    private boolean hasNetwork(Context context) {
        if (context == null) {
            return true;
        }
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable();
        }catch (Exception e){

        }
        return true;
    }

}
