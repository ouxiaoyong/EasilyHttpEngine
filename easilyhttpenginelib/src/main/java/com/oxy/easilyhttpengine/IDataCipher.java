package com.oxy.easilyhttpengine;

/**
 * Created by Administrator on 2017/7/20.
 */

public interface IDataCipher {

    /**
     * 解密
     * @param dataSource
     * @return
     */
    String decrypt(String dataSource) throws Exception;

    /**
     * 加密
     * @param dataSource
     * @return
     */
    String encrypt(String dataSource) throws Exception;
}
