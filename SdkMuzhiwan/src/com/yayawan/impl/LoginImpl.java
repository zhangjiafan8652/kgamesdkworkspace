package com.yayawan.impl;

import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.muzhiwan.sdk.core.MzwSdkController;
import com.muzhiwan.sdk.core.callback.MzwLoignCallback;

import com.muzhiwan.sdk.utils.CallbackCode;
import com.yayawan.asynchttp.AsyncHttpConnection;
import com.yayawan.asynchttp.StringResponseHandler;
import com.yayawan.asynchttp.support.ParamsWrapper;
import com.yayawan.callback.YYWUserCallBack;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWLoginer;
import com.yayawan.utils.DeviceUtil;
import com.yayawan.utils.Handle;
import com.yayawan.utils.HttpUtil;
import com.yayawan.utils.JSONUtil;

public class LoginImpl implements YYWLoginer {

	@Override
	public void login(final Activity paramActivity,
			YYWUserCallBack userCallBack, String paramString) {

		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {

				if (!Myconstant.ISINIT) {
					System.out.println("初始化未成功");
					return;
				}

				MzwSdkController.getInstance().doLogin(new MzwLoignCallback() {

							@Override
							public void onResult(final int code,
									final String data) {
								// TODO Auto-generated method stub
								paramActivity.runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										final String token=data+"";
										if (code == CallbackCode.SUCCESS) {

											Yibuhttputils yibuhttputils = new Yibuhttputils() {

												@Override
												public void sucee(String str) {
													// TODO Auto-generated method stub
													String uid = "";
													String username = "";
													System.out.println("返回来的用户信息:"
															+ str);
													try {
														JSONObject object = new JSONObject(
																str);
														int jsonObject2 = object
																.isNull("success") ? null
																: object.getInt("success");
														if (jsonObject2 ==0) {
															uid = object.isNull("uid") ? null
																	: object
																			.getString("uid");
															username = object
																	.isNull("username") ? null
																	: object
																			.getString("username");
														}
														
														if (YYWMain.mUserCallBack != null) {
															YYWMain.mUser = new YYWUser();

															YYWMain.mUser = new YYWUser();
															YYWMain.mUser.uid = DeviceUtil.getUnionid(paramActivity) + "-" + uid
																	+ "";
															YYWMain.mUser.userName = DeviceUtil.getUnionid(paramActivity) + "-"
																	+ uid;
															// yywUser.token =
															// tokenVerifyModel.getAccess_token();
															YYWMain.mUser.token = JSONUtil.formatToken(
																	paramActivity, token, YYWMain.mUser.userName);

															YYWMain.mUserCallBack.onLoginSuccess(YYWMain.mUser, "success");
															Handle.login_handler(paramActivity, YYWMain.mUser.uid,
																	YYWMain.mUser.userName);
															
														}
														
													} catch (Exception e) {
														// TODO Auto-generated catch
														// block
														e.printStackTrace();
													}
													
													
												}

												@Override
												public void faile(String err,
														String rescode) {
													// TODO Auto-generated method stub

												}
											};
											yibuhttputils.runHttp(
													"http://union.yayawan.com/get_userinfo/?token="
															+ data
															+ "&union_id="
															+ "312920543"
															+ "&game_id="
															+ "1774865554"
															, "",
													"GET", "");

										} else if (code == CallbackCode.ERROR) {

											YYWMain.mUserCallBack.onLoginFailed(null, null);
										} else if (code == CallbackCode.CANCEL) {
											YYWMain.mUserCallBack.onLoginFailed(null, null);
										}
									}
								});
							
								
								
							}
						});

			}
		});

	}

	@Override
	public void relogin(Activity paramActivity, YYWUserCallBack userCallBack,
			String paramString) {

		System.err.println("relogin");
	}

	public void getAccessToken(final Activity paramActivity, String code) {
		progress(paramActivity);
		AsyncHttpConnection http = AsyncHttpConnection.getInstance();
		ParamsWrapper params = new ParamsWrapper();
		if (!"".equalsIgnoreCase(code)) {

			params.put("grant_type", "authorization_code");
			params.put("code", code);
		}

		http.post("token_url", params, new StringResponseHandler() {
			@Override
			public void onResponse(String content, URL url, int code) {
				JSONObject j;
				try {
					j = new JSONObject(content);

				} catch (JSONException e) {
					e.printStackTrace();
					disprogress();
					if (YYWMain.mUserCallBack != null) {
						YYWMain.mUserCallBack.onLoginFailed("", "");
					}
				}
			}

		});
	}

	public void getAccessUserInfo(final Activity paramActivity, String code) {
		progress(paramActivity);
		AsyncHttpConnection http = AsyncHttpConnection.getInstance();
		ParamsWrapper params = new ParamsWrapper();
		if (!"".equalsIgnoreCase(code)) {

			params.put("grant_type", "authorization_code");
			params.put("code", code);
		}

		http.post("token_url", params, new StringResponseHandler() {
			@Override
			public void onResponse(String content, URL url, int code) {
				JSONObject j;
				try {
					j = new JSONObject(content);
					String error = j.optString("error", "");
					if (error == "") {
						YYWMain.mUser = new YYWUser();
						YYWMain.mUser.uid = j.optString("xmw_open_id", "");
						YYWMain.mUser.userName = j.optString("xmw_open_id", "");
						YYWMain.mUser.nick = j.optString("nickname", "");
						YYWMain.mUser.icon = j.optString("avatar", "");
						YYWMain.mUser.body = "";
						YYWMain.mUser.token = JSONUtil.formatToken(
								paramActivity, "access_token=" + "token"
										+ "&refresh_token="
										+ YYWMain.mUser.userName, error);

						if (YYWMain.mUserCallBack != null) {
							YYWMain.mUserCallBack.onLoginSuccess(YYWMain.mUser,
									"success");
							Handle.login_handler(paramActivity,
									YYWMain.mUser.uid, YYWMain.mUser.userName);
						}

					} else {
						if (YYWMain.mUserCallBack != null) {
							YYWMain.mUserCallBack.onLoginFailed("", "");
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
					disprogress();
					if (YYWMain.mUserCallBack != null) {
						YYWMain.mUserCallBack.onLoginFailed("", "");
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
		progressDialog.setMessage("获取游戏数据");
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
