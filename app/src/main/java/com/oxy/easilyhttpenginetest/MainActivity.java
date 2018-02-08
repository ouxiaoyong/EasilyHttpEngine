package com.oxy.easilyhttpenginetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.oxy.easilyhttpengine.HttpParams;
import com.oxy.easilyhttpengine.base.AbsMultiRequest;
import com.oxy.easilyhttpengine.base.AbsRequest;
import com.oxy.easilyhttpengine.base.AbsResponseCallback;
import com.oxy.easilyhttpengine.base.ConcurrentRequest;
import com.oxy.easilyhttpengine.base.IHttpClient;
import com.oxy.easilyhttpengine.base.MultiRequestListenerImp;
import com.oxy.easilyhttpengine.base.RequestInstance;
import com.oxy.easilyhttpengine.base.RequestListenerImp;
import com.oxy.easilyhttpengine.base.RequestViewImp;
import com.oxy.easilyhttpengine.base.SequentRequest;
import com.oxy.easilyhttpengine.log.Logger;
import com.oxy.easilyhttpenginetest.net.HttpTools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    TextView textView;
    String imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);

        //loadingView = new LoadingViewImpl(textView);
        /*loadingView.setLoadingListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRequest(null);
            }
        });*/
        File file = new File(getFilesDir(),"shenzhen.jpeg");
        if(!file.exists()){
            saveFile();
        }
        imagePath = file.getAbsolutePath();
        Logger.d(" imagePath:"+ imagePath);
    }


    private void saveFile(){
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("shenzhen.jpeg");
            File file = new File(getFilesDir(),"shenzhen.jpeg");
            outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) != -1){
                outputStream.write(buf,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onGet(View v){
        textView.setText("");
        /*HttpConfig config = new HttpConfig(this);
        config.typeClass = Weather.class;
        config.rqtCode = 100;
        config.dataCipher = new DoNothingDataCipher();
        config.responseHandler = new ResponseHandlerForGet();
        config.setLoadingText("获取北京市天气");
        config.url = "http://www.weather.com.cn/data/sk/101010100.html";
        HttpTools.httpGet(config,null,new HttpListenerImpl<Weather>(requestView){
            @Override
            public void onRequestSuccess(HttpConfig config, Weather data, String succesText, String response) {
                super.onRequestSuccess(config, data, succesText, response);
                Logger.d("data :"+data);
                textView.setText(response);
            }
        });*/
        new RequestInstance()
                .setLoaddingMsg("获取北京市天气。。。")
                .setApiUrl("http://www.weather.com.cn/data/sk/101010100.html")
                .setMethod(IHttpClient.Method.GET)
                .setRequestListener(new RequestListenerImp(new RequestViewImp(this))).execute(this);

    }

    public void onHttpMulti(View v){
        /*textView.setText("");

        IResponseHandler responseHandler = new JSONObjectResponseHandler(false){
            @Override
            public String getDataString(JSONObject object) {
                return object.toString();
            }

            @Override
            public boolean isSuccess(JSONObject object) {
                return true;
            }

            @Override
            public String getMessge(JSONObject respObject, boolean success) {
                return "";
            }
        };
        List<RequestInfo> requestInfos = new ArrayList<>();
        HttpConfig config = new HttpConfig(this);
        config.responseHandler = responseHandler;
        config.method = RequestType.GET;
        config.url = "http://www.weather.com.cn/data/sk/101010100.html";
        config.typeClass = Weather.class;
        config.dataCipher = new DoNothingDataCipher();
        config.setLoadingText("获取北京市天气");
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.config = config;
        requestInfo.params = new HttpParams();
        requestInfo.listener = listener;
        requestInfos.add(requestInfo);

        responseHandler = new DefaultResponseHandler(false);
        config = new HttpConfig(this);
        config.responseHandler = responseHandler;
        config.setLoadingText("获取URL1");
        config.typeClass = URLInfo.class;
        HttpParams params = createHttpParams("getRepairerURL");
        requestInfo = new RequestInfo();
        requestInfo.params = params;
        requestInfo.config = config;
        requestInfo.listener = listener;
        requestInfos.add(requestInfo);

        config = new HttpConfig(this);
        config.typeClass = Weather.class;
        config.method = RequestType.GET;
        config.url = "http://www.weather.com.cn/data/sk/101020100.html";
        config.responseHandler = new ResponseHandlerForGet();
        config.setLoadingText("获取上海市天气");
        config.dataCipher = new DoNothingDataCipher();
        requestInfo = new RequestInfo();
        requestInfo.config = config;
        requestInfo.listener = listener;
        requestInfos.add(requestInfo);
        HttpTools.httpMulti(requestInfos,new IMultiHttpListener() {
            @Override
            public void onMultiHttpStart() {
                Logger.d("onMultiHttpStart");
            }

            @Override
            public void onMultiHttpComplete(boolean isAllSuccess) {
                Logger.d("onMultiHttpComplete isAllSuccess:"+isAllSuccess);
            }
        });*/

        RequestListenerImp requestListenerImp = new RequestListenerImp(new RequestViewImp(this));
        new SequentRequest().addRequest(new RequestInstance()
                .setLoaddingMsg("获取北京市天气1。。。")
                .setApiUrl("http://www.weather.com.cn/data/sk/101010100.html")
                .setMethod(IHttpClient.Method.GET)
                .setRequestListener(new RequestListenerImp(new RequestViewImp(this))))
                .addRequest(new RequestInstance()
                        .setLoaddingMsg("获取北京市天气2。。。")
                        .setApiUrl("http://www.weather.com.cn/data/sk/101010100.html")
                        .setMethod(IHttpClient.Method.GET)
                        .setRequestListener(requestListenerImp))
                .addRequest(new RequestInstance()
                        .setLoaddingMsg("获取北京市天气3。。。")
                        .setApiUrl("http://www.weather.com.cn/data/sk/101010100.html")
                        .setMethod(IHttpClient.Method.GET)
                        .setRequestListener(requestListenerImp))
                .addRequest(new RequestInstance()
                        .setLoaddingMsg("获取北京市天气4。。。")
                        .setApiUrl("http://www.weather.com.cn/data/sk/101010100.html")
                        .setMethod(IHttpClient.Method.GET)
                        .setRequestListener(requestListenerImp))
                .execute(this);
    }

    public void onPost(View v){
        textView.setText("");
        /*HttpConfig config = new HttpConfig(this);
        config.setLoadingText("获取地址库");
        config.rqtCode = 10;
        config.typeClass = AddressInfo.class;
        HttpParams params = createHttpParams("getRepairerAddressList");
        HttpTools.httpPost(config,params,listener);*/
        HttpParams params = createHttpParams("getRepairerAddressList");
        HttpTools.getDefaultRequest(params)
                .setLoaddingMsg("获取地址。。。")
                .setRequestListener(new RequestListenerImp(new RequestViewImp(this))).execute(this);
    }

    public void onPostMulti(View v){
        textView.setText("");
        /*List<RequestInfo> requestInfos = new ArrayList<>();
        DefaultResponseHandler responseHandler = new DefaultResponseHandler(true);
        HttpConfig config = new HttpConfig(this);
        config.responseHandler = responseHandler;
        config.setLoadingText("获取地址库1");
        config.rqtCode = 10;
        config.typeClass = AddressInfo.class;
        HttpParams params = createHttpParams("getRepairerAddressList");

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.params = params;
        requestInfo.config = config;
        requestInfo.listener = listener;
        requestInfos.add(requestInfo);


        responseHandler = new DefaultResponseHandler(false);
        config = new HttpConfig(this);
        config.responseHandler = responseHandler;
        config.setLoadingText("获取URL1");
        config.rqtCode = 11;
        config.typeClass = URLInfo.class;
        params = createHttpParams("getRepairerURL");
        requestInfo = new RequestInfo();
        requestInfo.params = params;
        requestInfo.config = config;
        requestInfo.listener = listener;
        requestInfos.add(requestInfo);

        config = new HttpConfig(this);
        responseHandler = new DefaultResponseHandler(true);
        config.responseHandler = responseHandler;
        config.setLoadingText("获取地址库2");
        config.rqtCode = 12;
        config.typeClass = AddressInfo.class;
        params = createHttpParams("getRepairerAddressList");

        requestInfo = new RequestInfo();
        requestInfo.params = params;
        requestInfo.config = config;
        requestInfo.listener = listener;
        requestInfos.add(requestInfo);

        config = new HttpConfig(this);
        config.responseHandler = responseHandler;
        config.setLoadingText("获取URL2");
        config.rqtCode = 13;
        config.typeClass = URLInfo.class;
        params = createHttpParams("getRepairerURL");
        //responseHandler = new DefaultResponseHandler(false);
        requestInfo = new RequestInfo();
        requestInfo.params = params;
        requestInfo.config = config;
        requestInfo.listener = listener;
        requestInfos.add(requestInfo);

        HttpTools.httpPostMulti(requestInfos, new IMultiHttpListener() {
            @Override
            public void onMultiHttpStart() {
                Logger.d("onMultiHttpStart");
            }

            @Override
            public void onMultiHttpComplete(boolean isAllSuccess) {
                Logger.d("onMultiHttpComplete isAllSuccess:"+isAllSuccess);
            }
        });*/

        HttpParams params = createHttpParams("getRepairerAddressList");
        new SequentRequest().addRequest(new RequestInstance()
                .setLoaddingMsg("获取北京市天气1。。。")
                .setApiUrl("http://www.weather.com.cn/data/sk/101010100.html")
                .setMethod(IHttpClient.Method.GET)
                .setRequestListener(null))

                .addRequest(HttpTools.getDefaultRequest(params)

                        .setLoaddingMsg("获取地址1。。。")
                        .setRequestListener(null))

                .addRequest(new RequestInstance()
                        .setLoaddingMsg("获取北京市天气2。。。")
                        .setApiUrl("http://www.weather.com.cn/data/sk/101010100.html")
                        .setMethod(IHttpClient.Method.GET)
                        .setRequestListener(null))

                .addRequest(HttpTools.getDefaultRequest(params)
                        .setLoaddingMsg("获取地址2。。。")
                        .setRequestListener(null))
                .setMultiRequestListener(new MultiRequestListenerImp(new RequestViewImp(this)))
                .setLoaddingMsg("请求中。。。").execute(this);
    }

    public void onUploadImage(View v){
        textView.setText("");
        /*HttpConfig config = new HttpConfig(this);
        config.setLoadingText("上传图片");
        config.rqtCode = 11;
        config.typeClass = URLInfo.class;
        HttpParams params = createHttpParams("setRepairerUploadImage");
        HttpTools.uploadFile(config,params, imagePath,listener);*/
        HttpParams params = createHttpParams("setRepairerUploadImage");
        HttpTools.getDefaultRequest(params)
                .setMethod(IHttpClient.Method.UPLOAD)
                .setUploadFilePath(imagePath)
                .setLoaddingMsg("上传图片。。。")
                .setRequestListener(new RequestListenerImp(new RequestViewImp(this)))
        .execute(this);
    }

    /**
     *
     * 上传多张图片
     */
    AbsMultiRequest sequentRequest;
    public void onUploadImages(View v){
        List<String> paths = new ArrayList<>();
        for (int i = 0; i < 5; i ++){
            paths.add((imagePath));
        }
        textView.setText("");

        if(sequentRequest == null){
            HttpParams params = createHttpParams("setRepairerUploadImage");
            sequentRequest = new ConcurrentRequest().addRequest(HttpTools.getDefaultRequest(params)
                    .setMethod(IHttpClient.Method.UPLOAD)
                    .setUploadFilePath(imagePath))

                    .addRequest(HttpTools.getDefaultRequest(params)
                            .setMethod(IHttpClient.Method.UPLOAD)
                            .setUploadFilePath(imagePath))

                    .addRequest(HttpTools.getDefaultRequest(params)
                            .setMethod(IHttpClient.Method.UPLOAD)
                            .setUploadFilePath(imagePath))
                    .setLoaddingMsg("请求中。。。")
                    .setResponseCallback(new AbsResponseCallback() {
                        @Override
                        public void onSuccess(AbsRequest request, String msg) {
                            Logger.d("-----------------onSuccess--------------");
                            HttpParams params = createHttpParams("getRepairerAddressList");
                            new SequentRequest().addRequest(new RequestInstance()
                                    .setLoaddingMsg("获取北京市天气1。。。")
                                    .setApiUrl("http://www.weather.com.cn/data/sk/101010100.html")
                                    .setMethod(IHttpClient.Method.GET)
                                    .setRequestListener(null))

                                    .addRequest(HttpTools.getDefaultRequest(params)

                                            .setLoaddingMsg("获取地址1。。。")
                                            .setRequestListener(null))

                                    .addRequest(new RequestInstance()
                                            .setLoaddingMsg("获取北京市天气2。。。")
                                            .setApiUrl("http://www.weather.com.cn/data/sk/101010100.html")
                                            .setMethod(IHttpClient.Method.GET)
                                            .setRequestListener(null))

                                    .addRequest(HttpTools.getDefaultRequest(params)
                                            .setLoaddingMsg("获取地址2。。。")
                                            .setRequestListener(null))
                                    .setMultiRequestListener(new MultiRequestListenerImp(new RequestViewImp(MainActivity.this)))
                                    .setLoaddingMsg("请求中。。。").execute(MainActivity.this);
                        }
                    })
                    .setMultiRequestListener(new MultiRequestListenerImp(new RequestViewImp(this)));
        }
        sequentRequest.execute(this);

    }

    private HttpParams createHttpParams(String apiName){
        return new HttpParams()
                .put("apiName",apiName)
                .put("versionCode",43)
                .put("versionName","2.2.0")
                .put("deviceSN","1e7bf0541e62436caed9e0e8b89fd429")
                .put("longitude",1000)
                .put("latitude",1000)
                .put("deviceID","1888f7535e96456bafb7874f16303304")
                .put("clientID","728d5d77557a74d3217aa0127373efc6")
                .put("userToken","de7077791cdac26ba9283eb3ba7f30d8")
                .put("deviceType",0);
    }
}
