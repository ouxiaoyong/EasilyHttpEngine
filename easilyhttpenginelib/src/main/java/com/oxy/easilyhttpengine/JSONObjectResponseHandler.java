package com.oxy.easilyhttpengine;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */

public abstract class JSONObjectResponseHandler implements IResponseHandler<JSONObject>{
    private boolean isListData;
    public JSONObjectResponseHandler(boolean isListData){
        this.isListData = isListData;
    }

    public void setIsListData(boolean isListData){
        this.isListData = isListData;
    }

    @Override
    public JSONObject parseResponseObject(String response) throws Exception{
        return new JSONObject(response);
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
        return isListData;
    }
}
