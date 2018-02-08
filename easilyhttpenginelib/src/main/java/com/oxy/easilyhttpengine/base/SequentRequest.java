package com.oxy.easilyhttpengine.base;

import android.content.Context;

import com.oxy.easilyhttpengine.log.Logger;

/**
 * Created by BelleOU on 18/1/26.
 */

//串联的请求
public class SequentRequest extends AbsMultiRequest {
    int position = 0;
    Context mContext;
    int getRequestIndex(){
        for(int i = 0; i < requests.size(); i ++){
            if(!requests.get(i).isSuccess()){
                return i;
            }
        }
        return -1;
    }

    @Override
    protected boolean handleSuccess(String msg){
        int index = getRequestIndex();
        if(index == -1){
            position = 0;
            isEndRequest = true;
            if(multiRequestListener != null){
                multiRequestListener.onSuccess(this,msg);
            }
            return true;
        }else{
            position = index;
            execute(mContext);
        }
        return false;
    }

    @Override
    public void clear() {
        super.clear();
        position = 0;
    }

    @Override
    public void execute(Context context) {
        if(requests.size() == 0){
            Logger.d("requests list size is 0");
            return;
        }
        mContext = context;
        isEndRequest = false;
        if(multiRequestListener != null){
            multiRequestListener.onStart(this,getLoaddingMsg());
        }
        /*if(responseCallback != null){
            responseCallback.onStart(this);
        }*/
        if(isSuccess()){
            if(multiRequestListener != null){
                multiRequestListener.onSuccess(this,null);
            }
            if(responseCallback != null){
                responseCallback.onSuccess(this,null);
            }
            return;
        }
        requests.get(position).execute(context);
    }

    @Override
    public void cancel() {
        setCanceled(true);
        isEndRequest = true;
        if(currentRequest != null){
            currentRequest.cancel();
        }
    }

}
