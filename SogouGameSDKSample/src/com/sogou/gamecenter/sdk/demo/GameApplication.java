package com.sogou.gamecenter.sdk.demo;

import com.sogou.gamecenter.sdk.SogouGamePlatform;
import com.sogou.gamecenter.sdk.bean.SogouGameConfig;

import android.app.Application;
import android.util.Log;

public class GameApplication extends Application {
	private static final String TAG = GameApplication.class.getSimpleName();
	// 防止内存临界时，垃圾回收了SogouGamePlatform对象
	private SogouGamePlatform mSogouGamePlatform = SogouGamePlatform.getInstance();
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG,"GameApplication");
		// 配置游戏信息（gid、appKey由搜狗游戏平台统一分配）
		SogouGameConfig config = new SogouGameConfig();		
		// 开发模式为true，false是正式环境
	    // 请注意，提交版本设置正式环境 
		config.devMode = false;
		config.gid = 100;
		config.appKey="f0da851f02e56c515fca559ac8af9251b7baf6fdc1783fca112467e180380cad";
		config.gameName="搜狗游戏demo";
		// SDK准备初始化
		mSogouGamePlatform.prepare(this, config);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.d(TAG,"onTerminate");
		// 防止内存泄露，清理相关数据务必调用SDK结束接口
		mSogouGamePlatform.onTerminate();
	}
}
