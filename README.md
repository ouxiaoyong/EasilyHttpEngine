# PowerfulHttpEngine
一款基于okhttp库的http/https引擎库.
本库支持http库的切换，本库包含数据的加解密，数据的序列化，多个请求串联。

# 用法
在项目的build.gradle中加入以下代码
```
allprojects{
	repositories {
		maven {
			url "https://jitpack.io"
		}
	}
}

```
 然后在module的build.gradle中加入以下代码
```
dependencies{
	compile 'com.github.ouxiaoyong:PowerfulHttpEngine:1.0.2-alpha1'
}

```
你可以在http工具类中初始化一个HttpEngine
```
IHttpEngine httpEngine = new HttpEngine.Builder()
            .setServerURL("http://test.cus.lianbaowang.com/interface.php")
            .setParamsKey("data")
            .setUploadFileDispositionName("image")
            .setDefaultResponseHandler(new DefaultResponseHandler(false))
            .setDataCipher(new AesCipher())
            .create();

```
* setServerURL：设置默认的服务器地址
* setDataCipher：设置默认的加密解密器IDataCipher
* setParamsKey：设置请求参数数据包的key
* setUploadFileDispositionName：设置默认的上传文件的dispositionName
* setDefaultResponseHandler：设置默认的响应数据的解析处理 IResponseHandler


IHttpEngine.java
```
void httpPost(HttpConfig config, HttpParams params, HashMap<String, String> notEncryptParams, IHttpListener listener);
```

例如：后台需要我传入一个未加密的字段apiName
在工具类中封装了如下方法,将不需要加密的参数放入HashMap中，没有未加密参数则为null
```
public static void httpPost(HttpConfig config, HttpParams params,
                                      IHttpListener listener) {
        HashMap<String,String> unEncryptParams = new HashMap<>();
        unEncryptParams.put("apiName",params.getString(KEY_API_NAME));
        httpEngine.httpPost(config, params, unEncryptParams,listener);
    }
```

Get请求
```
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
```

Post请求
```
public void onPost(View v){
        textView.setText("");
        HttpConfig config = new HttpConfig(this);
        config.setLoadingText("获取联保地址");
        config.rqtCode = 10;
        config.typeClass = AddressInfo.class;
        HttpParams params = createHttpParams("getRepairerAddressList");
        HttpTools.httpPost(config,params,listener);
    }
```
上传文件
```
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
```

多个请求串联
```
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
```
更多用法请参见项目中的sample