package com.oxy.easilyhttpengine;

import android.content.Context;

/**
 * Created by Administrator on 2017/7/21.
 */

public class HttpConfig<T> {
    public String url;
    public IDataCipher dataCipher;
    public RequestType method;
    public IResponseHandler responseHandler;
    public Context context;
    public int rqtCode;
    public Object tag;
    private String loadingText = "";
    public Class<T> typeClass;
    public boolean showHintWhenSuccess = true;
    public boolean showHintWhenFailure = true;
    public boolean hideLoadingViewWhenSuccess = true;
    public HttpConfig(Context context){
        this(context, RequestType.POST);
    }
    public HttpConfig(Context context, RequestType method){
        this(context,method,0);
    }

    public HttpConfig(Context context,RequestType method,int rqtCode){
        this(context,method,rqtCode,"");
    }
    public HttpConfig(Context context, RequestType method,int rqtCode, String loadingText){
        this.context = context;
        this.method = method;
        this.rqtCode = rqtCode;
        setLoadingText(loadingText);
        if(context != null){
            tag = context.toString();
        }
        if(method == RequestType.GET){
            dataCipher = new DoNothingDataCipher();
        }
    }

    public void setLoadingText(String loadingText){
        this.loadingText = loadingText;
        if(loadingText != null && loadingText.equals("")){
            this.loadingText = "请求中,请稍后...";
        }else if(loadingText != null){
            this.loadingText = loadingText+"中,请稍后...";
        }else{
        }
    }

    public void setFullLoadingText(String loadingText){
        this.loadingText = loadingText;
    }

    public String getLoadingText(){
        return loadingText;
    }
}
