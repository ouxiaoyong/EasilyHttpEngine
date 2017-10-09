package com.oxy.easilyhttpengine;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.oxy.easilyhttpengine.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 */

public class HttpEngine implements IHttpEngine {
    public static final String TAG = "HttpEngine";
    private Handler handler = new Handler(Looper.getMainLooper());
    private String url;
    private String defaultPostParamsKey;
    private String defaultUploadDispositionName;
    private IDataCipher defaultDataCipher;
    private IHttpManager httpManager;
    private IResponseHandler defaultResponseHandler;

    private HttpEngine(Builder builder) {
        this.url = builder.url;
        this.defaultPostParamsKey = builder.paramsKey;
        this.defaultDataCipher = builder.dataCipher;
        this.httpManager = builder.httpManager;
        this.defaultUploadDispositionName = builder.dispositionName;
        this.defaultResponseHandler = builder.responseHandler;
        if (httpManager == null) {
            httpManager = new OKHttpManager();
        }
    }

    /**
     * 如果是上传文件则是同步操作，不能在UI线程中调用
     *
     */
    private <T, K> void httpRequest(boolean isAsync, final HttpConfig config, HttpParams params,
                                       HashMap<String, String> notEncryptParams,
                                       final IHttpListener<T> listener,
                                       String filePath) {
        if(config.method == null){
            if(listener != null){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onRequestFailure(config, "请HttpConfig里设置请求方法method", null);
                    }
                });
            }
            return;
        }
        String paramsStr = params != null ? params.toString() : null;
        Logger.d(TAG, "paramsStr:" + paramsStr);
        final IDataCipher finalDataCipher;
        if(config.dataCipher != null){
           finalDataCipher = config.dataCipher;
        }else{
            finalDataCipher = defaultDataCipher;
        }
        if (finalDataCipher != null) {
            try {
                paramsStr = finalDataCipher.encrypt(paramsStr);
            } catch (Exception e) {
                if (listener != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onRequestFailure(config, "数据加密错误", null);
                        }
                    });
                }
                return;
            }
        }
        HashMap<String, String> paramMap = new HashMap<>();
        if (!TextUtils.isEmpty(defaultPostParamsKey) && !TextUtils.isEmpty(paramsStr)) {
            paramMap.put(defaultPostParamsKey, paramsStr);
        }
        if (notEncryptParams != null) {
            paramMap.putAll(notEncryptParams);
        }
        IHttpCallback httpCallback = new IHttpCallback() {
            @Override
            public void onHttpStart(final AbsHttpCall httpCall) {
                if (listener == null) {
                    return;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onRequestStart(config, config.getLoadingText(), httpCall);
                    }
                });
            }

            @Override
            public void onHttpFailure(final String error) {
                if (listener == null) {
                    return;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onRequestFailure(config, error, null);
                    }
                });
            }

            @Override
            public void onHttpResponse(String response) {
                if (listener == null) {
                    return;
                }
                if (TextUtils.isEmpty(response)) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onRequestFailure(config, "服务器无数据", null);
                        }
                    });
                    return;
                }
                if (finalDataCipher != null) {
                    try {
                        response = finalDataCipher.decrypt(response);
                    } catch (Exception e) {
                        Logger.d(TAG, "decrypt happen exception original response:" + response);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onRequestFailure(config, "数据解密错误", null);
                            }
                        });
                        return ;
                    }
                }
                final String finalResponse = response;
                final IResponseHandler<K> finalResponseHandler;
                if (config.responseHandler != null) {
                    finalResponseHandler = config.responseHandler;
                } else {
                    finalResponseHandler = defaultResponseHandler;
                }

                if (finalResponseHandler == null) {
                    Logger.errord(TAG,"you haven't set up responseHandler, so the default response tp success!");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onRequestSuccess(config, null,
                                    null, finalResponse);
                        }
                    });
                    return;
                }

                K responseObject;
                try {
                    responseObject = finalResponseHandler.parseResponseObject(response);
                    Logger.d(TAG, "responseObject:" + responseObject);
                } catch (Exception e) {
                    Logger.d(TAG, "decrypted response:" + response);
                    Logger.d(TAG, e);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onRequestFailure(config, "数据解析错误", null);
                        }
                    });
                    return ;
                }
                boolean success = finalResponseHandler.isSuccess(responseObject);
                final String message = finalResponseHandler.getMessge(responseObject, success);
                if (success) {
                    T _data = null;
                    if (config.typeClass != null) {
                        try {
                            if (finalResponseHandler.isListData()) {
                                _data = (T) finalResponseHandler.parseDataString2List(config.typeClass, finalResponseHandler.getDataString(responseObject));
                            } else {
                                _data = (T) finalResponseHandler.parseDataString2Object(config.typeClass, finalResponseHandler.getDataString(responseObject));
                            }
                        } catch (Exception e) {
                            Logger.d(TAG, e);
                        }
                    }

                    final T data = _data;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onRequestSuccess(config, data,
                                    message, finalResponse);
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onRequestFailure(config, message, finalResponse);
                        }
                    });
                }
            }
        };
        if (config.method == RequestType.UPLOAD) {
            httpManager.uploadFile(isAsync, config.context,
                    TextUtils.isEmpty(config.url) ? this.url : config.url, paramMap,
                    defaultUploadDispositionName, filePath, httpCallback,
                    new IProgressListener() {

                        @Override
                        public void onProgress(final float percentage) {
                            if (listener == null) {
                                return;
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onProgress(config,percentage);
                                }
                            });
                        }
                    });
        } else{
            httpManager.requestGetOrPost(config.method, isAsync, config.context,
                    TextUtils.isEmpty(config.url) ? this.url : config.url, paramMap, httpCallback);
        }
    }

    @Override
    public void httpPost(HttpConfig config, HttpParams params,
                         HashMap<String, String> notEncryptParams,
                         IHttpListener listener) {
        config.method = RequestType.POST;
        httpRequest(true, config, params, notEncryptParams, listener, null);
    }

    @Override
    public void httpGet(HttpConfig config, HttpParams params, HashMap<String, String> notEncryptParams, IHttpListener listener) {
        config.method = RequestType.GET;
        httpRequest(true,config, params, notEncryptParams, listener, null);
    }

    @Override
    public void uploadFile( HttpConfig config,  HttpParams params,
                            HashMap<String, String> notEncryptParams,  String filePath,  IHttpListener listener) {
        config.method = RequestType.UPLOAD;
        httpRequest(true, config,
                params, notEncryptParams, listener, filePath);
    }

    @Override
    public void uploadFiles(HttpConfig config,  HttpParams params,
                             HashMap<String, String> notEncryptParams,  List<String> filePaths,
                             IHttpListener httpListener, IMultiHttpListener multiHttpListener) {
        List<RequestInfo> requestInfos = new ArrayList<>();
        int size = filePaths == null ? 0 : filePaths.size();
        config.method = RequestType.UPLOAD;
        RequestInfo requestInfo;
        for (int i = 0; i < size; i++) {
            requestInfo = new RequestInfo();
            requestInfo.listener = httpListener;
            requestInfo.config = config;
            requestInfo.filePath = filePaths.get(i);
            requestInfo.notEncryptParams = notEncryptParams;
            requestInfo.params = params;
            requestInfos.add(requestInfo);
        }
        httpMulti(requestInfos, multiHttpListener);
    }

    private void httpMulti(final List<RequestInfo> requestInfos, final MultiHttpListenerImpl multiHttpListener, final int pos) {
        RequestInfo requestInfo;
        int size = requestInfos.size();
        requestInfo = requestInfos.get(pos);
        final boolean isLastest = pos == size - 1;
        if (isLastest) {
            requestInfo.config.hideLoadingViewWhenSuccess = true;
        } else {
            requestInfo.config.hideLoadingViewWhenSuccess = false;
        }
        if (requestInfo.isSuccessed) {
            if (isLastest) {
                multiHttpListener.onMultiHttpComplete(true);
            } else {
                httpMulti(requestInfos, multiHttpListener, pos + 1);
            }
            return;
        }

        if (multiHttpListener.isFinished()) {
            multiHttpListener.onMultiHttpComplete(false);
            return;
        }

        final RequestInfo finalRequestInfo = requestInfo;
        IHttpListener listener = new IHttpListener() {
            @Override
            public void onRequestStart(HttpConfig config, String loadingText, AbsHttpCall httpCall) {
                httpCall.setCancelListener(new AbsHttpCall.CancelListener() {
                    @Override
                    public void onCancel() {
                        multiHttpListener.onMultiHttpComplete(false);
                    }
                });
                if (finalRequestInfo.listener != null) {
                    finalRequestInfo.listener.onRequestStart(config, loadingText, httpCall);
                }
            }

            @Override
            public void onRequestFailure(HttpConfig config, String error, String response) {
                if (multiHttpListener.isFinished()) {
                    return;
                }
                if (finalRequestInfo.listener != null) {
                    finalRequestInfo.listener.onRequestFailure(config, error, response);
                }
                multiHttpListener.onMultiHttpComplete(false);
            }

            @Override
            public void onRequestSuccess(HttpConfig config, Object data, String succesText, String response) {
                if(multiHttpListener.isFinished()){
                    return;
                }
                finalRequestInfo.isSuccessed = true;
                if (finalRequestInfo.listener != null) {
                    finalRequestInfo.listener.onRequestSuccess(config, data, succesText, response);
                }
                if (!isLastest) {
                    httpMulti(requestInfos, multiHttpListener, pos + 1);
                } else {
                    multiHttpListener.onMultiHttpComplete(true);
                }
            }

            @Override
            public void onProgress(HttpConfig config, float percentage) {
                if (finalRequestInfo.listener != null) {
                    finalRequestInfo.listener.onProgress(config, percentage);
                }
            }
        };
        if (requestInfo.config.method == RequestType.UPLOAD) {
            if(requestInfo.config.getLoadingText() != null){
                requestInfo.config.setFullLoadingText("上传图片中,请稍后...(" + (pos + 1) + "/" + requestInfos.size() + ")");
            }
            uploadFile(requestInfo.config, requestInfo.params,
                    requestInfo.notEncryptParams, requestInfo.filePath, listener);
        } else {
            httpRequest(true,requestInfo.config, requestInfo.params,
                    requestInfo.notEncryptParams, listener,null);
        }
    }

    @Override
    public void httpMulti(List<RequestInfo> requestInfos, IMultiHttpListener multiHttpListener){
        if (requestInfos == null || requestInfos.size() == 0) {
            return;
        }

        MultiHttpListenerImpl multiHttpListener1 = new MultiHttpListenerImpl(multiHttpListener);
        multiHttpListener1.onMultiHttpStart();
        httpMulti(requestInfos,multiHttpListener1 , 0);
    }

    @Override
    public void httpPostMulti(List<RequestInfo> requestInfos, IMultiHttpListener multiHttpListener) {
        if (requestInfos == null || requestInfos.size() == 0) {
            return;
        }
        for (RequestInfo requestInfo: requestInfos){
            requestInfo.config.method = RequestType.POST;
        }
        httpMulti(requestInfos, multiHttpListener);
    }

    @Override
    public void httpGetMulti(List<RequestInfo> requestInfos, IMultiHttpListener multiHttpListener) {
        if (requestInfos == null || requestInfos.size() == 0) {
            return;
        }
        for (RequestInfo requestInfo: requestInfos){
            requestInfo.config.method = RequestType.GET;
        }
        httpMulti(requestInfos, multiHttpListener);
    }

    public static class Builder {
        String url;
        IDataCipher dataCipher;
        IHttpManager httpManager;
        String paramsKey;
        String dispositionName;
        IResponseHandler responseHandler;

        public Builder setServerURL(String url) {
            this.url = url;
            return this;
        }

        public Builder setDefaultResponseHandler(IResponseHandler responseHandler) {
            this.responseHandler = responseHandler;
            return this;
        }

        public Builder setUploadFileDispositionName(String dispositionName) {
            this.dispositionName = dispositionName;
            return this;
        }

        public Builder setParamsKey(String paramsKey) {
            this.paramsKey = paramsKey;
            return this;
        }

        public Builder setDataCipher(IDataCipher dataCipher) {
            this.dataCipher = dataCipher;
            return this;
        }

        public Builder setHttpManager(IHttpManager httpManager) {
            this.httpManager = httpManager;
            return this;
        }

        public HttpEngine create() {
            return new HttpEngine(this);
        }
    }

}
