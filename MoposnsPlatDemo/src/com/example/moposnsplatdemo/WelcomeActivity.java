package com.example.moposnsplatdemo;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.moposnsplatdemo.rank.Top10Activity;
import com.skymobi.moposnsplatsdk.application.MoposnsPlatApplication;
import com.skymobi.moposnsplatsdk.pay.ConfigurationTools;
import com.skymobi.moposnsplatsdk.pay.MoposnsPlatPayServer;
import com.skymobi.moposnsplatsdk.plugins.account.SnsAccountServerSupport;
import com.wpb.rbn.dsb.abcdefghij.dcrfm.StatService;

public class WelcomeActivity extends Activity {
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		int appId = 7001348;
		String appId = ConfigurationTools.getAppId(this);
//		SnsAccountServerSupport.getInstance().initSDK(appId, true, "3rd",
//				WelcomeActivity.this);
		SnsAccountServerSupport.getInstance().initSDK(Integer.parseInt(appId), true, "3rd",
				WelcomeActivity.this);

		setContentView(R.layout.activity_main);

		mContext = this;

		Button regUI = (Button) findViewById(R.id.reglogin_with_ui);
		if (regUI != null) {
			regUI.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(new Intent(mContext, RegloginUIActivity.class));
				}
			});
		}

		Button selectLevel = (Button) findViewById(R.id.pay);
		selectLevel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String orderInfo = getOrderInfo();
				startPay(WelcomeActivity.this, orderInfo);
			}
		});

		Button top10 = (Button) findViewById(R.id.btn_top10);
		top10.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, Top10Activity.class));
			}
		});

		Button gameInfo = (Button) findViewById(R.id.btn_gameInfo);
		gameInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, GameInfoSaveActivity.class));
			}
		});

		UnCeHandler catchExcep = new UnCeHandler(getApplicationContext());
		Thread.setDefaultUncaughtExceptionHandler(catchExcep);
	}

	public void startPay(Activity activity, String orderInfo) {
		/*
		 * 1.初始化，设置支付回调handle
		 */
		PayResultHandle payCallBackHandle = new PayResultHandle();
		MoposnsPlatPayServer.getInstance().init(payCallBackHandle);
//		int initRet = MoposnsPlatPayServer.getInstance()
//				.init(payCallBackHandle);
//		if (MoposnsPlatPayServer.PAY_RETURN_SUCCESS == initRet) {
//			// 初始化成功！
//		} else {
//			// 可能连续点击引起的
//			Log.i("pay", "初始化失败:" + initRet);
//		}

		// 2、启动付费
		int payRet = MoposnsPlatPayServer.getInstance().startActivityAndPay(
				activity, orderInfo);
		if (MoposnsPlatPayServer.PAY_RETURN_SUCCESS == payRet) {
			// 初始化成功
			Log.i("pay", "接口斯凯付费调用成功");
		} else {
			// 未初始化 \传入参数有误 \服务正处于付费状态
			Log.i("pay", "调用接口失败:" + payRet);
		}
	}

	private String getOrderInfo() {
		// 请填写正确的商户密钥，否则支付无法成功
//		String merchantPasswd = MerchantPasswd;
//		if (Const.UseforAdultStore) {
//			merchantPasswd = MerchantPasswd_Store;
//		} else if (Const.UseforChess) {
//			merchantPasswd = MerchantPasswd_Chess;
//		}

		// 3.从AndroidManifest.xml中读取APP ID.(请务必填写正确，否则无法结算)
//		String appId = "7001348";
		String appId = ConfigurationTools.getAppId(mContext);
		if (appId == null) {
			Log.e("WelcomeActivity", "APPID为空");
		}

		if (appId.equals("300001")) {
			Log.w("WelcomeActivity", "警告！当前APP ID为斯凯测试APP ID!");
		}

		// 4.付费方式 sms 短代
//		String paymethod = "sms";
		String merchantPasswd = "sfjui@98393469"; // 斯凯分配的商户密码，此处为示例.
//		String merchantId = "13382"; // 斯凯分配的商户号，此处为示例.
		String merchantId = ConfigurationTools.getMerchantId(mContext); // 斯凯分配的商户号，此处为示例.

//		int appId = 7001348; // 斯凯分配的应用编号，此处为示例.
		String orderId = String.valueOf(System.currentTimeMillis()); //
		// 订单号，应保证唯一以便日后核对数据

		String notifyAddress = "http://www.demo.com/";
		String appName = "冒泡社区"; // 应用名称，不需要编码，建议使用中文，以便后台统计数据
		int appVersion = 10086; // 应用版本，不能含有特殊符号的整数。如：1.0 ，会报非法的appversion
		int price = 200; // 计费价格，单位：分
		String payMethod = "3rd"; // 设置使用运营商计费还是第三方计费；第三方：3rd 运营商：sms
		String gameType = "1"; // 游戏类型，0：单机 1：联网 2：弱联网
		int systemId = 300024; // 系统号，300024：支付中心合作接入
		int payType = 1; // 支付类型，3：充值
		String productName = "金币"; // 商品名称，不需要编码，建议使用中文

		/*
		 * .渠道号：若应用在斯凯渠道推广，则选择填写下列预定义值 a. 冒泡市场：1_zhiyifu_ b. 冒泡游戏：9_zhiyifu_
		 * 如果在斯凯以外的渠道推广，以daiji_打头，后面自定义（自定义内容不能含有‘zhiyifu’）
		 */
		String channelId = "_18_zhiyifu_"; // 渠道号，方便分渠道统计收入
//		String channelId = ConfigurationTools.getChannelId(mContext); // 渠道号，方便分渠道统计收入

		// 三个保留字段提供给CP使用，后台通知时将源数据返回，可以为null
		 String reserved1 = "1";
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
	
	
	@Override
	protected void onDestroy() {
		MoposnsPlatApplication.exit();
		System.exit(0);
		super.onDestroy();
	}

	private class UnCeHandler implements UncaughtExceptionHandler {
		private final Thread.UncaughtExceptionHandler defaultExceptionHandler;

		private final Context context;

		// private final ErrorPointManager _errorPointManager;

		public UnCeHandler(final Context context) {
			// 获取系统默认的UncaughtException处理器
			defaultExceptionHandler = Thread
					.getDefaultUncaughtExceptionHandler();
			this.context = context;
			// this._errorPointManager = new ErrorPointManager(this.context);
		}

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			Log.e("ERROR", "add uncaughtException log");
			if (defaultExceptionHandler != null && ex != null) {
				try {
					ex.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (context != null) {

					// 只写文件不上传,程序再次启动后考虑上传.程序后面马上就退出了.
					final String text = "uncaughtException:" + thread.getName()
							+ ":" + ex.toString() + ":"
							+ getStackString(ex.getCause()) + "\n\n";

					writeFile(text + getStackString(ex));
					Log.e("ERROR", "add uncaughtException log");
				} else {
					Log.e("ERROR", "add uncaughtException log failed!");
				}

				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			}
		}

		private String getStackString(final Throwable throwable) {
			StackTraceElement[] ste = throwable.getStackTrace();
			String stackString = "";
			if (ste.length >= 1) {
				for (int i = 0; i < ste.length; i++) {
					stackString += "File:" + ste[i].getFileName() + ", Line: "
							+ ste[i].getLineNumber() + ", MethodName:"
							+ ste[i].getMethodName();
				}
			}
			return stackString;
		}

		private void writeFile(final String errorLog) {
			FileUtil fileUtil = new FileUtil();

			File sdFile = Environment.getExternalStorageDirectory();
			String path = sdFile.getAbsolutePath() + "/"
					+ WelcomeActivity.this.getPackageName();

			fileUtil.putData2Disk(fileUtil.getRecordFile(path)
					.getAbsolutePath(), (errorLog + "\n\n").getBytes());
		}
	}

}
