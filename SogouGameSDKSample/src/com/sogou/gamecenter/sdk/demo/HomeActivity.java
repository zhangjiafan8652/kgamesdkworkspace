package com.sogou.gamecenter.sdk.demo;

import java.util.HashMap;
import java.util.Map;

import com.sogou.gamecenter.sdk.SogouGamePlatform;
import com.sogou.gamecenter.sdk.bean.SogouGameConfig;
import com.sogou.gamecenter.sdk.bean.UserInfo;
import com.sogou.gamecenter.sdk.listener.OnExitListener;
import com.sogou.gamecenter.sdk.listener.PayCallbackListener;
import com.sogou.gamecenter.sdk.listener.SwitchUserListener;
import com.sogou.gamecenter.sdk.util.GameUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * 游戏主玩区域界面，演示SDK接口使用与说明
 */
public class HomeActivity extends Activity {

	private static final String TAG = HomeActivity.class.getSimpleName();
	private SogouGamePlatform mSogouGamePlatform = SogouGamePlatform.getInstance();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	// ---------------------------搜狗支付中心演示-------------------------------
	// 支付接口：支付金额可编辑
	public void pay1(View view) {
//		Toast.makeText(this, "支付金额可编辑！", Toast.LENGTH_LONG).show();
		pay(true);
	}

	// 支付接口：支付金额不可编辑
	public void pay2(View view) {
//		Toast.makeText(this, "支付金额不可编辑！", Toast.LENGTH_LONG).show();
		pay(false);
	}

	/**
	 * 搜狗支付中心调用实例
	 * 
	 * @param isAmountEditable
	 *            金额是否可以编辑
	 */
	public void pay(boolean isAmountEditable) {
		Map<String, Object> data = new HashMap<String, Object>();
		// 游戏货币名字（必传）
		data.put("currency", "符石");
		// 人民币兑换比例（必传）,小数 比例得加 f
		data.put("rate", 1f);

		// 购买商品名字（必传）
		data.put("product_name", "倚天剑");
		// 充值金额,单位是元，在手游中数据类型为整型（必传）
		data.put("amount", 3);
		// 透传参数,游戏方自行定义（必传）
		data.put("app_data", "appdata_demo?");
		// 是否可以编辑支付金额（可选）如果不传递将表示金额不可以编辑
		data.put("appmodes", isAmountEditable);
		mSogouGamePlatform.pay(HomeActivity.this, data, new PayCallbackListener() {

			// 支付成功回调,游戏方可以做后续逻辑处理
			// 收到该回调说明提交订单成功，但成功与否要以服务器回调通知为准
			@Override
			public void paySuccess(String orderId, String appData) {
				// orderId是订单号，appData是游戏方自己传的透传消息
				Log.d(TAG, "paySuccess orderId:" + orderId + " appData:" + appData);
			}

			@Override
			public void payFail(int code, String orderId, String appData) {
				// 支付失败情况下,orderId可能为空
				if (orderId != null) {
					Log.e(TAG, "payFail code:" + code + "orderId:" + orderId + " appData:" + appData);
				} else {
					Log.e(TAG, "payFail code:" + code + " appData:" + appData);
				}
			}
		});
	}

	// 切换帐号接口
	public void switchUser(View view) {
		// 演示切换帐号接口使用，游戏有UI入口只需要调用该接口实现切换帐号
		mSogouGamePlatform.switchUser(this, new SwitchUserListener() {

			@Override
			public void switchSuccess(int code, UserInfo userInfo) {
				// 切换帐号成功回调此方法，拿到最新登录态信息
				Log.d(TAG, "switchSuccess:" + userInfo);
			}

			@Override
			public void switchFail(int code, String msg) {
				// 切换帐号失败回调
				Log.e(TAG, "switchFail code:" + code + " msg:" + msg);
			}
		});
	}

	// 提供测试专用，测试清除注册设备信息
	public void onClean(View view) {
		GameUtil.clearRegistedDeviceInfo(this);
	}

	// 提供测试专用，礼包中心
	public void GiftCenter(View view) {

	}

	// 提供测试专用，通过Log日志查看
	public void otherInfo(View view) {
		SogouGameConfig appInfo = mSogouGamePlatform.getGameConfig();
		String currentUserName = mSogouGamePlatform.getCurrentUser(this);
		int sid = mSogouGamePlatform.getSid();
		String sidName = mSogouGamePlatform.getSidName();
		String sourceId = mSogouGamePlatform.getSourceId();
		// 获取当前登录态信息
		UserInfo userInfo = mSogouGamePlatform.getUserInfo(this);
		Log.d(TAG, "appInfo:" + appInfo + "\ncurrentUserName:" + currentUserName + "\nsid:" + sid + "\nsidName:" + sidName + "\nsourceId:" + sourceId + "\nuserInfo:" + userInfo);
	}

	// 演示完善帐号接口使用
	public void perfectUser(View view) {
	}

	public static void actionHomeActivity(Context ctx) {
		Intent intent = new Intent(ctx, HomeActivity.class);
		ctx.startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		/*
		 * 演示退出接口使用 在游戏退出前，请调用sdk退出接口，当收到OnExitListener对象回调
		 * onCompleted方法执行真正游戏退出操作
		 */
		mSogouGamePlatform.exit(new OnExitListener(this) {

			@Override
			public void onCompleted() {
				// 游戏方执行退出游戏操作
				finish();
			}
		});
	}
}
