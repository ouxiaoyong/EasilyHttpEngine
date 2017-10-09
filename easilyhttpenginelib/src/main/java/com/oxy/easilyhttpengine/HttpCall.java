package com.oxy.easilyhttpengine;

import okhttp3.Call;

/**
 * Created by BelleOU on 17/9/7.
 */

public class HttpCall extends AbsHttpCall{
    Call call;
    public HttpCall(Call call){
        this.call = call;
    }

    @Override
    protected void onCancel() {
        if(call != null && !call.isCanceled() &&!call.isExecuted()){
            call.cancel();
        }
    }
}

