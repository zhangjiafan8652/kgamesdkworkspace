package com.yayawan.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.kkgame.sdk.utils.MD5;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;
import com.p75757.sdk.Com75757SDK;
import com.p75757.sdk.IResponse;
import com.p75757.sdk.PayOrderInfo;
import com.p75757.sdk.constant.Orientation;
import com.p75757.sdk.constant.ResultCode;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;
	private static IResponse loginResponse;
	private static boolean isinit = false;

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(final Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");

		String appid = DeviceUtil.getGameInfo(mactivity, "APPID");
		// SDK 初始化，第二个参数：APP_ID, 第三个参数：横版/竖版 (Orientation.LANDSCAPE /
		// Orientation.PORTRAIT)
		if (DeviceUtil.isLandscape(mactivity)) {
			Com75757SDK.init(mactivity, appid, Orientation.LANDSCAPE);
		} else {
			Com75757SDK.init(mactivity, appid, Orientation.PORTRAIT);
		}
		loginResponse = new IResponse() {
			@Override
			public void onResponse(int i, String s, Object token) {
				// 登录成功
				if (i == ResultCode.LOGIN_SUCCESS) {
					//Toast.makeText(mactivity, "登录成功 Token:" + token,
						//	Toast.LENGTH_SHORT).show();
					// 游戏自己的逻辑
					getUid(token.toString());
				}else {
					loginFail();
				}
			}
		};
	}

	public static void getUid(final String token) {
		// MD5（AppId + Token + Timestamp + APPKEY）
		try {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = sdf.format(date);
			String appid = DeviceUtil.getGameInfo(mActivity, "APPID");
			String appkey = DeviceUtil.getGameInfo(mActivity, "APPKEY");

			String sign = MD5.MD5(appid + token + time + appkey);
			HttpUtils httpUtils = new HttpUtils();
			RequestParams requestParams = new RequestParams();
			
			String url="http://api.75757.com/cp/user-info?AppId="+appid+"&Token="+token+"&Timestamp="+time+"&Sign="+sign;
			httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					// TODO Auto-generated method stub
					loginFail();
				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					// TODO Auto-generated method stub
					// 获取uid接口：{"ResultCode":1,"ResultMsg":"ok","AppId":"1066","Uid":23744,"Sign":"fb5270e611dbace9e8e4d02cb11a2b44"}
					try {
						JSONObject jsonObject = new JSONObject(arg0.result);
						String uid=jsonObject.getString("Uid");
						loginSuce(mActivity, uid, uid, token);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						loginFail();
						e.printStackTrace();
					}
					System.out.println("获取uid接口："+arg0.result);
				}
			});
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		// SDK登录接口
		Com75757SDK.login(loginResponse);
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {

		Yayalog.loger("YaYawanconstantssdk支付");
		
		  PayOrderInfo orderInfo = new PayOrderInfo();
          orderInfo.setCooperatorOrderSerial(morderid);       //订单ID
          orderInfo.setProductName(YYWMain.mOrder.goods);                   // 商品名
          orderInfo.setTotalPriceCent(YYWMain.mOrder.money);                         // 价格，单位：分
          orderInfo.setExtInfo(""+morderid);                               // 订单扩展信息
          // SDK支付接口
          Com75757SDK.pay(orderInfo, new IResponse<PayOrderInfo>() {
              @Override
              public void onResponse(int resultCode, String resultDesc, PayOrderInfo payOrderInfo) {
                  // 游戏自己的订单逻辑放在这
                  if (resultCode == ResultCode.PAY_SUCCESS) {
                     // Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                	  	paySuce();
                  }
                  else if (resultCode == ResultCode.PAY_FAIL) {
                     // Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                	  payFail();
                  }
                  else if (resultCode == ResultCode.PAY_CANCEL) {
                     // Toast.makeText(MainActivity.this, "用户取消支付", Toast.LENGTH_SHORT).show();
                	  payFail();
                  }
                  // 支付宝的特殊情况
                  else if (resultCode == ResultCode.PAY_SUBMIT_ORDER) {
                	  paySuce();
                     // Toast.makeText(MainActivity.this, "订单处理中", Toast.LENGTH_SHORT).show();
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
	public static void exit(Activity paramActivity,
			final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");

		//
		mActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 Com75757SDK.exit(mActivity, new IResponse() {
	                    @Override
	                    public void onResponse(int resultCode, String resultDesc, Object o) {
	                        // 游戏自己的退出逻辑放在这
	                        if (resultCode == ResultCode.EXIT_GAME) {       // 退出游戏
	                            callback.onExit();
	                        }
	                        else if (resultCode == ResultCode.EXIT_CANCEL) {        // 取消退出
	                           // Toast.makeText(MainActivity.this, "用户取消退出", Toast.LENGTH_SHORT).show();
	                        }
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
		Yayalog.loger("YaYawanconstants设置角色信息");
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
