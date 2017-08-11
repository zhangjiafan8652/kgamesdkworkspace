package com.yayawan.impl;


import com.snowfish.cn.ganga.helper.SFOnlineApplication;

import android.app.Application;

public class YYApplication extends SFOnlineApplication {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
}
