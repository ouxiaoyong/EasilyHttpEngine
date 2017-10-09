package com.oxy.easilyhttpengine;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 */

public interface IHttpEngine {
    void httpPost(HttpConfig config, HttpParams params, HashMap<String, String> notEncryptParams, IHttpListener listener);

    void httpGet(HttpConfig config, HttpParams params, HashMap<String, String> notEncryptParams, IHttpListener listener);

    void uploadFile(HttpConfig config, HttpParams params,
                    HashMap<String, String> notEncryptParams, String filePath, IHttpListener listener);

    void uploadFiles(HttpConfig config, HttpParams params,
                     HashMap<String, String> notEncryptParams, List<String> filePaths,
                     IHttpListener listener, IMultiHttpListener multiHttpListener);

    void httpPostMulti(List<RequestInfo> requestInfos, IMultiHttpListener multiHttpListener);
    void httpGetMulti(List<RequestInfo> requestInfos, IMultiHttpListener multiHttpListener);
    void httpMulti(List<RequestInfo> requestInfos, IMultiHttpListener multiHttpListener);

}