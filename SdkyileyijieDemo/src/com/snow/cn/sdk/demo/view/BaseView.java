/**
 * Copyright (c) 2013 DuoKu Inc.
 */
package com.snow.cn.sdk.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class BaseView extends RelativeLayout{
	public Context mContext;
	
	public BaseView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}

	public BaseView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public BaseView(Context context) {
		super(context);
	}

	public final int LOGIN_FRAGMENT_VIEW = 0;
	public final int CHARGE_FRAGMENT_VIEW = 1;
	public interface ChangeViewListener {
		void ChangeViewListener(int index);
	}
	public ChangeViewListener mChangeListener;
	public void setChangeListener(ChangeViewListener l){
		mChangeListener = l;
	}
	
}
