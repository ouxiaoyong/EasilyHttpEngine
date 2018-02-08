package com.oxy.easilyhttpengine.base;

/**
 * Created by BelleOU on 18/2/8.
 */

public interface IResponseCallback extends IProgressListener{
    void onStart(AbsRequest request);
    void onSuccess(AbsRequest request,String msg);
    void onFailure(AbsRequest request,String error);
}
