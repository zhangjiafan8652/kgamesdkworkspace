/**
 * Copyright (c) 2013 DuoKu Inc.
 */

package com.snow.cn.sdk.demo.activity;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.snow.cn.sdk.demo.utils.LoginHelper;
import com.snow.cn.sdk.demo.view.UserLoginView;
import com.snowfish.cn.ganga.base.SFConst;
import com.snowfish.cn.ganga.helper.SFOnlineExitListener;
import com.snowfish.cn.ganga.helper.SFOnlineHelper;

public class DemoMainActivity extends Activity implements com.snow.cn.sdk.demo.view.BaseView.ChangeViewListener{
	FrameLayout mViewContainer;
	private ChargeView mChargeView;
	private UserLoginView mUserLoginView;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demomain);
		mViewContainer = (FrameLayout) findViewById(R.id.main_viewpager);
		LayoutInflater inflater = LayoutInflater.from(this);
		
		mUserLoginView = (UserLoginView)inflater.inflate(R.layout.userloginlayout, null);
		mUserLoginView.setChangeListener(this);
		mViewContainer.addView(mUserLoginView);
		
		mChargeView = (ChargeView)inflater.inflate(R.layout.chargeviewlayout, null);
		mChargeView.setChangeListener(this);
		initSDK();
	}

	/**
	 * initSDK
	 * 初始化SDK
	 */
	private void initSDK() {
		//onCreate方法用于需要在游戏主Activity中的onCreate中调用，只需调用一次
		SFOnlineHelper.onCreate(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		//在游戏Activity中的onStop中调用
		SFOnlineHelper.onStop(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//在游戏Activity中的onDestroy中调用
		SFOnlineHelper.onDestroy(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		//在游戏Activity中的onResume中调用
		SFOnlineHelper.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		//在游戏Activity中的onPause中调用
		SFOnlineHelper.onPause(this);
	}
	@Override
	public void onRestart() {
		super.onRestart();
		//在游戏Activity中的onRestart中调用
		SFOnlineHelper.onRestart(this);
	}
	@Override
	public void onBackPressed() {
		// exit方法用于系统全局退出
		/*public static void exit(Activity context, SFOnlineExitListener listener)
		 *  @param context   上下文Activity 
		 *  @param listener  退出回调函数
		 */
		SFOnlineHelper.exit(this, new SFOnlineExitListener() {
			/*  onSDKExit
			 *  @description　当SDK有退出方法及界面，回调该函数
			 *  @param bool   是否退出标志位  
			 */
			@Override
			public void onSDKExit(boolean bool) {
				if (bool){
					//apk退出函数，demo中也有使用System.exit()方法；但请注意360SDK的退出使用exit（）会导致游戏退出异常
					finish();
				}
			}
			/*  onNoExiterProvide
			 *  @description　SDK没有退出方法及界面，回调该函数，可在此使用游戏退出界面
			 */
			@Override
			public void onNoExiterProvide() {
				AlertDialog.Builder builder = new Builder(DemoMainActivity.this);
				builder.setTitle("游戏自带退出界面");
				builder.setPositiveButton("退出",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						DemoMainActivity.this.finish();
						//								System.exit(0);
					}
				});
				builder.show();
			}
		});
	}
	
	@Override
	public void ChangeViewListener(int index) {
		mViewContainer.removeAllViews();
		switch (index) {
		case 0:
			mViewContainer.addView(mUserLoginView);
			break;
			
		case 1:
			mViewContainer.addView(mChargeView);
			break;
		}
	}

	/**
	 * 点击menu按钮时
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "查看版本信息"); // 添加选项
		return true;
	}

	/**
	 * 点击menu菜单中某一个选项时
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
		  showVersionInfo();
		}
		return super.onOptionsItemSelected(item);
	}

	private void showVersionInfo() {
		String text = "登录验证：";
		String orderText = "订单生成：";
		String loginCheckText = "未知";
		String orderServerText = "未知";
		if(LoginHelper.CP_LOGIN_CHECK_URL.equals("http://testomsdk.xiaobalei.com:5555/cp/user/paylog/checkLogin")){
			 loginCheckText = "测服";
			
		}else if(LoginHelper.CP_LOGIN_CHECK_URL.equals("http://testomsdk.xiaobalei.com:5555/cp/user/paylog/checkLoginP")){
			 loginCheckText = "正服";
			
		}else if(LoginHelper.CP_LOGIN_CHECK_URL.equals("http://testomsdk.xiaobalei.com:5555/cp/user/paylog/checkLoginZijian")){
			 loginCheckText = "自建服务器";
				
		}
		String order = new String(SFConst.ORDER_URL);
		if(order.equals("http://testomsdk.xiaobalei.com:5555/service/fs/order")){
			orderServerText = "测服";
		}else if(order.equals("http://service.1sdk.cn/fs/order")){
			orderServerText = "正服";
		}
		
		String showText = text+loginCheckText+" "+orderText+orderServerText;
		Toast.makeText(this, showText ,Toast.LENGTH_SHORT).show();
		
		
	}
}
