package com.oxy.easilyhttpengine.base;

import android.text.TextUtils;

import com.oxy.easilyhttpengine.log.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * Created by BelleOU on 18/1/29.
 */

public class OKHttpClient implements IHttpClient<Call> {

    private final OkHttpClient client = new OkHttpClient.Builder().build();
    @Override
    public Call request(String url, Method method, HashMap<String, String> extraHeader, HashMap<String, String> params, IResponseListener responseListener) {
        Request.Builder requestBuilder = new Request.Builder();
        if (method == Method.GET) {
            if (params != null && params.size() > 0) {
                url = addParamsForGet(url, params);
            }
            requestBuilder.url(url)
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .get();
        } else {
            FormBody.Builder builder = new FormBody.Builder();
            addParmsForPost(builder, params);
            requestBuilder.url(url)
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .post(builder.build());
        }
        if(extraHeader != null && extraHeader.size() > 0){
            requestBuilder.headers(Headers.of(extraHeader));
        }
        return executeRequest(requestBuilder.build(), responseListener);
    }

    @Override
    public Call upload(String url, HashMap<String, String> extraHeader, HashMap<String, String> params,
                       IResponseListener responseListener, String dispositionName,String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            if (responseListener != null) {
                responseListener.onError("文件路径为空！");
            }
            return null;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            if (responseListener != null) {
                responseListener.onError("文件:" + filePath + "不存在！");
            }
            return null;
        }

        RequestBody fileBody = createFileBody(file, responseListener);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM).addPart(
                Headers.of("Content-Disposition", "form-data;name=\"" + dispositionName + "\";"
                        + "filename=\"" + filePath + "\""), fileBody);

        addParmsForUploadFile(builder, params);

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url).cacheControl(CacheControl.FORCE_NETWORK)
                .post(builder.build());
        if(extraHeader != null && extraHeader.size() > 0){
            requestBuilder.headers(Headers.of(extraHeader));
        }

        return executeRequest(requestBuilder.build(), responseListener);
    }

    @Override
    public void cancel(Call call) {
        if(call == null || call.isCanceled()){
            return;
        }
        call.cancel();
    }

    private Call executeRequest(Request request, final IResponseListener responseListener) {
        Call call = client.newCall(request);
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (call.isCanceled()) {
                    return;
                }
                if(responseListener != null){
                    responseListener.onError(e == null? null:e.getLocalizedMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (call.isCanceled()) {
                    return;
                }
                if(responseListener != null){
                    try {
                        responseListener.onResponse(response.body().string());
                    }catch (Exception e){
                        responseListener.onResponse("");
                    }
                }
            }
        };
        call.enqueue(callback);
        return call;
    }
    private RequestBody createFileBody(final File file, final IResponseListener progressListener) {
        if (file == null || !file.exists()) {
            return null;
        }
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return MediaType.parse("application/octet-stream");
            }

            @Override
            public long contentLength() throws IOException {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                FileInputStream inputStream = new FileInputStream(file);
                int bufSize;
                long fileLength = contentLength();
                if (fileLength < 500 * 1024) {
                    bufSize = 16;
                } else {
                    bufSize = 32;
                }
                byte[] bytes = new byte[bufSize * 1024];
                int len;
                long curLength = 0;
                if (progressListener != null) {
                    progressListener.onProgress(0);
                }
                float percentage;
                long time = System.currentTimeMillis();
                float lastPercentage = 0;
                while ((len = inputStream.read(bytes, 0, bytes.length)) != -1) {
                    sink.write(bytes, 0, len);
                    curLength += len;
                    percentage = curLength * 1.0f / fileLength;
                    if (System.currentTimeMillis() - time >= 20) {
                        time = System.currentTimeMillis();
                        lastPercentage = percentage;
                        if (progressListener != null) {
                            progressListener.onProgress(percentage);
                        }
                    }

                }
                if (lastPercentage != 1.0f && progressListener != null) {
                    progressListener.onProgress(1.0f);
                }
                inputStream.close();
            }
        };
    }


    private void addParmsForPost(FormBody.Builder builder, HashMap<String, String> params) {
        if (params == null || params.size() == 0) {
            return;
        }
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        Map.Entry<String, String> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            builder.add(entry.getKey(), entry.getValue());
        }
    }

    private void addParmsForUploadFile(MultipartBody.Builder builder, HashMap<String, String> params) {
        if (params == null || params.size() == 0) {
            return;
        }
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        Map.Entry<String, String> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            builder.addPart(Headers.of("Content-Disposition", "form-data;name=\""
                            + entry.getKey() + "\""),
                    RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), entry.getValue()));
        }
    }

    private String addParamsForGet(String url, HashMap<String, String> params) {
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        Map.Entry<String, String> entry;
        StringBuilder stringBuilder = new StringBuilder();
        if(url.contains("?")){
            if(url.lastIndexOf("?") != url.length()-1){
                stringBuilder.append("&");
            }
        }else{
            stringBuilder.append("?");
        }
        int i = 0;
        while (iterator.hasNext()) {
            entry = iterator.next();
            if (i != 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
            i++;
        }
        url += stringBuilder.toString();
        return url;
    }
}
