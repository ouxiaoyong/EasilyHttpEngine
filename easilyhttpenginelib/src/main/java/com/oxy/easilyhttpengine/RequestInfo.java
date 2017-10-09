package com.oxy.easilyhttpengine;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/26.
 */

public class RequestInfo {
    boolean isSuccessed = false;
    public HttpConfig config;
    public IHttpListener listener;
    /**
     * 加密的参数 json格式
     */
    public HttpParams params;
    public HashMap<String, String> notEncryptParams;
    /**
     * 上传文件用
     */
    public String filePath;
}
