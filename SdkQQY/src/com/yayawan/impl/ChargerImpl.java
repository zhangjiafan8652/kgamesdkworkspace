package com.yayawan.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.utils.DeviceUtil;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;
import com.tencent.ysdk.api.YSDKApi;
import com.tencent.ysdk.framework.common.eFlag;
import com.tencent.ysdk.framework.common.ePlatform;
import com.tencent.ysdk.module.pay.PayListener;
import com.tencent.ysdk.module.pay.PayRet;
import com.tencent.ysdk.module.user.UserLoginRet;
import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.domain.YYWOrder;
import com.yayawan.impl.qqhelper.QqYsdkHelp;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWCharger;

public class ChargerImpl implements YYWCharger {

	@Override
	public void charge(Activity paramActivity, YYWOrder order,
			YYWPayCallBack callback) {

	}

	private Activity mactivity;

	@Override
	public void pay(final Activity paramActivity, final YYWOrder order,
			YYWPayCallBack callback) {

		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				mactivity = paramActivity;
				if (YYWMain.mUser != null) {
					// System.out.println("我要创建订单了");
					// 设置是第一次支付
					mactivity = paramActivity;
					Myconstants.ISFIRSTPAY = true;
					UserLoginRet ret = new UserLoginRet();
					int platform = YSDKApi.getLoginRecord(ret);
					Myconstants.mpayinfo.openKey = ret.getAccessToken();
					Myconstants.mpayinfo.qq_paytoken = ret.getPayToken();
					String openid = ret.open_id;
					// int flag = ret.flag;
					// String msg = ret.msg;
					Myconstants.mpayinfo.pf = YSDKApi.getPf();
					Myconstants.mpayinfo.pfKey = YSDKApi.getPfKey();

					ePlatform platform1 = QqYsdkHelp.getPlatform();
					String logintype = "";
					if (platform1 == ePlatform.QQ) {
						logintype = "qq";
						Myconstants.mpayinfo.openId = ret.open_id;
						Myconstants.mpayinfo.opentype = "qq";
					} else if (platform1 == ePlatform.WX) {
						logintype = "wx";
					}

					System.out.println(Myconstants.mpayinfo.toString());

					createOrder(paramActivity);
				} else {
					System.out.println("meiyouuser");
				}
				// pay_run(mactivity);

			}
		});

	}

	String orderId = null;
	String token_id = null;
	String url_params = null;

	public void createOrder(final Activity paramActivity) {
		progress(paramActivity);
		HttpUtils httpUtil = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("uid", YYWMain.mUser.yywuid);
		requestParams.addBodyParameter("openid", Myconstants.mpayinfo.openId);
		requestParams.addBodyParameter("app_id",
				DeviceUtil.getAppid(paramActivity));
		requestParams.addBodyParameter("openkey", Myconstants.mpayinfo.openKey);
		requestParams.addBodyParameter("pay_token",
				Myconstants.mpayinfo.qq_paytoken);
		requestParams.addBodyParameter("amount", "" + YYWMain.mOrder.money);
		requestParams.addBodyParameter("remark", YYWMain.mOrder.ext);
		requestParams.addBodyParameter("transid", YYWMain.mOrder.orderId);
		requestParams.addBodyParameter("username", YYWMain.mUser.userName);
		requestParams.addBodyParameter("pf", Myconstants.mpayinfo.pf);
		requestParams.addBodyParameter("pfkey", Myconstants.mpayinfo.pfKey);
		requestParams.addBodyParameter("zoneid", "1");
		requestParams.addBodyParameter("amt", "" + (YYWMain.mOrder.money / 10));
		requestParams.addBodyParameter("opentype",
				Myconstants.mpayinfo.opentype);

		System.out.println(Myconstants.mpayinfo.toString());
		System.out.println(YYWMain.mOrder.toString());
		System.out.println(YYWMain.mUser.toString());
		System.out.println("payitem:" + "123456" + "*"
				+ (YYWMain.mOrder.money / 10) + "*" + "" + 1);
		System.out.println("goodsmeta:" + YYWMain.mOrder.goods + "*" + "道具");
		System.out
				.println("goodsurl:"
						+ YYWMain.mOrder.goods
						+ "*"
						+ "http://img2.imgtn.bdimg.com/it/u=3188228834,2947524100&fm=21&gp=0.jpg");

		requestParams.addBodyParameter("payitem", "123456" + "*"
				+ (YYWMain.mOrder.money / 10) + "*" + "" + 1);
		requestParams.addBodyParameter("goodsmeta", YYWMain.mOrder.goods + "*"
				+ "道具");
		requestParams
				.addBodyParameter("goodsurl",
						"http://img2.imgtn.bdimg.com/it/u=3188228834,2947524100&fm=21&gp=0.jpg");

		httpUtil.send(HttpMethod.POST, ViewConstants.unionmakeorder,
				requestParams, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						disprogress();
					}

					@Override
					public void onSuccess(final ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						disprogress();
						try {
							JSONObject obj = new JSONObject(arg0.result);
							int err_code = obj.optInt("err_code");
							System.out.println("支付回来的结果++++++++++"
									+ arg0.result);

							// err_code = 11020 的时候 余额不足
							if (err_code == 11020) {
								
								new Handler(Looper.getMainLooper())
										.post(new Runnable() {

											@Override
											public void run() {
												System.out.println("支付回来的结果"
														+ arg0.result);
												pay_run(paramActivity);

											}
										});

							} else if (err_code==0) {
								new Handler(Looper.getMainLooper())
								.post(new Runnable() {

									@Override
									public void run() {
										YYWMain.mPayCallBack
												.onPaySuccess(
														YYWMain.mUser,
														YYWMain.mOrder,
														"success");

									}
								});
							}else {
								new Handler(Looper.getMainLooper())
								.post(new Runnable() {

									@Override
									public void run() {
										YYWMain.mPayCallBack
												.onPayFailed(null, null);

									}
								});
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
	}

	/*
	 * public void createOrder1(final Activity paramActivity) {
	 * progress(paramActivity);
	 * 
	 * AsyncHttpConnection http = AsyncHttpConnection.getInstance();
	 * ParamsWrapper pay_data = new ParamsWrapper();
	 * 
	 * pay_data.put("game_id", DeviceUtil.getGameId(paramActivity));
	 * 
	 * pay_data.put("uid", YYWMain.mUser.uid);
	 * 
	 * System.out.println("支付传的" + YYWMain.mUser.uid);
	 * 
	 * pay_data.put("union_id", DeviceUtil.getUnionid(paramActivity));
	 * 
	 * pay_data.put("username", YYWMain.mUser.userName);
	 * 
	 * pay_data.put("order_id", YYWMain.mOrder.orderId);
	 * 
	 * pay_data.put("ext", YYWMain.mOrder.ext); pay_data.put("amount",
	 * YYWMain.mOrder.money);
	 * 
	 * pay_data.put("openkey", Myconstants.mpayinfo.openKey);
	 * 
	 * System.out.println("支付传的openKey" + Myconstants.mpayinfo.openKey);
	 * pay_data.put("opentype", Myconstants.mpayinfo.opentype);
	 * 
	 * pay_data.put("pay_token", Myconstants.mpayinfo.qq_paytoken);
	 * pay_data.put("payitem", "123456" + "*" + (YYWMain.mOrder.money / 10) +
	 * "*" + "" + 1); pay_data.put("goodsmeta", YYWMain.mOrder.goods + "*" +
	 * "道具"); pay_data.put("goodsurl",
	 * "http://img2.imgtn.bdimg.com/it/u=3188228834,2947524100&fm=21&gp=0.jpg");
	 * pay_data.put("pf", Myconstants.mpayinfo.pf); pay_data.put("pfkey",
	 * Myconstants.mpayinfo.pfKey); // pay_data.put("pfkey", "pfkey");
	 * pay_data.put("zoneid", "1"); pay_data.put("amt", "" +
	 * (YYWMain.mOrder.money / 10));
	 * 
	 * ParamsWrapper params = new ParamsWrapper(); //
	 * System.out.println(pay_data.toString()); String hexString = null; try {
	 * hexString = CryptoUtil.encodeHexString(RSACoder
	 * .encryptByPublicKey(pay_data.toString().getBytes())); hexString =
	 * URLEncoder.encode(hexString, "UTF-8"); } catch (Exception e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * params.put("data", hexString);
	 * 
	 * http.post("http://union.yayawan.com/pay_handler", params, new
	 * StringResponseHandler() {
	 * 
	 * @Override public void onResponse(final String content, URL url, int code)
	 * {
	 * 
	 * disprogress();
	 * 
	 * try { System.out.println("支付回来的结果" + content);
	 * 
	 * JSONObject json = new JSONObject(content);
	 * 
	 * int success = json.getInt("success");
	 * 
	 * if (success == 1) { // System.out.println("下订单支付失败"); //
	 * Toastutils_jf.toastString(paramActivity, // "下订单支付失败"); //
	 * pay_run(paramActivity); if (Myconstants.ISFIRSTPAY) { new
	 * Handler(Looper.getMainLooper()) .post(new Runnable() {
	 * 
	 * @Override public void run() { System.out .println("支付回来的结果" + content);
	 * pay_run(paramActivity);
	 * 
	 * } }); } else {
	 * 
	 * new Handler(Looper.getMainLooper()) .post(new Runnable() {
	 * 
	 * @Override public void run() { YYWMain.mPayCallBack .onPayFailed(null,
	 * null);
	 * 
	 * } }); }
	 * 
	 * } else if (success == 0) { System.out.println("支付成功"); //
	 * Toastutils_jf.toastString(paramActivity, // "支付成功"); //
	 * YYWMain.mPayCallBack.onPaySuccess(arg0, arg1, // arg2); new
	 * Handler(Looper.getMainLooper()) .post(new Runnable() {
	 * 
	 * @Override public void run() { YYWMain.mPayCallBack .onPaySuccess(
	 * YYWMain.mUser, YYWMain.mOrder, "success");
	 * 
	 * } }); }
	 * 
	 * } catch (JSONException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * if (code != 200) {
	 * 
	 * new Handler(Looper.getMainLooper()) .post(new Runnable() {
	 * 
	 * @Override public void run() { Toast.makeText(paramActivity,
	 * "订单处理失败，请重新支付", Toast.LENGTH_SHORT).show();
	 * 
	 * } });
	 * 
	 * } else {
	 * 
	 * }
	 * 
	 * }
	 * 
	 * });
	 * 
	 * }
	 */
	private void pay_run(final Activity paramActivity) {

		String zoneId = "1";
		String saveValue = "" + YYWMain.mOrder.money / 10;
		boolean isCanChange = false;

		AssetManager assetManager = paramActivity.getAssets();
		InputStream istr = null;
		try {
			istr = assetManager.open("sample_yuanbao.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Bitmap bmp = BitmapFactory.decodeStream(istr);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] appResData = baos.toByteArray();

		String ysdkExt = "ysdkExt";
		YSDKApi.recharge(zoneId, saveValue, isCanChange, appResData, ysdkExt,
				new PayListener() {
					@Override
					public void OnPayNotify(PayRet ret) {
						if (PayRet.RET_SUCC == ret.ret) {
							// 支付流程成功
							switch (ret.payState) {
							// 支付成功
							case PayRet.PAYSTATE_PAYSUCC:
								System.out.println("用户支付成功，支付金额"
										+ ret.realSaveNum + ";" + "使用渠道："
										+ ret.payChannel + ";" + "发货状态："
										+ ret.provideState + ";" + "业务类型："
										+ ret.extendInfo + ";建议查询余额："
										+ ret.toString());
								new Handler(Looper.getMainLooper())
										.post(new Runnable() {

											@Override
											public void run() {
												Myconstants.ISFIRSTPAY = false;
												createOrder(mactivity);
											}
										});

								break;
							// 取消支付
							case PayRet.PAYSTATE_PAYCANCEL:
								System.out.println("用户取消支付：" + ret.toString());
								payFail();
								break;
							// 支付结果未知
							case PayRet.PAYSTATE_PAYUNKOWN:
								System.out.println("用户支付结果未知，建议查询余额："
										+ ret.toString());
								payFail();
								break;
							// 支付失败
							case PayRet.PAYSTATE_PAYERROR:
								payFail();
								System.out.println("支付异常" + ret.toString());
								break;
							}
						} else {
							switch (ret.flag) {
							// case eFlag.User_LocalTokenInvalid:
							// // 用户取消支付
							// System.out.println("登陆态过期，请重新登陆："
							// + ret.toString());
							// // mMainActivity.letUserLogout();
							// Toast.makeText(mactivity, "登陆时间过长，请新登陆游戏进行支付",
							// 0).show();
							// break;
							case eFlag.Pay_User_Cancle:
								// 用户取消支付
								payFail();
								System.out.println("用户取消支付：" + ret.toString());
								break;
							case eFlag.Pay_Param_Error:
								payFail();
								System.out.println("支付失败，参数错误" + ret.toString());
								break;
							case eFlag.Error:
							default:
								payFail();
								System.out.println("支付异常" + ret.toString());
								Toast.makeText(mactivity, "支付异常，请新登陆游戏进行支付", 0)
										.show();
								break;
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
