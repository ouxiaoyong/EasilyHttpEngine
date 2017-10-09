package com.oxy.easilyhttpengine;

import android.text.TextUtils;

import com.oxy.easilyhttpengine.baseview.ILoadingView;
import com.oxy.easilyhttpengine.baseview.IRequestView;

/**
 * Created by Administrator on 2017/7/25.
 */

public class HttpListenerImpl<T> implements IHttpListener<T> {
    private IRequestView requestView;
    private ILoadingView loadingView;
    public HttpListenerImpl(IRequestView requestView){
        this(requestView,null);
    }
    public HttpListenerImpl(IRequestView requestView,ILoadingView loadingView){
        this.requestView = requestView;
        this.loadingView = loadingView;
        if(loadingView != null){
            loadingView.showNodataView();
        }
    }
    @Override
    public void onRequestStart(HttpConfig config, String loadingText, final AbsHttpCall httpCall) {
        if(loadingView != null){
            loadingView.showLoadingView();
        }
        if(TextUtils.isEmpty(loadingText)){
            return;
        }
        if(requestView != null){
            requestView.showLoadingView(config, loadingText, loadingView == null? httpCall:new AbsHttpCall() {
                @Override
                protected void onCancel() {
                    super.onCancel();
                    httpCall.cancel();
                    loadingView.showNodataView();
                }

            });
        }
    }

    @Override
    public void onRequestFailure(HttpConfig config, String error, String response) {
        if(loadingView != null){
            loadingView.showNodataView();
        }
        if(config.getLoadingText() == null){
            return;
        }
        if(requestView != null){
            requestView.hideLoadingView(config,false);
            if(config.showHintWhenFailure){
                requestView.showMessgeWhenFailure(error);
            }
        }

    }

    @Override
    public void onRequestSuccess(HttpConfig config, T data, String succesText, String response) {
        if(loadingView != null){
            loadingView.showContentView();
        }
        if(!config.hideLoadingViewWhenSuccess){
            return;
        }
        if(config.getLoadingText() == null){
            return;
        }
        if(requestView != null){
            requestView.hideLoadingView(config,true);
            if(config.showHintWhenSuccess){
                requestView.showMessgeWhenSuccess(succesText);
            }
        }

    }

    @Override
    public void onProgress(HttpConfig config, float percentage) {
        if(requestView != null){
            int percentageInt = (int) (percentage * 100);
            requestView.updateLoadingView(config,config.getLoadingText()+percentageInt+"%");
        }
    }
}
