package com.oxy.easilyhttpengine;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
 * Created by Administrator on 2017/7/20.
 */

public class OKHttpManager implements IHttpManager {
    private final OkHttpClient client = new OkHttpClient.Builder().build();

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

    private boolean hasNetwork(Context context) {
        if (context == null) {
            return true;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }

    @Override
    public void uploadFile(boolean isAsync, Context context, String url,
                           HashMap<String, String> params, String dispositionName, String filePath,
                           final IHttpCallback callback, IProgressListener progressListener) {
        if (!hasNetwork(context)) {
            if (callback != null) {
                callback.onHttpFailure("无网络，请检查网络！");
            }
            return;
        }
        if (TextUtils.isEmpty(filePath)) {
            if (callback != null) {
                callback.onHttpFailure("文件路径为空！");
            }
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            if (callback != null) {
                callback.onHttpFailure("文件:" + filePath + "不存在！");
            }
            return;
        }

        RequestBody fileBody = createFileBody(file, progressListener);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM).addPart(
                Headers.of("Content-Disposition", "form-data;name=\"" + dispositionName + "\";"
                        + "filename=\"" + filePath + "\""), fileBody);
        addParmsForUploadFile(builder, params);
        Request request = new Request.Builder().url(url)
                .cacheControl(CacheControl.FORCE_NETWORK)
                .post(builder.build())
                .build();
        executeRequest(isAsync, request, callback);
    }

    private void executeRequest(boolean isAsync, final Request request, final IHttpCallback callback) {
        final Call call = client.newCall(request);
        Callback callback1 = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (call.isCanceled()) {
                    return;
                }
                if (callback != null) {
                    callback.onHttpFailure(e.getLocalizedMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                response(callback, call, response);
            }
        };
        if (callback != null) {
            callback.onHttpStart(new HttpCall(call));
        }
        if (isAsync) {
            call.enqueue(callback1);
        } else {
            executeSync(call, callback);
        }
    }

    private void response(IHttpCallback callback, Call call, Response response) {
        if (call.isCanceled()) {
            return;
        }
        if (callback != null) {
            int code = response.code();
            if (code >= 200 && code < 300) {
                try {
                    callback.onHttpResponse(response.body().string());
                } catch (Exception e) {
                    callback.onHttpFailure(e.getLocalizedMessage());
                    Logger.d(HttpEngine.TAG, e);
                }
            } else {
                callback.onHttpFailure("request failure httpCode:" + code);
            }
        }
    }

    private RequestBody createFileBody(final File file, final IProgressListener progressListener) {
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

    private void executeSync(Call call, IHttpCallback callback) {
        try {
            Response response = call.execute();
            response(callback, call, response);
        } catch (IOException e) {
            if (!call.isCanceled()) {
                callback.onHttpFailure(e.getLocalizedMessage());
            }
        }
    }

    private String addParamsForGet(String url, HashMap<String, String> params) {
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        Map.Entry<String, String> entry;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("?");
        int i = 0;
        while (iterator.hasNext()) {
            entry = iterator.next();
            if (i != 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append(entry.getKey() + "=" + entry.getValue());
            i++;
        }
        url += stringBuilder.toString();
        return url;
    }

    @Override
    public void requestGetOrPost(RequestType method, boolean isAsync, Context context, String url, HashMap<String, String> params, IHttpCallback callback) {
        if (!hasNetwork(context)) {
            if (callback != null) {
                callback.onHttpFailure("无网络，请检查网络！");
            }
            return;
        }
        Request request;
        if (method == RequestType.GET) {
            if (params != null && params.size() > 0) {
                url = addParamsForGet(url, params);
            }
            Logger.d(HttpEngine.TAG, "url:" + url);
            request = new Request.Builder().url(url)
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .get()
                    .build();
        } else {
            FormBody.Builder builder = new FormBody.Builder();
            addParmsForPost(builder, params);
            request = new Request.Builder().url(url)
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .post(builder.build())
                    .build();
        }
        executeRequest(isAsync, request, callback);
    }
}
