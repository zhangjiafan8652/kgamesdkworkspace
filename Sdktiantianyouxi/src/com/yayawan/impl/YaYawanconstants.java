package com.yayawan.impl;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdk.login.InitSdk;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.ltsdkgame.sdk.SDKManager;
import com.ltsdkgame.sdk.SDKState;
import com.ltsdkgame.sdk.listener.LTCallback;
import com.ltsdkgame.sdk.model.GameUserInfo;
import com.ltsdkgame.sdk.model.PayInfo;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(final Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("初始化sdk");
		SDKManager.iniSDK(mactivity, new LTCallback() {

			@Override
			public void callback(int code, String msg) {
				switch (code) {
				case SDKState.RESULT_CODE_LOGOUT:
					// Log.d(TAG, "iniSDK==callback==>切换账号和修改密码回调");
					// 用户切换账号或者修改密码成功后，重新展示登录界面
					// dispalyGameLoginUI();
					// 弹出登录框
					// ucSdkLogin();
					loginOut();
					break;
				case SDKState.RESULT_CODE_INITIALIZED:
					Log.d(TAG, "iniSDK==callback==>初始化成功");
					// 初始化成功之
					isinit=true;
					// dispalyGameLoginUI();
					//login(mactivity);
					break;
				case SDKState.RESULT_CODE_INITIALIZE_ERROR:
					// Log.d(TAG, "iniSDK==callback==>初始化失败");
					Toast.makeText(mactivity, "初始化失败", 0).show();
					isinit = false;
					break;
				default:
					break;
				}
			}
		});
	}

	private static String TAG = "tiantianyouxi";

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("sdk登录");
		if (isinit) {

			SDKManager.setLoginDlgEnable(true);
			SDKManager.userLogin(mactivity, new LTCallback() {
				@Override
				public void callback(int code, final String msg) {
					if (code == SDKState.SUCCESS) {
						// Ms 中包含用户的基本信息，游戏可当做展示用户信息
						Log.d(TAG, "login-success==>" + msg);
						try {
							JSONObject usermsgjson;

							usermsgjson = new JSONObject(msg);

							String userid = usermsgjson.optString("id");
							String token = usermsgjson.optString("token");
							loginSuce(mactivity, userid, userid, token);
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// paintGame();
					} else {
						Toast.makeText(mactivity, "登录失败", 0).show();
						Log.d(TAG, "login-fail==>" + msg);
					}
				}
			});

		} else {
			inintsdk(mactivity);
		}
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {

		Yayalog.loger("sdk支付");
		
		  PayInfo payInfo = new PayInfo();
		  	//此处订单id不要更改,默认即可,如果用自己后台下发的,注意保证唯一性
		        payInfo.setCpTradeNo(morderid);
		        if (YYWMain.mRole!=null) {
		        	 payInfo.setZoneName(YYWMain.mRole.getZoneName());
				        payInfo.setRoleName(YYWMain.mRole.getRoleName());
				}
		       
		        
		        payInfo.setPrice(YYWMain.mOrder.money/100);
		        payInfo.setWaresname(YYWMain.mOrder.goods);
		        //如有需求此处可以放置游戏一些信息,如游戏订单id,后台支付回调会原样返回
		        payInfo.setCpprivateinfo(YYWMain.mOrder.ext);
		        //此处替换为自己的支付回调地址（支付回调地址请参考后台文档）
		        payInfo.setNotifyUrl(DeviceUtil.getGameInfo(mactivity, "CallbackUrl"));
		        SDKManager.startPay(mactivity, payInfo, new LTCallback() {

		            @Override
		            public void callback(int code, String msg) {
		                if (code == SDKState.PAY_SUCCESS) {
		                    Log.d(TAG, "success" + msg);
		                    paySuce();
		                } else if (code == SDKState.PAY_CANCEL) {
		                    Log.d(TAG, "cancel" + msg);
		                   // payFail();
		                    payCancel();
		                } else {
		                    Log.d(TAG, "error" + msg);
		                    payFail();
		                }
		            }

		        });
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

		//

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

	}

	/**
	 * 设置角色信息
	 * 
	 * @param arg0
	 */
	public static void setData(Activity paramActivity, String roleId,
			String roleName, String roleLevel, String zoneId, String zoneName,
			String roleCTime, String ext) {
		// TODO Auto-generated method stub
		if (ext.contains("1")) {
			 GameUserInfo gameUserInfo = new GameUserInfo();
		        gameUserInfo.setZoneName(zoneId);
		        gameUserInfo.setRoleName(roleName);
		        SDKManager.submitZoneInfo(gameUserInfo, new LTCallback() {
		            @Override
		            public void callback(int code, String msg) {
		                Log.d(TAG, "上报区服回调" + code + "");
		               // Yayalog.loger("");
		            }
		        });
		}
		 
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
			YYWMain.mUserCallBack.onLoginSuccess(YYWMain.mUser, "success");
			Handle.login_handler(mactivity, YYWMain.mUser.uid,
					YYWMain.mUser.userName);
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

	/**
	 * 登出
	 */
	public static void loginOut() {
		if (YYWMain.mUserCallBack != null) {
			YYWMain.mUserCallBack.onLogout(null);

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

	//支付失败
	public static void payFail() {
		// 支付成功
		if (YYWMain.mPayCallBack != null) {
			YYWMain.mPayCallBack.onPayFailed(null, null);
		}
	}
	
	//支付取消
	public static void payCancel() {
		// 支付成功
		if (YYWMain.mPayCallBack != null) {
			YYWMain.mPayCallBack.onPayCancel(null, null);
		}
	}

	public static void onActivityResult(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

}
