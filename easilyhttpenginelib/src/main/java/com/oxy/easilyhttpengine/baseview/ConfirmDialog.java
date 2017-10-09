package com.oxy.easilyhttpengine.baseview;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.oxy.easilyhttpengine.R;

/**
 * Created by Administrator on 2017/7/11.
 */

public class ConfirmDialog extends CustomDialog {
	private View.OnClickListener cancelListener;
	private View.OnClickListener okListener;
	public ConfirmDialog(Context context) {
		super(context);
		setContent(R.layout.confirm_dialog);
		setCancelable(false);
		findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cancelListener != null){
					cancelListener.onClick(v);
				}
				dismiss();
			}
		});
		findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(okListener != null){
					okListener.onClick(v);
				}
				dismiss();
			}
		});
	}

	public ConfirmDialog setTitle(String title){
		TextView tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText(title);
		return this;
	}

	public ConfirmDialog setContentMsg(String msg){
		TextView tvMsg = (TextView) findViewById(R.id.tv_msg);
		tvMsg.setText(msg);
		return this;
	}

	@Override
	public ConfirmDialog setWidth(int width) {
		super.setWidth(width);
		return this;
	}

	@Override
	public ConfirmDialog setHeight(int height) {
		super.setHeight(height);
		return this;
	}

	@Override
	public ConfirmDialog setGravity(int gravity) {
		super.setGravity(gravity);
		return this;
	}

	@Override
	public ConfirmDialog setX(int x) {
		super.setX(x);
		return this;
	}

	@Override
	public ConfirmDialog setY(int y) {
		super.setY(y);
		return this;
	}

	public ConfirmDialog setOkListener(View.OnClickListener listener){
		okListener = listener;
		return this;
	}

	public ConfirmDialog setCancelListener(View.OnClickListener listener){
		cancelListener = listener;
		return this;
	}
}
