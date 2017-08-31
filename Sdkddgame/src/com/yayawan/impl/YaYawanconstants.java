package com.yayawan.impl;

import java.util.HashMap;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ddgame.callback.YYWPayCallBack;
import com.ddgame.callback.YYWUserCallBack;
import com.ddgame.domain.YYWOrder;
import com.ddgame.main.Dgame;
import com.ddgame.main.Kgame;

import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;


public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit=false;
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		Dgame.getInstance().initSdk(mactivity);
	}
	
	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		Dgame.getInstance().login(mactivity, new YYWUserCallBack() {
			
			@Override
			public void onLogout(Object arg0) {
				// TODO Auto-generated method stub
				loginOut();
			}
			
			@Override
			public void onLoginSuccess(com.ddgame.domain.YYWUser user, Object arg1) {
				// TODO Auto-generated method stub
				loginSuce(mactivity, user.uid, user.userName, user.token);
			}
			
			@Override
			public void onLoginFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				loginFail();
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				loginFail();
			}
		});
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {

		Yayalog.loger("YaYawanconstantssdk支付");
		
		
		YYWOrder order = new YYWOrder(UUID.randomUUID().toString(), "大罗鞭",
				10l, "");

		Dgame.getInstance().pay(mactivity, order,new YYWPayCallBack() {
			
			@Override
			public void onPaySuccess(com.ddgame.domain.YYWUser arg0, YYWOrder arg1,
					Object arg2) {
				// TODO Auto-generated method stub
				paySuce();
			}
			
			@Override
			public void onPayFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPayCancel(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	/**
	 * 退出
	 * 
	 * @param paramActivity
	 * @param callback
	 */
	public static void exit(Activity paramActivity,
			final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");

		Dgame.getInstance().exit(paramActivity, new com.ddgame.callback.YYWExitCallback() {
			
			@Override
			public void onExit() {
				// TODO Auto-generated method stub
				callback.onExit();
			}
		});
		//

	}

	/**
	 * 设置角色信息
	 * 
	 * @param arg0
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		// TODO Auto-generated method stub
		Yayalog.loger("YaYawanconstants设置角色信息");
		Dgame.getInstance().setData(paramActivity, roleId, roleName, roleLevel, zoneId, zoneName, roleCTime, ext);
	}
	public static void onResume(Activity paramActivity) {
		// TODO Auto-generated method stub
		Dgame.getInstance().onResume(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub
		Dgame.getInstance().onPause(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub
		Dgame.getInstance().onDestroy(paramActivity);
	}
	
	public static void onActivityResult(Activity paramActivity, int paramInt1, int paramInt2, Intent paramIntent) {
		// TODO Auto-generated method stub
		Dgame.getInstance().onActivityResult(paramActivity, paramInt1, paramInt2, paramIntent);
	}

	public static void onNewIntent(Intent paramIntent) {
		// TODO Auto-generated method stub
		Dgame.getInstance().onNewIntent(paramIntent);
	}

	public static void onStart(Activity mActivity2) {
		// TODO Auto-generated method stub
		Dgame.getInstance().onStop(mActivity2);
	}

	public static void onRestart(Activity paramActivity) {
		// TODO Auto-generated method stub
		Dgame.getInstance().onRestart(paramActivity);
	}

	public static void onCreate(Activity paramActivity) {
		// TODO Auto-generated method stub
		Dgame.getInstance().onCreate(paramActivity);
	}

	public static void onStop(Activity paramActivity) {
		// TODO Auto-generated method stub
		Dgame.getInstance().onStop(paramActivity);
	}

	

	/**
	 * 登录成功调用
	 * 
	 * @param mactivity
	 * @param uid
	 *            唯一id
	 * @param username
	 *            用户名..如果用户名为空.则拿uid作为用户名
	 * @param session
	 *            token验证码
	 */
	public static void loginSuce(Activity mactivity, String uid,
			String username, String session) {

		YYWMain.mUser = new YYWUser();

		YYWMain.mUser.uid = DeviceUtil.getGameId(mactivity) + "-" + uid + "";
		;
		if (username != null) {
			YYWMain.mUser.userName = DeviceUtil.getGameId(mactivity) + "-"
					+ username + "";
		} else {
			YYWMain.mUser.userName = DeviceUtil.getGameId(mactivity) + "-"
					+ uid + "";
		}

		// YYWMain.mUser.nick = data.getNickName();
		YYWMain.mUser.token = JSONUtil.formatToken(mactivity, session,
				YYWMain.mUser.userName);

		if (YYWMain.mUserCallBack != null) {
			YYWMain.mUserCallBack.onLoginSuccess(YYWMain.mUser, "success");
			Handle.login_handler(mactivity, YYWMain.mUser.uid,
					YYWMain.mUser.userName);
		}
	}

	
	/**
	 * 登出
	 */
	public static void loginOut() {
		if (YYWMain.mUserCallBack != null) {
			YYWMain.mUserCallBack.onLogout(null);

		}
	}
	/**
	 * 登录失败
	 */
	public static void loginFail() {
		if (YYWMain.mUserCallBack != null) {
			YYWMain.mUserCallBack.onLoginFailed(null, null);

		}
	}

	/*
	 * 支付成功
	 */
	public static void paySuce() {
		// 支付成功
		if (YYWMain.mPayCallBack != null) {
			YYWMain.mPayCallBack.onPaySuccess(YYWMain.mUser, YYWMain.mOrder,
					"success");
		}
	}

	public static void payFail() {
		// 支付成功
		if (YYWMain.mPayCallBack != null) {
			YYWMain.mPayCallBack.onPayFailed(null, null);
		}
	}

	
	

	

}
