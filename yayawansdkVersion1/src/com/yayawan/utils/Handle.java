package com.yayawan.utils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yayawan.callback.YYWLoginHandleCallback;
import com.yayawan.sdk.domain.BankInfo;
import com.yayawan.sdk.domain.PayMethod;
import com.yayawan.sdk.jflogin.ViewConstants;
import com.yayawan.sdk.jfutils.Sputils;
import com.yayawan.sdk.jfutils.Yayalog;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Handle {

	/**
	 * 注册回调
	 */
	public static void register_handler(final Context context,
			final String uid, final String userName) {
		// 访问网络,在线程中执行
		new Thread() {

			public void run() {
				HashMap<String, String> params = new HashMap<String, String>();

				try {
					params.put("data",
							encryptRegisterData(context, uid, userName));
					HttpUtil.post(URL.REGISTER_HANDLER, params, "UTF-8");
				} catch (Exception e) {
					e.printStackTrace();
				}
			};

		}.start();
	}

	/**
	 * 登录回调,直接return已经废弃
	 */
	public static void login_handler(final Context context, final String uid,
			final String userName) {
		 
		return;
		// 访问网络,在线程中执行
//		new Thread() {
//
//			public void run() {
//				HashMap<String, String> params = new HashMap<String, String>();
//
//				try {
//					params.put("data",
//							encryptRegisterData(context, uid, userName));
//					HttpUtil.post(URL.LOGIN_HANDLER, params, "UTF-8");
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			};
//
//		}.start();
	}
	
	/**
	 * 新版登陆回调
	 */
	public static void login_handler(final Context context, final String uid,
			final String userName,final YYWLoginHandleCallback callback) {
		// 访问网络,在线程中执行
		new Thread() {

			public void run() {
				HashMap<String, String> params = new HashMap<String, String>();

				try {
					
					params.put("data",
							encryptRegisterData(context, uid, userName));
					String post = HttpUtil.post(URL.LOGIN_HANDLER, params, "UTF-8");
					//Yayalog.loger("url:"+URL.LOGIN_HANDLER+" parms:"+params.toString());
					callback.onSuccess(post, "1");
					
					//Yayalog.loger(post);
				} catch (Exception e) {
					e.printStackTrace();
					callback.onFail(e.toString(), "1");
				}
			};

		}.start();
	}
	

	/**
	 * 激活回调
	 */
	public static void active_handler(final Context context) {
		// 访问网络,在线程中执行
		new Thread() {

			
			public void run() {
				HashMap<String, String> params = new HashMap<String, String>();
				String versioncode=1+"";
				try {
					versioncode=context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode+"";
				} catch (Exception e) {
					// TODO: handle exception
				}
				params.put("game_id", DeviceUtil.getGameId(context));
				params.put("union_id", DeviceUtil.getUnionid(context));
				params.put("device", DeviceUtil.getIMEI(context));
				params.put("ads_id", com.yayawan.sdk.utils.DeviceUtil.getSourceId(context));
				params.put("ios", "no");   //appversion
				params.put("osversion", android.os.Build.MODEL);//系统版本
				params.put("appversion", ""+versioncode); //app版本
			//	params.put("yayawan_source_id", com.yayawan.sdk.utils.DeviceUtil.getSourceId(context));
				
				
				try {
					Yayalog.loger("我在激活");
					String post = HttpUtil.post(URL.ACTIVE_HANDLER, params,
							"UTF-8");

					JSONObject object = new JSONObject(post);
					Yayalog.loger("激活信息:" + post);
					if (object.optInt("success") == 0) {

						int login_type = object.optInt("login_pay", 0);
						String login_pay_level = object.optString("login_pay_level", "");
						Sputils.putSPint("login_type", login_type, context);
						Sputils.putSPstring("login_pay_level", login_pay_level, context);
						
						//解析设置sms信息
						JSONObject smsobj = object.getJSONObject("sms");
						SharedPreferences sp = context.getSharedPreferences("config",
								Context.MODE_PRIVATE);
						Editor edit = sp.edit();
						edit.putString("CMCC", smsobj.optString("CMCC"));
						edit.putString("UNICOM", smsobj.optString("UNICOM"));
						edit.putString("TELECOM", smsobj.optString("TELECOM"));
						edit.putString("CMD", smsobj.optString("CMD"));
						edit.commit();
						//解析bank信息
						JSONArray jsonArray = object.getJSONArray("bank");
						for (int i = 0; i < jsonArray.length(); i++) {
							 String tempjson=jsonArray.get(i).toString();
							JSONObject tempjsonobj= new JSONObject(tempjson);
							PayMethod tempBankinfo=new PayMethod();
							int tempid=tempjsonobj.optInt("id");							
							tempBankinfo.setId(tempid+"");
							//tempBankinfo.setBank_id(tempid+"");
							tempBankinfo.setReason(tempjsonobj.optString("reason")+"");
							tempBankinfo.setStatus(tempjsonobj.optInt("status"));
							tempBankinfo.setDiscount(tempjsonobj.optInt("discount"));
							ViewConstants.banks.put(tempid, tempBankinfo);														
							// ViewConstants.banks.put(, value);
						}

					} else {
						 throw new Exception("game_id或source_id错误");
					}
					Yayalog.loger("激活后banks的信息:" + ViewConstants.banks.toString());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * 加密注册回调信息
	 * 
	 * @param context
	 * @throws Exception
	 */
	private static String encryptRegisterData(Context context, String uid,
			String userName) throws Exception {
		StringBuffer infoBuffer = new StringBuffer();
		String info = infoBuffer.append("game_id=")
				.append(DeviceUtil.getGameId(context)).append("&uid=")
				.append(uid).append("&union_id=")
				.append(DeviceUtil.getUnionid(context)).append("&brand=")
				.append(DeviceUtil.getBrand()).append("&device=")
				.append(DeviceUtil.getIMEI(context)).append("&mac=")
				.append(DeviceUtil.getMAC(context)).append("&model=")
				.append(DeviceUtil.getModel()).append("&username=")
				.append(userName).toString();
		String hexString = CryptoUtil.encodeHexString(RSACoder
				.encryptByPublicKey(info.getBytes()));
		return URLEncoder.encode(hexString, "UTF-8");

	}

	
	
	
}
