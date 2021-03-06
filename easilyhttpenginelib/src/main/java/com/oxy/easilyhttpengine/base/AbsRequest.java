package com.oxy.easilyhttpengine.base;

import com.oxy.easilyhttpengine.IParamsTransformer;
import com.oxy.easilyhttpengine.IResponseHandler;
import com.oxy.easilyhttpengine.IResponseTransformer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by BelleOU on 18/1/29.
 */

public abstract class AbsRequest implements IRequest {
    protected IHttpClient httpClient;
    protected String apiUrl;
    protected IResponseTransformer responseTransformer;
    protected IParamsTransformer paramsTransformer;
    protected String uploadDispositionName;

    public IResponseTransformer getResponseTransformer() {
        return responseTransformer;
    }

    public AbsRequest setResponseTransformer(IResponseTransformer responseTransformer) {
        this.responseTransformer = responseTransformer;
        return this;
    }

    public IParamsTransformer getParamsTransformer() {
        return paramsTransformer;
    }

    public AbsRequest setParamsTransformer(IParamsTransformer paramsTransformer) {
        this.paramsTransformer = paramsTransformer;
        return this;
    }

    protected IResponseCallback responseCallback;
    protected boolean isSuccess = false;

    public int getRqtCode() {
        return rqtCode;
    }

    public AbsRequest setRqtCode(int rqtCode) {
        this.rqtCode = rqtCode;
        return this;
    }

    public Object getRqtTag() {
        return rqtTag;
    }

    public AbsRequest setRqtTag(Object rqtTag) {
        this.rqtTag = rqtTag;
        return this;
    }

    private int rqtCode;
    private Object rqtTag;
    @Override
    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    @Override
    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isShowMsgUseDialogStyle() {
        return showMsgUseDialogStyle;
    }

    public AbsRequest setShowMsgUseDialogStyle(boolean showMsgUseDialogStyle) {
        this.showMsgUseDialogStyle = showMsgUseDialogStyle;
        return this;
    }

    public AbsRequest setResponseCallback(IResponseCallback callback){
        this.responseCallback = callback;
        return this;
    }
    /**
     * 成功和失败时的提示信息用dialog样式还是toast样式
     */
    protected boolean showMsgUseDialogStyle = false;
    public HashMap<String, String> getExtraHeader() {
        return extraHeader;
    }

    public AbsRequest setExtraHeader(HashMap<String, String> extraHeader) {
        this.extraHeader = extraHeader;
        return this;
    }

    protected HashMap<String,String> extraHeader;
    public Class<?> getTypeClass() {
        return typeClass;
    }

    public AbsRequest setTypeClass(Class<?> typeClass) {
        this.typeClass = typeClass;
        return this;
    }

    protected Class<?> typeClass;
    public IRequestListener getRequestListener() {
        return requestListener;
    }

    public AbsRequest setRequestListener(IRequestListener requestListener) {
        this.requestListener = requestListener;
        return this;
    }

    protected IRequestListener requestListener;
    public String getLoaddingMsg() {
        return loaddingMsg;
    }

    public AbsRequest setLoaddingMsg(String loaddingMsg) {
        this.loaddingMsg = loaddingMsg;
        return this;
    }

    protected String loaddingMsg;

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    public AbsRequest setUploadFilePath(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath;
        return this;
    }

    protected String uploadFilePath;

    public Map<String, String> getParams() {
        return params;
    }

    public AbsRequest setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    protected Map<String,String> params;
    protected IResponseHandler responseHandler;

    public IResponseHandler getResponseHandler() {
        return responseHandler;
    }

    public AbsRequest setResponseHandler(IResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
        return this;
    }

    public IHttpClient getHttpClient() {
        return httpClient;
    }

    public AbsRequest setHttpClient(IHttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public AbsRequest setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
        return this;
    }

    public String getUploadDispositionName() {
        return uploadDispositionName;
    }

    public AbsRequest setUploadDispositionName(String uploadDispositionName) {
        this.uploadDispositionName = uploadDispositionName;
        return this;
    }

    public IHttpClient.Method getMethod() {
        return method;
    }

    public AbsRequest setMethod(IHttpClient.Method method) {
        this.method = method;
        return this;
    }

    private IHttpClient.Method method;

    protected boolean isCanceled = false;
    @Override
    public void setCanceled(boolean isCanceled) {
        this.isCanceled = isCanceled;
    }

    @Override
    public boolean isCanceled() {
        return isCanceled;
    }
}
