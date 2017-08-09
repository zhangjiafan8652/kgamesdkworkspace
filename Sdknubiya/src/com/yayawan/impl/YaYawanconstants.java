package com.yayawan.impl;

import java.util.HashMap;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import cn.nubia.componentsdk.constant.ConstantProgram;


import cn.nubia.nbgame.sdk.GameSdk;
import cn.nubia.nbgame.sdk.entities.AppInfo;
import cn.nubia.nbgame.sdk.entities.ErrorCode;
import cn.nubia.nbgame.sdk.interfaces.CallbackListener;

import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.Kgame;
import com.yayawan.main.YYWMain;


public class YaYawanconstants {

	
    private static final String TAG = "gamesdk";
    private static final int REQUEST_STORAGE_PERMISSION_LOGIN = 100;
    private static final int REQUEST_STORAGE_PERMISSION_PAY = 101;

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit=false;
	public static String sign="";
	
	public static String pay_app_id="";
	public static String pay_uid="";
	public static String pay_cp_order_id="";
	public static String pay_amount="";
	public static String pay_product_name="";
	public static String pay_product_des="";
	public static String pay_number="";
	public static String pay_data_timestamp="";
	
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("初始化sdk");
		
	   

	}
	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		// TODO Auto-generated method stub
		if(!isMainProcess(applicationContext)){
            return;
        }
		  //如果当前进程不是主进程，则不执行初始化操作
	    int appId =Integer.parseInt(DeviceUtil.getGameInfo(applicationContext, "APP_ID")) ; // 配置您自己的appid
        String appKey = DeviceUtil.getGameInfo(applicationContext, "APP_KEY");// 配置您自己 的appkey
        String secrekey=DeviceUtil.getGameInfo(applicationContext, "APP_SECRET");
        AppConfig.APP_ID=appId;
        AppConfig.APP_KEY=appKey;
        AppConfig.APP_SECRET=secrekey;
        
        
        AppInfo appInfo = new AppInfo();
        appInfo.setAppId(appId);
        appInfo.setAppKey(appKey);
       
        appInfo.setChannelId(1); //选填，配置渠道,默认为1
        if (DeviceUtil.isLandscape(applicationContext)) {
        	 appInfo.setOrientation(0); //0：横屏；1：竖屏
		}else {
			appInfo.setOrientation(1); //0：横屏；1：竖屏
		}
       
        appInfo.setCanUseAdjunct(false);
        GameSdk.initSdk(applicationContext, appInfo,new CallbackListener<Bundle>() {
            @Override
            public void callback(int responseCode, Bundle bundle){
                String msg = "";
                if (responseCode == ErrorCode.SUCCESS) {
                    msg = "sdk初始化成功";
                  //  NeoLog.i(TAG, "init: " + msg);
                    isinit=true;
                } else {
                  //  msg = "sdk初始化失败（" + ErrorMsgUtil.translate(responseCode) + "）";
                   // NeoLog.e(TAG, "init: " + msg);
                }
               // Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show();
            }
        });
 
	}
	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("sdk登录");
		if (isinit) {
	        GameSdk.openLoginActivity(mactivity, new CallbackListener<Bundle>() {
	            @Override
	            public void callback(int responseCode, Bundle bundle){
	                switch (responseCode) {
	                    case ErrorCode.SUCCESS:
	                        //登陆成功，拿uid和sessionId去CP服务器完成角色创建或查询等操作
	                        String msg = String.format("账号:%s 登录", GameSdk.getLoginGameId());
	                        String uid=GameSdk.getLoginGameId();
	                        String username=GameSdk.getNickName();
	                        String session=GameSdk.getSessionId();
	                        loginSuce(mactivity, uid, username, session);
	                        Yayalog.loger("登陆成功："+uid+"username"+username+"session"+session);
	                       // showText(responseCode, msg);
	                       // Log.i(TAG, "login success");
	                        break;
	                    case ErrorCode.NO_PERMISSION:
	                        // Android6.0没允许安装和更新所需权限，需要运行时请求，主要是存储权限
	                        requestStoragePermission(REQUEST_STORAGE_PERMISSION_LOGIN);
//	                        Toast.makeText(MainActivity.this, "登录需要安装努比亚联运中心服务，未获得安装和更新所需权限", Toast.LENGTH_SHORT).show();
	                        //Log.e(TAG, "login failure: " + "code=" + responseCode + ", message=未获得安装和更新所需权限");
	                        break;
	                    default:
	                    	loginFail();
	                        // 登录失败，包含错误码和错误消息
	                       // Toast.makeText(MainActivity.this, "登录失败：" + ErrorMsgUtil.translate(responseCode), Toast.LENGTH_SHORT).show();
	                       // Log.e(TAG, "login failure: " + "code=" + responseCode + ", message=" + ErrorMsgUtil.translate(responseCode));
	                        break;
	                }
	            }
	        });
		}else {
			inintsdk(mactivity);
		}
	}

	private void checkRealIdentity() {
        GameSdk.checkRealIdentity(mActivity, new CallbackListener<Bundle>() {
            @Override
            public void callback(int responseCode, Bundle bundle) {
                Toast.makeText(mActivity, "实名认证：" + responseCode, Toast.LENGTH_SHORT).show();
               // Log.d(TAG, "实名认证：" + responseCode);
            }
        });
    }
	
	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {

		Yayalog.loger("sdk支付");
		
		  HashMap<String, String> map = new HashMap<String, String>();
	        map.put(ConstantProgram.TOKEN_ID, GameSdk.getSessionId());
	        map.put(ConstantProgram.UID, GameSdk.getLoginUid());
	        map.put(ConstantProgram.APP_ID, String.valueOf(AppConfig.APP_ID));
	        map.put(ConstantProgram.APP_KEY, AppConfig.APP_KEY);
	        map.put(ConstantProgram.AMOUNT, pay_amount);
	        map.put(ConstantProgram.PRICE, YYWMain.mOrder.money/100+"");
	        map.put(ConstantProgram.NUMBER, "1");
	        map.put(ConstantProgram.PRODUCT_NAME, pay_product_name);
	        map.put(ConstantProgram.PRODUCT_DES, pay_product_des);
	       // map.put(ConstantProgram.PRODUCT_ID, "A01");
	       // map.put(ConstantProgram.PRODUCT_UNIT, "个");
	        String timeStamp = String.valueOf(System.currentTimeMillis());
	        //由CP服务器返回的订单编号，订单号不能重复
	        String cpOrderId = morderid;
	        map.put(ConstantProgram.CP_ORDER_ID, pay_cp_order_id);
	        // 为了安全性考虑，doSign最好在服务端执行, 时间戳在DATA_TIMESTAMP和签名两个地方传递的必须是一致的
	        map.put(ConstantProgram.SIGN, sign);
	        Yayalog.loger(sign);
	        map.put(ConstantProgram.DATA_TIMESTAMP, pay_data_timestamp);
	        map.put(ConstantProgram.CHANNEL_DIS, "1");
	        map.put(ConstantProgram.GAME_ID, GameSdk.getLoginGameId());
	        Yayalog.loger("ConstantProgram.GAME_ID"+GameSdk.getLoginGameId());
	        Log.i(TAG, "支付请求参数：" + map.toString());
	        GameSdk.doPay(mactivity, map, new CallbackListener<String>() {
	            @Override
	            public void callback(int responseCode, String message) {
	                switch (responseCode) {
	                    case 0:
	                        // 支付完成
	                        showPayResult(responseCode, "支付成功!");
	                        break;
	                    case -1:
	                        // 本次支付失败
	                        showPayResult(responseCode, "支付失败!"+ "{" + message + "}");
	                        break;
	                    case 10001:
	                        // 用户取消了本次支付
	                        showPayResult(responseCode, "您已经取消本次支付"+ "{" + message + "}");
	                        break;
	                    case 10002:
	                        // 网络异常
	                        showPayResult(responseCode, "网络异常，请检查网络设置"+ "{" + message + "}");
	                        break;
	                    case 10003:
	                        // 订单结果确认中，建议客户端向自己业务服务器校验支付结果
	                        showPayResult(responseCode, "支付结果确认中，请稍后查看"+ "{" + message + "}");
	                        break;
	                    case 10004:
	                        // 支付服务正在升级
	                        showPayResult(responseCode, "支付服务正在升级"+ "{" + message + "}");
	                        break;
	                    case 10005:
	                        // 支付组件安装失败或是未安装
	                        showPayResult(responseCode, "支付服务安装失败或未安装"+ "{" + message + "}");
	                        break;
	                    case 10006:
	                        // 订单信息有误
	                        showPayResult(responseCode, "订单信息有误"+ "{" + message + "}");
	                        break;
	                    case 10007:
	                        // 获取支付渠道失败
	                        showPayResult(responseCode, "获取支付渠道失败，请稍后重试"+ "{" + message + "}");
	                        break;
	                    case 10008:
	                        // Android6.0没允许相关权限，需要运行时请求，主要是存储权限
	                        Toast.makeText(mActivity, "未获得安装和更新所需权限", Toast.LENGTH_SHORT).show();
	                        requestStoragePermission(REQUEST_STORAGE_PERMISSION_PAY);
	                        break;
	                    default:
	                        // 其他所有场景统一处理为支付失败
	                        showPayResult(responseCode, "支付失败! " + "{" + message + "}");
	                        break;
	                }
	            }
	        });
		
		
	}
	
	 public static void showPayResult(int code, String payResult) {
	        String msg =  payResult + " (错误码:" + code + ")";
	        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
	    }

	/**
	 * 退出
	 * 
	 * @param paramActivity
	 * @param callback
	 */
	public static void exit(final Activity paramActivity,
			final YYWExitCallback callback) {
		Yayalog.loger("sdk退出");

		paramActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				KgameSdk.Exitgame(paramActivity, new KgameSdkCallback() {
					
					@Override
					public void onSuccess(User arg0, int arg1) {
						// TODO Auto-generated method stub
						callback.onExit();
						//Kgame.getInstance().logout(paramActivity);//注销退出接口
					}
					
					@Override
					public void onLogout() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onError(int arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onCancel() {
						// TODO Auto-generated method stub
						
					}
				});
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
		
	}
	public static void onResume(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	public static void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	public static void onDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub

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
			Yayalog.loger("登陆成功回调:"+YYWMain.mUser.toString());
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

	public static void onActivityResult(Activity paramActivity) {
		// TODO Auto-generated method stub
		
	}
	  private static void requestStoragePermission(int requestCode) {
	        ActivityCompat.requestPermissions(mActivity,
	                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
	    }
	  
	  /**
	     * 假设当前APP的包名为cn.nubia.nbgame.sdk，则获取到的进程名称也为：cn.nubia.nbgame.sdk；
	     * 如果在AndroidManifest.xml的某个组件(activity,service,receiver,provider)中申明了android:process=":remote"，则获取到的进程名为：cn.nubia.nbgame.sdk:remote
	     *
	     * 当申明了独立进程名的组件被打开时，Android系统认为一个新的进程被触发了，会首先执行SdkApplication的onCreate()方法，从而导致再次执行初始化，
	     * 因此需要通过进程名来控制，确保初始化只被调用一次
	     *
	     * @param context
	     * @return
	     */
	    static String getCurProcessName(Context context) {
	        int pid = android.os.Process.myPid();
	        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
	            if (appProcess.pid == pid) {
	                return appProcess.processName;
	            }
	        }
	        return null;
	    }

	    /**
	     * 建议将初始化、登录、支付等与联运SDK相关的操作放到主进程(即进程名为APP包名cn.nubia.nbgame.sdk的进程)
	     * 原因是：不同的进程之间数据是隔离的，而登录或者支付失败等业务需要用到初始化完成后的数据
	     * @param context
	     * @return
	     */
	    private static boolean isMainProcess(Context context) {
	        String pkgName = context.getPackageName();
	        String curProcessName = getCurProcessName(context);
	        if((null!=curProcessName)&&(curProcessName.equals(pkgName))) {
	           // NeoLog.d(TAG, "Application isMainProcess: " + curProcessName);
	            return true;
	        }else{
	            //NeoLog.d(TAG, "Application isSubProcess: " + curProcessName);
	            return false;
	        }
	    }


}
