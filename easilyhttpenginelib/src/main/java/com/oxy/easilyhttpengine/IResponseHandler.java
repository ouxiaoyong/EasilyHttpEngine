package com.oxy.easilyhttpengine;

/**
 * Created by Administrator on 2017/7/21.
 */

import java.util.List;

/**
 *
 * @param <T> 要将服务器返回的字符串转换成数据格式，例如：JSONObject、JSONArray或者String
 */
public interface IResponseHandler<T> {

    //服务器返回的字符串转换成数据格式T
    T parseResponseObject(String response) throws Exception;
    //判断业务是否成功
    boolean isSuccess(T respObject);
    //获取成功或失败的提示内容
    String getMessge(T respObject, boolean success);

    //返回反序列化的数据字符串
    String getDataString(T respObject);

    //将数据字符串反序列化
    <K> K parseDataString2Object(Class<K> typeClass, String dataString);

    <K> List<K> parseDataString2List(Class<K> typeClass, String dataString);
    boolean isListData();
}
