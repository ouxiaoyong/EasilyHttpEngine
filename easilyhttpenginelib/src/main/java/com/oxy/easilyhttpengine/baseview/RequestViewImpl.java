package com.oxy.easilyhttpengine.baseview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.oxy.easilyhttpengine.AbsHttpCall;
import com.oxy.easilyhttpengine.HttpConfig;
import com.oxy.easilyhttpengine.R;
import com.oxy.easilyhttpengine.log.Logger;

/**
 * Created by Administrator on 2017/8/1.
 */

public class RequestViewImpl implements IRequestView,DialogInterface.OnCancelListener{

    private Dialog loadingDialog;
    private TextView textViewLoading;
    private TextView textViewMessge;
    private Toast toast;
    private AbsHttpCall httpCall;
    private Activity activity;
    public RequestViewImpl(Context context){
        if(context instanceof Activity){
            this.activity = (Activity) context;
        }
    }
    @Override
    public void showLoadingView(HttpConfig config, CharSequence loadingText,AbsHttpCall httpCall) {
        Logger.d("showLoadingView :" + loadingText);
        this.httpCall = httpCall;
        if (activity == null || activity.isFinishing()) {
            return;
        }
        if (config.getLoadingText() == null) {
            return;
        }
        initLoadingDialog();
        textViewLoading.setText(loadingText);
        if(loadingDialog.isShowing()){
            return;
        }
        try{
            Logger.d("------loadingDialog show---------");
            loadingDialog.show();
        }catch (Exception e){

        }
    }

    private void initLoadingDialog(){
        if(loadingDialog == null){
            loadingDialog = DialogFactory.createLoadingDialog(activity);
            loadingDialog.setOnCancelListener(this);
            textViewLoading = (TextView) loadingDialog.findViewById(R.id.dialog_hint);
        }
    }

    @Override
    public void updateLoadingView(HttpConfig config, CharSequence loadingText) {
        if(loadingDialog == null || textViewLoading == null){
            return;
        }
        if(loadingDialog.isShowing()){
            textViewLoading.setText(loadingText);
        }
    }

    @Override
    public final void onCancel(DialogInterface dialog) {
        if(httpCall != null){
            httpCall.cancel();
            httpCall = null;
        }
    }

    @Override
    public void hideLoadingView(HttpConfig config, boolean success) {
        Logger.d("hideLoadingView success:" + success);
        if (activity.isFinishing()) {
            return;
        }
        if(loadingDialog == null || !loadingDialog.isShowing()){
            return;
        }

        try{
            loadingDialog.dismiss();
        }catch (Exception e){

        }
    }

    @Override
    public void showMessgeWhenSuccess(CharSequence msg) {
        Logger.d("showMessgeWhenSuccess msg:" + msg);
        if(TextUtils.isEmpty(msg)){
            return;
        }
        if(toast == null){
            initToast();
        }
        textViewMessge.setText(msg);
        toast.show();
    }

    private void initToast(){
        toast = ToastFactory.createToast(activity);
        textViewMessge = (TextView) toast.getView().findViewById(R.id.message);
    }

    @Override
    public void showMessgeWhenFailure(CharSequence msg) {
        Logger.d("showMessgeWhenFailure msg:" + msg);
        if(TextUtils.isEmpty(msg)){
            return;
        }
        if(toast == null){
            initToast();
        }
        textViewMessge.setText(msg);
        toast.show();
    }
}
