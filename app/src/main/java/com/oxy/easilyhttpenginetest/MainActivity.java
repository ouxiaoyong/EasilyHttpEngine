package com.oxy.easilyhttpenginetest;

import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

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
import com.oxy.easilyhttpenginetest.net.HttpParams;
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
        Logger.d("pid:"+ Process.myPid()+"  currentThread:"+Thread.currentThread().toString());
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
        new RequestInstance()
                .setLoaddingMsg("获取北京市天气。。。")
                .setApiUrl("http://www.weather.com.cn/data/sk/101010100.html")
                .setMethod(IHttpClient.Method.GET)
                .setRequestListener(new RequestListenerImp(new RequestViewImp(this))).execute(this);

    }

    public void onHttpMulti(View v){

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

        HttpParams params = createHttpParams("getRepairerAddressList");
        HttpTools.getDefaultRequest(params)
                .setTypeClass(AddressInfo.class)
                .setLoaddingMsg("获取地址。。。")
                .setRequestListener(new RequestListenerImp<List<AddressInfo>>(new RequestViewImp(this)){
                    @Override
                    public void onSuccess(List<AddressInfo> data) {
                        super.onSuccess(data);
                        Logger.d(" data:"+data);

                        Logger.d(" data[0]:"+data.get(0));

                    }
                })
                .execute(this);
    }

    public void onPostMulti(View v){
        textView.setText("");

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
                .put("userToken","3b9289a020fd2d53dbcaea50459a0828")
                .put("deviceType",0);
    }
}
