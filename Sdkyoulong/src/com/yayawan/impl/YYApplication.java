package com.yayawan.impl;


import com.ylwl.supersdk.api.YLSuperApp;

import android.app.Application;

public class YYApplication extends YLSuperApp {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
}
