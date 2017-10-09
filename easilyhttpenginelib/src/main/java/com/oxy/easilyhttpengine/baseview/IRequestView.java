package com.oxy.easilyhttpengine.baseview;


import com.oxy.easilyhttpengine.AbsHttpCall;
import com.oxy.easilyhttpengine.HttpConfig;

/**
 * Created by Administrator on 2017/7/25.
 */

public interface IRequestView {
    void showLoadingView(HttpConfig config, CharSequence loadingText, AbsHttpCall httpCall);
    void updateLoadingView(HttpConfig config, CharSequence loadingText);
    void hideLoadingView(HttpConfig config, boolean success);
    void showMessgeWhenSuccess(CharSequence msg);
    void showMessgeWhenFailure(CharSequence msg);
}
