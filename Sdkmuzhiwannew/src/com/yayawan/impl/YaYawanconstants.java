package com.yayawan.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kkgame.sdk.utils.MD5;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.kkgame.utils.Yayalog;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;
import com.muzhiwan.sdk.core.MzwSdkController;
import com.muzhiwan.sdk.core.callback.MzwInitCallback;
import com.muzhiwan.sdk.core.callback.MzwLoignCallback;
import com.muzhiwan.sdk.core.callback.MzwPayCallback;
import com.muzhiwan.sdk.service.MzwOrder;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;


public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit=false;
	private static boolean isLogin = false;
	private static boolean isInit = false;
	private static MzwLoignCallback loginResponse;
	
	private static final int MSG_INIT = 0X01;
	private static final int MSG_LOGIN = 0X02;
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(final Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		
		if (DeviceUtil.isLandscape(mactivity)) {
			MzwSdkController.getInstance().init(mactivity, MzwSdkController. ORIENTATION_HORIZONTAL,  new MzwInitCallback() {

                @Override
                public void onResult(int code, String msg) {
                }
            });
		} else {
			MzwSdkController.getInstance().init(mactivity, MzwSdkController.ORIENTATION_VERTICAL, new MzwInitCallback() {

                @Override
                public void onResult(int code, String msg) {
                }
            });
		}
		loginResponse = new MzwLoignCallback() {
            @Override
            public void onResult(int i, String token) {
            	if (i == 1) {
                    isLogin = true;
                    getUid(token.toString());
                    //showToast("登录成功");
                }else {
					loginFail();
				}
            }
        };

	}
	
	public static void getUid(final String token) {
		// MD5（AppId + Token + Timestamp + APPKEY）
		try {
			String appkey = DeviceUtil.getGameInfo(mActivity, "MZWAPPKEY");
			HttpUtils httpUtils = new HttpUtils();
			RequestParams requestParams = new RequestParams();
			
			String url="http://sdk.muzhiwan.com/oauth2/getuser.php?token="+token+"&appkey="+appkey;
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
					Yayalog.loger("登陸結果："+arg0.result);
					try {
						JSONObject jsonObject = new JSONObject(arg0.result);
						JSONObject user=jsonObject.getJSONObject("user");
						String uid=user.getString("uid");
						String username=user.getString("username");
						loginSuce(mActivity, uid, username, token);
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
		MzwSdkController.getInstance().doLogin(loginResponse);
		
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {

		Yayalog.loger("YaYawanconstantssdk支付");
		MzwOrder order = new MzwOrder();
        order.setMoney((double)YYWMain.mOrder.money/100);
        order.setProductname(YYWMain.mOrder.goods);
        order.setProductdesc(YYWMain.mOrder.goods);
        order.setExtern(""+morderid);
        MzwSdkController.getInstance().doPay(order, new MzwPayCallback() {
            @Override
            public void onResult(int code, MzwOrder order) {
            	if (code == 1) {
               	  	paySuce();
            	}
            	else{
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
	public static void exit(Activity paramActivity,
			final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");

		//
mActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				MzwSdkController.getInstance().destory();
			}
		});

	}

	/**
	 * 设置角色信息
	 * 
	 * @param arg0
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
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
