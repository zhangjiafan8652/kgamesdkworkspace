package com.yayawan.implyy;

import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.yayawan.asynchttp.AsyncHttpConnection;
import com.yayawan.asynchttp.StringResponseHandler;
import com.yayawan.asynchttp.support.ParamsWrapper;
import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.domain.YYWOrder;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.yayawan.main.YaYaWan;
import com.yayawan.proxy.YYWCharger;
import com.yayawan.sdk.base.AgentApp;
import com.yayawan.sdk.callback.YayaWanPaymentCallback;
import com.yayawan.sdk.domain.Order;
import com.yayawan.sdk.domain.User;
import com.yayawan.sdk.jfutils.Sputils;
import com.yayawan.sdk.jfutils.Yayalog;
import com.yayawan.sdk.main.YayaWan;
import com.yayawan.utils.CryptoUtil;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.RSACoder;

public class SingleChargerImplyy implements YYWCharger {


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
					
				pay_run(paramActivity);
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
		Yayalog.loger(pay_data.toString());
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
					public void onResponse(String content, URL url, int code) {

						disprogress();

						try {
							Yayalog.loger(content);
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

		Yayalog.loger("yysingle支付");
    	Order order2 = new Order();

        order2.orderId = YYWMain.mOrder.orderId;
        order2.goods = YYWMain.mOrder.goods;
        order2.money = YYWMain.mOrder.money;
        order2.ext = YYWMain.mOrder.ext;
        
//        int loca_login_type = Sputils.getSPint("loca_login_type", 4,
//				paramActivity);
//        if (loca_login_type==3) {
        	//单机
        	AgentApp.mUser=new User();
        	AgentApp.mUser.setUid(new BigInteger("1234567"));
	        AgentApp.mUser.setUser_uid(YYWMain.mUser.uid);
	        AgentApp.mUser.setUserName(YYWMain.mUser.userName);
//		}

        YayaWan.payment(paramActivity, order2, true,new YayaWanPaymentCallback() {

            @Override
            public void onCancel() {
                if (YYWMain.mPayCallBack != null) {
                    YYWMain.mPayCallBack.onPayCancel("cancel", "");
                }
            }

            @Override
            public void onError(int arg0) {
                if (YYWMain.mPayCallBack != null) {
                    YYWMain.mPayCallBack.onPayFailed("failed", "");
                }
            }

            @Override
            public void onSuccess(User user, Order order, int arg2) {
                if (YYWMain.mPayCallBack != null) {
                    YYWUser yywUser = new YYWUser();

                    yywUser.uid = user.uid + "";
                    yywUser.icon = user.icon;
                    yywUser.body = user.body;
                    yywUser.lasttime = user.lasttime;
                    yywUser.money = user.money;
                    yywUser.nick = user.nick;
                    yywUser.password = user.password;
                    yywUser.phoneActive = user.phoneActive;
                    yywUser.success = user.success;
                    yywUser.token = user.token;
                    yywUser.userName = user.userName;

                    YYWOrder yywOrder = new YYWOrder();

                    yywOrder.orderId = order.orderId;
                    yywOrder.ext = order.ext;
                    yywOrder.gameId = order.gameId;
                    yywOrder.goods = order.goods;
                    yywOrder.id = order.id;
                    yywOrder.mentId = order.mentId;
                    yywOrder.money = order.money;
                    yywOrder.paytype = order.paytype;
                    yywOrder.serverId = order.serverId;
                    yywOrder.status = order.status;
                    yywOrder.time = order.time;
                    yywOrder.transNum = order.transNum;

                    YYWMain.mPayCallBack.onPaySuccess(yywUser, yywOrder, "success");
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