package com.yayawan.implyy;

import com.yayawan.main.YaYaWan;
import com.yayawan.sdk.jfutils.Yayalog;
import com.yayawan.sdk.main.YayaWan;

import android.app.Application;

public class YYApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Yayalog.loger("YYApplication");
	}
}
