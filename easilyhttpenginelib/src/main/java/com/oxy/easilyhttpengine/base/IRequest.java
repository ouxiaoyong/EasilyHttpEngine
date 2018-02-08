package com.oxy.easilyhttpengine.base;

import android.content.Context;

/**
 * Created by BelleOU on 18/1/26.
 */

public interface IRequest {
    void execute(Context context);
    void cancel();
    void setSuccess(boolean success);
    boolean isSuccess();
    boolean isCanceled();
    void setCanceled(boolean isCanceled);
    IRequest setResponseCallback(IResponseCallback callback);
}
