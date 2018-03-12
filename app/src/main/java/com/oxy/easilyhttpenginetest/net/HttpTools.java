package com.oxy.easilyhttpenginetest.net;

import com.oxy.easilyhttpengine.JSONObjectResponseHandler;
import com.oxy.easilyhttpengine.base.AbsRequest;
import com.oxy.easilyhttpengine.base.IHttpClient;
import com.oxy.easilyhttpengine.base.OKHttpClient;
import com.oxy.easilyhttpengine.base.RequestInstance;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/21.
 */

public class HttpTools {
    private static final String URL = "http://test.cus.lianbaowang.com/interface.php";
    private static final String KEY_API_NAME = "apiName";
    private static final AesCipher aesCipher = new AesCipher("E1A224AC55FD74E0");
    private static final IHttpClient httpClient = new OKHttpClient();
    private static final JSONObjectResponseHandler responseHandler = new DefaultResponseHandler(true);
    private static final JSONObjectResponseHandler responseHandler2 = new DefaultResponseHandler(false);
    public static AbsRequest getDefaultRequest(boolean isListData,HttpParams httpParams){
        Map<String,String> params = new HashMap<>();
        params.put("data",httpParams.toString());
        params.put(KEY_API_NAME,httpParams.getString(KEY_API_NAME));
        RequestInstance request = new RequestInstance();
        request.setApiUrl(URL);
        request.setUploadDispositionName("image");
        request.setHttpClient(httpClient);

        request.setResponseTransformer(aesCipher);
        request.setParamsTransformer(aesCipher);
        request.setResponseHandler(isListData? responseHandler:responseHandler2);
        request.setParams(params);
        return request;
    }

    public static AbsRequest getDefaultRequest(HttpParams httpParams){
        return getDefaultRequest(true,httpParams);
    }

}
