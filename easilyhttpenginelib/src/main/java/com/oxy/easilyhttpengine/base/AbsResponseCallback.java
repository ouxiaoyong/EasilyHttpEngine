package com.oxy.easilyhttpengine.base;

/**
 * Created by BelleOU on 18/2/8.
 */

public abstract class AbsResponseCallback implements IResponseCallback{

    @Override
    public void onStart(AbsRequest request) {

    }

    @Override
    public void onFailure(AbsRequest request, String error) {

    }

    @Override
    public void onProgress(AbsRequest request, float percentage) {

    }
}
