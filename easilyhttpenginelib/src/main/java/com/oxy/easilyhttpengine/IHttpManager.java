package com.oxy.easilyhttpengine;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/20.
 */

public interface IHttpManager {
    void uploadFile(boolean isAsync, Context context, String url,
                    HashMap<String, String> params, String dispositionName,
                    String filePath, IHttpCallback callback, IProgressListener progressListener);
    void requestGetOrPost(RequestType method, boolean isAsync, Context context, String url,
                          HashMap<String, String> params, IHttpCallback callback);
}
