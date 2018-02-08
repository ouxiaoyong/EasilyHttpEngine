package com.oxy.easilyhttpengine.base;

import android.content.Context;

import com.oxy.easilyhttpengine.log.Logger;

/**
 * Created by BelleOU on 18/1/26.
 */

//并发的请求
public class ConcurrentRequest extends AbsMultiRequest {

    @Override
    protected boolean handleSuccess(String msg){
        return isSuccess();
    }

    @Override
    public void execute(Context context) {
        if(requests.size() == 0){
            Logger.d("requests list size is 0");
            return;
        }
        isEndRequest = false;
        if(multiRequestListener != null){
            multiRequestListener.onStart(this,getLoaddingMsg());
        }
        if(isSuccess()){
            if(multiRequestListener != null){
                multiRequestListener.onSuccess(this,null);
            }
            if(responseCallback != null){
                responseCallback.onSuccess(this,null);
            }
            return;
        }
        IRequest request;
        for(int i = 0; i < requests.size(); i ++){
            request = requests.get(i);
            if(!request.isSuccess()){
                request.execute(context);
            }
        }
    }

}
