package com.yayawan.impl;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.gionee.gamesdk.GamePayer;
import com.gionee.gamesdk.OrderInfo;
import com.yayawan.asynchttp.AsyncHttpConnection;
import com.yayawan.asynchttp.ResponseCallback;
import com.yayawan.asynchttp.StringResponseHandler;
import com.yayawan.asynchttp.support.ParamsWrapper;
import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.domain.YYWOrder;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWCharger;
import com.yayawan.utils.Base64;
import com.yayawan.utils.CryptoUtil;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.HttpUtil;
import com.yayawan.utils.JSONUtil;
import com.yayawan.utils.RSACoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.Timestamp;

public class ChargerImpl implements YYWCharger {

	@Override
	public void charge(Activity paramActivity, YYWOrder order,
			YYWPayCallBack callback) {

	}

	
	private static boolean ispaying=false;//是否正在支付中
	
	@Override
	public void pay(final Activity paramActivity, final YYWOrder order,
			YYWPayCallBack callback) {

		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				System.err.println("pay start");
				if (YYWMain.mUser == null) {
					return;
				}
				if (ispaying) {
					return;
				}
				ispaying=true;
				createOrder(paramActivity);

			}
		});

	}

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
			e.printStackTrace();
		}

		params.put("data", hexString);

		http.post("http://union.yayawan.com/pay_handler", params,
				new StringResponseHandler() {
					@Override
					public void onResponse(final String content, URL url, int code) {

						disprogress();

						if (code != 200) {
							paramActivity.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									System.out.println(content);
									Toast.makeText(paramActivity,
											"订单处理失败，请重新支付", Toast.LENGTH_SHORT)
											.show();
								}
							});

						} else {
							pay_run(paramActivity);
						}

					}

				});

	}

	private void pay_run(final Activity paramActivity) {

		// progress(paramActivity);

		// String[] extStrings = YYWMain.mOrder.ext.split("_");
		// YYWMain.mOrder.serverId = Long.valueOf(extStrings[3]).longValue() -
		// 1000;

		// 创建订单信息
		final OrderInfo orderInfo = new OrderInfo();
		// 开发者后台申请的Apikey
		orderInfo.setApiKey(ActivityStubImpl.API_KEY);
		// 商户订单号，与创建支付订单中的"out_order_no"值相同
		orderInfo.setOutOrderNo(YYWMain.mOrder.orderId);
		// 支付订单提交时间，与创建支付订单中的"submit_time"值相同
		// orderInfo.setSubmitTime(new
		// SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

		Order order = new Order(YYWMain.mOrder.orderId, YYWMain.mUser.uid,
				YYWMain.mOrder.goods, ActivityStubImpl.API_KEY, new BigDecimal(
						YYWMain.mOrder.money / 100), new BigDecimal(
						YYWMain.mOrder.money / 100), new Timestamp(
						System.currentTimeMillis()), null, null);

		String deliverType = "1";

		orderInfo.setSubmitTime(order.getSubmitTime());

		JSONObject jsonReq = new JSONObject();
		ParamsWrapper params = new ParamsWrapper();
		String expireTime = order.getExpireTime();
		String notifyURL = "http://union.yayawan.com/pay/notify/495450117/1043348637/";
		notifyURL = DeviceUtil.getGameInfo(paramActivity, "notifyURL");

		StringBuilder signContent = new StringBuilder();

		try {
			signContent.append(order.getApiKey());

			jsonReq.put("api_key", order.getApiKey());

			signContent.append(order.getDealPrice());
			jsonReq.put("deal_price", order.getDealPrice().toString());
			signContent.append(deliverType);
			jsonReq.put("deliver_type", deliverType);

			// signContent.append(expireTime);
			// jsonReq.put("expire_time", expireTime);

			signContent.append(notifyURL);
			jsonReq.put("notify_url", notifyURL);

			signContent.append(order.getOutOrderNo());
			jsonReq.put("out_order_no", order.getOutOrderNo());
			signContent.append(order.getSubject());
			jsonReq.put("subject", order.getSubject());
			signContent.append(order.getSubmitTime());
			jsonReq.put("submit_time", order.getSubmitTime());
			signContent.append(order.getTotalFee());
			jsonReq.put("total_fee", order.getTotalFee().toString());

		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		String sign = null;
		System.out.println(signContent.toString());
		System.out.println(ActivityStubImpl.PRIVATE_KEY);
		try {
			sign = RSASignature.sign(signContent.toString(),
					ActivityStubImpl.PRIVATE_KEY, "utf-8");
		} catch (InvalidKeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidKeySpecException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SignatureException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			jsonReq.put("sign", sign);

			jsonReq.put("player_id", order.getPlayerId());

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// player_id不参与签名

		// return jsonReq.toJSONString();

		System.err.println(jsonReq);

		// params.put("jsonString", jsonReq.toString());

		AsyncHttpConnection http = AsyncHttpConnection.getInstance();

		HttpUtils.post("https://pay.gionee.com/order/create",
				jsonReq.toString(), new StringResponseHandler() {
					@Override
					public void onResponse(String content, URL url, int code) {

						System.err.println(content);
						ispaying=false;
						// disprogress();

						if (code != 200) {
							Toast.makeText(paramActivity, "订单处理失败，请重新支付",
									Toast.LENGTH_SHORT).show();
						} else {
							GamePayer mGamePayer = new GamePayer(paramActivity);

							// 调用启动收银台接口
							try {
								mGamePayer.pay(orderInfo,
										mGamePayer.new GamePayCallback() {

											// 支付成功
											@Override
											public void onPaySuccess() {
												// 可以在这里处理自己的业务
												YYWMain.mPayCallBack
														.onPaySuccess(
																YYWMain.mUser,
																YYWMain.mOrder,
																"success");
											}

											// 支付取消
											@Override
											public void onPayCancel() {
												// 可以在这里处理自己的业务
												YYWMain.mPayCallBack
														.onPayCancel("", "");
											}

											// 支付失败，stateCode为支付失败状态码，详见接入指南
											@Override
											public void onPayFail(
													String stateCode) {
												// 可以在这里处理自己的业务
												YYWMain.mPayCallBack
														.onPayCancel("", "");
											}
										});
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
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

class Order {
	// 商户订单号
	private final String outOrderNo;
	// 玩家playerId
	private final String playerId;
	// 商品名称
	private final String subject;
	// 商户apiKey
	private final String apiKey;
	// 需支付金额，dealPrice值需要等于totalFee
	private final BigDecimal totalFee;
	// 商户总金额，dealPrice值需要等于totalFee
	private final BigDecimal dealPrice;
	// 订单提交时间，格式为yyyyMMddHHmmss，由商户服务器提供，客户端调起支付收银台时需要使用这个值
	private final String submitTime;
	// 订单失效时间，可选，格式为yyyyMMddHHmmss，如果有值必须参与签名
	private final String expireTime;
	// 服务器通知地址，可选，不能超过1024个字符，如果有该字段，必须参与签名
	private final String notifyURL;

	/**
	 * @param outOrderNo
	 *            商户订单号 [必填]
	 * @param playerId
	 *            玩家playerId [必填]
	 * @param subject
	 *            商品名称 [必填]
	 * @param apiKey
	 *            商户apiKey [必填]
	 * @param totalFee
	 *            需支付金额，dealPrice值需要等于totalFee [必填]
	 * @param dealPrice
	 *            商户总金额，dealPrice值需要等于totalFee [必填]
	 * @param submitTime
	 *            订单提交时间，格式为yyyyMMddHHmmss，由商户服务器提供，客户端调起支付收银台时需要使用这个值 [必填]
	 * @param expireTime
	 *            订单失效时间，可选，格式为yyyyMMddHHmmss，如果有值必须参与签名 [可选]
	 * @param notifyURL
	 *            服务器通知地址，不能超过1024个字符，如果有该字段，必须参与签名(如果商户需要自定义参数，可以在创建订单时以
	 *            "http://www.partner.com/notifyReceiver?param1=value1&param2=value2"
	 *            的形式定义url) [可选]
	 */
	public Order(String outOrderNo, String playerId, String subject,
			String apiKey, BigDecimal totalFee, BigDecimal dealPrice,
			Timestamp submitTime, Timestamp expireTime, String notifyURL) {
		this.outOrderNo = outOrderNo;
		this.playerId = playerId;
		this.subject = subject;
		this.apiKey = apiKey;
		this.totalFee = totalFee;
		this.dealPrice = dealPrice;
		this.submitTime = SubmitTimeUtil.toString(submitTime);
		this.expireTime = SubmitTimeUtil.toString(expireTime);
		this.notifyURL = notifyURL;
	}

	public String getOutOrderNo() {
		return outOrderNo;
	}

	public String getPlayerId() {
		return playerId;
	}

	public String getSubject() {
		return subject;
	}

	public String getApiKey() {
		return apiKey;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public BigDecimal getDealPrice() {
		return dealPrice;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public String getNotifyURL() {
		return notifyURL;
	}

	@Override
	public String toString() {
		return "Order [outOrderNo=" + outOrderNo + ", playerId=" + playerId
				+ ", subject=" + subject + ", apiKey=" + apiKey + ", totalFee="
				+ totalFee + ", dealPrice=" + dealPrice + ", submitTime="
				+ submitTime + ", expireTime=" + expireTime + ", notifyURL="
				+ notifyURL + "]";
	}

}

class SubmitTimeUtil {
	public static final String DEFAULT_FORMAT = "yyyyMMddHHmmss";

	public static Timestamp toTimestamp(String submitTime) {
		submitTime = new StringBuilder().append(
				submitTime.substring(0, 4) + "-" + submitTime.substring(4, 6)
						+ "-" + submitTime.substring(6, 8) + " "
						+ submitTime.substring(8, 10) + ":"
						+ submitTime.substring(10, 12) + ":"
						+ submitTime.substring(12, 14)).toString();
		return Timestamp.valueOf(submitTime);
	}

	public static String toString(Date date) {
		return toString(date, DEFAULT_FORMAT);
	}

	public static String toString(Date date, String format) {
		if (date == null) {
			return "";
		}
		return new SimpleDateFormat(format).format(date);
	}
}
/*
class RSASignature {

	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	*//**
	 * RSA签名
	 * 
	 * @param content
	 *            待签名数据
	 * @param privateKey
	 *            商户私钥
	 * @param encode
	 *            字符集编码
	 * @return 签名值
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws SignatureException
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 *//*
	public static String sign(String content, String privateKey, String encode)
			throws NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidKeyException, SignatureException,
			UnsupportedEncodingException {
		String charset = "utf-8";
		if (encode != null) {
			charset = encode;
		}
		PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
				Base64.decode(privateKey));
		KeyFactory keyf = KeyFactory.getInstance("RSA");
		PrivateKey priKey = keyf.generatePrivate(priPKCS8);

		java.security.Signature signature = java.security.Signature
				.getInstance(SIGN_ALGORITHMS);
		signature.initSign(priKey);
		signature.update(content.getBytes(charset));
		byte[] signed = signature.sign();
		return Base64.encode(signed);

	}

}*/

class HttpUtils {

	private static final int CONNECTION_TIMEOUT = 5000;
	private static final int READ_TIMEOUT = 5000;

	public static void post(final String reqUrl, final String body,
			final ResponseCallback callback) {

		new Thread() {
			public void run() {

				String invokeUrl = reqUrl;
				URL serverUrl;
				try {
					serverUrl = new URL(invokeUrl);
					HttpURLConnection conn = (HttpURLConnection) serverUrl
							.openConnection();
					// HttpsURLConnection conn = (HttpsURLConnection)
					// serverUrl.openConnection();
					conn.setConnectTimeout(CONNECTION_TIMEOUT);
					conn.setReadTimeout(READ_TIMEOUT);
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
					conn.connect();

					conn.getOutputStream().write(body.getBytes("utf-8"));
					BufferedReader in = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					StringBuffer buffer = new StringBuffer();
					String line = "";
					while ((line = in.readLine()) != null) {
						buffer.append(line);
					}
					in.close();
					String response = buffer.toString();
					conn.disconnect();
					callback.onResponse(null, response, serverUrl, 200);
					// return response;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}.start();
	}
}
