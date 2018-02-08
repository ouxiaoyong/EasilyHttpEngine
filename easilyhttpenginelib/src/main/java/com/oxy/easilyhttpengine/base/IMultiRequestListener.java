package com.oxy.easilyhttpengine.base;

/**
 * Created by BelleOU on 18/1/26.
 */

public interface IMultiRequestListener {
    void onStart(AbsRequest multiRequest,String loaddingText);
    void onSuccess(AbsRequest multiRequest,String msg);
    void onFailure(AbsRequest multiRequest,String error);
}
