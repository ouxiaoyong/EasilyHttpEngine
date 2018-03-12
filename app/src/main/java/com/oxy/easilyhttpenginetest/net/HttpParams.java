package com.oxy.easilyhttpenginetest.net;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/7/20.
 */

public class HttpParams {
    private JSONObject paramsObj = new JSONObject();
    public HttpParams put(String key, Object value){
        try {
            paramsObj.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public String getString(String key){
        return paramsObj.optString(key);
    }

    public int getInt(String key){
        return paramsObj.optInt(key);
    }

    public Object getObject(String key){
        return paramsObj.opt(key);
    }

    public JSONObject getParamsObj(){
        return paramsObj;
    }

    @Override
    public String toString() {
        return paramsObj.toString();
    }
}
