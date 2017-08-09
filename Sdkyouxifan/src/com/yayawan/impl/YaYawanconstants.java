package com.yayawan.impl;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.game.sdk.YXFSDKManager;
import com.game.sdk.domain.LoginErrorMsg;
import com.game.sdk.domain.LogincallBack;
import com.game.sdk.domain.OnLoginListener;
import com.game.sdk.domain.OnPaymentListener;
import com.game.sdk.domain.PaymentCallback;
import com.game.sdk.domain.PaymentErrorMsg;
import com.game.sdk.domain.RoleInfo;
import com.game.sdk.domain.RolecallBack;
import com.game.sdk.domain.onRoleListener;
import com.game.sdk.util.Constants;
import com.game.sdk.util.LogUtil;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.lidroid.xutils.util.LogUtils;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.Kgame;
import com.yayawan.main.YYWMain;


public class YaYawanconstants {

	
	private static final String TAG = "-----MainActivity-----";
	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit=false;
	private static boolean islogin=false;

	private static YXFSDKManager manager;
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		manager = YXFSDKManager.getInstance(mactivity);
		if (DeviceUtil.isLandscape(mactivity)) {
			YXFSDKManager.setIsLandscape(true);// false: 竖屏 true：横屏
		}else {
			YXFSDKManager.setIsLandscape(false);// false: 竖屏 true：横屏
		}
		
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
		YXFSDKManager.getInstance(mactivity).showLogin(
				mactivity, true, new OnLoginListener() {
					@Override
					public void loginSuccess(LogincallBack logincallback) {
						/*Toast.makeText(mactivity,
								logincallback.toString(),
								Toast.LENGTH_SHORT).show();*/
						islogin=true;
						LogUtil.getInstance(TAG).d(
								"登录成功-----" + logincallback.toString());
						loginSuce(mactivity, logincallback.userId, logincallback.username, logincallback.sign);
					}

					@Override
					public void loginError(LoginErrorMsg errorMsg) {
						/*Toast.makeText(mactivity, errorMsg.msg,
								Toast.LENGTH_SHORT).show();*/
						loginFail();
					}
				});
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(final Activity mactivity, String morderid) {

		Yayalog.loger("YaYawanconstantssdk支付");
		YXFSDKManager.getInstance(mactivity).showPay(
				mactivity, ""+YYWMain.mRole.getRoleId(), YYWMain.mOrder.money/100+"", ""+YYWMain.mRole.getZoneId(), YYWMain.mOrder.goods, DeviceUtil.getGameInfo(mactivity, "goodsname"),
				morderid,
				new OnPaymentListener() {
					@Override
					public void paymentSuccess(PaymentCallback callbackInfo) {
						
						paySuce();
					}

					@Override
					public void paymentError(PaymentErrorMsg errorMsg) {
						
						payFail();
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

		KgameSdk.Exitgame(paramActivity, new KgameSdkCallback() {
			
			@Override
			public void onSuccess(User arg0, int arg1) {
				// TODO Auto-generated method stub
				callback.onExit();
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
		if (YYWMain.mRole!=null) {
			RoleInfo info = new RoleInfo();
			info.setRoleName(""+YYWMain.mRole.getRoleName());// 角色名
			info.setRoleVIP("");// 角色VIP
			info.setRoleLevel(""+YYWMain.mRole.getRoleLevel());// 角色等级
			info.setServerID(""+YYWMain.mRole.getZoneId());// 区服id
			info.setServerName(YYWMain.mRole.getZoneName());// 区服名
		if (Integer.parseInt(ext)==1) {
			
			YXFSDKManager.getInstance(paramActivity).getRoleInfo(paramActivity, info,
					Constants.TYPE_LOGIN_SUCCESS, new onRoleListener() {
						@Override
						public void onSuccess(RolecallBack rolecallBack) {
							LogUtils.i(TAG + "提交用户信息成功 = "
									+ rolecallBack.toString());
						}

						@Override
						public void onError(RolecallBack rolecallBack) {
							LogUtils.i(TAG + "提交用户信息失败 = "
									+ rolecallBack.toString());
						}
					});
			YXFSDKManager.getInstance(paramActivity).getRoleInfo(paramActivity, info,
					Constants.TYPE_SELECT_SERVER, new onRoleListener() {
						@Override
						public void onSuccess(RolecallBack rolecallBack) {
							LogUtils.i(TAG + "提交用户信息成功 = "
									+ rolecallBack.toString());
						}

						@Override
						public void onError(RolecallBack rolecallBack) {
							LogUtils.i(TAG + "提交用户信息失败 = "
									+ rolecallBack.toString());
						}
					});
		}else if(Integer.parseInt(ext)==2) {
			YXFSDKManager.getInstance(paramActivity).getRoleInfo(paramActivity, info,
					Constants.TYPE_CREATE_ROLE, new onRoleListener() {
						@Override
						public void onSuccess(RolecallBack rolecallBack) {
							LogUtils.i(TAG + "提交用户信息成功 = "
									+ rolecallBack.toString());
						}

						@Override
						public void onError(RolecallBack rolecallBack) {
							LogUtils.i(TAG + "提交用户信息失败 = "
									+ rolecallBack.toString());
						}
					});
		}else if(Integer.parseInt(ext)==3){
			YXFSDKManager.getInstance(paramActivity).getRoleInfo(paramActivity, info,
					Constants.TYPE_LEVEL_UP, new onRoleListener() {
						@Override
						public void onSuccess(RolecallBack rolecallBack) {
							LogUtils.i(TAG + "提交用户信息成功 = "
									+ rolecallBack.toString());
						}

						@Override
						public void onError(RolecallBack rolecallBack) {
							LogUtils.i(TAG + "提交用户信息失败 = "
									+ rolecallBack.toString());
						}
					});
		}
		}
	}
	public static void onResume(Activity paramActivity) {
		// TODO Auto-generated method stub
		if (islogin) {
			YXFSDKManager.getInstance(paramActivity).showFloatView();
		}
		//
	}

	public static void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	public static void onDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub
		if (YYWMain.mRole!=null) {
			RoleInfo info = new RoleInfo();
			info.setRoleName(""+YYWMain.mRole.getRoleName());// 角色名
			info.setRoleVIP("");// 角色VIP
			info.setRoleLevel(""+YYWMain.mRole.getRoleLevel());// 角色等级
			info.setServerID(""+YYWMain.mRole.getZoneId());// 区服id
			info.setServerName(YYWMain.mRole.getZoneName());// 区服名
			YXFSDKManager.getInstance(paramActivity).LoginOut(info,
					Constants.TYPE_EXIT_GAME, new onRoleListener() {
						@Override
						public void onSuccess(RolecallBack rolecallBack) {
							LogUtils.i(TAG + "提交用户信息成功 = "
									+ rolecallBack.toString());
						}

						@Override
						public void onError(RolecallBack rolecallBack) {
							LogUtils.i(TAG + "提交用户信息失败 = "
									+ rolecallBack.toString());
						}
					});
		}
		// 游戏退出必须调用
			
	}
	
	public static void onActivityResult(Activity paramActivity) {
		// TODO Auto-generated method stub
		
	}

	public static void onNewIntent(Intent paramIntent) {
		// TODO Auto-generated method stub
		
	}

	public static void onStart(Activity mActivity2) {
		// TODO Auto-generated method stub
		
	}

	public static void onRestart(Activity paramActivity) {
		// TODO Auto-generated method stub
		
	}

	public static void onCreate(Activity paramActivity) {
		// TODO Auto-generated method stub
		
	}

	public static void onStop(Activity paramActivity) {
		// TODO Auto-generated method stub
		if (islogin) {
			YXFSDKManager.getInstance(paramActivity).removeFloatView();
		}
		
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
