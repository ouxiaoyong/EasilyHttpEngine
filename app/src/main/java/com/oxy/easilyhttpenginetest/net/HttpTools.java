package com.oxy.easilyhttpenginetest.net;

import com.oxy.easilyhttpengine.HttpConfig;
import com.oxy.easilyhttpengine.HttpEngine;
import com.oxy.easilyhttpengine.HttpParams;
import com.oxy.easilyhttpengine.IHttpEngine;
import com.oxy.easilyhttpengine.IHttpListener;
import com.oxy.easilyhttpengine.IMultiHttpListener;
import com.oxy.easilyhttpengine.RequestInfo;
import com.oxy.easilyhttpengine.RequestType;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/7/21.
 */

public class HttpTools {
    private static final String KEY_API_NAME = "apiName";
    private static IHttpEngine httpEngine = new HttpEngine.Builder()
            //填写您的服务器地址
            .setServerURL("")
            //填写参数key
            .setParamsKey("data")
            //填写上传文件的DispositionName key
            .setUploadFileDispositionName("image")
            .setDefaultResponseHandler(new DefaultResponseHandler(false))
            .setDataCipher(new AesCipher())
            .create();

    public static void httpPost(HttpConfig config, HttpParams params,
                                IHttpListener listener) {
        HashMap<String,String> unEncryptParams = new HashMap<>();
        unEncryptParams.put(KEY_API_NAME,params.getString(KEY_API_NAME));
        httpEngine.httpPost(config, params, unEncryptParams,listener);
    }

    public static void httpGet(HttpConfig config, HttpParams params, IHttpListener listener){
        httpEngine.httpGet(config, params,null,listener);
    }

    public static void httpGetMulti(List<RequestInfo> requestInfos, IMultiHttpListener listener){
        httpEngine.httpGetMulti(requestInfos,listener);
    }

    public static void httpMulti(List<RequestInfo> requestInfos, IMultiHttpListener listener){
        for (RequestInfo requestInfo : requestInfos){
            if(requestInfo.config.method == RequestType.GET ||
                    requestInfo.params == null){
                continue;
            }
            HashMap<String,String> unEncryptParams = new HashMap<>();
            unEncryptParams.put(KEY_API_NAME,requestInfo.params.getString(KEY_API_NAME));
            requestInfo.notEncryptParams = unEncryptParams;
        }
        httpEngine.httpMulti(requestInfos,listener);
    }

    public static void httpPostMulti(List<RequestInfo> requestInfos, IMultiHttpListener listener){
        for (RequestInfo requestInfo : requestInfos){
            HashMap<String,String> unEncryptParams = new HashMap<>();
            unEncryptParams.put(KEY_API_NAME,requestInfo.params.getString(KEY_API_NAME));
            requestInfo.notEncryptParams = unEncryptParams;
        }
        httpEngine.httpPostMulti(requestInfos,listener);
    }

    public static void uploadFiles(HttpConfig config, HttpParams params,
                            List<String> filePaths,
                            IHttpListener httpListener,
                            IMultiHttpListener multiHttpListener) {
        HashMap<String,String> unEncryptParams = new HashMap<>();
        unEncryptParams.put(KEY_API_NAME,params.getString(KEY_API_NAME));
        httpEngine.uploadFiles(config, params, unEncryptParams,filePaths,httpListener,multiHttpListener);
    }

    public static void uploadFile(HttpConfig config,  HttpParams params,
                             String filePath,  IHttpListener listener) {
        HashMap<String,String> unEncryptParams = new HashMap<>();
        unEncryptParams.put(KEY_API_NAME,params.getString(KEY_API_NAME));
        httpEngine.uploadFile(config, params, unEncryptParams,filePath,listener);
    }
}
