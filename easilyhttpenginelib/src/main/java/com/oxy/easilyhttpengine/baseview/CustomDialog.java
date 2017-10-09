package com.oxy.easilyhttpengine.baseview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.oxy.easilyhttpengine.R;


/**
 * Created by Administrator on 2017/7/10.
 */

public class CustomDialog extends Dialog {
	private FrameLayout contentLayout;
	private View contentView;
	private int width = WindowManager.LayoutParams.WRAP_CONTENT;
	private int height = WindowManager.LayoutParams.WRAP_CONTENT;
	private int gravity = Gravity.CENTER;
	private int x = 0;
	private int y = 0;
	private WindowManager.LayoutParams layoutParams;
	public CustomDialog(Context context) {
		super(context, R.style.my_dialog);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentLayout = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.my_dialog, null);
		setContentView(contentLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		if(contentView == null){
			return;
		}
		if(contentView.getLayoutParams() == null){
			contentView.setLayoutParams(new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.MATCH_PARENT,
					FrameLayout.LayoutParams.MATCH_PARENT
			));
		}
		contentLayout.addView(contentView);

		Window dialogWindow = getWindow();
		if(dialogWindow == null){
			return;
		}

		if(layoutParams != null){
			dialogWindow.setAttributes(layoutParams);
		}else{
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			lp.width = width;
			lp.height = height;
			lp.gravity = gravity;
			lp.x = x;
			lp.y = y;
			dialogWindow.setAttributes(lp);
		}
	}

	public CustomDialog setAttributes(WindowManager.LayoutParams layoutParams){
		this.layoutParams = layoutParams;
		return this;
	}

	public CustomDialog setWidth(int width){
		this.width = width;
		return this;
	}

	public CustomDialog setHeight(int height){
		this.height = height;
		return this;
	}

	public CustomDialog setGravity(int gravity){
		this.gravity = gravity;
		return this;
	}

	public CustomDialog setX(int x){
		this.x = x;
		return this;
	}

	public CustomDialog setY(int y){
		this.y = y;
		return this;
	}

	public View findViewById(int id){
		if(contentView == null){
			return null;
		}
		return contentView.findViewById(id);
	}
	public CustomDialog setContent(int layoutResId){
		return setContent(LayoutInflater.from(getContext()).inflate(layoutResId,null));
	}

	public CustomDialog setContent(View view){
		contentView = view;
		return this;
	}
}
