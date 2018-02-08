package com.oxy.easilyhttpengine.base;


/**
 * Created by Administrator on 2017/7/25.
 */

public interface IRequestView {
    void showLoadingView(AbsRequest request,CharSequence loadingText);
    void hideLoadingView();
    void showNodataView(AbsRequest request);
    void showContentView();
    void showMessgeWhenSuccess(boolean useDialogStyle,CharSequence msg);
    void showMessgeWhenFailure(boolean useDialogStyle,CharSequence msg);
}
