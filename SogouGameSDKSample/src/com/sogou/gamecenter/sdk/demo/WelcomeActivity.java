package com.sogou.gamecenter.sdk.demo;

import com.sogou.gamecenter.sdk.SogouGamePlatform;
import com.sogou.gamecenter.sdk.bean.UserInfo;
import com.sogou.gamecenter.sdk.listener.InitCallbackListener;
import com.sogou.gamecenter.sdk.listener.LoginCallbackListener;
import com.sogou.gamecenter.sdk.util.Logger;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * 游戏启动第一入口界面
 */
public class WelcomeActivity extends Activity {

	public static final String TAG = "WelcomeActivity";
	private SogouGamePlatform mSogouGamePlatform = SogouGamePlatform.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		// 发布正式版本，请注释掉打印日志功能
		mSogouGamePlatform.openLogInfo();
		// SDK初始化，整个接入流程只调用一次
		mSogouGamePlatform.init(this, new InitCallbackListener() {

			@Override
			public void initSuccess() {
				mSogouGamePlatform.login(WelcomeActivity.this, new LoginCallbackListener() {

					@Override
					public void loginSuccess(int code, UserInfo userInfo) {
						Log.d(TAG, "CP loginSuccess:" + userInfo);
						gameLogin(WelcomeActivity.this, userInfo);
					}

					@Override
					public void loginFail(int code, String msg) {
						Log.d(TAG, "CP loginFail:" + msg);
					}
				});

			}

			@Override
			public void initFail(int code, String msg) {
				// SDK初始化失败再此关闭游戏即可
				Toast.makeText(WelcomeActivity.this, msg, Toast.LENGTH_LONG).show();
				finish();
			}
		});
	}

	// SDK已经处于登录态，后续逻辑由游戏方控制
	public static void gameLogin(Activity activity, UserInfo userInfo) {
		// 一般会在此进行登录游戏服务器，登录成功后进入选区服界面
		if (activity != null && userInfo != null) {
			StartGameActivity.actionStartGameActivity(activity);
			activity.finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Logger.d(TAG, "orientation newConfig");
	}
}
