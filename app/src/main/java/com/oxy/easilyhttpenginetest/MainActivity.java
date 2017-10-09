package com.oxy.easilyhttpenginetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


import com.oxy.easilyhttpengine.DoNothingDataCipher;
import com.oxy.easilyhttpengine.HttpConfig;
import com.oxy.easilyhttpengine.HttpListenerImpl;
import com.oxy.easilyhttpengine.HttpParams;
import com.oxy.easilyhttpengine.IHttpListener;
import com.oxy.easilyhttpengine.IMultiHttpListener;
import com.oxy.easilyhttpengine.IResponseHandler;
import com.oxy.easilyhttpengine.JSONObjectResponseHandler;
import com.oxy.easilyhttpengine.RequestInfo;
import com.oxy.easilyhttpengine.RequestType;
import com.oxy.easilyhttpengine.baseview.IRequestView;
import com.oxy.easilyhttpengine.baseview.LoadingViewImpl;
import com.oxy.easilyhttpengine.baseview.RequestViewImpl;
import com.oxy.easilyhttpengine.log.Logger;
import com.oxy.easilyhttpenginetest.net.DefaultResponseHandler;
import com.oxy.easilyhttpenginetest.net.HttpTools;
import com.oxy.easilyhttpenginetest.net.ResponseHandlerForGet;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    TextView textView;
    private LoadingViewImpl loadingView;
    IHttpListener listener;
    String imagePath;
    IRequestView requestView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestView = new RequestViewImpl(this);
        textView = (TextView) findViewById(R.id.text);
        listener = new HttpListenerImpl(requestView){
            @Override
            public void onRequestSuccess(HttpConfig config, Object data, String succesText, String response) {
                super.onRequestSuccess(config, data, succesText, response);
                Logger.d("ouxiaoyong","data:"+data);
                if(textView.getText().length() == 0){
                    textView.setText(response);
                }else{
                    textView.setText(textView.getText()+"\n\n"+response);
                }
            }
        };
        //loadingView = new LoadingViewImpl(textView);
        /*loadingView.setLoadingListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRequest(null);
            }
        });*/
        File file = new File(getFilesDir(),"shenzhen.png");
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
            inputStream = getAssets().open("shenzhen.png");
            File file = new File(getFilesDir(),"shenzhen.png");
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
        HttpConfig config = new HttpConfig(this);
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
        });
    }

    public void onHttpMulti(View v){
        textView.setText("");

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
        });
    }

    public void onPost(View v){
        textView.setText("");
        HttpConfig config = new HttpConfig(this);
        config.setLoadingText("获取地址库");
        config.rqtCode = 10;
        config.typeClass = AddressInfo.class;
        HttpParams params = createHttpParams("getRepairerAddressList");
        HttpTools.httpPost(config,params,listener);
    }

    public void onPostMulti(View v){
        textView.setText("");
        List<RequestInfo> requestInfos = new ArrayList<>();
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
        });
    }

    public void onUploadImage(View v){
        textView.setText("");
        HttpConfig config = new HttpConfig(this);
        config.setLoadingText("上传图片");
        config.rqtCode = 11;
        config.typeClass = URLInfo.class;
        HttpParams params = createHttpParams("setRepairerUploadImage");
        HttpTools.uploadFile(config,params, imagePath,listener);
    }

    /**
     *
     * 上传多张图片
     */
    public void onUploadImages(View v){
        List<String> paths = new ArrayList<>();
        for (int i = 0; i < 5; i ++){
            paths.add((imagePath));
        }
        textView.setText("");
        HttpConfig config = new HttpConfig(this);
        config.setLoadingText("上传图片");
        config.rqtCode = 10;
        config.typeClass = URLInfo.class;
        HttpParams params = createHttpParams("setRepairerUploadImage");
        HttpTools.uploadFiles(config,params,paths,listener,new IMultiHttpListener() {
            @Override
            public void onMultiHttpStart() {
                Logger.d("onMultiHttpStart");
            }

            @Override
            public void onMultiHttpComplete(boolean isAllSuccess) {
                Logger.d("onMultiHttpComplete isAllSuccess:"+isAllSuccess);
            }
        });
    }

    private HttpParams createHttpParams(String apiName){
        return new HttpParams()
                .put("apiName",apiName)
                .put("versionCode",31)
                .put("versionName","2.2.0")
                .put("deviceSN","1e7bf0541e62436caed9e0e8b89fd429")
                .put("longitude",1000)
                .put("latitude",1000)
                .put("deviceID","1888f7535e96456bafb7874f16303304")
                .put("clientID","728d5d77557a74d3217aa0127373efc6")
                .put("userToken","85e887bd398706c32c3aab4c7906e4c4")
                .put("deviceType",0);
    }
}
