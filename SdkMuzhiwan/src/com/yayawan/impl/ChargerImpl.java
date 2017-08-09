package com.yayawan.impl;

import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.muzhiwan.sdk.core.MzwSdkController;
import com.muzhiwan.sdk.core.callback.MzwPayCallback;

import com.muzhiwan.sdk.service.MzwOrder;
import com.muzhiwan.sdk.utils.CallbackCode;
import com.yayawan.asynchttp.AsyncHttpConnection;
import com.yayawan.asynchttp.StringResponseHandler;
import com.yayawan.asynchttp.support.ParamsWrapper;
import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.domain.YYWOrder;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.yayawan.main.YaYaWan;
import com.yayawan.proxy.YYWCharger;
import com.yayawan.utils.CryptoUtil;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.RSACoder;

public class ChargerImpl implements YYWCharger {

	@Override
	public void charge(Activity paramActivity, YYWOrder order,
			YYWPayCallBack callback) {

	}

	@Override
	public void pay(final Activity paramActivity, final YYWOrder order,
			YYWPayCallBack callback) {

		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {

				if (YYWMain.mUser!=null) {
					createOrder(paramActivity);
				}else {
				
				}
				
				// System.err.println("pay start");

			}
		});

	}

	String orderId = null;

	public void createOrder(final Activity paramActivity) {
		progress(paramActivity);

		AsyncHttpConnection http = AsyncHttpConnection.getInstance();
		ParamsWrapper pay_data = new ParamsWrapper();

		pay_data.put("game_id", DeviceUtil.getGameId(paramActivity));

		pay_data.put("uid", YYWMain.mUser.uid);

		pay_data.put("union_id", DeviceUtil.getUnionid(paramActivity));

		pay_data.put("username", YYWMain.mUser.userName);

		pay_data.put("order_id", YYWMain.mOrder.orderId);

		pay_data.put("ext", YYWMain.mOrder.ext);
		pay_data.put("amount", YYWMain.mOrder.money);

		ParamsWrapper params = new ParamsWrapper();

		String hexString = null;
		try {
			hexString = CryptoUtil.encodeHexString(RSACoder
					.encryptByPublicKey(pay_data.toString().getBytes()));
			hexString = URLEncoder.encode(hexString, "UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Yayalog.loger("在发送创建订单出错"+e.toString());
			e.printStackTrace();
		}

		params.put("data", hexString);

		http.post("http://union.yayawan.com/pay_handler", params,
				new StringResponseHandler() {
					@Override
					public void onResponse(String content, URL url, int code) {

						disprogress();

						try {
							Yayalog.loger("返回的订单号"+content);
							JSONObject json = new JSONObject(content);
							JSONObject orderString = json.optJSONObject("info");
							orderId = orderString.optString("id", "");

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (code != 200) {
							Toast.makeText(paramActivity, "订单处理失败，请重新支付",
									Toast.LENGTH_SHORT).show();
						} else {
							new Handler(Looper.getMainLooper())
									.post(new Runnable() {

										@Override
										public void run() {
											pay_run(paramActivity);

										}
									});
						}

					}

				});

	}

	private void pay_run(final Activity paramActivity) {

		MzwOrder order = new MzwOrder();
		order.setProductname(YYWMain.mOrder.goods);
		order.setProductid(YYWMain.mOrder.goods_id);
		order.setExtern(orderId);
		
		int money = Integer.parseInt(((YYWMain.mOrder.money) / 100) + "");
		order.setMoney(money);
		Yayalog.loger("给的钱"+money);
		MzwSdkController.getInstance().doPay(order,
				new MzwPayCallback() {

					@Override
					public void onResult(int code, MzwOrder data) {
						Yayalog.loger(code+data.toString());
						if (code == CallbackCode.SUCCESS) {
							Yayalog.loger("支付成功"+data.toString());
							if (YYWMain.mPayCallBack != null) {
								YYWMain.mPayCallBack.onPaySuccess(
										YYWMain.mUser, YYWMain.mOrder,
										"success");
							}

						} else if (code == CallbackCode.PROCESSING) {
							Yayalog.loger("正在支付"+data.toString());
							if (YYWMain.mPayCallBack != null) {
								YYWMain.mPayCallBack.onPayFailed(null, null);
										
							}
							new Handler(Looper.getMainLooper()).post(new Runnable() {

								@Override
								public void run() {
									Toast.makeText(paramActivity, "支付正在确认中...", Toast.LENGTH_LONG).show();

								}
							});
							

						} else if (code == CallbackCode.CANCEL) {
							Yayalog.loger("取消支付"+data.toString());
							
						} else {
							
							if (YYWMain.mPayCallBack != null) {
								YYWMain.mPayCallBack.onPayFailed(null, null);
							}
						}

					}
				});

	}

	ProgressDialog progressDialog = null;

	private void progress(Activity paramActivity) {
		progressDialog = new ProgressDialog(paramActivity);
		// 设置进度条风格，风格为圆形，旋转的
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialog 标题
		// progressDialog.setTitle("提示");
		// 设置ProgressDialog 提示信息
		progressDialog.setMessage("订单处理中");
		// 设置ProgressDialog 标题图标
		// progressDialog.setIcon(R.drawable.a);
		// 设置ProgressDialog 的进度条是否不明确
		progressDialog.setIndeterminate(true);
		// 设置ProgressDialog 是否可以按退回按键取消
		progressDialog.setCancelable(false);
		// 设置ProgressDialog 的一个Button
		// progressDialog.setButton("确定", new SureButtonListener());
		// 让ProgressDialog显示
		try {
			progressDialog.show();
		} catch (Exception e) {

		}
	}

	private void disprogress() {
		if (progressDialog != null) {
			if (progressDialog.isShowing())
				progressDialog.dismiss();
		}
	}
}
