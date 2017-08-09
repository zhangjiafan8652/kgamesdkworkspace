package com.yayawan.sdk.main;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.Toast;

import com.blankj.utilcode.utils.Utils;
import com.yayawan.main.YYWMain;
import com.yayawan.sdk.asynchttp.AsyncHttpConnection;
import com.yayawan.sdk.asynchttp.StringResponseHandler;
import com.yayawan.sdk.asynchttp.support.ParamsWrapper;
import com.yayawan.sdk.base.AgentApp;
import com.yayawan.sdk.callback.YayaWanCallback;
import com.yayawan.sdk.callback.YayaWanPaymentCallback;
import com.yayawan.sdk.callback.YayaWanUpdateCallback;
import com.yayawan.sdk.callback.YayaWanUserCallback;
import com.yayawan.sdk.callback.YayawanStartAnimationCallback;
import com.yayawan.sdk.domain.Order;
import com.yayawan.sdk.domain.RoleData;
import com.yayawan.sdk.domain.User;
import com.yayawan.sdk.jflogin.BaseLogin_Activity;
import com.yayawan.sdk.jflogin.HelpActivty;
import com.yayawan.sdk.jflogin.InitSdk;
import com.yayawan.sdk.jflogin.Login_demo_dialog;
import com.yayawan.sdk.jflogin.Login_ho_dialog;
import com.yayawan.sdk.jflogin.Startlogin_dialog;
import com.yayawan.sdk.jflogin.ViewConstants;
import com.yayawan.sdk.jfpay.Yayapaystart_dialog;
import com.yayawan.sdk.jfutils.Logger;
import com.yayawan.sdk.jfutils.LogoWindow;
import com.yayawan.sdk.jfutils.Sputils;
import com.yayawan.sdk.jfutils.Yayalog;
import com.yayawan.sdk.utils.CryptoUtil;
import com.yayawan.sdk.utils.DeviceUtil;
import com.yayawan.sdk.utils.RSACoder;
import com.yayawan.sdk.utils.UpdateUtil;
import com.yayawan.sdk.utils.UrlUtil;

/**
 * 供用户调用的方法都在这个类中,设置成废弃，防止接入sdk的时候YaYawan写成这个
 * 
 * 
 * @author wjy
 * 
 */
@Deprecated
public class YayaWan {

	public static YayaWanUserCallback mUserCallback; // 登录的回调

	public static YayaWanPaymentCallback mPaymentCallback; // 支付回调

	public static YayawanStartAnimationCallback mStartAnimationCallback; // 开始动画回调

	public static YayaWanCallback mCallback;

	public static YayaWanUpdateCallback mUpdateCallback;

	public static Order mPayOrder; // 订单

	/**
	 * 丫丫玩动画的调用
	 * 
	 * @param paramActivity
	 * @param paramCallback
	 */
	public static void animation(Activity paramActivity,
			YayawanStartAnimationCallback paramCallback) {
		mStartAnimationCallback = paramCallback;
		Intent intent = new Intent(paramActivity.getApplicationContext(),
				BaseLogin_Activity.class);

		intent.putExtra("type", ViewConstants.STARTANIMATION);
		paramActivity.startActivityForResult(intent, 10200);

	} 

	/**
	 * 登录接口
	 * 
	 * @param paramActivity
	 * @param paramCallback
	 */
	public static void login(Activity paramActivity,
			YayaWanUserCallback paramCallback) {
		mUserCallback = paramCallback;
		ViewConstants.mMainActivity = paramActivity;

		Startlogin_dialog startlogin_dialog = new Startlogin_dialog(
				paramActivity);
		startlogin_dialog.dialogShow();

	}

	/**
	 * demo登陸接口
	 * 
	 * @param paramActivity
	 * @param paramCallback
	 */
	public static void loginDemo(Activity paramActivity,
			YayaWanUserCallback paramCallback) {
		mUserCallback = paramCallback;
		ViewConstants.mMainActivity = paramActivity;

		Login_demo_dialog login_demo_dialog = new Login_demo_dialog(
				paramActivity);
		login_demo_dialog.dialogShow();

	}

