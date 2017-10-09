package com.oxy.easilyhttpenginetest.net;


import com.oxy.easilyhttpengine.JSONObjectResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/7/21.
 */

public class DefaultResponseHandler extends JSONObjectResponseHandler {
    public DefaultResponseHandler(boolean isListData){
        super(isListData);
    }

    @Override
    public boolean isSuccess(JSONObject object) {
        return object.optInt("state") == 1;
    }

    @Override
    public String getMessge(JSONObject object, boolean success) {
        return object.optString("message");
    }

    @Override
    public String getDataString(JSONObject object) {
        if(object == null){
            return "";
        }
        if(isListData()){
            return object.optString("selectData");
        }else{
            JSONArray array = object.optJSONArray("selectData");
            if(array == null || array.length() == 0){
                return "";
            }
            return array.optString(0);
        }
    }
}
