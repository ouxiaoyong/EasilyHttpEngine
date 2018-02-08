package com.oxy.easilyhttpengine.base;

/**
 * Created by BelleOU on 18/1/30.
 */

public interface IRequestListener<T> extends IProgressListener{
    void onStart(AbsRequest request);
    void onFailure(AbsRequest request, String error, String response);
    void onSuccess(T data,AbsRequest request, String succesText, String response);
}