	/**
	 * 支付接口
	 * 
	 * @param paramActivity
	 * @param paramCallback
	 */
	public static void payment(Activity paramActivity, Order paramOrder,Boolean issinglepay,
			YayaWanPaymentCallback paramCallback) {
		if (AgentApp.mUser == null) {
			Toast.makeText(paramActivity.getApplicationContext(), "请先登录",
					Toast.LENGTH_SHORT).show();
			return;
		}
		mPaymentCallback = paramCallback;

		mPayOrder = paramOrder;

		AgentApp.mPayOrder = paramOrder;

		
	
	        if (issinglepay) {
	        	//如果是单机直接跳往支付界面,丫丫玩则是启动丫丫币支付,单机支付为1，否则为0
	        	
	        	Sputils.putSPint("issinglepay", 1,
	  					paramActivity);
	        	//单机
	        	Intent intent = new Intent(paramActivity,
	    				BaseLogin_Activity.class);

	    		intent.putExtra("type", ViewConstants.YAYAPAYMAIN);
	    		paramActivity.startActivity(intent);
			}else {
				Sputils.putSPint("issinglepay", 0,
		  					paramActivity);
				if (com.yayawan.utils.DeviceUtil.getUnionid(paramActivity).equals("1354981926")) {
					Yayalog.loger("丫丫玩的包");
					Yayapaystart_dialog yayapaystart_dialog = new Yayapaystart_dialog(
							paramActivity);
					yayapaystart_dialog.dialogShow();
				}else {
					Intent intent = new Intent(paramActivity,
		    				BaseLogin_Activity.class);

		    		intent.putExtra("type", ViewConstants.YAYAPAYMAIN);
		    		paramActivity.startActivity(intent);
				}
				
				
			}
		
		
		
	}
	
	/**
	 * 支付接口
	 * 
	 * @param paramActivity
	 * @param paramCallback
	 */
	public static void paymentLianhe(Activity paramActivity, Order paramOrder,Boolean issinglepay,
			YayaWanPaymentCallback paramCallback) {
		if (AgentApp.mUser == null) {
			Toast.makeText(paramActivity.getApplicationContext(), "请先登录",
					Toast.LENGTH_SHORT).show();
			return;
		}
		mPaymentCallback = paramCallback;

		mPayOrder = paramOrder;

		AgentApp.mPayOrder = paramOrder;

		
	
	        if (issinglepay) {
	        	//如果是单机直接跳往支付界面,丫丫玩则是启动丫丫币支付,单机支付为1，否则为0
	        	
	        	Sputils.putSPint("issinglepay", 1,
	  					paramActivity);
	        	//单机
	        	Intent intent = new Intent(paramActivity,
	    				BaseLogin_Activity.class);

	    		intent.putExtra("type", ViewConstants.YAYAPAYMAIN);
	    		paramActivity.startActivity(intent);
			}else {
				Sputils.putSPint("issinglepay", 0,
		  					paramActivity);
				if (com.yayawan.utils.DeviceUtil.getUnionid(paramActivity).equals("1354981926")) {
					Yayalog.loger("丫丫玩的包");
					Yayapaystart_dialog yayapaystart_dialog = new Yayapaystart_dialog(
							paramActivity);
					yayapaystart_dialog.dialogShow();
				}else {
					Intent intent = new Intent(paramActivity,
		    				BaseLogin_Activity.class);

		    		intent.putExtra("type", ViewConstants.YAYAPAYMAIN);
		    		paramActivity.startActivity(intent);
				}
				
				
			}
		
		
		
	}

	/**
	 * 个人中心
	 * 
	 * @param paramActivity
	 * @param paramCallback
	 */
	public static void accountManager(Activity paramActivity) {
		if (AgentApp.mUser == null) {
			Toast.makeText(paramActivity.getApplicationContext(), "请先登录",
					Toast.LENGTH_SHORT).show();
			return;
		}

		Intent intent = new Intent(paramActivity.getApplicationContext(),
				BaseLogin_Activity.class);

		intent.putExtra("type", ViewConstants.ACCOUNTMANAGER);
		paramActivity.startActivity(intent);

	}

