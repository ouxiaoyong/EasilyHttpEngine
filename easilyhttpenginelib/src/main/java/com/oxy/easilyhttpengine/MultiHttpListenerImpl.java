package com.oxy.easilyhttpengine;

/**
 * Created by BelleOU on 17/9/7.
 */

public class MultiHttpListenerImpl implements IMultiHttpListener {
    private boolean isFinished = false;
    private IMultiHttpListener listener;
    public MultiHttpListenerImpl(IMultiHttpListener listener){
        this.listener = listener;
    }

    @Override
    public void onMultiHttpStart() {
        isFinished = false;
        if(listener != null){
            listener.onMultiHttpStart();
        }
    }

    @Override
    public void onMultiHttpComplete(boolean isAllSuccess) {
        isFinished = true;
        if(listener != null){
            listener.onMultiHttpComplete(isAllSuccess);
        }
    }

    public boolean isFinished(){
        return isFinished;
    }
}
