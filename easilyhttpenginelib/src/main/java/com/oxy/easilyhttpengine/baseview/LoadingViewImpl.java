package com.oxy.easilyhttpengine.baseview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.oxy.easilyhttpengine.R;

/**
 * Created by Administrator on 2017/8/2.
 */

public class LoadingViewImpl implements ILoadingView {
    private View contentView;
    private View nodataView;
    private TextView hintTextView;
    private View.OnClickListener clickListener;
    public LoadingViewImpl(View contentView) {
        this.contentView = contentView;
        nodataView = LayoutInflater.from(contentView.getContext()).inflate(
                R.layout.nodata_layout, null);
        ViewGroup parent = (ViewGroup) contentView.getParent();
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        parent.removeView(contentView);

        FrameLayout frameLayout = new FrameLayout(contentView.getContext());
        frameLayout.setId(contentView.getId());
        frameLayout.addView(contentView, createLayoutParams());
        frameLayout.addView(nodataView, createLayoutParams());
        parent.addView(frameLayout, layoutParams);
        hintTextView = (TextView) nodataView.findViewById(R.id.tv_loading_msg);
        nodataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListener != null){
                    clickListener.onClick(v);
                }
            }
        });
        showNodataView();
    }

    public void setLoadingListener(View.OnClickListener clickListener){
        this.clickListener = clickListener;
    }

    private FrameLayout.LayoutParams createLayoutParams() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void showLoadingView() {
        nodataView.setEnabled(false);
        contentView.setVisibility(View.GONE);
        nodataView.setVisibility(View.VISIBLE);
        hintTextView.setText("正在加载数据，请稍后...");
    }

    @Override
    public void showNodataView() {
        nodataView.setEnabled(true);
        contentView.setVisibility(View.GONE);
        nodataView.setVisibility(View.VISIBLE);
        hintTextView.setText("点击重新加载");
    }

    @Override
    public void showContentView() {
        nodataView.setEnabled(false);
        contentView.setVisibility(View.VISIBLE);
        nodataView.setVisibility(View.GONE);
    }
}
