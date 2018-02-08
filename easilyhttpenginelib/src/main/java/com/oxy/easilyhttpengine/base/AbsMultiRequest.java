package com.oxy.easilyhttpengine.base;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BelleOU on 18/1/26.
 */

public abstract class AbsMultiRequest extends AbsRequest{
    protected boolean isEndRequest = true;
    List<IRequest> requests = new ArrayList<>();
    IMultiRequestListener multiRequestListener;
    protected IRequest currentRequest;
    protected IResponseCallback callback = new IResponseCallback() {
        @Override
        public void onProgress(AbsRequest request, float percentage) {
            if(isEndRequest){
                return;
            }
            String loaddingText = request.getLoaddingMsg();
            if(multiRequestListener != null && !TextUtils.isEmpty(loaddingText)){
                int percInt = (int) (100 * percentage);
                multiRequestListener.onStart(AbsMultiRequest.this,loaddingText+" "+percInt+"%");
            }
        }

        @Override
        public void onStart(AbsRequest request) {
            if(isEndRequest){
                return;
            }
            String loaddingText = request.getLoaddingMsg();
            if(multiRequestListener != null && !TextUtils.isEmpty(loaddingText)){
                multiRequestListener.onStart(AbsMultiRequest.this,loaddingText);
            }
        }

        @Override
        public void onSuccess(AbsRequest request,String msg) {
            if(isEndRequest){
                return;
            }
            request.setSuccess(true);
            if(handleSuccess(msg)){
                isEndRequest = true;
                if(multiRequestListener != null){
                    multiRequestListener.onSuccess(AbsMultiRequest.this,msg);
                }
                if(responseCallback != null){
                    responseCallback.onSuccess(request,msg);
                }
            }
        }

        @Override
        public void onFailure(AbsRequest request,String error) {
            if(isEndRequest){
                return;
            }
            isEndRequest = true;
            if(multiRequestListener != null){
                multiRequestListener.onFailure(AbsMultiRequest.this,error);
            }

            if(responseCallback != null){
                responseCallback.onFailure(request,error);
            }
        }

    };

    public AbsMultiRequest setResponseCallback(IResponseCallback callback){
        super.setResponseCallback(callback);
        return this;
    }

    protected abstract boolean handleSuccess(String msg);

    public void reexecute(Context context){
        for (int i = 0 ; i < requests.size(); i ++){
            requests.get(i).setSuccess(false);
        }
        execute(context);
    }

    public boolean isSuccess(){
        for(int i = 0; i < requests.size(); i ++){
            if(!requests.get(i).isSuccess()){
                return false;
            }
        }
        return true;
    }

    public AbsMultiRequest addRequest(IRequest request){
        request.setResponseCallback(callback);
        requests.add(request);
        return this;
    }

    @Override
    public AbsMultiRequest setLoaddingMsg(String loaddingMsg) {
        super.setLoaddingMsg(loaddingMsg);
        return this;
    }

    public void clear(){
        requests.clear();
    }

    public AbsMultiRequest setMultiRequestListener(IMultiRequestListener multiRequestListener){
        this.multiRequestListener = multiRequestListener;
        return this;
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
