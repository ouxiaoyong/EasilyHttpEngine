package com.oxy.easilyhttpengine.base;


/**
 * Created by BelleOU on 18/1/30.
 */

public class MultiRequestListenerImp implements IMultiRequestListener {
    IRequestView requestView;
    public MultiRequestListenerImp(IRequestView requestView){
        this.requestView = requestView;
    }

    @Override
    public void onStart(AbsRequest multiRequest,String loaddingText) {
        if(requestView != null){
            requestView.showNodataView(multiRequest);
            requestView.showLoadingView(multiRequest,loaddingText);
        }
    }

    @Override
    public void onSuccess(AbsRequest multiRequest,String msg) {
        if(requestView != null){
            requestView.hideLoadingView();
            requestView.showContentView();
            requestView.showMessgeWhenSuccess(multiRequest.isShowMsgUseDialogStyle(),null);
        }
    }

    @Override
    public void onFailure(AbsRequest multiRequest,String error) {
        if(requestView != null){
            requestView.hideLoadingView();
            requestView.showNodataView(multiRequest);
            requestView.showMessgeWhenFailure(multiRequest.isShowMsgUseDialogStyle(),error);
        }
    }
}