	/**
	 * 设置角色id
	 * 
	 * @param paramActivity
	 * @param roleId
	 * @param roleName
	 * @param roleLevel
	 * @param zoneId
	 * @param zoneName
	 */
	public static void setRoleData(Activity paramActivity, String roleId,
			String roleName, String roleLevel, String zoneId, String zoneName) {
		// TODO Auto-generated method stub

		AgentApp.mRoleData = new RoleData(roleId, roleName, roleLevel, zoneId,
				zoneName);
		ParamsWrapper params = new ParamsWrapper();
		params.put("token", AgentApp.mUser.token);
		params.put("app_id", DeviceUtil.getGameId(paramActivity));
		params.put("uid", AgentApp.mUser.uid.toString());
		params.put("ads_id", DeviceUtil.getSourceId(paramActivity));
		params.put("role_id", roleId);
		params.put("role_name", roleName);
		params.put("role_level", roleLevel);
		params.put("zone_id", zoneId);
		params.put("zone_name", zoneName);
		// this.mUserManager.setRoleData(paramActivity);

		ParamsWrapper encryptParams = new ParamsWrapper();
		try {
			encryptParams.put("data", CryptoUtil.encodeHexString(RSACoder
					.encryptByPublicKey(params.getStringParams().getBytes())));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		AsyncHttpConnection.getInstance().post(UrlUtil.ROLEDATA, encryptParams,
				new StringResponseHandler() {

					@Override
					public void onFailure(String paramString, URL paramURL,
							int paramInt) {
						// TODO Auto-generated method stub

					}

					@Override
					protected void onResponse(String paramString, URL paramURL,
							int paramInt) {
						// TODO Auto-generated method stub

					}
				});
	}

	/**
	 * 初始化sdk
	 * 
	 * @param activity
	 */
	public static void initSdk(Activity activity) {
		
		//工具类初始化，在支付安装插件时候用到
		Utils.init(activity);

		ViewConstants.nochangeacount = com.yayawan.utils.DeviceUtil
				.changeAcount(activity);
		Yayalog.loger("初始化viewconstantnochangeacount"
				+ ViewConstants.nochangeacount);
		InitSdk.getAnnouncement(activity);
		try {
			InitSdk.SendinitData(activity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String systemProperty = InitSdk
				.getSystemProperty("ro.miui.ui.version.name");
		Yayalog.loger("是否为miui" + systemProperty);
		if (TextUtils.isEmpty(systemProperty)) {
			ViewConstants.ismiui = false;
		} else {
			ViewConstants.ismiui = true;
		}

	}

	/**
	 * 开启悬浮窗
	 * 
	 * @param activity
	 */
	public static void init(Activity activity) {

		// 如果是demo丫丫玩登陸则不开启悬浮窗
		if (ViewConstants.demoyayalogin) {
			return;
		}

		// 判断玩家是否关闭了小兔子
		if (ViewConstants.iscloseyylog) {
			Yayalog.loger("关闭了兔子兔子了");
			return;
		}
		if (AgentApp.mUser != null) {

			Yayalog.loger("我要开始兔子了");
			LogoWindow.getInstants(activity).start();

		}

	}

	/**
	 * 关闭悬浮窗
	 * 
	 * @param activity
	 */
	public static void stop(Activity activity) {

		// if (ViewConstants.ismiui) {
		LogoWindow.getInstants(activity).Stop();
		// }else {
		// HelpActivty.getInstance().onDestroy();
		// }

	}

	/**
	 * 更新
	 * 
	 * @param activity
	 */
	public static void update(Activity activity,
			YayaWanUpdateCallback updateCallback) {
		mUpdateCallback = updateCallback;
		UpdateUtil.isUpdate(activity);

	}

	/**
	 * 注销账号
	 * 
	 * @param activity
	 */
	public static void logout(Activity activity) {
		Yayalog.loger("yayasdk注销");
		Sputils.putSPint("ischanageacount", 0, ViewConstants.mMainActivity);
		// YayaWan.mUserCallback.onLogout();

	}

	/**
	 * 设置渠道id
	 * 
	 * @param activity
	 */
	public static void setSourceID(String sourceId) {

		AgentApp.mSourceId = sourceId;
	}

	public static String getSdkversion() {

		return ViewConstants.SDKVERSION;
	}

	/**
	 * 退出登录
	 * 
	 * @param activitiy
	 * @param onexit
	 */
	public static void Exitgame(Activity activitiy, final YayaWanCallback onexit) {

		Dialog dialog = new AlertDialog.Builder(activitiy).setTitle("退出游戏提示")

		.setMessage("是否退出游戏？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						onexit.onSuccess();
						
					}
				})
				.setNeutralButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}). create();

		dialog.show();
	}
}
