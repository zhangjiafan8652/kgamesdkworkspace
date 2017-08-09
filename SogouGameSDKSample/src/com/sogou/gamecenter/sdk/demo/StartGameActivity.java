package com.sogou.gamecenter.sdk.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.sogou.gamecenter.sdk.SogouGamePlatform;
import com.sogou.gamecenter.sdk.bean.UserInfo;
import com.sogou.gamecenter.sdk.listener.OnExitListener;
import com.sogou.gamecenter.sdk.listener.SwitchUserListener;
import com.sogou.gamecenter.sdk.views.FloatMenu;

/**
 * SDK登录流程结束后(SDK已经处于登录态)，一般游戏会在这选区服进入游戏操作
 */
public class StartGameActivity extends FragmentActivity {

	private static final String TAG = StartGameActivity.class.getSimpleName();
	private SogouGamePlatform mSogouGamePlatform = SogouGamePlatform.getInstance();
	/**
	 * 浮动菜单接入说明：浮动菜单在当前界面创建后 注意： 1.当前界面切换到前台，调用浮动菜单show方法
	 * 2.当前界面切换到后台，调用浮动菜单hide方法 3.浮层有切换帐号功能，请在show方法后设置帐号变化监听器或者设置切换帐号监听器
	 */
	private FloatMenu mFloatMenu;
	private Button start;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_start);
		/**
		 * 开始游戏操作 有些游戏方在此做登录流程操作，不建议在此做
		 * SDK不处于登录态情况下，后续接口无法使用，后续接口务必在SDK登录态下使用（包括浮动菜单、支付、普通切换帐号接口等）
		 * 进入该界面，login流程已经走完，SDK已经处于最新登录态
		 */
		start = (Button) findViewById(R.id.start_game);
		mSogouGamePlatform.openLogInfo();

		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSogouGamePlatform.isLogin(StartGameActivity.this)) {
					// 进入游戏主界面
					Intent intent = new Intent(StartGameActivity.this, HomeActivity.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(StartGameActivity.this, "请先登录账号！", Toast.LENGTH_LONG).show();
				}
			}
		});

		// 创建浮动菜单务必在SDK登台态情况下 当前是全屏模式，isFullscreen为true
		mFloatMenu = mSogouGamePlatform.createFloatMenu(this, true);
	}

	/**
	 * 游戏方自定义切换账号UI入口,可以调用切换账号接口
	 * 
	 * @param view
	 */
	public void switchUser(View view) {
		mSogouGamePlatform.switchUser(this, new SwitchUserListener() {
			@Override
			public void switchSuccess(int code, UserInfo userInfo) {
				Log.d(TAG, "switchSuccess code:" + code + " userInfo:" + userInfo);
			}

			@Override
			public void switchFail(int code, String msg) {
				Log.e(TAG, "switchFail code:" + code + " msg:" + msg);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 当前界面切换到前台，调用浮动菜单show方法
		if (mFloatMenu != null) {
			// 默认浮在右上角位置，距左边为10，距下边为100位置，单位为像素
			mFloatMenu.setParamsXY(10, 100);
			mFloatMenu.show();
			// 浮动设置切换帐号监听器
			mFloatMenu.setSwitchUserListener(new SwitchUserListener() {
				@Override
				public void switchSuccess(int code, UserInfo userInfo) {
					Log.d(TAG, "FloatMenus witchSuccess code:" + code + " userInfo:" + userInfo);
				}

				@Override
				public void switchFail(int code, String msg) {
					Log.e(TAG, "FloatMenus switchFail code:" + code + " msg:" + msg);
				}
			});
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mFloatMenu != null)
			mFloatMenu.hide();
	}

	public static void actionStartGameActivity(Context ctx) {
		Intent intent = new Intent(ctx, StartGameActivity.class);
		ctx.startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		/*
		 * 演示退出接口使用 在游戏退出前，请调用sdk退出接口，当收到OnExitListener对象回调
		 * onCompleted方法执行真正游戏退出操作
		 */
		mSogouGamePlatform.exit(new OnExitListener(this) {

			@Override
			public void onCompleted() {
				// 游戏方执行退出游戏操作
				finish();
			}
		});
	}
}
