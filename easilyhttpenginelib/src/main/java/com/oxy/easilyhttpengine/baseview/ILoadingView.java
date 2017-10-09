package com.oxy.easilyhttpengine.baseview;

/**
 * Created by Administrator on 2017/8/2.
 */

public interface ILoadingView {
    /**
     * 显示正在加载的view
     */
    void showLoadingView();

    /***
     * 显示加载失败或者未加载的view
     */
    void showNodataView();

    /**
     * 显示加载成功的view
     */
    void showContentView();

}
