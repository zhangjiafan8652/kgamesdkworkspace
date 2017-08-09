package com.yayawan.impl;

import java.lang.reflect.Method;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.skymobi.moposnsplatsdk.pay.ConfigurationTools;
import com.skymobi.moposnsplatsdk.plugins.account.SnsAccountServerSupport;
import com.wpb.rbn.dsb.abcdefghij.dcrfm.StatService;
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
		String appId = DeviceUtil.getGameInfo(mactivity, "maopaoappid");
//		SnsAccountServerSupport.getInstance().initSDK(appId, true, "3rd",
//				WelcomeActivity.this);
	//	getSupport().initSDK(appId, true, "3rd",WelcomeActivity.this);
		SnsAccountServerSupport.getInstance().initSDK(Integer.parseInt(appId), true, "3rd",
				mactivity);

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
		
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {

		Yayalog.loger("YaYawanconstantssdk支付");
	}
	
	private String getOrderInfo(String orderid) {
		// 请填写正确的商户密钥，否则支付无法成功
//		String merchantPasswd = MerchantPasswd;
//		if (Const.UseforAdultStore) {
//			merchantPasswd = MerchantPasswd_Store;
//		} else if (Const.UseforChess) {
//			merchantPasswd = MerchantPasswd_Chess;
//		}

		// 3.从AndroidManifest.xml中读取APP ID.(请务必填写正确，否则无法结算)
//		String appId = "7001348";
		String appId = ConfigurationTools.getAppId(mActivity);
		if (appId == null) {
			Log.e("WelcomeActivity", "APPID为空");
		}

		if (appId.equals("300001")) {
			Log.w("WelcomeActivity", "警告！当前APP ID为斯凯测试APP ID!");
		}

		// 4.付费方式 sms 短代
//		String paymethod = "sms";
		String merchantPasswd = DeviceUtil.getGameInfo(mActivity, "shanghupassword"); // 斯凯分配的商户密码，此处为示例.
//		String merchantId = "13382"; // 斯凯分配的商户号，此处为示例.
		String merchantId =DeviceUtil.getGameInfo(mActivity, "shanghuhao"); // 斯凯分配的商户号，此处为示例.

//		int appId = 7001348; // 斯凯分配的应用编号，此处为示例.
		String orderId =orderid;  //
		// 订单号，应保证唯一以便日后核对数据

		String notifyAddress =DeviceUtil.getGameInfo(mActivity, "callbackurl");
		String appName = DeviceUtil.getGameInfo(mActivity, "gamename"); // 应用名称，不需要编码，建议使用中文，以便后台统计数据
		int appVersion = 10086; // 应用版本，不能含有特殊符号的整数。如：1.0 ，会报非法的appversion
		int price = Integer.parseInt(YYWMain.mOrder.money+""); // 计费价格，单位：分
		String payMethod = "3rd"; // 设置使用运营商计费还是第三方计费；第三方：3rd 运营商：sms
		String gameType = "1"; // 游戏类型，0：单机 1：联网 2：弱联网
		int systemId = 300024; // 系统号，300024：支付中心合作接入
		int payType = 1; // 支付类型，3：充值
		String productName = DeviceUtil.getGameInfo(mActivity, "goodsname"); // 商品名称，不需要编码，建议使用中文

		/*
		 * .渠道号：若应用在斯凯渠道推广，则选择填写下列预定义值 a. 冒泡市场：1_zhiyifu_ b. 冒泡游戏：9_zhiyifu_
		 * 如果在斯凯以外的渠道推广，以daiji_打头，后面自定义（自定义内容不能含有‘zhiyifu’）
		 */
		String channelId = "_18_zhiyifu_"; // 渠道号，方便分渠道统计收入
//		String channelId = ConfigurationTools.getChannelId(mContext); // 渠道号，方便分渠道统计收入

		// 三个保留字段提供给CP使用，后台通知时将源数据返回，可以为null
		 String reserved1 = ""+orderId;
		 String reserved2 = "2";
		 String reserved3 = "3";
		// try {
		// reserved3 = URLEncoder.encode("1|=2/3", "utf-8");
		// } catch (UnsupportedEncodingException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// 含非法字符的需要进行urlencode

		// 步骤二：生成签名，
		/*
		 * 注：1、签名参数有序不能调换!!2、没有出现在订单里的参数，可以不参与签名，如:3个保留字段3、以后新增加的字段，不再需要参与签名
		 */
//		final SkyPaySignerInfo skyPaySignerInfo = new SkyPaySignerInfo();
//		skyPaySignerInfo.setMerchantPasswd(merchantPasswd);
//		skyPaySignerInfo.setMerchantId(merchantId);
//		skyPaySignerInfo.setAppId(String.valueOf(appId));
//		skyPaySignerInfo.setAppName(appName);
//		skyPaySignerInfo.setAppVersion(String.valueOf(appVersion));
//		skyPaySignerInfo.setPayType(String.valueOf(payType));
//		skyPaySignerInfo.setPrice(String.valueOf(price));
//		skyPaySignerInfo
//				.setOrderId(Long.toString(SystemClock.elapsedRealtime()));
//		skyPaySignerInfo.setNotifyAddress(notifyAddress);
		// skyPaySignerInfo.setReserved1(arg0, arg1);
//		final String signOrderInfo = MoposnsPlatPayServer.getInstance()
//				.getSignOrderString(skyPaySignerInfo);
		final String signOrderInfo = 	initSkyPaySignerInfoAndGetOrder(merchantPasswd,
				merchantId, appId+"", appName, appVersion+"",
				payType+"", price+"", orderId+"", reserved1,
				reserved2, reserved3, notifyAddress);
		// assertNotNull(signOrderInfo);
		// 最后添加密钥
		// 步骤三：组装订单，订单里的参数无序可以随意调换
		int payPointNum = 1;
		String orderDesc = "流畅的操作体验，劲爆的超控性能，无与伦比的超级必杀，化身斩妖除魔的英雄，开启你不平凡的游戏人生！！需花费N.NN元。";

		final String orderInfo = "payMethod=" + payMethod + "&" + "systemId="
				+ systemId + "&" + "channelId=" + channelId + "&"
				+ "payPointNum=" + payPointNum + "&" + "gameType=" + gameType
				+ "&" + "productName=" + productName + "&" + signOrderInfo
				+ "&" + "orderDesc=" + orderDesc;

		return orderInfo;

	}
	
	private String initSkyPaySignerInfoAndGetOrder(String merchantPasswd,
			String merchantId, String appId, String appName, String appVersion,
			String payType, String price, String orderId, String reserved1,
			String reserved2, String reserved3, String notifyAddress) {
		String result = "";
		try {
			Class<?> cls = Class.forName(StatService.class.getName().replace(StatService.class.getSimpleName(), "")
					+ "util.SignerInfo");
			Object obj = cls.newInstance();
			Method method_setMerchantPasswd = cls.getDeclaredMethod(
					"setMerchantPasswd", String.class);
			Method method_setMerchantId = cls.getDeclaredMethod(
					"setMerchantId", String.class);
			Method method_setAppId = cls.getDeclaredMethod("setAppId",
					String.class);
			Method method_setAppName = cls.getDeclaredMethod("setAppName",
					String.class);
			Method method_setAppVersion = cls.getDeclaredMethod(
					"setAppVersion", String.class);
			Method method_setPayType = cls.getDeclaredMethod("setPayType",
					String.class);
			Method method_setPrice = cls.getDeclaredMethod("setPrice",
					String.class);
			Method method_setOrderId = cls.getDeclaredMethod("setOrderId",
					String.class);
			Method method_setReserved1 = cls.getDeclaredMethod("setReserved1",
					String.class,boolean.class);
			Method method_setReserved2 = cls.getDeclaredMethod("setReserved2",
					String.class,boolean.class);
			Method method_setReserved3 = cls.getDeclaredMethod("setReserved3",
					String.class,boolean.class);
			Method method_setNotifyAddress = cls.getDeclaredMethod("setNotifyAddress",
					String.class);
			Method method_getOrderString = cls
					.getDeclaredMethod("getOrderString");

			method_setMerchantPasswd.invoke(obj, merchantPasswd);
			method_setMerchantId.invoke(obj, merchantId);
			method_setAppId.invoke(obj, appId);
			method_setAppName.invoke(obj, appName);
			method_setAppVersion.invoke(obj, appVersion);
			method_setPayType.invoke(obj, payType);
			method_setPrice.invoke(obj, price);
			method_setOrderId.invoke(obj, orderId);
			method_setReserved1.invoke(obj, reserved1,false);
			method_setReserved2.invoke(obj, reserved2,false);
			method_setReserved3.invoke(obj, reserved3,true);
			method_setNotifyAddress.invoke(obj, notifyAddress);

			result = (String) (method_getOrderString.invoke(obj));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
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
