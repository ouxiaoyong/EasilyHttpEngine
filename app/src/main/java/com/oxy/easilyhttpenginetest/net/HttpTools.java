package com.oxy.easilyhttpenginetest.net;

import com.oxy.easilyhttpengine.HttpParams;
import com.oxy.easilyhttpengine.JSONObjectResponseHandler;
import com.oxy.easilyhttpengine.base.AbsRequest;
import com.oxy.easilyhttpengine.base.IHttpClient;
import com.oxy.easilyhttpengine.base.OKHttpClient;
import com.oxy.easilyhttpengine.base.RequestInstance;

import java.util.HashMap;

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
        HashMap<String,String> unEncryptParams = new HashMap<>();
        if(httpParams != null){
            unEncryptParams.put(KEY_API_NAME,httpParams.getString(KEY_API_NAME));
        }
        RequestInstance request = new RequestInstance();
        request.setApiUrl(URL);
        request.setUploadDispositionName("image");
        request.setHttpClient(httpClient);

        request.setDataCipher(aesCipher);
        request.setParamsKey("data");
        request.setResponseHandler(isListData? responseHandler:responseHandler2);
        request.setExtraParams(unEncryptParams);
        request.setHttpParams(httpParams);
        return request;
    }

    public static AbsRequest getDefaultRequest(HttpParams httpParams){
        return getDefaultRequest(true,httpParams);
    }

}
