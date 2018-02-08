package com.oxy.easilyhttpengine.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.oxy.easilyhttpengine.R;
import com.oxy.easilyhttpengine.baseview.DialogFactory;
import com.oxy.easilyhttpengine.baseview.ToastFactory;
import com.oxy.easilyhttpengine.log.Logger;

/**
 * Created by BelleOU on 18/1/30.
 */

public class RequestViewImp implements IRequestView {
    private Dialog loadingDialog;
    private TextView textViewLoading;
    private TextView toastTextViewMessge;
    private TextView dialogTextViewMessge;
    private Dialog msgDialog;
    private Toast toast;
    private Activity activity;
    private AbsRequest request;
    public RequestViewImp(Activity activity){
        this.activity = activity;
    }
    private void initLoadingDialog(){
        if(loadingDialog == null){
            loadingDialog = DialogFactory.createLoadingDialog(activity);
            loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if(request != null){
                        request.cancel();
                    }
                }
            });
            textViewLoading = (TextView) loadingDialog.findViewById(R.id.dialog_hint);
        }
    }

    private void initMsgDialog(){
        if(msgDialog == null){
        }
    }

    private void initToast(){
        if(toast == null){
            toast = ToastFactory.createToast(activity);
            toastTextViewMessge = (TextView) toast.getView().findViewById(R.id.message);
        }
    }

    @Override
    public void showLoadingView(AbsRequest request,CharSequence loadingText) {
        if (activity.isFinishing()) {
            request = null;
            return;
        }
        this.request = request;
        initLoadingDialog();
        textViewLoading.setText(loadingText);

        try {
            loadingDialog.show();
        }catch (Exception e){

        }
    }

    @Override
    public void hideLoadingView() {
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
    public void showNodataView(AbsRequest request) {

    }

    @Override
    public void showContentView() {

    }

    @Override
    public void showMessgeWhenSuccess(boolean useDialogStyle, CharSequence msg) {
        Logger.d("showMessgeWhenSuccess msg:" + msg);
        if(TextUtils.isEmpty(msg)){
            return;
        }
        if(useDialogStyle){
            initMsgDialog();
            dialogTextViewMessge.setText(msg);
            try {
                msgDialog.show();
            }catch (Exception e){
            }
        }else{
            initToast();

            toastTextViewMessge.setText(msg);
            toast.show();
        }
    }

    @Override
    public void showMessgeWhenFailure(boolean useDialogStyle, CharSequence msg) {
        Logger.d("showMessgeWhenFailure msg:" + msg);
        if(TextUtils.isEmpty(msg)){
            return;
        }
        if(useDialogStyle){
            initMsgDialog();
            dialogTextViewMessge.setText(msg);
            try {
                msgDialog.show();
            }catch (Exception e){
            }
        }else{
            initToast();

            toastTextViewMessge.setText(msg);
            toast.show();
        }

    }
}
