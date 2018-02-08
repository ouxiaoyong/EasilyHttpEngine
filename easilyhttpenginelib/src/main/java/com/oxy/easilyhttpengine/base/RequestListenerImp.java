package com.oxy.easilyhttpengine.base;

import com.oxy.easilyhttpengine.log.Logger;

/**
 * Created by BelleOU on 18/1/30.
 */

public class RequestListenerImp<T> implements IRequestListener<T> {

    private IRequestView requestView;
    public RequestListenerImp(IRequestView requestView){
        this.requestView = requestView;
    }
    @Override
    public void onStart(AbsRequest request) {
        if(requestView != null){
            requestView.showNodataView(request);
            requestView.showLoadingView(request,request.getLoaddingMsg());
        }
    }

    @Override
    public void onFailure(AbsRequest request, String error, String response) {
        if(requestView != null){
            requestView.hideLoadingView();
            requestView.showNodataView(request);
            requestView.showMessgeWhenFailure(request.isShowMsgUseDialogStyle(),error);
        }
    }

    @Override
    public void onSuccess(T data, AbsRequest request, String succesText, String response) {
        onSuccess(data);
        if(requestView != null){
            requestView.hideLoadingView();
            requestView.showContentView();
            requestView.showMessgeWhenSuccess(request.isShowMsgUseDialogStyle(),succesText);
        }
    }

    public void onSuccess(T data){

    }

    @Override
    public void onProgress(AbsRequest request, float percentage) {
        Logger.d("onProgress:"+percentage);
        if(requestView != null){
            int perInt = (int)(100 * percentage);
            requestView.showLoadingView(request,request.getLoaddingMsg()+" "+perInt+"%");
        }
    }
}
