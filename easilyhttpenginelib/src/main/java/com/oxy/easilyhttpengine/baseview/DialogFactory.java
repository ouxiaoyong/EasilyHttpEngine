package com.oxy.easilyhttpengine.baseview;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.oxy.easilyhttpengine.R;


/**
 * Created by Administrator on 2017/7/28.
 */

public class DialogFactory {
    public static Dialog createLoadingDialog(final Activity activity) {
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        CustomDialog loadingDialog = new CustomDialog(activity).setContent(R.layout.loading_dialog_layout);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCancelable(true);
        final TextView tvTip = (TextView) loadingDialog.findViewById(R.id.dialog_hint);
        tvTip.setMaxWidth((int) (metrics.widthPixels - 60 * metrics.density));
        tvTip.setMaxHeight((int) (metrics.widthPixels - 60 * metrics.density));
        loadingDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                String text = tvTip.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    tvTip.setVisibility(View.GONE);
                } else {
                    tvTip.setVisibility(View.VISIBLE);
                }
            }
        });

        return loadingDialog;
    }
}
