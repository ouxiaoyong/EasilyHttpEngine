package com.oxy.easilyhttpenginetest.net;

import com.oxy.easilyhttpengine.HttpParams;
import com.oxy.easilyhttpengine.base.AbsRequest;
import com.oxy.easilyhttpengine.base.RequestInstance;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/21.
 */

public class HttpTools {
    private static final String KEY_API_NAME = "apiName";
    public static AbsRequest getDefaultRequest(boolean isListData,HttpParams httpParams){
        HashMap<String,String> unEncryptParams = new HashMap<>();
        if(httpParams != null){
            unEncryptParams.put(KEY_API_NAME,httpParams.getString(KEY_API_NAME));
        }
        RequestInstance request = new RequestInstance();
        request.setApiUrl("http://test.cus.lianbaowang.com/interface.php");
        request.setUploadDispositionName("image");
        request.setDataCipher(new AesCipher("E1A224AC55FD74E0"));
        request.setParamsKey("data");
        request.setResponseHandler(new DefaultResponseHandler(isListData));
        request.setExtraParams(unEncryptParams);
        request.setHttpParams(httpParams);
        return request;
    }

    public static AbsRequest getDefaultRequest(HttpParams httpParams){
        return getDefaultRequest(true,httpParams);
    }

}
