package com.oxy.easilyhttpenginetest.net;

import com.alibaba.fastjson.JSON;
import com.oxy.easilyhttpengine.IResponseHandler;

import java.util.List;

/**
 * Created by Administrator on 2017/7/21.
 */

public class ResponseHandlerForGet implements IResponseHandler<String> {
    @Override
    public String parseResponseObject(String response) throws Exception{
        return response;
    }

    @Override
    public boolean isSuccess(String object) {
        return true;
    }

    @Override
    public String getMessge(String object,boolean success) {
        return "";
    }

    @Override
    public String getDataString(String object) {
        return object;
    }

    @Override
    public <K> K parseDataString2Object(Class<K> typeClass, String dataString) {
        return JSON.parseObject(dataString,typeClass);
    }

    @Override
    public <K> List<K> parseDataString2List(Class<K> typeClass, String dataString) {
        return JSON.parseArray(dataString,typeClass);
    }

    @Override
    public boolean isListData() {
        return false;
    }
}
