package com.oxy.easilyhttpengine.base;

/**
 * Created by Administrator on 2017/7/28.
 */

public interface IProgressListener {
    void onProgress(AbsRequest request, float percentage);
}
