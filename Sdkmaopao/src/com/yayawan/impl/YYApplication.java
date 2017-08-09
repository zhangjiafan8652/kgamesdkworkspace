package com.yayawan.impl;


import com.skymobi.moposnsplatsdk.application.MoposnsPlatApplication;

import android.app.Application;

public class YYApplication extends MoposnsPlatApplication {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
}
