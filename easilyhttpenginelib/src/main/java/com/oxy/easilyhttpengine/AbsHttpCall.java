package com.oxy.easilyhttpengine;

/**
 * Created by BelleOU on 17/9/7.
 */

public abstract class AbsHttpCall {
    interface CancelListener{
        void onCancel();
    }

    private CancelListener listener;
    protected boolean isCanceled = false;

    public void setCancelListener(CancelListener listener){
        this.listener = listener;
    }

    public final void cancel(){
        isCanceled = true;
        onCancel();
    }

    public final boolean isCanceled(){
        return isCanceled;
    }

    protected void onCancel(){
        if(listener != null){
            listener.onCancel();
        }
    }
}
