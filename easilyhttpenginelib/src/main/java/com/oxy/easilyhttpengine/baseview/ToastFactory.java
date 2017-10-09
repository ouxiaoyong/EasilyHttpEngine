package com.oxy.easilyhttpengine.baseview;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.oxy.easilyhttpengine.R;

/**
 * Created by Administrator on 2017/7/28.
 */

public class ToastFactory {
    public static Toast createToast(Context con) {
        DisplayMetrics metrics = con.getResources().getDisplayMetrics();
        Toast toast = new Toast(con);
        View v = LayoutInflater.from(con).inflate(R.layout.toast_layout,
                null);
        TextView tv = (TextView) v.findViewById(R.id.message);
        tv.setMaxHeight((int) (metrics.heightPixels - 80 * metrics.density));
        tv.setMaxWidth((int) (metrics.widthPixels - 40 * metrics.density));

        toast.setView(v);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL,
                0, (int) (30 * metrics.density));
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }

    public static void showToast(Context context, String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        Toast toast = createToast(context);
        TextView tvTostMsg = (TextView) toast.getView().findViewById(R.id.message);
        tvTostMsg.setText(text);
        toast.show();
    }
}
