package com.yayawan.impl;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.kkgame.utils.Yayalog;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

import com.ylwl.supersdk.api.YLSuperSDK;
import com.ylwl.supersdk.callback.YLAuthCallBack;
import com.ylwl.supersdk.callback.YLExitCallBack;
import com.ylwl.supersdk.callback.YLLoginCallBack;
import com.ylwl.supersdk.callback.YLLogoutCallBack;
import com.ylwl.supersdk.callback.YLPayCallBack;
import com.ylwl.supersdk.model.params.PayParams;
import com.ylwl.supersdk.model.params.UserInfo;

public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		YLSuperSDK.onCreate(mactivity);
		mActivity = mactivity;
		Yayalog.loger("初始化sdk");
		HashMap<String, String> config = new HashMap<String, String>();
		/*{
			  "DeepChannel":"deep19196",
			  "ChannelKey":"ylwl",
			  "Pid":"104955",
			  "PKey":"d5f0dc410786519473d9bf7f588ef651",
			  "AppVersion":"1.0",
			  "GameName":"洪荒之力",
			  "GameId":"111",
			  "ScreenType":"1",
			  "FullScreen":"true",
			  "Switch":"true",
			  "Debug":"true"
			}*/
		config.put("DeepChannel", "deep19196");
		config.put("ChannelKey", "ylwl");
		config.put("Pid", "104955");
		config.put("PKey", "d5f0dc410786519473d9bf7f588ef651");
		config.put("AppVersion", "1.0");
		config.put("GameName", "洪荒之力");
		config.put("GameId", "111");
		if (DeviceUtil.isLandscape(mActivity)) {
			config.put("ScreenType", "1");
		}else {
			config.put("ScreenType", "2");
		}
		
		config.put("FullScreen", "true");
		config.put("Switch", "true");
		config.put("Debug", "true");
		YLSuperSDK.auth(mactivity, config, new YLAuthCallBack() {
			
			@Override
			public void onAuthSuccess() {
				// TODO Auto-generated method stub
				Yayalog.loger("初始化成功");
			}
			
			@Override
			public void onAuthFailed() {
				// TODO Auto-generated method stub
				
			}
		});
		// 注册注销账号时的回调,在用户注销账号前调用都可以
		YLSuperSDK.registerLogoutCallBack(new YLLogoutCallBack() {

			@Override
			public void onSwitch() {
				// 在这里实现切换账号的逻辑, 注意： 此时 会 主动弹出登录框，无需继续调用登录接口！！！！
				// login_bt.setEnabled(true);
				loginOut();

			}

			@Override
			public void onLogoutSuccess() {
				// 在这里实现切换账号的逻辑, 注意： 此时 会 主动弹出登录框，无需继续调用登录接口！！！！
				// login_bt.setEnabled(true);
				loginOut();
			}

			@Override
			public void onLogoutFail() {

			}

		});
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
		Yayalog.loger("sdk登录");
		YLSuperSDK.login(mactivity, new YLLoginCallBack() {
			@Override
			public void onLoginSuccess(UserInfo userInfo) {
				// login_bt.setEnabled(false);
				System.out.println("登录成功");
				String uid = userInfo.getUserId();
				String sesion = userInfo.getGameToken();
				loginSuce(mActivity, uid, uid, sesion);
			}

			@Override
			public void onLoginFailed() {
				System.out.println("登录失败");
				loginFail();
			}

			@Override
			public void onLoginCanceled() {
				System.out.println("退出登录");
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

		Yayalog.loger("sdk支付");
		// 创建订单信息
		PayParams mPayParams = new PayParams();
		if (YYWMain.mRole != null) {
			mPayParams.setUsername(YYWMain.mRole.getRoleId());
			mPayParams.setRolename(YYWMain.mRole.getRoleName());
			mPayParams.setRolenid(YYWMain.mRole.getRoleName());
			mPayParams.setGameServerName(YYWMain.mRole.getZoneName());
			mPayParams.setGameServerId(YYWMain.mRole.getZoneId());
		}

		mPayParams.setAmount(Double
				.parseDouble(YYWMain.mOrder.money / 100 + ""));
		mPayParams.setOrderid(morderid);
		mPayParams.setProductname(YYWMain.mOrder.goods);
		mPayParams.setProductDesc(YYWMain.mOrder.goods);

		mPayParams.setExtra(YYWMain.mOrder.ext);

		/**
		 * 支付接口,需要同时提供支付和登录的回调接口,若用户没用登录,将直接跳转至登录界面 如果不使用回调,传null即可
		 */
		YLSuperSDK.pay(mactivity, mPayParams, new YLPayCallBack() {
			@Override
			public void onPaySuccess() {
				Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();
				System.out.println("支付成功");
				paySuce();
			}

			@Override
			public void onPayFailed() {
				Toast.makeText(mActivity, "支付失败", Toast.LENGTH_SHORT).show();
				System.out.println("支付失败");
				payFail();
			}

			@Override
			public void onPayCancel() {
				Toast.makeText(mActivity, "退出支付", Toast.LENGTH_SHORT).show();
				System.out.println("退出支付");
				payFail();
			}

			@Override
			public void onPayChecking() {
				paySuce();
				Toast.makeText(mActivity, "支付检查中", Toast.LENGTH_SHORT).show();
				System.out.println("支付检查中");
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
		Yayalog.loger("sdk退出");

		YLSuperSDK.exit(paramActivity, new YLExitCallBack() {
			@Override
			public void onExit() {
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
	public static void setData(Activity paramActivity, String roleId,
			String roleName, String roleLevel, String zoneId, String zoneName,
			String roleCTime, String ext) {
		// TODO Auto-generated method stub
		YLSuperSDK
				.setUserGameRole(paramActivity, YLSuperSDK.getUserName(),
						zoneId, zoneName, roleName, roleId,
						Integer.parseInt(roleLevel));
	}

	public static void onResume(Activity paramActivity) {
		// TODO Auto-generated method stub
		YLSuperSDK.onResume(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub

		YLSuperSDK.onPause(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub
		YLSuperSDK.onDestroy(paramActivity);
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

	public static void onActivityResult(Activity paramActivity, int paramInt1, int paramInt2, Intent paramIntent) {
		// TODO Auto-generated method stub
		 YLSuperSDK.onActivityResult(paramActivity, paramInt1,paramInt2,  paramIntent);
	}

	public static void onNewIntent(Intent paramIntent) {
		// TODO Auto-generated method stub
		 YLSuperSDK.onNewIntent(mActivity, paramIntent);
	}

	public static void onStart(Activity arg0) {
		// TODO Auto-generated method stub
		YLSuperSDK.onStart(arg0);
	}

	public static void onRestart(Activity paramActivity) {
		// TODO Auto-generated method stub
		 YLSuperSDK.onRestart(paramActivity);
	}

	public static void onStop(Activity paramActivity) {
		// TODO Auto-generated method stub
		YLSuperSDK.onStop(paramActivity);
	}

}
