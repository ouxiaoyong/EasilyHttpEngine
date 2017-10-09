package com.oxy.easilyhttpengine;

/**
 * Created by Administrator on 2017/8/3.
 */

public class DoNothingDataCipher implements IDataCipher {
    @Override
    public String decrypt(String dataSource) throws Exception {
        return dataSource;
    }

    @Override
    public String encrypt(String dataSource) throws Exception {
        return dataSource;
    }
}
